package de.uni_passau.fim.se2.st.fuzzing.coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Leon Föckersperger
 */
class CoverageTrackerTest {

  @BeforeEach
  void resetCoverageTracker() {
    CoverageTracker.resetInstance();
  }

  @Test
  void testGetNumberOfCoveredLinesNotZero() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLineVisit(10, "SomeClass");
    int numberOfCoveredLines = coverageTracker.getNumberOfCoveredLines();
    Assertions.assertNotEquals(0, numberOfCoveredLines, "Number of covered lines should not be 0");
  }

  @Test
  void testGetNumberOfCoveredLines() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLineVisit(10, "SomeClass");
    CoverageTracker.trackLineVisit(1, "SomeClass");
    int numberOfCoveredLines = coverageTracker.getNumberOfCoveredLines();
    Assertions.assertEquals(2, numberOfCoveredLines, "Number of covered lines should be 2");
  }

  @Test
  void testGetNumberOfLines() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLine(10, "SomeClass");
    CoverageTracker.trackLine(1, "SomeClass");
    int numberOfCoveredLines = coverageTracker.getTotalNumberOfLines();
    Assertions.assertEquals(2, numberOfCoveredLines, "Number of lines should 2");
  }

  @Test
  void getCoverage() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLineVisit(1, "SomeClass");
    CoverageTracker.trackLine(2, "SomeClass");
    double coverage = coverageTracker.getCoverage();
    Assertions.assertEquals(0.5, coverage, "Coverage should be 0,5");
  }

  @Test
  void testMergeWithNewLineElse() {
    CoverageTracker.ClassTracker tracker1 = new CoverageTracker.ClassTracker();
    tracker1.visitLine(1);
    CoverageTracker.ClassTracker tracker2 = new CoverageTracker.ClassTracker();
    tracker2.visitLine(2);
    tracker2.visitLine(1);
    tracker1.merge(tracker2);
    assertTrue(tracker1.getVisitedLines().containsKey(2));
    assertEquals(2, tracker1.getVisitedLines().get(1));
  }

  @Test
  void testSingletonBehavior() {
    CoverageTracker firstInstance = CoverageTracker.getInstance();
    CoverageTracker secondInstance = CoverageTracker.getInstance();
    assertSame(firstInstance, secondInstance, "getInstance should return the same instance");
  }

  @Test
  void testTrackInvalidLine() {
    assertThrows(IllegalArgumentException.class, () -> CoverageTracker.trackLine(-1, "SomeClass"));
  }

  @Test
  void testTrackVisitInvalidLine() {
    assertThrows(
        IllegalArgumentException.class, () -> CoverageTracker.trackLineVisit(-1, "SomeClass"));
  }

  @Test
  void testTrackInvalidClass() {
    assertThrows(IllegalArgumentException.class, () -> CoverageTracker.trackLine(1, null));
  }

  @Test
  void testTrackVisitInvalidClass() {
    assertThrows(IllegalArgumentException.class, () -> CoverageTracker.trackLineVisit(1, null));
  }

  @Test
  void testGetClassTracker() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLine(1, "SomeClass");
    CoverageTracker.trackLineVisit(1, "SomeClass");
    CoverageTracker.trackLine(1, "SomeClass2");
    CoverageTracker.trackLineVisit(1, "SomeClass2");
    Map<String, CoverageTracker.ClassTracker> classTracker = coverageTracker.getClassTrackers();
    StringBuilder sb = new StringBuilder();
    classTracker.forEach(
        (key, value) -> {
          sb.append(key).append(": \n");
          sb.append("Tracked Lines: ").append(value.getLines()).append("\n");
          sb.append("Visited Lines: ").append(value.getVisitedLines()).append("\n");
        });
    String expectedOuputString =
        """
                SomeClass:\s
                Tracked Lines: [1]
                Visited Lines: {1=1}
                SomeClass2:\s
                Tracked Lines: [1]
                Visited Lines: {1=1}
                                """;
    assertEquals(expectedOuputString, sb.toString());
  }

  @Test
  void testCoverageZero() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    assertEquals(0, coverageTracker.getCoverage());
  }

  @Test
  void testDoubleVisit() {
    CoverageTracker coverageTracker = CoverageTracker.getInstance();
    CoverageTracker.trackLineVisit(1, "SomeClass");
    CoverageTracker.trackLineVisit(1, "SomeClass");
    assertEquals(1, coverageTracker.getNumberOfCoveredLines());
  }
}
