package com.griddynamics;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void mainEncryptShiftConsole() {
        Main.main(new String[]{
                "-mode", "enc",
                "-key", "1",
                "-data", "abc"
        });

        assertEquals("bcd", outContent.toString().trim());
    }

    @Test
    void mainDecryptShiftConsole() {
        Main.main(new String[]{
                "-mode", "dec",
                "-key", "1",
                "-data", "bcd"
        });

        assertEquals("abc", outContent.toString().trim());
    }

    @Test
    void mainEncryptUnicodeConsole() {
        Main.main(new String[]{
                "-mode", "enc",
                "-key", "1",
                "-alg", "unicode",
                "-data", "ABC"
        });

        assertEquals("BCD", outContent.toString().trim());
    }

    @Test
    void mainDecryptUnicodeConsole() {
        Main.main(new String[]{
                "-mode", "dec",
                "-key", "1",
                "-alg", "unicode",
                "-data", "BCD"
        });

        assertEquals("ABC", outContent.toString().trim());
    }

    @Test
    void mainWritesFile() throws IOException {
        Path temp = Files.createTempFile("out", ".txt");

        Main.main(new String[]{
                "-mode", "enc",
                "-key", "2",
                "-data", "abc",
                "-out", temp.toString()
        });

        String result = Files.readString(temp);
        assertEquals("cde", result);
    }

    @Test
    void mainReadsInputFile() throws IOException {
        Path input = Files.createTempFile("input", ".txt");
        Files.writeString(input, "xyz");

        Path output = Files.createTempFile("output", ".txt");

        Main.main(new String[]{
                "-mode", "enc",
                "-key", "2",
                "-in", input.toString(),
                "-out", output.toString()
        });

        assertEquals("zab", Files.readString(output).trim());
    }

    @Test
    void mainMissingInput() {
        Main.main(new String[]{
                "-mode", "enc",
                "-key", "1"
        });

        assertEquals("Error: No input data provided.", outContent.toString().trim());
    }

    @Test
    void mainInvalidNumberFormatKey() {
        Main.main(new String[]{
                "-key", "NOT_A_NUMBER",
                "-data", "abc"
        });

        assertEquals("Error: Key must be a number.", outContent.toString().trim());
    }

    @Test
    void mainUnknownArgument() {
        Main.main(new String[]{
                "-key", "1",
                "-data", "abc",
                "-unknown", "value"
        });

        assertTrue(outContent.toString().contains("Unknown argument -unknown"));
    }

    // -------------------------------------------------------
    //  FILE I/O
    // -------------------------------------------------------

    @Test
    void writeFileAndReadBack() throws IOException {
        Path temp = Files.createTempFile("test", ".txt");

        Main.writeFile("Hello File!", temp.toString());
        String read = Main.readFileCipher(temp.toString());

        assertEquals("Hello File!", read);
        Files.deleteIfExists(temp);
    }

    @Test
    void readFile_cipherThrowsIOException() {
        assertThrows(IOException.class, () ->
                Main.readFileCipher("nonexistent_file_123.txt")
        );
    }

    @Test
    void writeFileThrowsRuntime() {
        // Attempt writing to a directory (illegal)
        Path dir = Paths.get(System.getProperty("java.io.tmpdir"));

        assertThrows(RuntimeException.class,
                () -> Main.writeFile("content", dir.toString())
        );
    }

    // -------------------------------------------------------
    //  CIPHER METHOD TESTS
    // -------------------------------------------------------

    @Test
    void encryptShiftCipherNonLetter() {
        assertEquals("!", Main.encryptShiftCipher("!", 5));
    }

    @Test
    void decryptShiftCipherNonLetter() {
        assertEquals("!", Main.decryptShiftCipher("!", 5));
    }
}