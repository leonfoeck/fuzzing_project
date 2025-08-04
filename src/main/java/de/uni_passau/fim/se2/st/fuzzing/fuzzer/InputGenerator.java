package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

/** An interface for the input generator to a fuzzer. */
public interface InputGenerator {

  /**
   * Provides a random string that can be used as an input to the fuzzer.
   *
   * @return A random string
   */
  String provideRandomString();

  /**
   * Provides a random input based on the class type.
   *
   * @param classType The type of input to be generated
   * @return An Object of the same type as classType
   */
  Object provideRandomInput(Class<?> classType);

  /**
   * Instantiates an object based on the first constructor.
   *
   * @param classType The type of class to be instantiated
   * @return An Object of the same type as classType
   */
  Object instantiateClass(Class<?> classType);
}
