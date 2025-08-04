package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.util.Random;

public class StringMutation {

  private final int maxMutations;
  private final Random rng = new Random();

  /**
   * Initializes a new {@code StringMutation} object.
   *
   * @param maxMutations The maximum number of mutations per round.
   * @author Jakob Edmaier
   */
  public StringMutation(int maxMutations) {
    this.maxMutations = maxMutations;
  }

  /**
   * Mutates the given string by inserting, replacing and removing random characters.
   *
   * @param input The input string.
   * @return The mutated string.
   * @author Jakob Edmaier
   */
  public String mutate(String input) {
    int mutations = rng.nextInt(1, maxMutations);
    String result = input;
    for (int i = 0; i < mutations; i++) {
      double d = rng.nextDouble();
      if (d < 0.33) {
        result = insertCharacter(result);
      } else if (d < 0.67) {
        result = replaceCharacter(result);
      } else {
        result = removeCharacter(result);
      }
    }
    return result;
  }

  /**
   * @author Jakob Edmaier
   */
  private String insertCharacter(String input) {
    if (input.isEmpty()) {
      return String.valueOf((char) rng.nextInt(256));
    }

    int position = rng.nextInt(input.length());
    char ch = (char) rng.nextInt(256);
    return input.substring(0, position) + ch + input.substring(position);
  }

  /**
   * @author Jakob Edmaier
   */
  private String replaceCharacter(String input) {
    if (input.isEmpty()) {
      return "";
    }

    int position = rng.nextInt(input.length());
    char ch = (char) rng.nextInt(256);
    return input.substring(0, position) + ch + input.substring(position + 1);
  }

  /**
   * @author Jakob Edmaier
   */
  private String removeCharacter(String input) {
    if (input.isEmpty()) {
      return "";
    }
    int position = rng.nextInt(input.length());
    return input.substring(0, position) + input.substring(position + 1);
  }
}
