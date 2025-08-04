package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.CSVParser;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.Example;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;

class FuzzerTest {

  @Test
  void testCreateFirstCandidate() {
    CoverageTracker instance = mock(CoverageTracker.class);
    when(instance.getCoverage()).thenReturn(1.0);

    try (MockedStatic<CoverageTracker> staticTracker = mockStatic(CoverageTracker.class)) {
      staticTracker.when(CoverageTracker::getInstance).thenReturn(instance);

      StoppingCondition condition = new CoverageStoppingCondition(0.5);
      Fuzzer fuzzer = new Fuzzer(0, condition);
      fuzzer.fuzz();
      assertFalse(fuzzer.getCoveringStrings().isEmpty());
    }
  }

  @Test
  void testGetCurrentCoverage() {
    CoverageTracker instance = mock(CoverageTracker.class);
    when(instance.getCoverage()).thenReturn(0.57);

    try (MockedStatic<CoverageTracker> staticTracker = mockStatic(CoverageTracker.class)) {
      staticTracker.when(CoverageTracker::getInstance).thenReturn(instance);

      StoppingCondition condition = new CoverageStoppingCondition(0.5);
      Fuzzer fuzzer = new Fuzzer(0, condition);
      fuzzer.fuzz();
      assertEquals(0.57, fuzzer.getCurrentCoverage());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An unexpected exception occurred: " + e.getMessage());
    }
  }

  @Test
  void testChooseCandidate() {
    StoppingCondition conditionMock = mock(StoppingCondition.class);
    StringMutation mutationMock = mock(StringMutation.class);

    Random rngMock = mock(Random.class);
    when(rngMock.nextDouble()).thenReturn(0.5);
    when(rngMock.nextInt(anyInt())).thenReturn(1);

    Set<String> coveringStrings = new TreeSet<>();
    coveringStrings.add("abc");
    coveringStrings.add("def");

    Fuzzer fuzzer = new Fuzzer(0, conditionMock);

    try {
      // Replace random number generator
      Field rngField =
          ReflectionUtils.findFields(
                  Fuzzer.class,
                  p -> p.getName().equals("rng"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      rngField.setAccessible(true);
      rngField.set(fuzzer, rngMock);

      // Replace set of covering strings
      Field coveringStringsField =
          ReflectionUtils.findFields(
                  Fuzzer.class,
                  p -> p.getName().equals("coveringStrings"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      coveringStringsField.setAccessible(true);
      coveringStringsField.set(fuzzer, coveringStrings);

      // Replace mutation generator
      Field mutationField =
          ReflectionUtils.findFields(
                  Fuzzer.class,
                  p -> p.getName().equals("mutation"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      mutationField.setAccessible(true);
      mutationField.set(fuzzer, mutationMock);

      // Call chooseCandidate()
      Method method = ReflectionUtils.findMethod(Fuzzer.class, "chooseCandidate").orElseThrow();
      method.setAccessible(true);
      method.invoke(fuzzer);
      verify(mutationMock).mutate("def");
    } catch (ReflectiveOperationException e) {
      e.printStackTrace();
      fail("An unexpected exception occurred: " + e.getMessage());
    }
  }

  @Test
  void testFuzzMethod() {
    Fuzzer fuzzer = new Fuzzer(1000, new TimeoutStoppingCondition(1000));
    assertDoesNotThrow(() -> fuzzer.fuzz(CSVParser.class));
  }

  @Test
  void testFuzzMethod2() {
    CoverageTracker instance = mock(CoverageTracker.class);
    when(instance.getCoverage()).thenReturn(1.0);

    try (MockedStatic<CoverageTracker> staticTracker = mockStatic(CoverageTracker.class)) {
      staticTracker.when(CoverageTracker::getInstance).thenReturn(instance);

      StoppingCondition condition = new CoverageStoppingCondition(0.8);
      Fuzzer fuzzer = new Fuzzer(0, condition);

      assertDoesNotThrow(() -> fuzzer.fuzz(Example.class));
    }
  }

  /**
   * @author Leon Föckersperger
   */
  @Test
  void testFuzzingForStaticMethod() throws ClassNotFoundException {
    CoverageTracker instance = mock(CoverageTracker.class);
    when(instance.getCoverage()).thenReturn(0.57);
    Class<?> staticClass =
        Class.forName("de.uni_passau.fim.se2.st.fuzzing.fuzztarget.TestClassWithStaticMethod");

    try (MockedStatic<CoverageTracker> staticTracker = mockStatic(CoverageTracker.class)) {
      staticTracker.when(CoverageTracker::getInstance).thenReturn(instance);
      StoppingCondition condition = new CoverageStoppingCondition(0.5);
      Fuzzer fuzzer = new Fuzzer(0, condition);
      fuzzer.fuzz(staticClass);
      assertTrue(
          fuzzer.getFuzzingClassResults().methodResults().stream()
              .anyMatch(m -> m.methodName().equals("staticMethod")));
    }
  }
}
