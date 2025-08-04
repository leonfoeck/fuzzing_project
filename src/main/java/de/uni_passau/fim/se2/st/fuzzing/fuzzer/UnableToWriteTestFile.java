package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.io.Serial;

/**
 * @author Leon Föckersperger
 */
public class UnableToWriteTestFile extends Exception {

  @Serial private static final long serialVersionUID = 1L;

  public UnableToWriteTestFile() {
    super();
  }

  public UnableToWriteTestFile(String message) {
    super(message);
  }

  public UnableToWriteTestFile(String message, Throwable cause) {
    super(message, cause);
  }

  public UnableToWriteTestFile(Throwable cause) {
    super(cause);
  }
}
