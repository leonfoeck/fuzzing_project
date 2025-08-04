package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

/** Models the type of {@link Dish}. */
enum DishType {
  APPETISER("Vorspeise"),
  MAIN("Hauptspeise"),
  SIDE("Beilage"),
  DESSERT("Dessert");

  private final String value;

  DishType(String value) {
    this.value = value;
  }

  /**
   * Provides the type of the {@link Dish}.
   *
   * @return The type of the {@link Dish}
   */
  public String getValue() {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("DishType %s", getValue());
  }
}
