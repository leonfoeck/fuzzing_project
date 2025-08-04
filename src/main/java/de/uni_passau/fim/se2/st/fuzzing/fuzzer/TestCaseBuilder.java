package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Constructs test case classes for Java applications based on fuzzing results. It generates test
 * cases by utilizing fuzzing method results, managing imports, and creating test methods for both
 * public and private methods of the target class. The class supports reflection for accessing
 * private methods and handles different assertion types based on the method outcomes.
 *
 * @author Leon Föckersperger
 */
public class TestCaseBuilder {

  private final String packageName;

  private final String className;

  private final List<String> imports = new LinkedList<>();

  private final StringBuilder testCaseBody = new StringBuilder();

  private final List<FuzzingMethodResult> fuzzingMethodResultList;

  private static final String CLASS_TEMPLATE = "%s\nclass %sTest {\n\n%s\n}\n";

  private static final String METHOD_TEMPLATE = "    @Test\n    void %s() %s {\n    %s\n    }\n";
  private static final String PUBLIC_NON_STATIC_METHOD_BODY_TEMPLATE =
      "    %s targetObject = %s;\n%s";
  private static final String ASSERT_TEMPLATE = "    %s(%s);\n";
  private static final String PRIVATE_METHOD_BODY_TEMPLATE =
      "    Class<?> classToTest = Class.forName(\"%s\");\n        Method method = classToTest.getDeclaredMethod(\"%s\"%s);\n        method.setAccessible(true);\n%s\n";
  private static final String REFLECTION_METHOD_CALL_TEMPLATE = "method.invoke(%s%s)";
  private static final String PRIVATE_METHOD_WITH_EXCEPTION_TEMPLATE =
      "        InvocationTargetException exception = %s        assertInstanceOf(%s.class, exception.getTargetException());\n";

  private static final String IMPORT_ASSERTION =
      "import static org.junit.jupiter.api.Assertions.*;\n";

  private static final String IMPORT_REFLECTION = "import java.lang.reflect.*;\n";

  private static final String REFLECTION_EXCEPTIONS =
      "throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException";

  private static final String IMPORT_TEST = "import org.junit.jupiter.api.Test;\n";

  /**
   * Initializes a new instance of the {@code TestCaseBuilder} class with the specified fuzzing
   * class result. It sets up the package name, class name, and collects fuzzing method results to
   * be used in test case generation.
   *
   * @param result The fuzzing class result containing information about the class to be tested and
   *     its methods.
   */
  public TestCaseBuilder(FuzzingClassResult result) {
    this.packageName = result.packageName();
    this.className = result.className();
    this.fuzzingMethodResultList = result.methodResults();
    initializeImports();
  }

  /**
   * Initializes the import statements required for the test case file. This includes imports for
   * JUnit assertions, reflection (if necessary), and the JUnit {@code Test} annotation.
   */
  private void initializeImports() {
    imports.add(IMPORT_TEST);
    imports.add(IMPORT_ASSERTION);
    if (fuzzingMethodResultList.stream().anyMatch(FuzzingMethodResult::isPrivate)) {
      imports.add(IMPORT_REFLECTION);
    }
  }

  /**
   * Builds the body of the test case file by iterating over each fuzzing method result and
   * generating the appropriate test method for it.
   */
  private void buildTestCaseBody() {
    this.fuzzingMethodResultList.forEach(this::buildTestMethod);
  }

  /**
   * Generates a test method for the given fuzzing method result. Depending on whether the target
   * method is private or public, it builds the test method accordingly, handling private methods
   * through reflection.
   *
   * @param methodResult The fuzzing method result containing information about the method to be
   *     tested.
   */
  public void buildTestMethod(FuzzingMethodResult methodResult) {
    String methodName = methodResult.methodName() + "_Test_" + System.nanoTime();
    if (methodResult.isPrivate()) {
      buildPrivateTestMethod(methodResult, methodName);
    } else {
      buildPublicTestMethod(methodResult, methodName);
    }
  }

