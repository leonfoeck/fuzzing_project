package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

/** A stopping condition checking whether a certain timout is reached. */
public class TimeoutStoppingCondition implements StoppingCondition {

  private final long timeout;
  private long startTime;

  /**
   * Creates a new {@code TimeoutStoppingCondition} with the given timeout.
   *
   * @param timeout The timeout in milliseconds
   */
  public TimeoutStoppingCondition(long timeout) {
    this.timeout = timeout;
    startTime = System.currentTimeMillis();
  }

  /** {@inheritDoc} */
  @Override
  public void start() {
    startTime = System.currentTimeMillis();
  }

  /** {@inheritDoc} */
  @Override
  public boolean shouldStop() {
    return System.currentTimeMillis() - startTime >= timeout;
  }

  /**
   * Does nothing.
   *
   * @param event Ignored
   */
  @Override
  public void notify(Object event) {
    // Nothing to do here
  }
}
