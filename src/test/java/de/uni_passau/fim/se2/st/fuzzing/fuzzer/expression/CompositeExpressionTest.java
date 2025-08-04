package de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class CompositeExpressionTest {

  @Test
  public void testToString() {
    try {
      Constructor<?> constructor = BigInteger.class.getConstructor(String.class);
      GenericExpression argument = mock(LeafExpression.class);
      String inputString = "\"-12345\"";
      when(argument.toString()).thenReturn(inputString);
      CompositeExpression exp =
          new CompositeExpression(constructor, new GenericExpression[] {argument});
      assertEquals("new java.math.BigInteger(" + inputString + ")", exp.toString());
    } catch (NoSuchMethodException e) {
      fail();
    }
  }

  @Test
  public void testThrowsReflectiveOperationException() {
    try {
      Constructor<?> constructor = mock(Constructor.class);
      GenericExpression argument = mock(GenericExpression.class);
      when(argument.resolve()).thenThrow(ReflectiveOperationException.class);

      CompositeExpression composite =
          new CompositeExpression(constructor, new GenericExpression[] {argument});
      assertThrows(ReflectiveOperationException.class, composite::resolve);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
