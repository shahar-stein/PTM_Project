package test;

import java.util.Arrays;

public class Tile {
  public final char letter;
  public final int score;

  private Tile(char letter) {
    this.letter = letter;
    this.score = getScore(letter);
  }

  private static int getScore(char letter) {
    if (letter < 65 || letter > 90) {
      return 0;
    }
    if (Arrays.asList('Q', 'Z').contains(letter)) {
      return 10;
    }
    if (Arrays.asList('J', 'X').contains(letter)) {
      return 8;
    }
    if (Arrays.asList('K').contains(letter)) {
      return 5;
    }
    if (Arrays.asList('F', 'H', 'V', 'W', 'Y').contains(letter)) {
      return 4;
    }
    if (Arrays.asList('B', 'C', 'M', 'P').contains(letter)) {
      return 3;
    }
    if (Arrays.asList('D', 'G').contains(letter)) {
      return 2;
    }
    return 1;
  }

  public boolean equals(Tile other) {
    return this.letter == other.letter;
  }

  public int hashCode() {
    return this.letter - 65;
  }

  public static class Bag {
    private Tile[] ts;
    private int[] quan;
    private int[] oldQuan;
    private static Bag bag;

    private Bag() {
      this.ts = new Tile[26];
      this.quan = new int[] { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
      this.oldQuan = Arrays.copyOf(this.quan, 26);
      for (int i = 0; i < 26; i++) {
        this.ts[i] = new Tile((char) (i + 65));
      }
    }

    public static Bag getBag() {
      if (bag == null) {
        bag = new Bag();
      }
      return bag;
    }

    public Tile getRand() {
      if (Arrays.stream(this.quan).sum() == 0) {
        return null;
      }
      int index = (int) (Math.random() * 26);
      while (this.quan[index] == 0) {
        index = (int) (Math.random() * 26);
      }
      this.quan[index]--;
      return this.ts[index];
    }

    public Tile getTile(char letter) {
      if (letter < 65 || letter > 90) {
        return null;
      }
      if (this.quan[letter - 65] == 0) {
        return null;
      }
      return this.ts[letter - 65];
    }

    public void put(Tile tile) {
      if (this.quan[tile.letter - 65] == this.oldQuan[tile.letter - 65]) {
        return;
      }
      this.quan[tile.letter - 65]++;
    }

    public int size() {
      return Arrays.stream(this.quan).sum();
    }

    public int[] getQuantities() {
      return Arrays.copyOf(this.quan, 26);
    }
  }

}
