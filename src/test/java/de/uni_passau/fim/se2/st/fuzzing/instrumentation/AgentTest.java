package de.uni_passau.fim.se2.st.fuzzing.instrumentation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.Objects;
import org.junit.jupiter.api.Test;

/**
 * @author Leon Föckersperger
 */
class AgentTest {

  private static final String SAMPLE_TEST_CLASS_PATH =
      "/de/uni_passau/fim/se2/st/fuzzing/fuzztarget/SampleTestClass.class";
  private static final byte[] EXPECTED_TRANSFORMED_BYTES = {
    -54, -2, -70, -66, 0, 0, 0, 65, 0, 42, 10, 0, 2, 0, 3, 7, 0, 4, 12, 0, 5, 0, 6, 1, 0, 16, 106,
    97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 1, 0, 6, 60, 105, 110, 105,
    116, 62, 1, 0, 3, 40, 41, 86, 9, 0, 8, 0, 9, 7, 0, 10, 12, 0, 11, 0, 12, 1, 0, 16, 106, 97, 118,
    97, 47, 108, 97, 110, 103, 47, 83, 121, 115, 116, 101, 109, 1, 0, 3, 111, 117, 116, 1, 0, 21,
    76, 106, 97, 118, 97, 47, 105, 111, 47, 80, 114, 105, 110, 116, 83, 116, 114, 101, 97, 109, 59,
    8, 0, 14, 1, 0, 13, 72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33, 10, 0, 16, 0,
    17, 7, 0, 18, 12, 0, 19, 0, 20, 1, 0, 19, 106, 97, 118, 97, 47, 105, 111, 47, 80, 114, 105, 110,
    116, 83, 116, 114, 101, 97, 109, 1, 0, 7, 112, 114, 105, 110, 116, 108, 110, 1, 0, 21, 40, 76,
    106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 7, 0, 22,
    1, 0, 59, 100, 101, 47, 117, 110, 105, 95, 112, 97, 115, 115, 97, 117, 47, 102, 105, 109, 47,
    115, 101, 50, 47, 115, 116, 47, 102, 117, 122, 122, 105, 110, 103, 47, 102, 117, 122, 122, 116,
    97, 114, 103, 101, 116, 47, 83, 97, 109, 112, 108, 101, 84, 101, 115, 116, 67, 108, 97, 115,
    115, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84,
    97, 98, 108, 101, 1, 0, 3, 97, 100, 100, 1, 0, 5, 40, 73, 73, 41, 73, 1, 0, 10, 112, 114, 105,
    110, 116, 72, 101, 108, 108, 111, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0,
    20, 83, 97, 109, 112, 108, 101, 84, 101, 115, 116, 67, 108, 97, 115, 115, 46, 106, 97, 118, 97,
    3, 0, 0, 0, 3, 1, 0, 65, 100, 101, 47, 117, 110, 105, 95, 112, 97, 115, 115, 97, 117, 47, 102,
    105, 109, 47, 115, 101, 50, 47, 115, 116, 47, 102, 117, 122, 122, 105, 110, 103, 47, 102, 117,
    122, 122, 116, 97, 114, 103, 101, 116, 47, 83, 97, 109, 112, 108, 101, 84, 101, 115, 116, 67,
    108, 97, 115, 115, 46, 99, 108, 97, 115, 115, 8, 0, 31, 1, 0, 57, 100, 101, 47, 117, 110, 105,
    95, 112, 97, 115, 115, 97, 117, 47, 102, 105, 109, 47, 115, 101, 50, 47, 115, 116, 47, 102, 117,
    122, 122, 105, 110, 103, 47, 99, 111, 118, 101, 114, 97, 103, 101, 47, 67, 111, 118, 101, 114,
    97, 103, 101, 84, 114, 97, 99, 107, 101, 114, 7, 0, 33, 1, 0, 14, 116, 114, 97, 99, 107, 76,
    105, 110, 101, 86, 105, 115, 105, 116, 1, 0, 22, 40, 73, 76, 106, 97, 118, 97, 47, 108, 97, 110,
    103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 12, 0, 35, 0, 36, 10, 0, 34, 0, 37, 3, 0, 0,
    0, 5, 3, 0, 0, 0, 9, 3, 0, 0, 0, 10, 0, 33, 0, 21, 0, 2, 0, 0, 0, 0, 0, 3, 0, 1, 0, 5, 0, 6, 0,
    1, 0, 23, 0, 0, 0, 36, 0, 2, 0, 1, 0, 0, 0, 12, 18, 30, 18, 32, -72, 0, 38, 42, -73, 0, 1, -79,
    0, 0, 0, 1, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, 3, 0, 1, 0, 25, 0, 26, 0, 1, 0, 23, 0, 0, 0, 35,
    0, 2, 0, 3, 0, 0, 0, 11, 18, 39, 18, 32, -72, 0, 38, 27, 28, 96, -84, 0, 0, 0, 1, 0, 24, 0, 0,
    0, 6, 0, 1, 0, 0, 0, 5, 0, 1, 0, 27, 0, 6, 0, 1, 0, 23, 0, 0, 0, 51, 0, 2, 0, 1, 0, 0, 0, 23,
    18, 40, 18, 32, -72, 0, 38, -78, 0, 7, 18, 13, -74, 0, 15, 18, 41, 18, 32, -72, 0, 38, -79, 0,
    0, 0, 1, 0, 24, 0, 0, 0, 10, 0, 2, 0, 0, 0, 9, 0, 15, 0, 10, 0, 1, 0, 28, 0, 0, 0, 2, 0, 29
  };

