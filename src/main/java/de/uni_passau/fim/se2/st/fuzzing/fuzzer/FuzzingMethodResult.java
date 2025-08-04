package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The FuzzingMethodResult record holds the results of the fuzzing process for a single method. It
 * stores information about the method that is to be used for testing, including the method itself,
 * its parameters, the expected result, and other relevant details.
 *
 * @author Leon Föckersperger
 */
public record FuzzingMethodResult(
    String methodName,
    List<String> methodParameters,
    Class<?>[] parameterTypes,
    String constructor,
    Object expectedResult,
    boolean isStatic,
    boolean isPrivate,
    Exception expectedException,
    boolean throwException,
    Class<?> returnType) {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FuzzingMethodResult that = (FuzzingMethodResult) o;
    return isStatic == that.isStatic
        && isPrivate == that.isPrivate
        && throwException == that.throwException
        && Objects.equals(methodName, that.methodName)
        && Objects.equals(methodParameters, that.methodParameters)
        && Arrays.equals(parameterTypes, that.parameterTypes)
        && Objects.equals(constructor, that.constructor)
        && Objects.equals(expectedResult, that.expectedResult)
        && compareExceptions(expectedException, that.expectedException)
        && Objects.equals(returnType, that.returnType);
  }

  private boolean compareExceptions(Exception e1, Exception e2) {
    if (e1 == e2) return true;
    if (e1 == null || e2 == null) return false;
    if (!e1.getClass().equals(e2.getClass())) return false;
    return Objects.equals(e1.getMessage(), e2.getMessage());
  }

  @Override
  public int hashCode() {
    int result =
        Objects.hash(
            methodName,
            methodParameters,
            constructor,
            expectedResult,
            isStatic,
            isPrivate,
            expectedException,
            throwException,
            returnType);
    result = 31 * result + Arrays.hashCode(parameterTypes);
    return result;
  }

  @Override
  public String toString() {
    return "FuzzingMethodResult{"
        + "methodName='"
        + methodName
        + '\''
        + ", methodParameters="
        + methodParameters
        + ", parameterTypes="
        + Arrays.toString(parameterTypes)
        + ", constructor='"
        + constructor
        + '\''
        + ", expectedResult="
        + expectedResult
        + ", isStatic="
        + isStatic
        + ", isPrivate="
        + isPrivate
        + ", expectedException="
        + expectedException
        + ", throwException="
        + throwException
        + ", returnType="
        + returnType
        + '}';
  }
}
