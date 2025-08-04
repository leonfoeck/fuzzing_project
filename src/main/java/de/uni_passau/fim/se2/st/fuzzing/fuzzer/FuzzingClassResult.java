package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.util.List;

/**
 * The FuzzingClassResult record holds the results of the fuzzing process for an entire class. It
 * stores information about the class and the results of individual methods that were analyzed
 * during the fuzzing process.
 *
 * @author Leon Föckersperger
 */
public record FuzzingClassResult(
    String packageName, String className, List<FuzzingMethodResult> methodResults) {}
