package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

/**
 * The TestGenerator interface defines a method for generating test classes based on the results of
 * fuzzing operations on classes.
 *
 * @author Leon Föckersperger
 */
public interface TestGenerator {

  /**
   * Generates a test class for the given fuzzing class result. Implementations of this method
   * should be capable of generating a test class that contains test cases for the methods of the
   * given class based on the fuzzing results.
   *
   * @param fuzzingClassResult the result of the fuzzing process for a class.
   */
  void generateTestClass(FuzzingClassResult fuzzingClassResult) throws UnableToWriteTestFile;
}