  @Test
  void testPremainAddsInstrumentationTransformer() {
    Instrumentation instrumentation = mock(Instrumentation.class);
    // Simulate the calling of the premain method
    Agent.premain("", instrumentation);
    // Verify that addTransformer was called once with any instance of InstrumentationTransformer
    verify(instrumentation, times(1)).addTransformer(any(Agent.InstrumentationTransformer.class));
  }

  @Test
  void testTransformIgnored() throws IOException {
    byte[] classBytes = readBytesFromClasspath();
    byte[] transformedBytes =
        new Agent.InstrumentationTransformer()
            .transform(null, "SampleTestClass.class", null, null, classBytes);
    assertNotNull(transformedBytes, "Transformed bytes should not be null");
    assertArrayEquals(
        classBytes, transformedBytes, "Transformed bytes did not match the original bytes");
  }

  @Test
  void testTransformIgnoredBecauseTestClass() throws IOException {
    byte[] classBytes = readBytesFromClasspath();
    byte[] transformedBytes =
        new Agent.InstrumentationTransformer()
            .transform(
                null,
                "de/uni_passau/fim/se2/st/fuzzing/fuzztarget/SampleTest",
                null,
                null,
                classBytes);
    assertNotNull(transformedBytes, "Transformed bytes should not be null");
    assertArrayEquals(
        classBytes, transformedBytes, "Transformed bytes did not match the original bytes");
  }

  @Test
  void testTransform() throws IOException {
    byte[] classBytes = readBytesFromClasspath();
    byte[] transformedBytes =
        new Agent.InstrumentationTransformer()
            .transform(
                null,
                "de/uni_passau/fim/se2/st/fuzzing/fuzztarget/SampleTestClass.class",
                null,
                null,
                classBytes);
    assertNotNull(transformedBytes, "Transformed bytes should not be null");
    assertArrayEquals(
        EXPECTED_TRANSFORMED_BYTES,
        transformedBytes,
        "Transformed bytes did not match expected bytes");
  }

  private byte[] readBytesFromClasspath() throws IOException {
    try (InputStream resourceStream =
        getClass().getResourceAsStream(AgentTest.SAMPLE_TEST_CLASS_PATH)) {
      return Objects.requireNonNull(resourceStream).readAllBytes();
    }
  }
}
