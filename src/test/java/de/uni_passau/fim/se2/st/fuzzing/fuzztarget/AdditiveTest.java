package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import static com.google.common.truth.Truth.assert_;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdditiveTest {

  @ParameterizedTest
  @MethodSource("provideAdditivesForIndex")
  void test_getAdditiveForIndex(Additive expected, int index) {
    assert_().that(Additive.getAdditiveForIndex(index)).isEqualTo(Optional.of(expected));
  }

  @Test
  void test_getAdditiveForNonExistingIndex() {
    assert_().that(Additive.getAdditiveForIndex(-1)).isEqualTo(Optional.empty());
    assert_().that(Additive.getAdditiveForIndex(18)).isEqualTo(Optional.empty());
  }

  private static Stream<Arguments> provideAdditivesForIndex() {
    return Stream.of(
        Arguments.of(Additive.A, 1),
        Arguments.of(Additive.B, 2),
        Arguments.of(Additive.C, 3),
        Arguments.of(Additive.D, 4),
        Arguments.of(Additive.E, 5),
        Arguments.of(Additive.F, 6),
        Arguments.of(Additive.G, 7),
        Arguments.of(Additive.H, 8),
        Arguments.of(Additive.I, 9),
        Arguments.of(Additive.J, 10),
        Arguments.of(Additive.K, 11),
        Arguments.of(Additive.L, 12),
        Arguments.of(Additive.M, 13),
        Arguments.of(Additive.N, 14),
        Arguments.of(Additive.O, 15),
        Arguments.of(Additive.P, 16),
        Arguments.of(Additive.Q, 17));
  }
}
