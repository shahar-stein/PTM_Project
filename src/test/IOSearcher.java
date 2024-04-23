package test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class IOSearcher {
  public static boolean search(String word, String... fileNames) {
    for (String fileName : fileNames) {
      try {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          if (line.contains(word)) {
            scanner.close();
            return true;
          }
        }
        scanner.close();
      } catch (FileNotFoundException e) {
        System.err.println("File not found");
      }
    }
    return false;
  }
}
