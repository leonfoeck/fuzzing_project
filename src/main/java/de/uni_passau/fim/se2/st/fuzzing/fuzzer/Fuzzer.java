package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import com.google.common.collect.ImmutableSet;
import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.CSVParser;
import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code Fuzzer} class is responsible for fuzz testing, aiming to discover coding errors and
 * security loopholes in software by inputting massive amounts of random data ("fuzz") to the system
 * in an attempt to make it crash. It primarily focuses on fuzzing methods within the CSVParser
 * class.
 */
public class Fuzzer {

  private static final Logger logger = Logger.getLogger(Fuzzer.class.getName());
  private final StoppingCondition stoppingCondition;
  private double currentCoverage;
  private final Set<String> coveringStrings;
  private final Random rng = new Random();
  private final CSVGenerator csvGenerator = new CSVGenerator();
  private final StringMutation mutation = new StringMutation(20);

  private FuzzingClassResult fuzzingClassResults;

  /**
   * Constructs a new {@code Fuzzer} object with the given stopping condition.
   *
   * @param timeout the time limit for the fuzzing operation (currently ignored)
   * @param stoppingCondition the condition that determines when to stop the fuzzing loop
   */
  public Fuzzer(int timeout, StoppingCondition stoppingCondition) {
    this.stoppingCondition = stoppingCondition;
    currentCoverage = 0;
    coveringStrings = new LinkedHashSet<>();
  }

  /**
   * Run the legacy fuzzing loop.
   *
   * @author Jakob Edmaier
   */
  public void fuzz() {
    logger.log(Level.FINE, "Started fuzzing loop");
    stoppingCondition.start();

    while (!stoppingCondition.shouldStop()) {
      String candidate = chooseCandidate();

      try {
        CSVParser parser = new CSVParser();
        parser.parseDishes(new BufferedReader(new StringReader(candidate)));
      } catch (Exception e) {
        // Ignore
      }

      double newCoverage = CoverageTracker.getInstance().getCoverage();
      if (newCoverage > currentCoverage) {
        currentCoverage = newCoverage;
        coveringStrings.add(candidate);
        stoppingCondition.notify(newCoverage);
        logger.log(Level.FINE, "Found covering input, new coverage: {0}", newCoverage);
      }
    }
    logger.log(Level.FINE, "Stopped, total coverage: {0}", currentCoverage);
  }

  /**
   * Runs the fuzzing process for the specified target class. It randomly selects methods from the
   * target class and generates input data to execute and analyze the methods, aiming to maximize
   * code coverage.
   *
   * @param target the class to fuzz
   * @author Michael Ertl, Jakob Edmaier
   */
  public void fuzz(Class<?> target) {
    logger.fine("Started fuzzing loop");
    stoppingCondition.start();
    CSVGenerator generator = new CSVGenerator();
    List<FuzzingMethodResult> coveringMethods = new ArrayList<>();
    Method[] declaredMethods = target.getDeclaredMethods();
    declaredMethods =
        Arrays.stream(declaredMethods)
            .filter(m -> !m.isSynthetic() && !m.isBridge())
            .toArray(Method[]::new);

    while (!stoppingCondition.shouldStop()) {
      Method targetMethod = pickRandomMethod(declaredMethods);
      if (Modifier.isPrivate(targetMethod.getModifiers())) {
        targetMethod.setAccessible(true);
      }
      FuzzingMethodResult methodResult = executeAndAnalyzeMethod(target, targetMethod, generator);
      if (methodResult != null) {
        coveringMethods.add(methodResult);
        double newCoverage = CoverageTracker.getInstance().getCoverage();
        stoppingCondition.notify(newCoverage);
        String coverageLog = String.format("Found covering input, new coverage: %.4f", newCoverage);
        logger.fine(coverageLog);
      }
    }
    fuzzingClassResults =
        new FuzzingClassResult(target.getPackageName(), target.getSimpleName(), coveringMethods);
    String totalCoverage = String.format("Stopped, total coverage: %.4f", currentCoverage);
    logger.fine(totalCoverage);
  }

