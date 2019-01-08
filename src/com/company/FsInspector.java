package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.company.Console.*;

public class FsInspector {

  private static final Map<String, AtomicInteger> visitedPaths = new HashMap<>();

  public static void main(String[] args)
          throws IOException {

    if (args.length < 3 || !args[1].equals("-name"))
      usage();

    Path startingDir = Paths.get(args[0]);
//    String pattern = args[2];

    boolean exit = false;
    while (!exit) {
      try {
        walkFileTree(startingDir);
        exit = true;
      } catch (UncheckedIOException e) {
        Throwable t = e.getCause();
        if (t instanceof NotDirectoryException) {
          tryReadFile((NotDirectoryException) t);
        }
      }
    }
  }

  private static void tryReadFile(NotDirectoryException e) {
    String path = e.getMessage();
    try (FileInputStream is = new FileInputStream(new File(path))) {
      is.read();
    } catch (Exception fileAccessEx) {
      fileAccessEx.printStackTrace();
    }
  }

  private static void walkFileTree(Path startingDir) throws IOException {
    Files.find(
            startingDir,
            Integer.MAX_VALUE,
            (path, basicFileAttributes) -> basicFileAttributes.isDirectory()
    )
            .forEach(p -> {
                      if (p.toString().endsWith(".txt") || p.toString().endsWith(".jar")) {
                        AtomicInteger counter = visitedPaths.get(p.toString());
                        if (counter == null ) {
                          counter = new AtomicInteger(0);
                          visitedPaths.put(p.toString(), counter);
                        }
                        counter.incrementAndGet();
                        if (counter.get() > 1) {
                          rered(counter.get() + "\t - " + p);
                        } else {
                          newline();
                          red(counter.get() + "\t - " + p);
                        }
                      }
                    }
            );
  }

  private static void usage() {
    System.err.println("java FsInspector <path>" +
            " -name \"<glob_pattern>\"");
    System.exit(-1);
  }

}