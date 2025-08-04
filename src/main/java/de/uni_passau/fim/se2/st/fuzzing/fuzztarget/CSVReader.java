package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import com.google.common.collect.ImmutableList;
import de.uni_passau.fim.se2.st.fuzzing.fuzztarget.CSVReader.CSVRow;
import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;

/**
 * A reader for CSV files.
 *
 * <p>This reader will work on any kind of input that can be represented by a {@link
 * BufferedReader}. It expects the input to be in the format of a CSV file.
 */
public class CSVReader implements Iterable<CSVRow> {

  private final BufferedReader reader;
  private final CharSequence delimiter;

  /**
   * Initialises a {@code CSVReader} using a {@link BufferedReader} as its input source.
   *
   * <p>This sets the default delimiter character to {@code ;}.
   *
   * @param reader The input source
   */
  public CSVReader(BufferedReader reader) {
    this(reader, ";");
  }

  /**
   * Initialises a {@code CSVReader} using a {@link BufferedReader} as its input source.
   *
   * <p>This allows to set the delimiter character to {@code delimiter}.
   *
   * @param reader The input source
   * @param delimiter The delimiter character
   */
  public CSVReader(BufferedReader reader, CharSequence delimiter) {
    this.reader = reader;
    this.delimiter = delimiter;
  }

  @Override
  public Iterator<CSVRow> iterator() {
    return reader.lines().map(this::getRowFromLine).iterator();
  }

  private CSVRow getRowFromLine(String line) {
    String[] columns = line.split(delimiter.toString());
    return new CSVRow(ImmutableList.copyOf(columns));
  }

  /** Represents a single row in a CSV file. */
  public static class CSVRow {

    private final List<String> values;

    private CSVRow(List<String> values) {
      this.values = ImmutableList.copyOf(values);
    }

    /**
     * Returns the value of the column at the given index.
     *
     * @param index The index of the column
     * @return The value of the column
     */
    public String get(int index) {
      return values.get(index);
    }

    /**
     * Provides the entries of the row.
     *
     * @return The entries of the row
     */
    public List<String> getValues() {
      return values;
    }
  }
}
