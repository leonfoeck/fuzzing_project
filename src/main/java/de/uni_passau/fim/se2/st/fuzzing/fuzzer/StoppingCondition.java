package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

/** A stopping condition for the fuzzer. */
public interface StoppingCondition {

  /** Called when the fuzzer starts. */
  void start();

  /** Returns true if the fuzzer should stop. */
  boolean shouldStop();

  /**
   * Called in every iteration of the fuzzing loop.
   *
   * <p>The event parameter can be used to pass information to the stopping condition.
   *
   * <p>The stopping condition can use this method to update its internal state. It might not be
   * necessary that every stopping condition takes care of what the {@code event} parameter is.
   *
   * @param event Used to pass information to the stopping condition
   */
  void notify(Object event);
}
