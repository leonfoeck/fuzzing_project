package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Leon Föckersperger
 */
class FuzzingMethodResultTest {

  @Test
  void testEqualsSelf() {
    FuzzingMethodResult result =
        new FuzzingMethodResult(
            "method",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor",
            "expectedResult",
            true,
            false,
            null,
            false,
            String.class);
    assertEquals(result, result);
  }

  @Test
  void testEqualsNull() {
    FuzzingMethodResult result =
        new FuzzingMethodResult(
            "method",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor",
            "expectedResult",
            true,
            false,
            null,
            false,
            String.class);
    assertNotEquals(result, null);
  }

  @Test
  void testEqualsDifferentClass() {
    FuzzingMethodResult result =
        new FuzzingMethodResult(
            "method",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor",
            "expectedResult",
            true,
            false,
            null,
            false,
            String.class);
    assertNotEquals(result, new Object());
  }

  @Test
  void testEqualsDifferentValues() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method2",
            List.of("param2"),
            new Class<?>[] {Integer.class},
            "constructor2",
            "result2",
            false,
            false,
            null,
            false,
            String.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues2() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            String.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues3() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            false,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues4() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            null,
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues5() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            false,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues6() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            false,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues7() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result2",
            true,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues8() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor2",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues9() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues10() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method1",
            List.of("param2"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsDifferentValues11() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method1",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method2",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor1",
            "result1",
            true,
            true,
            new RuntimeException(),
            true,
            void.class);
    assertNotEquals(result1, result2);
  }

  @Test
  void testEqualsSameValues() {
    FuzzingMethodResult result1 =
        new FuzzingMethodResult(
            "method",
            List.of("param"),
            new Class<?>[] {String.class},
            "constructor",
            "result",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    FuzzingMethodResult result2 =
        new FuzzingMethodResult(
            "method",
            List.of("param"),
            new Class<?>[] {String.class},
            "constructor",
            "result",
            true,
            true,
            new RuntimeException(),
            true,
            Void.class);
    assertEquals(result1, result2);
  }

  @Test
  void testExceptionMessageDontEquals()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        FuzzingMethodResult.class.getDeclaredMethod(
            "compareExceptions", Exception.class, Exception.class);
    method.setAccessible(true);
    Exception e1 = new RuntimeException("message");
    Exception e2 = new RuntimeException("message1");
    assertFalse(
        (boolean)
            method.invoke(
                new FuzzingMethodResult(
                    "method",
                    List.of("param"),
                    new Class<?>[] {String.class},
                    "constructor",
                    "result",
                    true,
                    true,
                    new RuntimeException(),
                    true,
                    Void.class),
                e1,
                e2));
  }

  @Test
  void testExceptionEqualsSecondIsNull()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        FuzzingMethodResult.class.getDeclaredMethod(
            "compareExceptions", Exception.class, Exception.class);
    method.setAccessible(true);
    Exception e1 = new RuntimeException("message");
    assertFalse(
        (boolean)
            method.invoke(
                new FuzzingMethodResult(
                    "method",
                    List.of("param"),
                    new Class<?>[] {String.class},
                    "constructor",
                    "result",
                    true,
                    true,
                    new RuntimeException(),
                    true,
                    Void.class),
                e1,
                null));
  }

  @Test
  void testExceptionEqualsFirstIsNull()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        FuzzingMethodResult.class.getDeclaredMethod(
            "compareExceptions", Exception.class, Exception.class);
    method.setAccessible(true);
    Exception e1 = new RuntimeException("message");
    assertFalse(
        (boolean)
            method.invoke(
                new FuzzingMethodResult(
                    "method",
                    List.of("param"),
                    new Class<?>[] {String.class},
                    "constructor",
                    "result",
                    true,
                    true,
                    new RuntimeException(),
                    true,
                    Void.class),
                null,
                e1));
  }

  @Test
  void testExceptionEqualsSameObject()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        FuzzingMethodResult.class.getDeclaredMethod(
            "compareExceptions", Exception.class, Exception.class);
    method.setAccessible(true);
    Exception e1 = new RuntimeException("message");
    assertTrue(
        (boolean)
            method.invoke(
                new FuzzingMethodResult(
                    "method",
                    List.of("param"),
                    new Class<?>[] {String.class},
                    "constructor",
                    "result",
                    true,
                    true,
                    new RuntimeException(),
                    true,
                    Void.class),
                e1,
                e1));
  }

  @Test
  void testExceptionEqualsDifferentClasses()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        FuzzingMethodResult.class.getDeclaredMethod(
            "compareExceptions", Exception.class, Exception.class);
    method.setAccessible(true);
    Exception e1 = new RuntimeException("message");
    Exception e2 = new IllegalArgumentException("message");
    assertFalse(
        (boolean)
            method.invoke(
                new FuzzingMethodResult(
                    "method",
                    List.of("param"),
                    new Class<?>[] {String.class},
                    "constructor",
                    "result",
                    true,
                    true,
                    new RuntimeException(),
                    true,
                    Void.class),
                e1,
                e2));
  }

  @Test
  void testHashCodeConsistency() {
    FuzzingMethodResult result =
        new FuzzingMethodResult(
            "method",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor",
            "expectedResult",
            true,
            false,
            null,
            false,
            String.class);
    assertEquals(result.hashCode(), result.hashCode());
  }

  @Test
  void testToString() {
    FuzzingMethodResult result =
        new FuzzingMethodResult(
            "method",
            List.of("param1"),
            new Class<?>[] {String.class},
            "constructor",
            "expectedResult",
            true,
            false,
            null,
            false,
            String.class);
    String expected =
        "FuzzingMethodResult{methodName='method', methodParameters=[param1], parameterTypes=[class java.lang.String], constructor='constructor', expectedResult=expectedResult, isStatic=true, isPrivate=false, expectedException=null, throwException=false, returnType=class java.lang.String}";
    assertEquals(expected, result.toString());
  }
}
