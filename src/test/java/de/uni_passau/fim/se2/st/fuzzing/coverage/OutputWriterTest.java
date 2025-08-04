package de.uni_passau.fim.se2.st.fuzzing.coverage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uni_passau.fim.se2.st.fuzzing.fuzzer.Fuzzer;
import de.uni_passau.fim.se2.st.fuzzing.fuzzer.TimeoutStoppingCondition;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author Michael Ertl
 */
class OutputWriterTest {

  @Test
  void testShellOutput() {
    Fuzzer fuzz = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    fuzz.fuzz();
    Map<String, CoverageTracker.ClassTracker> classTrackers =
        CoverageTracker.getInstance().getClassTrackers();
    assertDoesNotThrow(() -> OutputWriter.writeShellOutput(classTrackers));
  }

  @Test
  void testHTMLOutput() throws IOException {
    Path current = Paths.get(".").toAbsolutePath();
    Path target = current.resolve("fuzzing-report");
    Files.createDirectories(target);
    Fuzzer fuzz = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    fuzz.fuzz();
    CoverageTracker mockedTracker = mock(CoverageTracker.class);
    CoverageTracker.ClassTracker tracker = new CoverageTracker.ClassTracker();
    for (int i = 0; i < 20; i++) {
      tracker.trackLine(i + 1);
      tracker.visitLine(i + 1);
    }
    Map<String, CoverageTracker.ClassTracker> classTrackers = new HashMap<>();
    classTrackers.put("de/uni_passau/fim/se2/st/fuzzing/fuzztarget/Additive", tracker);
    when(mockedTracker.getClassTrackers()).thenReturn(classTrackers);
    assertDoesNotThrow(
        () ->
            OutputWriter.writeHTMLOutput(
                classTrackers, current.resolve("src").resolve("main").resolve("java"), target));
  }

  @Test
  void testXMLOutput() throws IOException {
    Path current = Paths.get(".").toAbsolutePath();
    Path target = current.resolve("fuzzing-report");
    Files.createDirectories(target);
    Fuzzer fuzz = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    fuzz.fuzz();
    CoverageTracker mockedTracker = mock(CoverageTracker.class);
    CoverageTracker.ClassTracker tracker = new CoverageTracker.ClassTracker();
    for (int i = 0; i < 20; i++) {
      tracker.trackLine(i + 1);
      tracker.visitLine(i + 1);
    }
    Map<String, CoverageTracker.ClassTracker> classTrackers = new HashMap<>();
    classTrackers.put("de/uni_passau/fim/se2/st/fuzzing/fuzztarget/Additive", tracker);
    when(mockedTracker.getClassTrackers()).thenReturn(classTrackers);
    assertDoesNotThrow(
        () -> OutputWriter.writeXMLOutput(classTrackers, target.resolve("coverage.xml")));
  }

  @Test
  void testHTMLOutput2() throws IOException {
    Path current = Paths.get(".").toAbsolutePath();
    Path target = current.resolve("fuzzing-report");
    Files.createDirectories(target);
    Fuzzer fuzz = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    fuzz.fuzz();
    CoverageTracker mockedTracker = mock(CoverageTracker.class);
    CoverageTracker.ClassTracker tracker = new CoverageTracker.ClassTracker();
    for (int i = 0; i < 20; i++) {
      tracker.trackLine(i + 1);
      tracker.visitLine(i + 1);
    }
    Map<String, CoverageTracker.ClassTracker> classTrackers = new HashMap<>();
    classTrackers.put("de/uni_passau/fim/se2/st/fuzzing/fuzztarget/CSVReader$CSVRow", tracker);
    when(mockedTracker.getClassTrackers()).thenReturn(classTrackers);
    assertDoesNotThrow(
        () ->
            OutputWriter.writeHTMLOutput(
                classTrackers, current.resolve("src").resolve("main").resolve("java"), target));
  }

  /**
   * @author Leon Föckersperger
   */
  @Test
  void testWriteHTMLOutputWithMissingFile(@TempDir Path tempDir) throws IOException {
    // Setup
    Path sourceRoot = tempDir.resolve("src");
    Path outputPath = tempDir.resolve("out");
    Files.createDirectories(sourceRoot);
    Files.createDirectories(outputPath);
    Map<String, CoverageTracker.ClassTracker> trackerMap = new HashMap<>();
    trackerMap.put("NonExistentClass", new CoverageTracker.ClassTracker());
    try {
      OutputWriter.writeHTMLOutput(trackerMap, sourceRoot, outputPath);
      assertTrue(Files.exists(outputPath.resolve("index.html")));
    } catch (IOException e) {
      fail("IOException should not have been thrown");
    }
  }

  @Test
  void testShellOutput2() throws IOException {
    Path current = Paths.get(".").toAbsolutePath();
    Path target = current.resolve("fuzzing-report");
    Files.createDirectories(target);
    Fuzzer fuzz = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    fuzz.fuzz();
    CoverageTracker mockedTracker = mock(CoverageTracker.class);
    CoverageTracker.ClassTracker tracker = new CoverageTracker.ClassTracker();
    for (int i = 0; i < 20; i++) {
      tracker.trackLine(i + 1);
      tracker.visitLine(i + 1);
    }
    Map<String, CoverageTracker.ClassTracker> classTrackers = new HashMap<>();
    classTrackers.put("de/uni_passau/fim/se2/st/fuzzing/fuzztarget/Additive", tracker);
    when(mockedTracker.getClassTrackers()).thenReturn(classTrackers);
    assertDoesNotThrow(() -> OutputWriter.writeShellOutput(classTrackers));
  }
}
