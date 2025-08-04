package de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression;

/** Representation of an abstract expression. */
public interface GenericExpression {

  /** Evaluate the expression to create an actual {@code Object}. */
  Object resolve() throws ReflectiveOperationException;
}
