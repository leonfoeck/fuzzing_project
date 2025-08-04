package de.uni_passau.fim.se2.st.fuzzing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

/**
 * @author Michael Ertl
 */
class CoverageGuidedFuzzerTest {

  @Test
  void testSetTimeout() {
    CoverageGuidedFuzzer fuzzer = new CoverageGuidedFuzzer();
    assertDoesNotThrow(() -> fuzzer.setTimeout(1000));
    assertThrows(Exception.class, () -> fuzzer.setTimeout(-1));
    CoverageGuidedFuzzer fuzz = new CoverageGuidedFuzzer();
    String[] args = {"-t", "5", "-q"};
    int exitCode = new CommandLine(new CoverageGuidedFuzzer()).execute(args);
    assertThrows(NullPointerException.class, () -> fuzz.setTimeout(-1));
  }

  /**
   * @author Leon Föckersperger
   */
  @Test
  void testDirectoryInit() throws IOException {
    CoverageGuidedFuzzer fuzzer = new CoverageGuidedFuzzer();
    Path current = Path.of(System.getProperty("user.dir"));
    Path target = current.resolve("fuzzing-report");
    if (Files.exists(target)) {
      deleteDirectory(target.toFile());
    }
    fuzzer.setTargetPackage("de.uni_passau.fim.se2.st.fuzzing.fuzztarget");
    fuzzer.setTargetClass("Rational");
    fuzzer.call();
    assertTrue(Files.exists(target));
  }

  /**
   * @author Leon Föckersperger
   */
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