  /**
   * Builds a public test method for the given fuzzing method result. It generates an assertion
   * statement based on the expected outcome of the method and constructs the test method body
   * accordingly.
   *
   * @param methodResult The fuzzing method result for a public method.
   * @param methodName The generated name for the test method.
   */
  private void buildPublicTestMethod(FuzzingMethodResult methodResult, String methodName) {
    String assertionCall = buildAssertionCall(methodResult);
    String assertionStatement =
        String.format(
            ASSERT_TEMPLATE,
            assertionCall,
            buildAssertionParameters(methodResult, "", assertionCall));
    String methodBody =
        methodResult.isStatic()
            ? assertionStatement
            : String.format(
                PUBLIC_NON_STATIC_METHOD_BODY_TEMPLATE,
                packageName + "." + className,
                methodResult.constructor(),
                assertionStatement);
    this.testCaseBody.append(String.format(METHOD_TEMPLATE, methodName, "", methodBody));
  }

  /**
   * Builds a private test method for the given fuzzing method result using reflection. It generates
   * the necessary reflection code to invoke the private method and constructs an assertion
   * statement based on the expected outcome.
   *
   * @param methodResult The fuzzing method result for a private method.
   * @param methodName The generated name for the test method.
   */
  private void buildPrivateTestMethod(FuzzingMethodResult methodResult, String methodName) {
    String callingObject = methodResult.isStatic() ? "null" : methodResult.constructor();
    String assertStatement;
    if (methodResult.throwException()) {
      assertStatement =
          String.format(
              PRIVATE_METHOD_WITH_EXCEPTION_TEMPLATE,
              String.format(
                  ASSERT_TEMPLATE,
                  "assertThrows",
                  buildAssertionParameters(methodResult, callingObject, "assertThrows")),
              methodResult.expectedException().getClass().getCanonicalName());
    } else {
      String assertionCall = buildAssertionCall(methodResult);
      assertStatement =
          String.format(
              ASSERT_TEMPLATE,
              assertionCall,
              buildAssertionParameters(methodResult, callingObject, assertionCall));
    }
    String methodBody =
        String.format(
            PRIVATE_METHOD_BODY_TEMPLATE,
            packageName + "." + className,
            methodResult.methodName(),
            buildObjectParameterClasses(methodResult),
            assertStatement);
    this.testCaseBody.append(
        String.format(METHOD_TEMPLATE, methodName, REFLECTION_EXCEPTIONS, methodBody));
  }

  /**
   * Builds the string representation of method parameters' classes for reflection use. It is used
   * to specify parameter types when retrieving the method via reflection.
   *
   * @param methodResult The fuzzing method result containing parameter types.
   * @return A string representation of the method parameters' classes.
   */
  private String buildObjectParameterClasses(FuzzingMethodResult methodResult) {
    if (methodResult.methodParameters().isEmpty()) {
      return "";
    }
    return ", "
        + Arrays.stream(methodResult.parameterTypes())
            .map(type -> type.getCanonicalName() + ".class")
            .collect(Collectors.joining(", "));
  }

  /**
   * Builds the string representation of method parameters for method invocation. It handles special
   * cases for null parameters and differentiates between static and instance method invocations.
   *
   * @param methodResult The fuzzing method result containing the method's parameters.
   * @return A string representation of the method parameters.
   */
  private String buildMethodParameters(FuzzingMethodResult methodResult) {
    if (methodResult.methodParameters().isEmpty()) {
      return "";
    }
    List<String> parameters = methodResult.methodParameters();
    if (methodResult.isPrivate()
        && methodResult.throwException()
        && parameters.size() == 1
        && "null".equals(parameters.get(0))) {
      return ", (Object) null";
    }
    String prefix = methodResult.isPrivate() ? ", " : "";
    return prefix + String.join(", ", parameters);
  }

  /**
   * Determines the appropriate assertion method call based on the method result. It selects between
   * various assertions like assertEquals, assertTrue, etc., based on the method's expected outcome.
   *
   * @param methodResult The fuzzing method result.
   * @return The assertion method to be called.
   */
  private String buildAssertionCall(FuzzingMethodResult methodResult) {
    if (methodResult.throwException()) {
      return "assertThrows";
    }
    if (methodResult.expectedResult() == null) {
      return "assertNull";
    }
    if (isBooleanType(methodResult.returnType())) {
      return (Boolean) methodResult.expectedResult() ? "assertTrue" : "assertFalse";
    }
    if (isPrimitiveOrWrapperOrString(methodResult.returnType())) {
      return "assertEquals";
    }
    return "assertNotNull";
  }

