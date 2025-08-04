package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

public class StringMutationTest {

  @Test
  public void testInsertCharacterEmptyInput() {
    try {
      StringMutation mutation = new StringMutation(1);

      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "insertCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String result = (String) method.invoke(mutation, "");
      assertEquals(1, result.length());
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testInsertCharacter() {
    try {
      StringMutation mutation = new StringMutation(1);

      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "insertCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String input = "input";
      String result = (String) method.invoke(mutation, input);
      assertEquals(input.length() + 1, result.length());
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testReplaceCharacterEmptyInput() {
    try {
      StringMutation mutation = new StringMutation(1);

      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "replaceCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String result = (String) method.invoke(mutation, "");
      assertEquals("", result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testReplaceCharacterNonEmptyInput() {
    try {
      StringMutation mutation = new StringMutation(1);

      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "replaceCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String input = "input";
      String result = (String) method.invoke(mutation, input);
      assertEquals(input.length(), result.length());
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testRemoveCharacter() {
    try {
      StringMutation mutation = new StringMutation(1);
      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "removeCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String input = "word";
      String result = (String) method.invoke(mutation, input);
      assertThat(List.of("ord", "wrd", "wod", "wor")).contains(result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testRemoveCharacterEmptyInput() {
    try {
      StringMutation mutation = new StringMutation(1);
      Method method =
          ReflectionUtils.findMethod(StringMutation.class, "removeCharacter", String.class)
              .orElseThrow();
      method.setAccessible(true);
      String result = (String) method.invoke(mutation, "");
      assertEquals("", result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }

  @Test
  public void testMutate() {
    int maxMutations = 10;
    Random rngMock = mock(Random.class);
    when(rngMock.nextInt(256)).thenReturn((int) 'x');
    when(rngMock.nextInt(1, maxMutations)).thenReturn(3);

    // Index at which char is inserted
    when(rngMock.nextInt(5)).thenReturn(0);

    // Indices at which chars are replaced and removed
    when(rngMock.nextInt(6)).thenReturn(2).thenReturn(4);

    // Insert first, then replace and remove last
    when(rngMock.nextDouble()).thenReturn(0.2).thenReturn(0.5).thenReturn(0.8);

    StringMutation mutation = new StringMutation(maxMutations);

    try {
      Field rngField =
          ReflectionUtils.findFields(
                  StringMutation.class,
                  p -> p.getName().equals("rng"),
                  ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
              .get(0);
      rngField.setAccessible(true);
      rngField.set(mutation, rngMock);

      String result = mutation.mutate("input");
      assertEquals("xixpt", result);
    } catch (ReflectiveOperationException e) {
      fail();
    }
  }
}
