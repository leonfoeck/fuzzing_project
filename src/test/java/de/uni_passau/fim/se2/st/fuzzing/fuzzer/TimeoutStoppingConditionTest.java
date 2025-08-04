package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class TimeoutStoppingConditionTest {

  @Test
  public void testShouldStop() throws InterruptedException {
    StoppingCondition condition = new TimeoutStoppingCondition(1000);
    TimeUnit.MILLISECONDS.sleep(900);
    assertFalse(condition.shouldStop());
    TimeUnit.MILLISECONDS.sleep(200);
    assertTrue(condition.shouldStop());
  }

  @Test
  public void testStart() throws InterruptedException {
    StoppingCondition condition = new TimeoutStoppingCondition(100);
    TimeUnit.MILLISECONDS.sleep(200);
    condition.start();
    assertFalse(condition.shouldStop());
  }
}
