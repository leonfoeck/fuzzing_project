package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression.CompositeExpression;
import de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression.GenericExpression;
import de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression.LeafExpression;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class CSVGenerator implements InputGenerator {

  private static final int MAX_STRING_LENGTH = 255;
  private static final int MAX_BIGINT_DIGITS = 20;
  private static final int MAX_LINES = 20;
  private static final int MAX_FIELDS = 12;
  private static final String[] additives = {
    "A", "AA", "AB", "AC", "AD", "AE", "AF", "B", "C", "D", "E", "F", "G", "H", "HA", "HB", "HC",
    "HD", "HE", "HF", "HG", "HH", "HI", "I", "J", "K", "L", "M", "N", "O", "P"
  };
  private static final String[] allergens = {
    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"
  };
  private static final String[] tags = {
    "G", "S", "R", "F", "A", "V", "VG", "MV", "J", "BL", "L", "W", "B"
  };
  private static final String[] dishTypes = {"Suppe", "HG", "B", "N"};

  private final Random rng = new Random();
  private final Map<Object, String> memory = new HashMap<>();
  private final CharSequence delimiter;

  /**
   * Initializes a new {@code CSVGenerator}, using the default delimiter {@code ";"}.
   *
   * @author Jakob Edmaier
   */
  public CSVGenerator() {
    this(";");
  }

  /**
   * Initializes a new {@code CSVGenerator}, using the given delimiter.
   *
   * @param delimiter The delimiter to use.
   * @author Jakob Edmaier
   */
  public CSVGenerator(CharSequence delimiter) {
    this.delimiter = delimiter;
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public String provideRandomString() {
    int numberOfLines = rng.nextInt(MAX_LINES);
    int numberOfFields = rng.nextInt(MAX_FIELDS);

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < numberOfLines; i++) {
      sb.append(randomCSVRow(numberOfFields));
      if (i < numberOfLines - 1) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  /**
   * Provides a random object of the specified class type.
   *
   * @param classType The class type of the object to be generated.
   * @return A random instance of the specified class type.
   * @author Michael Ertl, Jakob Edmaier
   */
  @Override
  public Object provideRandomInput(Class<?> classType) {
    try {
      GenericExpression expression = buildExpression(classType);
      Object resolved = expression.resolve();
      memory.put(resolved, expression.toString());
      return resolved;
    } catch (ReflectiveOperationException e) {
      return null;
    }
  }

  /**
   * @author Michael Ertl, Leon Föckersperger
   */
  private String generateRandomString() {
    int length = rng.nextInt(MAX_STRING_LENGTH);
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      // Choose random printable character (code points 32 - 126)
      char ch = (char) (rng.nextInt(95) + 32);
      sb.append(ch);
    }
    return sb.toString();
  }

  /**
   * @author Michael Ertl, Leon Föckersperger, Jakob Edmaier
   */
  private GenericExpression buildExpression(Class<?> classType) {
    // Use a map of strategies for each class type to reduce conditional complexity.
    Map<Class<?>, Supplier<GenericExpression>> strategies = new HashMap<>();
    strategies.put(Integer.class, () -> new LeafExpression(rng.nextInt()));
    strategies.put(int.class, () -> new LeafExpression(rng.nextInt()));
    strategies.put(Short.class, () -> new LeafExpression((short) rng.nextInt(Short.MAX_VALUE + 1)));
    strategies.put(short.class, () -> new LeafExpression((short) rng.nextInt(Short.MAX_VALUE + 1)));
    strategies.put(Long.class, () -> new LeafExpression(rng.nextLong()));
    strategies.put(long.class, () -> new LeafExpression(rng.nextLong()));
    strategies.put(Byte.class, () -> new LeafExpression((byte) rng.nextInt(Byte.MAX_VALUE + 1)));
    strategies.put(byte.class, () -> new LeafExpression((byte) rng.nextInt(Byte.MAX_VALUE + 1)));
    strategies.put(Float.class, () -> new LeafExpression(rng.nextFloat()));
    strategies.put(float.class, () -> new LeafExpression(rng.nextFloat()));
    strategies.put(Double.class, () -> new LeafExpression(rng.nextDouble()));
    strategies.put(double.class, () -> new LeafExpression(rng.nextDouble()));
    strategies.put(Character.class, () -> new LeafExpression((char) (rng.nextInt(26) + 'a')));
    strategies.put(char.class, () -> new LeafExpression((char) (rng.nextInt(26) + 'a')));
    strategies.put(Boolean.class, () -> new LeafExpression(rng.nextBoolean()));
    strategies.put(boolean.class, () -> new LeafExpression(rng.nextBoolean()));
    strategies.put(String.class, () -> new LeafExpression(generateRandomString()));
    strategies.put(Object.class, () -> new LeafExpression(null));
    strategies.put(BigInteger.class, this::createBigIntegerExpression);

    // Attempt to get a strategy for the provided classType.
    Supplier<GenericExpression> strategy = strategies.get(classType);
    if (strategy != null) {
      return strategy.get();
    } else {
      // Handle cases not covered by the strategy map.
      return buildComplexExpression(classType);
    }
  }

  private GenericExpression createBigIntegerExpression() {
    try {
      Constructor<?> constructor = BigInteger.class.getConstructor(String.class);
      GenericExpression[] arguments =
          new GenericExpression[] {new LeafExpression(randomNumericString())};
      return new CompositeExpression(constructor, arguments);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException();
    }
  }

  /**
   * @author Jakob Edmaier
   */
  private GenericExpression buildComplexExpression(Class<?> classType) {
    Constructor<?>[] constructors = classType.getConstructors();
    if (constructors.length == 0) {
      // Object cannot be created
      return new LeafExpression(null);
    }

    Parameter[] parameters = constructors[0].getParameters();
    GenericExpression[] arguments = new GenericExpression[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      arguments[i] = buildExpression(parameters[i].getType());
    }
    return new CompositeExpression(constructors[0], arguments);
  }

  /**
   * @param classType The type of class to be instantiated
   * @return Returns an Object of the given Class.
   * @author Michael Ertl, Jakob Edmaier
   */
  @Override
  public Object instantiateClass(Class<?> classType) {
    return provideRandomInput(classType);
  }

  /**
   * Clears the memory of the CSVGenerator, i.e., deletes the recorded constructor calls.
   *
   * @author Jakob Edmaier
   */
  public void clearMemory() {
    memory.clear();
  }

  /**
   * Returns the recorded constructor call that was made to create the given object (possibly
   * including nested constructor calls). If no constructor call is recorded for the given object,
   * {@code null} is returned.
   *
   * @param object The object
   * @return The constructor call as a string.
   * @author Jakob Edmaier
   */
  public String getExpressionString(Object object) {
    return memory.get(object);
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomCSVRow(int numberOfFields) {
    StringBuilder sb = new StringBuilder();
    String[] values = {
      randomDateString(),
      randomWeekday(),
      randomDishType(),
      randomDishName(),
      randomListOfTags(),
      randomPrice(),
      randomPrice(),
      randomPrice()
    };

    for (int i = 0; i < numberOfFields; i++) {
      if (i < values.length) {
        sb.append(values[i]);
      } else {
        sb.append(randomAlpabeticString());
      }

      if (i < numberOfFields - 1) {
        sb.append(delimiter);
      }
    }
    return sb.toString();
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomDateString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate origin = LocalDate.of(2000, 1, 1);
    int days = rng.nextInt(30 * 365);
    LocalDate choice = origin.plusDays(days);
    return choice.format(formatter);
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomWeekday() {
    String[] weekdays = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
    return weekdays[rng.nextInt(weekdays.length)];
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomDishType() {
    return dishTypes[rng.nextInt(4)];
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomDishName() {
    StringBuilder sb = new StringBuilder();
    sb.append(randomAlpabeticString());
    if (rng.nextBoolean()) {
      sb.append(" (");
      sb.append(randomListOfAdditivesAndAllergens());
      sb.append(')');
    }
    return sb.toString();
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomListOfAdditivesAndAllergens() {
    String additivesString = randomList(additives, 6);
    String allergensString = randomList(allergens, 6);

    if (!additivesString.isEmpty() && !allergensString.isEmpty()) {
      return additivesString + "," + allergensString;
    } else {
      return additivesString + allergensString;
    }
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomListOfTags() {
    return randomList(tags, 5);
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomList(String[] values, int maxLength) {
    int length = rng.nextInt(maxLength);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int index = rng.nextInt(values.length);
      sb.append(values[index]);
      if (i < length - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomPrice() {
    double d = 10 * Math.random();
    return String.format(Locale.GERMAN, "%.2f", d);
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomAlpabeticString() {
    String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";

    int length = rng.nextInt(25);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      char ch = alphabet.charAt(rng.nextInt(alphabet.length()));
      sb.append(ch);
    }
    return sb.toString();
  }

  /**
   * @author Jakob Edmaier
   */
  private String randomNumericString() {
    int length = rng.nextInt(MAX_BIGINT_DIGITS);
    if (length == 0) {
      return "0";
    } else {
      StringBuilder sb = new StringBuilder(length);
      if (rng.nextBoolean()) {
        sb.append("-");
      }

      // First digit should not be 0
      sb.append(rng.nextInt(1, 10));
      for (int i = 1; i < length; i++) {
        int nextDigit = rng.nextInt(10);
        sb.append(nextDigit);
      }
      return sb.toString();
    }
  }
}
