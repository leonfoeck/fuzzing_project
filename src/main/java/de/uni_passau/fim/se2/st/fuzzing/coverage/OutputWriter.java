package de.uni_passau.fim.se2.st.fuzzing.coverage;

import de.uni_passau.fim.se2.st.fuzzing.coverage.CoverageTracker.ClassTracker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OutputWriter {

  private OutputWriter() {}

  public static void writeShellOutput(Map<String, ClassTracker> trackerMap) {
    for (Entry<String, ClassTracker> tracker : trackerMap.entrySet()) {
      String className = tracker.getKey();
      ClassTracker classTracker = tracker.getValue();
      Set<Integer> lines = classTracker.getLines();
      Map<Integer, Integer> visitedLines = classTracker.getVisitedLines();

      int linesSize = lines.size();
      int visitedLinesSize = visitedLines.size();

      System.out.printf("Coverage Data for Class: %s%n", className);

      for (Entry<Integer, Integer> entry : visitedLines.entrySet()) {
        System.out.printf("    line %d visited %d times%n", entry.getKey(), entry.getValue());
      }

      System.out.printf("    number of lines %d%n", linesSize);
      System.out.printf(
          "    line coverage %2.2f%% (%d/%d)%n",
          (float) visitedLinesSize / linesSize * 100, visitedLinesSize, linesSize);
    }
  }

  /**
   * Writes the coverage information into a xml file.
   *
   * @param trackerMap Map containing the name of the class and its coverage information
   * @param xmlPath Path where the xml file should be stored
   * @throws IOException When an error during writing occurs
   * @author Michael Ertl
   */
  public static void writeXMLOutput(Map<String, ClassTracker> trackerMap, Path xmlPath)
      throws IOException {

    // Append the desired file name ("report.xml") to the output path
    Files.deleteIfExists(xmlPath);
    Path xmlfile = Files.createFile(xmlPath);
    StringBuilder builder = new StringBuilder();
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(System.lineSeparator());
    builder.append("<report>");
    builder.append(System.lineSeparator());
    for (Entry<String, ClassTracker> tracker : trackerMap.entrySet()) {
      String className = tracker.getKey();
      ClassTracker classTracker = tracker.getValue();
      Set<Integer> lines = classTracker.getLines();
      Map<Integer, Integer> visitedLines = classTracker.getVisitedLines();
      appendIndent(builder);
      builder.append("<class name=\"" + className + "\">");
      builder.append(System.lineSeparator());
      for (Entry<Integer, Integer> line : visitedLines.entrySet()) {
        appendIndent(builder);
        appendIndent(builder);
        builder.append(
            "<line number=\"" + line.getKey() + "\" times=\"" + line.getValue() + "\"/>");
        builder.append(System.lineSeparator());
      }
      appendIndent(builder);
      appendIndent(builder);
      builder.append("<lines>" + lines.size() + "</lines>");
      builder.append(System.lineSeparator());
      DecimalFormat df = new DecimalFormat("0.00");
      String percentage = (df.format((double) visitedLines.size() / (double) lines.size() * 100));
      appendIndent(builder);
      appendIndent(builder);
      builder.append(
          "<coverage value=\""
              + percentage
              + "\" covered=\""
              + visitedLines.size()
              + "\" total=\""
              + lines.size()
              + "/>");
      builder.append(System.lineSeparator());
      appendIndent(builder);
      builder.append("</class>");
    }
    builder.append("</report>");
    builder.append(System.lineSeparator());
    Files.writeString(xmlfile, builder.toString());
  }

  private static void appendIndent(StringBuilder builder) {
    builder.append("        ");
  }

  /**
   * Writes the coverage information into a html file and formats it.
   *
   * @param trackerMap Map containing the name of the class and its coverage information
   * @param sourceRoot Path to the root folder containing the Java source files
   * @param outputPath Path where the html file should be stored
   * @throws IOException When an error during writing occurs
   * @author Michael Ertl
   */
  public static void writeHTMLOutput(
      Map<String, ClassTracker> trackerMap, Path sourceRoot, Path outputPath) throws IOException {

    Path htmlFile = outputPath.resolve("index.html");
    Files.deleteIfExists(htmlFile);
    Files.createFile(htmlFile);
    StringBuilder builder = new StringBuilder();
    builder.append("<!DOCTYPE html>");
    builder.append(System.lineSeparator());
    builder.append("<html>");
    builder.append(System.lineSeparator());
    for (Entry<String, ClassTracker> tracker : trackerMap.entrySet()) {
      String className = tracker.getKey();
      // if the classname contains a dollar sign, it should be skipped
      if (className.contains("$")) {
        className = className.substring(0, className.indexOf("$"));
      }
      ClassTracker classTracker = tracker.getValue();
      Set<Integer> lines = classTracker.getLines();
      Map<Integer, Integer> visitedLines = classTracker.getVisitedLines();

      List<String> classContent = new ArrayList<>();
      try {
        Path targetPath = sourceRoot.resolve(Path.of(className));
        File sourceFile = new File(targetPath.toString() + ".java");
        classContent = Files.readAllLines(Path.of(sourceFile.getAbsolutePath()));
      } catch (NoSuchFileException e) {
        // Some Path must have been invalid
        // We continue to print the next class, because this one is wrong
        continue;
      }

      // Add all the lines of the class
      /*
      for(Integer line:lines) {
          int greenValue = 255;
          if (visitedLines.containsKey(line)) {
              greenValue = Math.max(50,greenSpectrum(visitedLines.get(line)));
              builder.append("<div style=background-color:rgb(0,"+ greenValue +",0);white-space:pre>");
          } else {
              builder.append("<div style=white-space:pre>");
          }
          builder.append(line + ":" + classContent.get(line));
          builder.append("</div>");
          builder.append(System.lineSeparator());
      }

         */

      for (int i = 1; i <= classContent.size(); i++) {
        int greenValue = 255;
        if (visitedLines.containsKey(i)) {
          greenValue = Math.max(50, greenSpectrum(visitedLines.get(i)));
          builder.append(
              "<div style=background-color:rgb(0," + greenValue + ",0);white-space:pre>");
        } else {
          builder.append("<div style=white-space:pre>");
        }
        builder.append(i + ":" + classContent.get(i - 1));
        builder.append("</div>");
        builder.append(System.lineSeparator());
      }
    }
    builder.append("</html>");
    builder.append(System.lineSeparator());
    Files.writeString(htmlFile, builder.toString());
  }

  private static int greenSpectrum(int visitCount) {
    double max = 255;
    double greenCode = max - (10 * Math.log(visitCount));
    return (int) Math.max(100, greenCode);
  }
}