  /**
   * Executes the specified method on the target class and analyzes the result to calculate code
   * coverage. If an exception occurs during method execution, it is logged and handled.
   *
   * @param target the target class
   * @param targetMethod the method to execute and analyze
   * @param generator the CSV generator used to generate inputs for the method
   * @return a {@link FuzzingMethodResult} containing the results of the execution and analysis, or
   *     null if the execution did not result in new coverage
   * @author Michael Ertl, Jakob Edmaier
   */
  private FuzzingMethodResult executeAndAnalyzeMethod(
      Class<?> target, Method targetMethod, CSVGenerator generator) {
    List<String> methodParams = new ArrayList<>();
    Object callingObject = null;
    if (!Modifier.isStatic(targetMethod.getModifiers())) {
      // Instantiate the class only if the method is not static
      callingObject = generator.instantiateClass(target);
    }
    Object[] inputParams = initParams(targetMethod, generator);
    Object result = null;
    Throwable exception = null;
    boolean exceptionWasThrown = false;

    try {
      result = targetMethod.invoke(callingObject, inputParams);
    } catch (IllegalAccessException e) {
      logger.log(Level.WARNING, "Zugriff auf die Methode nicht möglich", e);
      return null;
    } catch (InvocationTargetException e) {
      exceptionWasThrown = true;
      exception = e.getTargetException();
    }

    for (Object param : inputParams) {
      methodParams.add(generator.getExpressionString(param));
    }

    double newCoverage = CoverageTracker.getInstance().getCoverage();
    if (newCoverage > currentCoverage) {
      currentCoverage = newCoverage;
      return new FuzzingMethodResult(
          targetMethod.getName(),
          methodParams,
          targetMethod.getParameterTypes(),
          generator.getExpressionString(callingObject),
          result,
          Modifier.isStatic(targetMethod.getModifiers()),
          Modifier.isPrivate(targetMethod.getModifiers()),
          (Exception) exception,
          exceptionWasThrown,
          targetMethod.getReturnType());
    }

    return null;
  }

  /**
   * Returns the current code coverage achieved by the fuzzing.
   *
   * @return the current coverage as a double
   */
  public double getCurrentCoverage() {
    return currentCoverage;
  }

  /**
   * Returns the results of the fuzzing process for each class that was fuzzed.
   *
   * @return a list of {@link FuzzingClassResult} objects
   * @author Leon Föckersperger
   */
  public FuzzingClassResult getFuzzingClassResults() {
    return fuzzingClassResults;
  }

  /**
   * Returns the set of strings that increased code coverage when used as input during fuzzing.
   *
   * @return an immutable set of strings that contributed to increased coverage
   */
  public Set<String> getCoveringStrings() {
    return ImmutableSet.copyOf(coveringStrings);
  }

  /**
   * Chooses a candidate string for fuzzing, either by mutating an existing string that increased
   * coverage or by generating a new random string.
   *
   * @return the chosen candidate string for fuzzing
   * @author Jakob Edmaier
   */
  private String chooseCandidate() {
    if (coveringStrings.isEmpty()) {
      return csvGenerator.provideRandomString();
    } else {
      // Either mutate existing input or create new one
      double threshold = 1.0 / Math.pow(2, coveringStrings.size());
      if (rng.nextDouble() < threshold) {
        return csvGenerator.provideRandomString();
      } else {
        int choice = rng.nextInt(coveringStrings.size());
        Iterator<String> iterator = coveringStrings.iterator();
        for (int i = 0; i < choice; i++) {
          iterator.next();
        }
        return mutation.mutate(iterator.next());
      }
    }
  }

  /**
   * Selects a random method from the array of methods.
   *
   * @param methods an array of {@link Method} objects
   * @return a randomly selected {@link Method} object
   * @author Michael Ertl
   */
  private Method pickRandomMethod(Method[] methods) {
    return methods[rng.nextInt(methods.length)];
  }

  /**
   * Initializes the parameters for the target method by instantiating objects of the required
   * types.
   *
   * @param target the target method
   * @param generator the CSV generator used to generate parameter instances
   * @return an array of initialized parameters for the method
   * @author Michael Ertl
   */
  private Object[] initParams(Method target, CSVGenerator generator) {
    Parameter[] p = target.getParameters();
    List<Object> actual = new ArrayList<>();
    for (Parameter parameter : p) {
      actual.add(generator.instantiateClass(parameter.getType()));
    }
    return actual.toArray();
  }
}
