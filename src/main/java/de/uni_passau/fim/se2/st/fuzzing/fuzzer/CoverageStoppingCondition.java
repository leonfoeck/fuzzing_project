package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

/** A stopping condition that stops the fuzzer when a certain coverage is reached. */
public class CoverageStoppingCondition implements StoppingCondition {

  private final double threshold;
  private double currentCoverage = 0.0;

  public CoverageStoppingCondition() {
    this(1.0);
  }

  public CoverageStoppingCondition(double threshold) {
    this.threshold = threshold;
  }

  /** {@inheritDoc} */
  @Override
  public void start() {
    // Nothing to do here
  }

  /**
   * Returns whether the fuzzer should stop, i.e., whether the current coverage is greater than the
   * threshold.
   *
   * @return Whether the current coverage is greater than the threshold
   */
  @Override
  public boolean shouldStop() {
    return currentCoverage >= threshold;
  }

  /**
   * Updates the current coverage.
   *
   * @param event The current coverage, as a {@code double} value
   */
  @Override
  public void notify(Object event) {
    if (event instanceof Double coverage) {
      currentCoverage = coverage;
    } else {
      throw new IllegalArgumentException("Event must be of type Double");
    }
  }
}
