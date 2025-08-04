package de.uni_passau.fim.se2.st.fuzzing.fuzzer.expression;

import de.uni_passau.fim.se2.st.fuzzing.fuzzer.TestCaseBuilder;

/**
 * Representation of a simple value. Simple values are ones that can be initialized with a literal
 * expression, for example integers, booleans and strings.
 */
public class LeafExpression implements GenericExpression {
  private final Object value;

  public LeafExpression(Object value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public Object resolve() {
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * @author Jakob Edmaier
   */
  @Override
  public String toString() {
    return TestCaseBuilder.convertObjectToString(value);
  }
}
