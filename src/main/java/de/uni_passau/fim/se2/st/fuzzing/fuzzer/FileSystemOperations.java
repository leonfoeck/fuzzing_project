package de.uni_passau.fim.se2.st.fuzzing.fuzzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Leon Föckersperger
 */
public class FileSystemOperations {

  public void createDirectories(Path dir) throws IOException {
    Files.createDirectories(dir);
  }

  public void write(Path path, byte[] bytes) throws IOException {
    Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
  }
}
