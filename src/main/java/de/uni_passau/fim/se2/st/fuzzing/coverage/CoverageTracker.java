package de.uni_passau.fim.se2.st.fuzzing.coverage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Tracks coverage information, i.e., which line was visited how many times. */
// Needs to be public to be callable during test execution.
public class CoverageTracker {

  private static CoverageTracker instance = null;
  private static final Map<String, ClassTracker> classTrackers = new TreeMap<>();

  private CoverageTracker() {}

  public static synchronized CoverageTracker getInstance() {
    if (instance == null) {
      instance = new CoverageTracker();
    }
    return instance;
  }

  static synchronized void resetInstance() {
    instance = null; // Setzt die Singleton-Instanz zurück
    classTrackers.clear();
  }

  public Map<String, ClassTracker> getClassTrackers() {
    return classTrackers;
  }

  /**
   * Track a visit of a line.
   *
   * @param pLineNumber The line number that was tracked
   * @param pClassName The class in which the line is
   * @author Leon Föckersperger
   */
  // Needs to be public to be callable during test execution.
  public static void trackLineVisit(final int pLineNumber, final String pClassName) {
    if (pClassName == null || pLineNumber <= 0) {
      throw new IllegalArgumentException(
          "Invalid input: pClassName cannot be null and pLineNumber must be positive.");
    }
    ClassTracker tracker = classTrackers.getOrDefault(pClassName, new ClassTracker());
    tracker.visitLine(pLineNumber);
    tracker.trackLine(pLineNumber);
    classTrackers.putIfAbsent(pClassName, tracker);
  }

  /**
   * Track the existence of a line.
   *
   * @param pLineNumber The line number to track
   * @param pClassName The class in which the line is
   * @author Leon Föckersperger
   */
  // Needs to be public to be callable during test execution.
  public static void trackLine(final int pLineNumber, final String pClassName) {
    if (pClassName == null || pLineNumber <= 0) {
      throw new IllegalArgumentException(
          "Invalid input: pClassName cannot be null and pLineNumber must be positive.");
    }
    ClassTracker tracker = classTrackers.getOrDefault(pClassName, new ClassTracker());
    tracker.trackLine(pLineNumber);
    classTrackers.putIfAbsent(pClassName, tracker);
  }

  /**
   * Provides the total number of lines that are tracked from the fuzzing subject.
   *
   * @return The total number of lines from the fuzzing subject
   * @author Leon Föckersperger
   */
  public int getTotalNumberOfLines() {
    return classTrackers.values().stream().mapToInt(classes -> classes.getLines().size()).sum();
  }

  /**
   * Provides the number of covered lines from the fuzzing subject.
   *
   * @return The number of covered lines from the fuzzing subject
   * @author Leon Föckersperger
   */
  public int getNumberOfCoveredLines() {
    return classTrackers.values().stream()
        .mapToInt(classes -> classes.getVisitedLines().size())
        .sum();
  }

  /**
   * Provides the coverage on the fuzzing subject.
   *
   * @return The coverage on the fuzzing subject
   */
  public double getCoverage() {
    double coverage = (double) getNumberOfCoveredLines() / getTotalNumberOfLines();
    if (Double.isNaN(coverage)) {
      return 0;
    } else {
      return coverage;
    }
  }

  public static final class ClassTracker {
    private final Map<Integer, Integer> visitedLines = new TreeMap<>();
    private final Set<Integer> lines = new HashSet<>();

    void visitLine(int lineNumber) {
      if (visitedLines.containsKey(lineNumber)) {
        visitedLines.merge(lineNumber, 1, Integer::sum);
      } else {
        visitedLines.put(lineNumber, 1);
      }
    }

    void trackLine(int lineNumber) {
      lines.add(lineNumber);
    }

    public Map<Integer, Integer> getVisitedLines() {
      return visitedLines;
    }

    public Set<Integer> getLines() {
      return lines;
    }

    public void merge(ClassTracker other) {
      other
          .getVisitedLines()
          .forEach(
              (line, count) -> {
                if (visitedLines.containsKey(line)) {
                  visitedLines.merge(line, count, Integer::sum);
                } else {
                  visitedLines.put(line, count);
                }
              });
      lines.addAll(other.getLines());
    }
  }
}
