package com.griddynamics;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Main class for encryption and decryption utility.
 */
public final class Main {

    /** Constant alphabet size for shift cipher. */
    private static final int ALPHABET_SIZE = 26;

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {

        int key = 0;
        String data = "";
        String mode = "enc";
        String in = "";
        String out = "";
        String alg = "shift";

        try {
            for (int i = 0; i < args.length; i += 2) {
                final int j = i + 1;

                switch (args[i]) {
                    case "-key":
                        key = Integer.parseInt(args[j]);
                        break;
                    case "-data":
                        data = args[j];
                        break;
                    case "-mode":
                        mode = args[j];
                        break;
                    case "-in":
                        in = args[j];
                        break;
                    case "-out":
                        out = args[j];
                        break;
                    case "-alg":
                        alg = args[j];
                        break;
                    default:
                        System.out.println("Unknown argument " + args[i]);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Key must be a number.");
            return;
        } catch (Exception e) {
            System.out.println("Error parsing arguments: " + e.getMessage());
            return;
        }

        String inputText = data;

        if (inputText.isEmpty() && !in.isEmpty()) {
            try {
                inputText = readFileCipher(in);
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
        } else if (inputText.isEmpty()) {
            System.out.println("Error: No input data provided.");
            return;
        }

        String result;
        try {
            if ("unicode".equals(alg)) {
                if ("dec".equals(mode)) {
                    result = decryptUnicodeCipher(inputText, key);
                } else {
                    result = encryptUnicodeCipher(inputText, key);
                }
            } else {
                if ("dec".equals(mode)) {
                    result = decryptShiftCipher(inputText, key);
                } else {
                    result = encryptShiftCipher(inputText, key);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in encrypt/decrypt: " + e.getMessage());
            return;
        }

        if (!out.isEmpty()) {
            writeFile(result, out);
        } else {
            System.out.println(result);
        }
    }

    /**
     * Writes text to a file.
     *
     * @param text     the text to write
     * @param fileName output filename
     */
    static void writeFile(final String text, final String fileName) {
        try {
            Files.writeString(
                    Paths.get(fileName),
                    text,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads UTF-8 text from file.
     *
     * @param fileName input filename
     * @return file contents
     * @throws IOException if reading fails
     */
    static String readFileCipher(final String fileName) throws IOException {
        return Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
    }

    /**
     * Encrypts text using Unicode shift cipher.
     *
     * @param message message to encrypt
     * @param shift   shift amount
     * @return encrypted text
     */
    static String encryptUnicodeCipher(final String message, final int shift) {
        StringBuilder result = new StringBuilder();
        for (char symbol : message.toCharArray()) {
            result.append((char) (symbol + shift));
        }
        return result.toString();
    }

    /**
     * Encrypts text using alphabetic shift cipher.
     *
     * @param message message to encrypt
     * @param shift   shift value
     * @return encrypted message
     */
    static String encryptShiftCipher(final String message, final int shift) {
        StringBuilder result = new StringBuilder();

        for (char symbol : message.toCharArray()) {
            if (Character.isLetter(symbol)) {
                char base = Character.isUpperCase(symbol) ? 'A' : 'a';
                char repositioning =
                        (char) ((symbol - base + shift) % ALPHABET_SIZE + base);
                result.append(repositioning);
            } else {
                result.append(symbol);
            }
        }
        return result.toString();
    }

    /**
     * Decrypts Unicode-shift cipher text.
     *
     * @param message encrypted text
     * @param shift   shift amount
     * @return decrypted text
     */
    static String decryptUnicodeCipher(final String message, final int shift) {
        StringBuilder result = new StringBuilder();
        for (char symbol : message.toCharArray()) {
            result.append((char) (symbol - shift));
        }
        return result.toString();
    }

    /**
     * Decrypts alphabetic shift cipher text.
     *
     * @param message encrypted message
     * @param shift   shift amount
     * @return decrypted output
     */
    static String decryptShiftCipher(final String message, final int shift) {
        StringBuilder result = new StringBuilder();

        for (char symbol : message.toCharArray()) {
            if (Character.isLetter(symbol)) {
                char base = Character.isUpperCase(symbol) ? 'A' : 'a';
                int adjustedShift = (symbol - base - shift) % ALPHABET_SIZE;

                if (adjustedShift < 0) {
                    adjustedShift += ALPHABET_SIZE;
                }

                result.append((char) (adjustedShift + base));
            } else {
                result.append(symbol);
            }
        }
        return result.toString();
    }
}
