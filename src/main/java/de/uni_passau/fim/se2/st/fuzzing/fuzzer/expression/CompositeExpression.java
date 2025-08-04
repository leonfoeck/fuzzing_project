package de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression;

import java.lang.reflect.Constructor;

/** Representation of a constructor call. */
public class CompositeExpression implements GenericExpression {
  private final Constructor<?> constructor;
  private final GenericExpression[] arguments;

  public CompositeExpression(Constructor<?> constructor, GenericExpression[] arguments) {
    this.constructor = constructor;
    this.arguments = arguments;
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("new ");
    sb.append(constructor.getDeclaringClass().getName());
    sb.append('(');
    for (int i = 0; i < arguments.length; i++) {
      sb.append(arguments[i].toString());
      if (i < arguments.length - 1) {
        sb.append(", ");
      }
    }
    sb.append(')');
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public Object resolve() throws ReflectiveOperationException {
    Object[] resolvedArgs = new Object[arguments.length];
    for (int i = 0; i < arguments.length; i++) {
      resolvedArgs[i] = arguments[i].resolve();
    }
    return constructor.newInstance(resolvedArgs);
  }
}
