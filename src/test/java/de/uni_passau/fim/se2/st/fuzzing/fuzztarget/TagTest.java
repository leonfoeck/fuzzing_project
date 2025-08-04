package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import static com.google.common.truth.Truth.assert_;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TagTest {

  @ParameterizedTest
  @MethodSource("provideTagsForToken")
  void test_getTagForToken(Tag expected, String token) {
    assert_().that(Tag.getTagForToken(token)).isEqualTo(Optional.of(expected));
  }

  @Test
  void test_getTagForNonExistingToken() {
    assert_().that(Tag.getTagForToken("nonExistingToken")).isEqualTo(Optional.empty());
  }

  private static Stream<Arguments> provideTagsForToken() {
    return Stream.of(
        Arguments.of(Tag.G, "G"),
        Arguments.of(Tag.S, "S"),
        Arguments.of(Tag.R, "R"),
        Arguments.of(Tag.F, "F"),
        Arguments.of(Tag.A, "A"),
        Arguments.of(Tag.V, "V"),
        Arguments.of(Tag.VG, "VG"),
        Arguments.of(Tag.MV, "MV"),
        Arguments.of(Tag.J, "J"),
        Arguments.of(Tag.BL, "BL"),
        Arguments.of(Tag.L, "L"),
        Arguments.of(Tag.W, "W"),
        Arguments.of(Tag.B, "B"));
  }
}
