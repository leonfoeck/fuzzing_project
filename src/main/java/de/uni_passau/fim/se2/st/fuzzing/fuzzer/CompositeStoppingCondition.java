package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.util.Collection;

public class CompositeStoppingCondition implements StoppingCondition {

  private final Collection<StoppingCondition> stoppingConditions;

  /**
   * @author Jakob Edmaier
   */
  public CompositeStoppingCondition(final Collection<StoppingCondition> conditions) {
    stoppingConditions = conditions;
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public void start() {
    stoppingConditions.forEach(StoppingCondition::start);
  }

  /**
   * Returns whether the fuzzer should stop, i.e. whether calling {@code shouldStop} on any of the
   * managed conditions returns true.
   *
   * @author Jakob Edmaier
   */
  @Override
  public boolean shouldStop() {
    return stoppingConditions.stream().anyMatch(StoppingCondition::shouldStop);
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public void notify(Object event) {
    stoppingConditions.forEach(condition -> condition.notify(event));
  }
}