  private boolean isBooleanType(Class<?> type) {
    return type.equals(Boolean.TYPE) || type.equals(Boolean.class);
  }

  private boolean isPrimitiveOrWrapperOrString(Class<?> type) {
    return type.isPrimitive()
        || type.equals(Integer.class)
        || type.equals(Double.class)
        || type.equals(Float.class)
        || type.equals(Long.class)
        || type.equals(Byte.class)
        || type.equals(Short.class)
        || type.equals(Character.class)
        || type.equals(String.class);
  }

  /**
   * Constructs the parameters for the assertion call, including handling special cases for method
   * invocation and expected results.
   *
   * @param methodResult The fuzzing method result.
   * @param callingObject The object on which the method is to be invoked, if applicable.
   * @param assertion The assertion method being called.
   * @return The parameters for the assertion call.
   */
  private String buildAssertionParameters(
      FuzzingMethodResult methodResult, String callingObject, String assertion) {
    String methodCallPrefix =
        methodResult.isStatic() ? packageName + "." + className + "." : "targetObject.";
    String methodCall =
        methodResult.isPrivate()
            ? String.format(
                REFLECTION_METHOD_CALL_TEMPLATE, callingObject, buildMethodParameters(methodResult))
            : methodCallPrefix
                + methodResult.methodName()
                + "("
                + buildMethodParameters(methodResult)
                + ")";
    if (methodResult.throwException()) {
      String exceptionType =
          methodResult.isPrivate()
              ? "InvocationTargetException"
              : methodResult.expectedException().getClass().getCanonicalName();
      return String.format("%s.class,() -> %s", exceptionType, methodCall);
    }
    String expectedResult = determineExpectedResult(methodResult, assertion);

    return expectedResult + methodCall;
  }

  /**
   * Determines the expected result based on the given method result and assertion. If the return
   * type of the method is boolean, returns "(Boolean)". Otherwise, if the assertion is not one of
   * the predefined types, converts the expected result of the method to a string representation.
   *
   * @param methodResult The result of the method under fuzzing.
   * @param assertion The assertion to be performed on the method result.
   * @return The expected result as a string representation or "(Boolean)" if the return type is
   *     boolean.
   */
  private String determineExpectedResult(FuzzingMethodResult methodResult, String assertion) {
    if ((methodResult.returnType().equals(Boolean.TYPE)
            || methodResult.returnType().equals(Boolean.class))
        && methodResult.isPrivate()) {
      return "(Boolean) ";
    } else {
      return isNoExpectedType(assertion)
          ? ""
          : convertObjectToString(methodResult.expectedResult()) + ", ";
    }
  }

  /**
   * Checks if the given assertion is not one of the predefined types.
   *
   * @param assertion The assertion to be checked.
   * @return {@code true} if the assertion is not one of the predefined types, {@code false}
   *     otherwise.
   */
  private boolean isNoExpectedType(String assertion) {
    return assertion.equals("assertNull")
        || assertion.equals("assertNotNull")
        || assertion.equals("assertTrue")
        || assertion.equals("assertFalse");
  }

  /**
   * Converts an object to a string representation, handling various special types.
   *
   * @param value The object to be converted.
   * @return The string representation of the object.
   */
  public static String convertObjectToString(Object value) {
    if (value == null) {
      return "null";
    } else if (value instanceof String str) {
      String escaped = str.replace("\\", "\\\\").replace("\"", "\\\"");
      return "\"" + escaped + "\"";
    } else if (value instanceof Long) {
      return value + "L";
    } else if (value instanceof Float) {
      return value + "F";
    } else if (value instanceof Double) {
      return value + "D";
    } else if (value instanceof Character) {
      return "'" + value + "'";
    } else if (value instanceof Byte byteValue) {
      return "0b" + Integer.toBinaryString(byteValue);
    } else {
      return value.toString();
    }
  }

  /**
   * Builds a test case class using the stored imports, class name, and test case body.
   *
   * @return A string representing the complete test case class.
   */
  public String build() {
    String importStatements = String.join("\n", imports);
    buildTestCaseBody();
    return String.format(CLASS_TEMPLATE, importStatements, className, testCaseBody);
  }
}
