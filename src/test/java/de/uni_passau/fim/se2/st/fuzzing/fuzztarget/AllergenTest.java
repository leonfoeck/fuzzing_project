package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import static com.google.common.truth.Truth.assert_;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AllergenTest {

  @ParameterizedTest
  @MethodSource("provideAllergensForToken")
  void test_getAllergenForToken(Allergen expected, String token) {
    assert_().that(Allergen.getAllergenForToken(token)).isEqualTo(Optional.of(expected));
  }

  @Test
  void test_getAllergenForNonExistingToken() {
    assert_().that(Allergen.getAllergenForToken("non-existing")).isEqualTo(Optional.empty());
  }

  private static Stream<Arguments> provideAllergensForToken() {
    return Stream.of(
        Arguments.of(Allergen.A, "A"),
        Arguments.of(Allergen.AA, "AA"),
        Arguments.of(Allergen.AB, "AB"),
        Arguments.of(Allergen.AC, "AC"),
        Arguments.of(Allergen.AD, "AD"),
        Arguments.of(Allergen.AE, "AE"),
        Arguments.of(Allergen.AF, "AF"),
        Arguments.of(Allergen.B, "B"),
        Arguments.of(Allergen.C, "C"),
        Arguments.of(Allergen.D, "D"),
        Arguments.of(Allergen.E, "E"),
        Arguments.of(Allergen.F, "F"),
        Arguments.of(Allergen.G, "G"),
        Arguments.of(Allergen.H, "H"),
        Arguments.of(Allergen.HA, "HA"),
        Arguments.of(Allergen.HB, "HB"),
        Arguments.of(Allergen.HC, "HC"),
        Arguments.of(Allergen.HD, "HD"),
        Arguments.of(Allergen.HE, "HE"),
        Arguments.of(Allergen.HF, "HF"),
        Arguments.of(Allergen.HG, "HG"),
        Arguments.of(Allergen.HH, "HH"),
        Arguments.of(Allergen.HI, "HI"),
        Arguments.of(Allergen.I, "I"),
        Arguments.of(Allergen.J, "J"),
        Arguments.of(Allergen.K, "K"),
        Arguments.of(Allergen.L, "L"),
        Arguments.of(Allergen.M, "M"),
        Arguments.of(Allergen.N, "N"),
        Arguments.of(Allergen.O, "O"),
        Arguments.of(Allergen.P, "P"));
  }
}
