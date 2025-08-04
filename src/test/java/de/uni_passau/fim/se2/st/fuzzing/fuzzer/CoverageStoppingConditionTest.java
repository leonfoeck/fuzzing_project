package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CoverageStoppingConditionTest {

  @Test
  public void testDefaultConstructor() {
    StoppingCondition condition = new CoverageStoppingCondition();
    condition.notify(0.99);
    assertFalse(condition.shouldStop());
    condition.notify(1.0);
    assertTrue(condition.shouldStop());
  }

  @Test
  public void testNotifyInvalidInput() {
    StoppingCondition condition = new CoverageStoppingCondition();
    assertThrows(IllegalArgumentException.class, () -> condition.notify("invalid input"));
  }
}
