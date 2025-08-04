package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import java.util.Arrays;
import java.util.Optional;

/** Models the tags of a {@link Dish}. */
enum Tag {
  G("Geflügel"),
  S("Schweinefleish"),
  R("Rindfleisch"),
  F("Fisch"),
  A("Alkohol"),
  V("Vegetarisch"),
  VG("Vegan"),
  MV("Mensa Vital"),
  J("Juradistl"),
  BL("Bioland"),
  L("Lamm"),
  W("Wild"),
  B("DE-ÖKO-006 mit ausschließlich biologisch erzeugten Rohstoffen");

  private final String tag;

  Tag(String tag) {
    this.tag = tag;
  }

  /**
   * Provides the name of a tag.
   *
   * @return The name of the tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Provides a tag for a token.
   *
   * @param token The token of the tag
   * @return An optional tag if one is found with the given token
   */
  public static Optional<Tag> getTagForToken(String token) {
    return Arrays.stream(values()).filter(t -> t.name().equals(token)).findFirst();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getTag();
  }
}
