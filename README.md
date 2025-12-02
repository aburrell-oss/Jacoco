# Encrypt Decrypt Utility

A simple command-line program written in Java that encrypts and decrypts text using either:

* Shift cipher (Caesar cipher)
* Unicode cipher (shifts all Unicode characters)


The program is now set up with **Maven** for building and managing dependencies. You can use Maven to easily compile, test, and run the program.

## Usage

```java
java Main -mode <enc|dec> -key <number> [-data <text> | -in <file>] [-out <file>] [-alg <shift|unicode>]
```

## Arguments
| Argument | Required              | Description |
| -------  | --------------------- | ----------- |
| `-mode`  | No (default = `enc`)  | Mode: `enc` for encryption; `dec` for decryption  |
| `-key`   | Yes                   | Numerical key used to shift characters  |
| `-data`  | Optional              | Text to encrypt or decrypt |
| `-in`    | Optional              | Input file path |
| `-out`   | Optional              | Output file path |
| `-alg`   | No (default = `shift`)| Algorithm: `shift` or `unicode` |

> If both `-data` & `-in` are provided, `-data` takes priority.

> Arguments are not frigid; they have no specific order to be inputted.

## Examples
### 1. Encrypt using unicode algorithm
```java
-mode enc -key 5 -data "Welcome to hyperskill!" -alg unicode
```
Output:
```java
\jqhtrj%yt%m~ujwxpnqq&
```

### 2. Decrypt using shift algorithm
```java
-key 5 -alg shift -data "Bjqhtrj yt mdujwxpnqq!" -mode dec
```
Output:
```java
Welcome to hyperskill!
```

### 3. Reading & Writing to file
```java
-mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode
```
Output:  
*This command must get data from road_to_treasure.txt, encrypt the data with the key of 5, create protected.txt, and write ciphertext into it.*

### 4. Maven
```
mvn exec:java -Dexec.mainClass=com.griddynamics.Main -Dexec.args="-mode enc -key 5 -data 'Welcome to hyperskill!' -alg unicode"
```
Output:
```
\jqhtrj%yt%m~ujwxpnqq&
```

## Static Code Analysis
The project is configured to run **PMD**, **Checkstyle**, and **SpotBugs** automatically during Maven builds.

### Running analysis tools
Use the Maven wrapper to execute the verify phase:
```
./mvnw verify
```

This will:
* Compile the project
* Execute PMD
* Execute SpotBugs
* Execute Checkstyle
* Generate reports for each tool in `target/site/`
* Fail the build if any critical violations are found

### Reports Location
After running `./mvnw verify`, reports are generated here:

| Tool       | Report Location             |
| -----------| --------------------------- |
| PMD        |`target/site/pmd.html`       |
| SpotBugs   |`target/site/spotbugs.html`  |
| Checkstyle |`target/site/checkstyle.html`|

### Failing Build on Violations
Critical rules are configured so that the build will fail if:
* PMD detects critical code issues
* SpotBugs detects security issues
* Checkstyle detects important style violations

## Unit Testing
The project now includes a full **JUnit 5** test suite to validate all major functionality of the encryption/decryption utility.
The tests use:
* Temporary files (`Files.createTempFile`)
* Output-stream capturing for console testing
* Assertions from `org.junit.jupiter.api.Assertions`

### Running tests
Run all unit tests using:
```
./mvnw test
```
or
```
./mvnw verify
```

## Jacoco
The project has implemented **Jacoco** to measure whether the line or branch test coverage falls below *80%*.

The Maven configuration generates a coverage report when you run:
```
./mvnw verify
```
Goals:
* Produce an HTML coverage report at `target/site/jacoco-ut/index.html`
