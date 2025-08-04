# Coverage-Guided Fuzzing Framework

[![Java](https://img.shields.io/badge/Java-17%2B-007396?logo=java)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apache-maven)](https://maven.apache.org/)
[![JUnit5](https://img.shields.io/badge/JUnit5-25A162?logo=junit5)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 🚀 Project Overview

This project implements a **coverage-guided fuzzing** framework for Java applications. Fuzzing is an automated testing technique that involves providing invalid, unexpected, or random data as inputs to a program. The "coverage-guided" aspect means the fuzzer uses code coverage feedback to intelligently explore different execution paths in the target program.

### 🔍 What is Coverage-Guided Fuzzing?

Coverage-guided fuzzing is a powerful testing methodology that:
- Generates test inputs automatically
- Monitors which code paths are executed by each input
- Uses this information to guide the generation of new test cases
- Helps discover edge cases and potential bugs that traditional testing might miss

## 🛠️ Technical Stack

- **Language**: Java 17+
- **Build Tool**: Apache Maven
- **Testing Framework**: JUnit 5
- **Bytecode Manipulation**: ASM 9.6
- **Utilities**: Google Guava
- **Command-Line Interface**: Picocli
- **Code Coverage**: JaCoCo

## 🏗️ Project Structure

```
src/
├── main/java/de/uni_passau/fim/se2/st/fuzzing/
│   ├── CoverageGuidedFuzzer.java  # Main entry point
│   ├── coverage/                  # Coverage tracking
│   ├── fuzzer/                   # Core fuzzing logic
│   └── fuzztarget/               # Example targets for fuzzing
└── test/                         # Test cases
```

## ✨ Key Features

- **Intelligent Test Generation**: Creates meaningful test cases that maximize code coverage
- **Bytecode Instrumentation**: Uses ASM to monitor code execution paths
- **Command-Line Interface**: Easy-to-use interface for configuring fuzzing sessions
- **Extensible Architecture**: Design allows for custom fuzzing strategies and input generators
- **Comprehensive Reporting**: Generates detailed coverage reports and test results

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Apache Maven 3.6.0 or higher

### Building the Project

```bash
mvn clean install
```

### Running the Fuzzer

```bash
mvn exec:java -Dexec.mainClass="de.uni_passau.fim.se2.st.fuzzing.CoverageGuidedFuzzer" \
    -Dexec.args="--class com.example.YourClass --package com.example"
```

## 🎯 Use Cases

This fuzzing framework is particularly useful for:
- Finding edge cases in Java applications
- Improving test coverage
- Security testing (identifying potential vulnerabilities)
- API testing
- Property-based testing

## 🧪 Example Usage

```java
// Example of how to use the fuzzer programmatically
Fuzzer fuzzer = new Fuzzer();
fuzzer.setTargetClass("com.example.YourClass");
fuzzer.setTargetPackage("com.example");
fuzzer.setTimeout(60); // 60 seconds
fuzzer.fuzz();
```

## 📊 Performance

The fuzzer is designed to be efficient and effective, with features like:
- Parallel test execution
- Smart input generation
- Coverage-guided path exploration
- Configurable timeouts and resource limits

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📚 Resources

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [ASM Java Bytecode Framework](https://asm.ow2.io/)
- [Coverage-Guided Fuzzing Introduction](https://llvm.org/docs/LibFuzzer.html#fuzz-target)

---

Developed as part of a university project at the University of Passau.