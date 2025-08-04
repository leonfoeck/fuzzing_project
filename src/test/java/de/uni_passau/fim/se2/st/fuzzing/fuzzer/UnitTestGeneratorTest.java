package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * @author Leon Föckersperger
 */
class UnitTestGeneratorTest {

  private FileSystemOperations fileSystemOperationsMock;
  private UnitTestGenerator unitTestGenerator;

  @BeforeEach
  void setUp() {
    // Erstellen Sie einen Mock für die FileSystemOperations-Schnittstelle
    fileSystemOperationsMock = Mockito.mock(FileSystemOperations.class);
    // Initialisieren Sie den UnitTestGenerator mit dem Mock
    unitTestGenerator = new UnitTestGenerator(fileSystemOperationsMock);
  }

  @Test
  void testGenerateTestClass() throws Exception {
    Path current = Path.of(System.getProperty("user.dir"));
    Path target = current.resolve("fuzzing-report");
    if (Files.exists(target)) {
      deleteDirectory(target.toFile());
    }
    // This test focuses on the public method generateTestClass
    FuzzingClassResult result = new FuzzingClassResult("YourTestClass", "YourTestClass", List.of());
    unitTestGenerator.generateTestClass(result);

    // Verify that private methods are indirectly called through generateTestClass
    // Reflection is not used here as this is a public method
    Path expectedFilePath = Paths.get("fuzzing-report/YourTestClassTest.java");
    verify(fileSystemOperationsMock).createDirectories(expectedFilePath.getParent());
    verify(fileSystemOperationsMock).write(eq(expectedFilePath), any(byte[].class));
  }

  @Test
  void testWriteTestClassToFile() throws Exception {
    // Directly test the private method writeTestClassToFile using reflection
    Method method =
        UnitTestGenerator.class.getDeclaredMethod(
            "writeTestClassToFile", String.class, String.class);
    method.setAccessible(true);

    String testClassContent = "public class Test {}";
    String className = "Test";
    method.invoke(unitTestGenerator, testClassContent, className);

    Path expectedFilePath = Paths.get("fuzzing-report/TestTest.java");
    verify(fileSystemOperationsMock).write(eq(expectedFilePath), any(byte[].class));
  }

  @Test
  void testCreateTestFilePath() throws Exception {
    // Directly test the private method createTestFilePath using reflection
    Method method = UnitTestGenerator.class.getDeclaredMethod("createTestFilePath", String.class);
    method.setAccessible(true);

    String className = "Test";
    Path result = (Path) method.invoke(unitTestGenerator, className);

    Path expectedPath = Paths.get("fuzzing-report/TestTest.java");
    assertEquals(expectedPath.toString(), result.toString());
  }

  @Test
  void testCreateParentDirectoryIfNeeded() throws Exception {
    // Directly test the private method createParentDirectoryIfNeeded using reflection
    Method method =
        UnitTestGenerator.class.getDeclaredMethod("createParentDirectoryIfNeeded", Path.class);
    method.setAccessible(true);
    Path current = Path.of(System.getProperty("user.dir"));
    Path target = current.resolve("fuzzing-report");
    if (Files.exists(target)) {
      deleteDirectory(target.toFile());
    }

    Path parentPath = Paths.get("fuzzing-report/YourTestClass").getParent();
    method.invoke(unitTestGenerator, parentPath);

    verify(fileSystemOperationsMock).createDirectories(eq(parentPath));
  }

  @Test
  void shouldThrowUnableToWriteTestFileWhenIOExceptionOccursDuringCreateDirectory()
      throws Exception {
    // Directly test the private method createParentDirectoryIfNeeded using reflection
    doThrow(IOException.class).when(fileSystemOperationsMock).createDirectories(any(Path.class));
    Method method =
        UnitTestGenerator.class.getDeclaredMethod("createParentDirectoryIfNeeded", Path.class);
    method.setAccessible(true);
    Path current = Path.of(System.getProperty("user.dir"));
    Path target = current.resolve("fuzzing-report");
    if (Files.exists(target)) {
      deleteDirectory(target.toFile());
    }

    Path parentPath = Paths.get("fuzzing-report/YourTestClass").getParent();

    InvocationTargetException thrown =
        assertThrows(
            InvocationTargetException.class, () -> method.invoke(unitTestGenerator, parentPath));
    assertTrue(thrown.getCause() instanceof UnableToWriteTestFile);
  }

  @Test
  void shouldThrowUnableToWriteTestFileWhenIOExceptionOccursDuringWrite() throws Exception {
    // Configure the mock to throw an IOException when the write method is called
    doThrow(IOException.class)
        .when(fileSystemOperationsMock)
        .write(any(Path.class), any(byte[].class));

    // Use reflection to directly test writeContentToFile, which is responsible for handling
    // IOException
    Method method =
        UnitTestGenerator.class.getDeclaredMethod("writeContentToFile", String.class, Path.class);
    method.setAccessible(true);

    Path filePath = Paths.get("fuzzing-report/TestTest.java");
    String content = "test content";

    // Assert that UnableToWriteTestFile is thrown when an IOException occurs
    InvocationTargetException thrown =
        assertThrows(
            InvocationTargetException.class,
            () -> method.invoke(unitTestGenerator, content, filePath));
    assertTrue(thrown.getCause() instanceof UnableToWriteTestFile);
  }

  private boolean deleteDirectory(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectory(file);
      }
    }
    return directoryToBeDeleted.delete();
  }
}
