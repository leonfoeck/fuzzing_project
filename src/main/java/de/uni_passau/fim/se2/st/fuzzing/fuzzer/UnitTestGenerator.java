package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The UnitTestGenerator class generates automated unit tests based on fuzzing results. It generates
 * test classes and stores them in a predefined directory.
 *
 * @author Leon Föckersperger
 */
public class UnitTestGenerator implements TestGenerator {

  private static final Logger LOGGER = Logger.getLogger(UnitTestGenerator.class.getName());
  private static final Path REPORT_DIRECTORY = Paths.get("fuzzing-report/");
  private static final String TEST_CLASS_SUFFIX = "Test.java";

  private final FileSystemOperations fileSystemOperations;

  public UnitTestGenerator(FileSystemOperations fileSystemOperations) {
    this.fileSystemOperations = fileSystemOperations;
  }

  /** {@inheritDoc} */
  @Override
  public void generateTestClass(FuzzingClassResult result) throws UnableToWriteTestFile {
    TestCaseBuilder builder = new TestCaseBuilder(result);
    String testClassContent = builder.build();
    String className = result.className();
    writeTestClassToFile(testClassContent, className);
  }

  /**
   * Writes the generated test class content to a file.
   *
   * @param testClassContent The content of the test class to be written.
   * @param className The name of the class for which the test is generated.
   * @throws UnableToWriteTestFile If there is an issue writing the test class to file.
   */
  private void writeTestClassToFile(String testClassContent, String className)
      throws UnableToWriteTestFile {
    Path testFilePath = createTestFilePath(className);
    createParentDirectoryIfNeeded(testFilePath.getParent());
    writeContentToFile(testClassContent, testFilePath);
  }

  /**
   * Creates the file path for the test class based on its class name.
   *
   * @param className The name of the class for which the test is generated.
   * @return The path for the test class file.
   */
  private Path createTestFilePath(String className) {
    String directoryPath = className.replace('.', File.separatorChar);
    return REPORT_DIRECTORY.resolve(directoryPath + TEST_CLASS_SUFFIX);
  }

  /**
   * Creates the parent directory for the test class file if it doesn't exist.
   *
   * @param parentDir The parent directory path.
   * @throws UnableToWriteTestFile If there is an issue creating the parent directory.
   */
  private void createParentDirectoryIfNeeded(Path parentDir) throws UnableToWriteTestFile {
    if (!Files.exists(parentDir)) {
      try {
        fileSystemOperations.createDirectories(parentDir);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Failed to create directories for path: " + parentDir, e);
        throw new UnableToWriteTestFile("Failed to create parent directories for test file.", e);
      }
    }
  }

  /**
   * Writes the given content to the specified file path.
   *
   * @param content The content to be written to the file.
   * @param filePath The path of the file to write the content to.
   * @throws UnableToWriteTestFile If there is an issue writing the content to file.
   */
  private void writeContentToFile(String content, Path filePath) throws UnableToWriteTestFile {
    try {
      fileSystemOperations.write(filePath, content.getBytes());
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to write to test file: " + filePath, e);
      throw new UnableToWriteTestFile("Failed to write test class content to file.", e);
    }
  }
}
