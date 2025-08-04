package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.Rational;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Leon Föckersperger
 */
class TestCaseBuilderTest {

  private static Stream<FuzzingClassResult> testCases() {
    FuzzingMethodResult publicMethodWithParameters =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "expectedResult",
            false,
            false,
            null,
            false,
            String.class);
    FuzzingMethodResult methodExpectsObject =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("new Object()"),
            new Class<?>[] {Object.class},
            "new ExampleClass()",
            new Rational(1, 2),
            false,
            false,
            null,
            false,
            Object.class);

    FuzzingMethodResult publicMethodWithParametersReturnInteger =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "12",
            false,
            false,
            null,
            false,
            Integer.class);
    FuzzingMethodResult publicMethodWithParametersReturnDouble =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "12D",
            false,
            false,
            null,
            false,
            Double.class);
    FuzzingMethodResult publicMethodWithParametersReturnFloat =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "12D",
            false,
            false,
            null,
            false,
            Float.class);
    FuzzingMethodResult publicMethodWithParametersReturnByte =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "0b1",
            false,
            false,
            null,
            false,
            Byte.class);
    FuzzingMethodResult publicMethodWithParametersReturnCharacter =
        new FuzzingMethodResult(
            "publicMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "c",
            false,
            false,
            null,
            false,
            Character.class);
    FuzzingMethodResult privateMethodThrowsException =
        new FuzzingMethodResult(
            "privateMethod",
            List.of("\"test\"", "42"),
            new Class<?>[] {String.class, int.class},
            "new ExampleClass()",
            "c",
            false,
            true,
            new RuntimeException("Expected Exception"),
            true,
            Character.class);
    FuzzingMethodResult staticMethodNoParameters =
        new FuzzingMethodResult(
            "staticMethod",
            List.of(),
            new Class<?>[] {},
            "",
            42,
            true,
            false,
            null,
            false,
            int.class);
    FuzzingMethodResult staticMethodNullAsParameters =
        new FuzzingMethodResult(
            "staticMethod",
            List.of(),
            new Class<?>[] {},
            "",
            42,
            true,
            false,
            null,
            false,
            int.class);
    FuzzingMethodResult publicMethodNoParameters =
        new FuzzingMethodResult(
            "publicMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            "0L",
            false,
            false,
            null,
            false,
            Long.class);
    FuzzingMethodResult methodReturnsBoolean =
        new FuzzingMethodResult(
            "booleanMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            true,
            false,
            false,
            null,
            false,
            Boolean.class);
    FuzzingMethodResult methodReturnsFalse =
        new FuzzingMethodResult(
            "booleanMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            false,
            false,
            false,
            null,
            false,
            Boolean.class);
    FuzzingMethodResult methodReturnsFalseboolean =
        new FuzzingMethodResult(
            "booleanMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            false,
            false,
            false,
            null,
            false,
            boolean.class);
    FuzzingMethodResult methodPrivateNoException =
        new FuzzingMethodResult(
            "privateMethod",
            List.of("\"test\""),
            new Class<?>[] {String.class},
            "new ExampleClass()",
            "expectedResult",
            false,
            true,
            null,
            false,
            String.class);
    FuzzingMethodResult methodReturnsNull =
        new FuzzingMethodResult(
            "nullMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            null,
            false,
            false,
            null,
            false,
            void.class);
    FuzzingMethodResult methodPrivateThrowExceptionAndNullParameter =
        new FuzzingMethodResult(
            "privateMethod",
            List.of("null"),
            new Class<?>[] {},
            "new ExampleClass()",
            null,
            false,
            true,
            new RuntimeException("Expected Exception"),
            true,
            void.class);
    FuzzingMethodResult methodPrivateThrowExceptionAndNoNullParameter =
        new FuzzingMethodResult(
            "privateMethod",
            List.of("hello"),
            new Class<?>[] {String.class},
            "new ExampleClass()",
            null,
            false,
            true,
            new RuntimeException("Expected Exception"),
            true,
            void.class);

    FuzzingMethodResult methodPrivateNoParameters =
        new FuzzingMethodResult(
            "privateMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            "0b1",
            false,
            true,
            null,
            false,
            byte.class);
    FuzzingMethodResult methodIsPrivateAndStatic =
        new FuzzingMethodResult(
            "privateStaticMethod",
            List.of(),
            new Class<?>[] {},
            "",
            "1",
            true,
            true,
            null,
            false,
            Short.class);
    FuzzingMethodResult methodPublicThrowsException =
        new FuzzingMethodResult(
            "publicMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            "expectedResult",
            false,
            false,
            new RuntimeException("Expected Exception"),
            true,
            Float.class);
    FuzzingMethodResult methodBooleanReturnAndPrivate =
        new FuzzingMethodResult(
            "privateBooleanMethod",
            List.of(),
            new Class<?>[] {},
            "new ExampleClass()",
            true,
            false,
            true,
            null,
            false,
            Boolean.class);

    return Stream.of(
        new FuzzingClassResult("com.example", "ExampleClass", List.of(publicMethodWithParameters)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(privateMethodThrowsException)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(staticMethodNoParameters)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodReturnsBoolean)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodPrivateNoException)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodReturnsNull)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(methodPrivateThrowExceptionAndNullParameter)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(publicMethodNoParameters)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodPrivateNoParameters)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodReturnsFalse)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodIsPrivateAndStatic)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodPublicThrowsException)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(publicMethodWithParametersReturnInteger)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(publicMethodWithParametersReturnDouble)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(publicMethodWithParametersReturnFloat)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(publicMethodWithParametersReturnByte)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(publicMethodWithParametersReturnCharacter)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(staticMethodNullAsParameters)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodExpectsObject)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(methodBooleanReturnAndPrivate)),
        new FuzzingClassResult("com.example", "ExampleClass", List.of(methodReturnsFalseboolean)),
        new FuzzingClassResult(
            "com.example", "ExampleClass", List.of(methodPrivateThrowExceptionAndNoNullParameter)));
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testCaseBuilderTest(FuzzingClassResult classResult) {
    TestCaseBuilder builder = new TestCaseBuilder(classResult);
    String generatedTestClass = builder.build();
    FuzzingMethodResult methodResult =
        classResult
            .methodResults()
            .get(0); // Assuming one method result per class result for simplicity

    // Check for method name in generated test class
    assertTrue(generatedTestClass.contains(methodResult.methodName() + "_Test_"));

    // Check for assertion type based on methodResult properties
    if (methodResult.expectedException() != null) {
      assertTrue(generatedTestClass.contains("assertThrows"));
    } else if (methodResult.returnType().equals(boolean.class)) {
      assertTrue(
          generatedTestClass.contains("assertTrue") || generatedTestClass.contains("assertFalse"));
    } else if (methodResult.returnType().equals(void.class)) {
      assertTrue(generatedTestClass.contains("assertNull"));
    } else if (!methodResult.methodParameters().isEmpty()
        && methodResult.methodParameters().get(0).equals("null")) {
      assertTrue(generatedTestClass.contains("(Object) null"));
    } else if (!methodResult.isPrivate()
        && !methodResult.isStatic()
        && methodResult.methodParameters().isEmpty()) {
      assertTrue(generatedTestClass.contains("targetObject." + methodResult.methodName() + "()"));
    } else if (methodResult.returnType().equals(Boolean.class) && methodResult.isPrivate()) {
      assertTrue(generatedTestClass.contains("(Boolean)"));
    } else if (methodResult.expectedResult() instanceof Rational) {
      assertTrue(generatedTestClass.contains("assertNotNull"));
    } else {
      assertTrue(generatedTestClass.contains("assertEquals"));
    }
  }
}
