package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import java.util.Arrays;
import java.util.Optional;

/** Models the allergens of a {@link Dish}. */
enum Allergen {
  A("Gluten"),
  AA("Weizengluten"),
  AB("Roggengluten"),
  AC("Gerstengluten"),
  AD("Hafergluten"),
  AE("Dinkelgluten"),
  AF("Kamutgluten"),
  B("Krebstiere"),
  C("Eier"),
  D("Fisch"),
  E("Erdnüsse"),
  F("Soja"),
  G("Milch und Milchprodukte"),
  H("Nuss"),
  HA("Mandel"),
  HB("Haselnuss"),
  HC("Walnuss"),
  HD("Cashew"),
  HE("Pecanuss"),
  HF("Paranuss"),
  HG("Pistazie"),
  HH("Macadamianuss"),
  HI("Queenslandnuss"),
  I("Sellerie"),
  J("Senf"),
  K("Sesamsamen"),
  L("Schwefeldioxid und Sulfite"),
  M("Lupinen"),
  N("Weichtiere"),
  O("Nitrat"),
  P("Nitritpökelsalz");

  private final String allergen;

  Allergen(String allergen) {
    this.allergen = allergen;
  }

  /**
   * Provides the name of the allergen.
   *
   * @return The name of the allergen
   */
  public String getAllergen() {
    return allergen;
  }

  /**
   * Provides an allergen for a given token.
   *
   * @param token The token of the allergen
   * @return An optional allergen if one is found for the given token
   */
  public static Optional<Allergen> getAllergenForToken(String token) {
    return Arrays.stream(values()).filter(a -> a.name().equals(token)).findFirst();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getAllergen();
  }
}
