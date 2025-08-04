package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.CSVReader.CSVRow;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** A parser for the CSV files. */
public class CSVParser {

  private final DateTimeFormatter dateTimeFormatter;

  public CSVParser() {
    dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  }

  public List<Dish> parseDishes(final BufferedReader csvContent) {
    ImmutableList.Builder<Dish> dishBuilder = new Builder<>();
    CSVReader reader = new CSVReader(csvContent);
    for (CSVRow row : reader) {
      dishBuilder.add(buildDish(row));
    }
    return dishBuilder.build();
  }

  private Dish buildDish(CSVRow row) {
    LocalDate date = extractDate(row.get(0));
    DishType type = extractDishType(row.get(2));
    BigDecimal studentPrice = new BigDecimal(replaceDecimalSeparator(row.get(5)));
    BigDecimal staffPrice = new BigDecimal(replaceDecimalSeparator(row.get(6)));
    BigDecimal guestPrice = new BigDecimal(replaceDecimalSeparator(row.get(7)));
    String name = extractDishName(row.get(3));

    int bracketIndex = row.get(3).indexOf('(');
    String additivesAllergens = row.get(3).substring(bracketIndex + 1, row.get(3).length() - 1);

    Dish.Builder builder = new Dish.Builder();
    return builder
        .setDate(date)
        .setType(type)
        .setName(name)
        .setAdditives(extractAdditives(additivesAllergens))
        .setAllergens(extractAllergens(additivesAllergens))
        .setTags(extractTags(row.get(4)))
        .setStudentPrice(studentPrice)
        .setStaffPrice(staffPrice)
        .setGuestPrice(guestPrice)
        .build();
  }

  private String replaceDecimalSeparator(String value) {
    return value.replace(',', '.');
  }

  private DishType extractDishType(String element) {
    final DishType type;
    if (element.equals("Suppe")) {
      type = DishType.APPETISER;
    } else if (element.startsWith("HG")) {
      type = DishType.MAIN;
    } else if (element.startsWith("B")) {
      type = DishType.SIDE;
    } else if (element.startsWith("N")) {
      type = DishType.DESSERT;
    } else {
      throw new IllegalArgumentException("Unknown dish type: " + element);
    }
    return type;
  }

  private LocalDate extractDate(String element) {
    return LocalDate.parse(element, dateTimeFormatter);
  }

  private String extractDishName(String element) {
    String[] elementParts = element.split("[()]");
    Preconditions.checkState(elementParts.length > 0);
    return elementParts[0].strip();
  }

  private Collection<Additive> extractAdditives(String element) {
    return Arrays.stream(element.split(","))
        .filter(c -> c.matches("\\d+"))
        .map(Integer::parseInt)
        .map(Additive::getAdditiveForIndex)
        .flatMap(Optional::stream)
        .toList();
  }

  private Collection<Allergen> extractAllergens(String element) {
    return Arrays.stream(element.split(","))
        .filter(c -> c.matches("[A-Z]+"))
        .map(Allergen::getAllergenForToken)
        .flatMap(Optional::stream)
        .toList();
  }

  private Collection<Tag> extractTags(String element) {
    return Arrays.stream(element.split(","))
        .map(Tag::getTagForToken)
        .flatMap(Optional::stream)
        .toList();
  }
}
