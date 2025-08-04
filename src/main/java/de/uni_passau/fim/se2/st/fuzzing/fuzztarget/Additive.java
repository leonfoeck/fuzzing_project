package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import java.util.Optional;

/** Models the additives of a {@link Dish}. */
enum Additive {
  A("Farbstoff"),
  B("Konservierungsstoff"),
  C("Antioxidationsmittel"),
  D("Geschmacksverstärker"),
  E("Geschwefelt"),
  F("Geschwärzt"),
  G("Gewachst"),
  H("Phosphat"),
  I("Säuerungsmittel Saccharin"),
  J("Säuerungsmittel Aspartam (enthält Phenylalaninquelle)"),
  K("Säuerungsmittel Cyclamat"),
  L("Säuerungsmittel Acesulfam"),
  M("Chininhaltig"),
  N("Coffeinhaltig"),
  O("Gentechnisch verändert"),
  P("Sulfide"),
  Q("Phenylalanin");

  private final String additive;

  Additive(String additive) {
    this.additive = additive;
  }

  /**
   * Provides the name of the additive.
   *
   * @return The name of the additive
   */
  public String getAdditive() {
    return additive;
  }

  /**
   * Provides the additive for a givven index.
   *
   * @param index The index of the additive
   * @return An optional additive if one is found
   */
  public static Optional<Additive> getAdditiveForIndex(int index) {
    if ((index > 0) && (index < 18)) {
      return Optional.of(Additive.valueOf(String.valueOf((char) (64 + index))));
    }
    return Optional.empty();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getAdditive();
  }
}
