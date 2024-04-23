package test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Dictionary {
  private List<String> fileNames;
  private CacheManager rightWords;
  private CacheManager wrongWords;
  private BloomFilter bloomFilter;

  public Dictionary(String... fileNames) {
    this.fileNames = new ArrayList<>();
    for (String fileName : fileNames) {
      this.fileNames.add(fileName);
    }

    this.rightWords = new CacheManager(400, new LRU());
    this.wrongWords = new CacheManager(100, new LFU());
    this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");

    for (String fileName : this.fileNames) {
      File file = new File(fileName);
      if (file.exists()) {
        try {
          Scanner scanner = new Scanner(file);
          while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (String word : line.split(" ")) {
              this.bloomFilter.add(word);
            }
          }
          scanner.close();
        } catch (java.io.FileNotFoundException e) {
          System.err.println("File not found");
        }
      }
    }
  }

  public boolean query(String word) {
    if (this.rightWords.query(word)) {
      return true;
    } else if (this.wrongWords.query(word)) {
      return false;
    } else if (this.bloomFilter.contains(word)) {
      rightWords.add(word);
      return true;
    }
    wrongWords.add(word);
    return false;
  }

  public boolean challenge(String word) {
    String[] fileNames = new String[this.fileNames.size()];
    for (int i = 0; i < this.fileNames.size(); i++) {
      fileNames[i] = this.fileNames.get(i);
    }
    if (IOSearcher.search(word, fileNames)) {
      rightWords.add(word);
      return true;
    }
    wrongWords.add(word);
    return false;
  }
}
