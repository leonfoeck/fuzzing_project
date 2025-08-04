package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CompositeStoppingConditionTest {

  @Test
  public void testShouldStop() {
    StoppingCondition first = mock(StoppingCondition.class);
    StoppingCondition second = mock(StoppingCondition.class);

    when(first.shouldStop()).thenReturn(false);
    when(second.shouldStop()).thenReturn(true);

    StoppingCondition compositeCondition = new CompositeStoppingCondition(List.of(first, second));
    assertTrue(compositeCondition.shouldStop());
  }

  @Test
  public void testNotify() {
    StoppingCondition first = mock(StoppingCondition.class);
    StoppingCondition second = mock(StoppingCondition.class);

    StoppingCondition compositeCondition = new CompositeStoppingCondition(List.of(first, second));
    compositeCondition.notify(1.0);
    verify(first).notify(1.0);
    verify(second).notify(1.0);
  }

  @Test
  public void testStart() {
    StoppingCondition first = mock(StoppingCondition.class);
    StoppingCondition second = mock(StoppingCondition.class);

    StoppingCondition compositeCondition = new CompositeStoppingCondition(List.of(first, second));
    compositeCondition.start();
    verify(first).start();
    verify(second).start();
  }
}
