package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.CSVParser;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.Rational;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;

class CSVGeneratorTest {

  private CSVGenerator generator;

  @BeforeEach
  void setup() {
    generator = new CSVGenerator();
  }

  @Test
  void testRandomCSVRow() {
    try {
      Method method =
          ReflectionUtils.findMethod(CSVGenerator.class, "randomCSVRow", int.class).orElseThrow();
      method.setAccessible(true);

      String result = (String) method.invoke(generator, 10);
      long numberOfSeparators = result.chars().filter(c -> c == ';').count();
      assertEquals(9, numberOfSeparators);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  void testRandomList() {
    int maxLength = 6;
    String[] values = {"a", "b", "c", "d", "e"};
    try {
      Method method =
          ReflectionUtils.findMethod(CSVGenerator.class, "randomList", String[].class, int.class)
              .orElseThrow();
      method.setAccessible(true);

      String result = (String) method.invoke(generator, values, maxLength);
      if (!result.isEmpty()) {
        String[] parts = result.split(",");
        assertTrue(parts.length <= maxLength);
        boolean allValid = Arrays.stream(parts).allMatch(p -> Arrays.asList(values).contains(p));
        assertTrue(allValid);
      }
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  void testProvideRandomString() {
    Random rngMock = mock(Random.class);
    when(rngMock.nextInt(20)).thenReturn(5);
    when(rngMock.nextInt(12)).thenReturn(5);
    try {
      Field rngField =
          ReflectionUtils.findFields(
                  CSVGenerator.class,
                  f -> f.getName().equals("rng"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      rngField.setAccessible(true);
      rngField.set(generator, rngMock);

      String result = generator.provideRandomString();
      String[] lines = result.split("\n");
      assertEquals(5, lines.length);
      long separators = lines[0].chars().filter(c -> c == ';').count();
      assertEquals(4, separators);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  void testEmptyNumericString() {
    Random rngMock = mock(Random.class);
    when(rngMock.nextInt(anyInt())).thenReturn(0);

    try {
      Field rngField =
          ReflectionUtils.findFields(
                  CSVGenerator.class,
                  f -> f.getName().equals("rng"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      rngField.setAccessible(true);
      rngField.set(generator, rngMock);

      Method method =
          ReflectionUtils.findMethod(CSVGenerator.class, "randomNumericString").orElseThrow();
      method.setAccessible(true);
      String result = (String) method.invoke(generator);
      assertEquals("0", result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  void testNegativeNumericString() {
    Random rngMock = mock(Random.class);
    when(rngMock.nextBoolean()).thenReturn(true);
    when(rngMock.nextInt(anyInt())).thenReturn(5);
    when(rngMock.nextInt(1, 10)).thenReturn(1);

    try {
      Field rngField =
          ReflectionUtils.findFields(
                  CSVGenerator.class,
                  f -> f.getName().equals("rng"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      rngField.setAccessible(true);
      rngField.set(generator, rngMock);

      Method method =
          ReflectionUtils.findMethod(CSVGenerator.class, "randomNumericString").orElseThrow();
      method.setAccessible(true);
      String result = (String) method.invoke(generator);
      assertEquals("-15555", result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @ParameterizedTest
  @MethodSource("provideSimpleTypes")
  void testProvideRandomInput(Class<?> inputType, Class<?> expectedType) {
    Object result = generator.provideRandomInput(inputType);
    assertEquals(expectedType, result.getClass());
  }

  @ParameterizedTest
  @MethodSource("provideComplexTypes")
  void testInstantiateClass(Class<?> classType) {
    Object actual = generator.instantiateClass(classType);
    assertEquals(classType, actual.getClass());
  }

  @Test
  void testInstantiateObject() {
    Object result = generator.instantiateClass(Object.class);
    assertNull(result);
  }

  @Test
  void testInstantiateClassWithoutPublicConstructors() {
    Object result = generator.instantiateClass(Math.class);
    assertNull(result);
  }

  @Test
  void testClearMemory() {
    Rational result = (Rational) generator.provideRandomInput(Rational.class);
    generator.clearMemory();
    assertNull(generator.getExpressionString(result));
  }

  private static Stream<Arguments> provideSimpleTypes() {
    return Stream.of(
        Arguments.of(int.class, Integer.class),
        Arguments.of(short.class, Short.class),
        Arguments.of(byte.class, Byte.class),
        Arguments.of(long.class, Long.class),
        Arguments.of(float.class, Float.class),
        Arguments.of(double.class, Double.class),
        Arguments.of(char.class, Character.class),
        Arguments.of(boolean.class, Boolean.class),
        Arguments.of(BigInteger.class, BigInteger.class),
        Arguments.of(String.class, String.class));
  }

  private static Stream<Class<?>> provideComplexTypes() {
    return Stream.of(Rational.class, Random.class, CSVParser.class);
  }
}
