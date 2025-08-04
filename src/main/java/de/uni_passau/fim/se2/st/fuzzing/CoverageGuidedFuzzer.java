package de.uni_passau.fim.se2.st.fuzzing;

import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker;
import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker.ClassTracker;
import de.uni_passau.fim.se2.st.fuzzing.coverage.OutputWriter;
import de.uni_passau.fim.se2.st.fuzzing.fuzzer.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

/**
 * The CoverageGuidedFuzzer class provides a command-line interface to execute fuzzing tests on a
 * given class. It initializes the fuzzing process, executes the fuzzing, and handles the results by
 * writing the outputs to specified files. It also provides options to configure the fuzzing
 * operation like setting a timeout, specifying the class and package to fuzz, and toggling the
 * verbosity of the output.
 *
 * @author Michael Ertl, Jakob Edmaier, Leon Föckersperger
 */
public class CoverageGuidedFuzzer implements Callable<Integer> {

  @Spec CommandSpec spec;
  private long timeout;
  private boolean quiet;
  private String targetClass;
  private String targetPackage;

  private final Logger logger = Logger.getLogger(CoverageGuidedFuzzer.class.getName());

  public CoverageGuidedFuzzer() {
    quiet = false;
  }

  public CoverageGuidedFuzzer(boolean quiet) {
    setTimeout(timeout);
    setQuiet(quiet);
  }

  /**
   * Main method that initializes and executes the fuzzer with provided command-line arguments.
   *
   * @param args The command-line arguments.
   */
  public static void main(String[] args) {
    int exitCode = new CommandLine(new CoverageGuidedFuzzer()).execute(args);
    System.exit(exitCode);
  }

  /**
   * Executes the fuzzing process. It includes validating the configuration, setting the log level,
   * initializing the fuzzer, executing the fuzzing, generating tests, and writing outputs.
   *
   * @return System error code. Returns 0 for successful execution and one if the target class is
   *     not found.
   * @throws IOException When an error occurs during output writing.
   * @author Michael Ertl, Jakob Edmaier, Leon Föckersperger
   */
  @Override
  public Integer call() throws IOException {
    validateConfiguration();
    setLogLevel(this.quiet);
    try {
      Class<?> target = Class.forName(targetPackage + "." + targetClass);
      Fuzzer fuzzer = new Fuzzer(0, new TimeoutStoppingCondition(timeout * 1000));
      fuzzer.fuzz(target);
      TestGenerator testGenerator = new UnitTestGenerator(new FileSystemOperations());
      testGenerator.generateTestClass(fuzzer.getFuzzingClassResults());
      writeOutputs();
      return 0;
    } catch (UnableToWriteTestFile e) {
      logger.severe(() -> "Unable to write test file for class: " + targetClass);
      return 1;
    } catch (ClassNotFoundException e) {
      logger.severe(() -> "Target class not found: " + targetClass);
      return 2;
    }
  }

  /**
   * Writes the output of the fuzzing process. It includes writing the shell output, XML output, and
   * HTML output.
   *
   * @throws IOException When an error occurs during writing.
   */
  private void writeOutputs() throws IOException {
    Path current = Paths.get(".").toAbsolutePath();
    Path target = current.resolve("fuzzing-report");
    Map<String, ClassTracker> classTrackers = CoverageTracker.getInstance().getClassTrackers();
    if (!quiet) {
      OutputWriter.writeShellOutput(classTrackers);
    }
    OutputWriter.writeXMLOutput(classTrackers, target.resolve("coverage.xml"));
    OutputWriter.writeHTMLOutput(
        classTrackers, current.resolve("src").resolve("main").resolve("java"), target);
  }

  /**
   * Validates the configuration by ensuring that the target class is provided.
   *
   * @throws IllegalArgumentException If the target class is not provided.
   * @author Leon Föckersperger
   */
  private void validateConfiguration() {
    if (targetClass == null || targetClass.isEmpty()) {
      throw new IllegalArgumentException("Target class must be provided.");
    }
  }

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public void setTargetPackage(String targetPackage) {
    this.targetPackage = targetPackage;
  }

  /**
   * Sets the timeout for the fuzzing run.
   *
   * @param timeout The timeout in seconds.
   * @throws ParameterException If the timeout is not a positive integer.
   */
  // @formatter:off
  @Option(
      names = {"-t", "--timeout"},
      description = "Timeout in seconds for each fuzzing run.",
      defaultValue = "10")
  // @formatter:on
  public void setTimeout(long timeout) {
    if (timeout < 0) {
      throw new ParameterException(spec.commandLine(), "Timeout must be a positive integer.");
    }
    this.timeout = timeout;
  }

  /**
   * Sets the verbosity of the output.
   *
   * @param quiet If true, the output will be minimal.
   */
  // @formatter:off
  @Option(
      names = {"-q", "--quiet"},
      description = "Do not print any output.",
      defaultValue = "false")
  // @formatter:on
  public void setQuiet(boolean quiet) {
    this.quiet = quiet;
  }

  /**
   * Sets the name of the class to be fuzzed.
   *
   * @param className The name of the class.
   * @author Jakob Edmaier
   */
  @Option(
      names = {"-c", "--class"},
      description = "Name of the class to fuzz.",
      required = true)
  // @formatter:on
  public void setClass(String className) {
    this.targetClass = className;
  }

  /**
   * Sets the name of the package that contains the class to be fuzzed.
   *
   * @param packageName The name of the package.
   * @author Jakob Edmaier
   */
  @Option(
      names = {"-p", "--package"},
      description = "Name of the package that contains the class to fuzz.",
      required = true)
  // @formatter:on
  public void setPackage(String packageName) {
    this.targetPackage = packageName;
  }

  /**
   * Sets the log level of the application based on the verbosity setting.
   *
   * @param quiet If true, sets the log level to INFO, else sets to FINE.
   */
  private void setLogLevel(boolean quiet) {
    Logger rootLogger = LogManager.getLogManager().getLogger("");
    Level level = quiet ? Level.INFO : Level.FINE;
    rootLogger.setLevel(level);
    for (Handler handler : rootLogger.getHandlers()) {
      handler.setLevel(level);
    }
  }
}
