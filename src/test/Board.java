package test;

import java.util.ArrayList;

public class Board {
  private static Board board;
  private Tile[][] ts;
  private boolean newBoard = true;

  public static Board getBoard() {
    if (board == null) {
      board = new Board();
    }
    return board;
  }

  private Board() {
    this.ts = new Tile[15][15];
  }

  public Tile[][] getTiles() {
    Tile[][] copy = new Tile[15][15];
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        copy[i][j] = this.ts[i][j];
      }
    }
    return copy;
  }

  private boolean isOnMiddle(Word word) {
    if (!this.newBoard) {
      return false;
    }
    if (word.isVertical()) {
      return word.getCol() == 7 && word.getRow() <= 7 && word.getRow() + word.getTiles().length > 7
          && word.getTiles()[7 - word.getRow()] != null;
    } else {
      return word.getRow() == 7 && word.getCol() <= 7 && word.getCol() + word.getTiles().length > 7
          && word.getTiles()[7 - word.getCol()] != null;
    }
  }

  private boolean isInBoard(Word word) {
    if (!word.isVertical() && word.getCol() + word.getTiles().length > 15) {
      return false;
    }
    if (word.isVertical() && word.getRow() + word.getTiles().length > 15) {
      return false;
    }
    if (word.getRow() < 0 || word.getRow() > 14 || word.getCol() < 0 || word.getCol() > 14) {
      return false;
    }
    return true;
  }

  private boolean isThereTilesAroundCell(int row, int col) {
    if (row - 1 >= 0 && this.ts[row - 1][col] != null) {
      return true;
    }
    if (row + 1 < 15 && this.ts[row + 1][col] != null) {
      return true;
    }
    if (col - 1 >= 0 && this.ts[row][col - 1] != null) {
      return true;
    }
    if (col + 1 < 15 && this.ts[row][col + 1] != null) {
      return true;
    }
    return false;
  }

  private boolean isTouchingToOtherTiles(Word word) {
    if (word.isVertical()) {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (this.isThereTilesAroundCell(word.getRow() + i, word.getCol())) {
          return true;
        }
      }
    } else {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (this.isThereTilesAroundCell(word.getRow(), word.getCol() + i)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isOverriding(Word word) {
    if (word.isVertical()) {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (word.getTiles()[i] != null && this.ts[word.getRow() + i][word.getCol()] != null) {
          return true;
        }
      }
    } else {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (word.getTiles()[i] != null && this.ts[word.getRow()][word.getCol() + i] != null) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean boardLegal(Word word) {
    if (!isInBoard(word)) {
      return false;
    }
    if (!isTouchingToOtherTiles(word) && !isOnMiddle(word)) {
      return false;
    }
    if (isOverriding(word)) {
      return false;
    }
    return true;
  }

  private ArrayList<Word> getNewWords(Word word) {
    ArrayList<Word> words = new ArrayList<Word>();
    int[][] newTiles = new int[word.getTiles().length][2];
    for (int i = 0; i < word.getTiles().length; i++) {
      if (word.getTiles()[i] == null) {
        continue;
      }
      if (word.isVertical()) {
        newTiles[i][0] = word.getRow() + i;
        newTiles[i][1] = word.getCol();
      } else {
        newTiles[i][0] = word.getRow();
        newTiles[i][1] = word.getCol() + i;
      }
    }

    int checkRowStart = word.getRow();
    int checkColStart = word.getCol();
    int checkRowEnd = word.getRow() + 1;
    int checkColEnd = word.getCol() + 1;
    if (word.isVertical()) {
      checkRowEnd = word.getRow() + word.getTiles().length;
    } else {
      checkColEnd = word.getCol() + word.getTiles().length;
    }

    for (int i = checkRowStart; i < checkRowEnd; i++) {
      int start = checkColStart;
      int end = checkColEnd;
      while (start > 0 && this.ts[i][start - 1] != null) {
        start--;
      }
      while (end < 15 && this.ts[i][end] != null) {
        end++;
      }
      if (end - start > 1) {
        Tile[] tiles = new Tile[end - start];
        for (int j = start; j < end; j++) {
          tiles[j - start] = this.ts[i][j];
        }
        boolean isNewWord = false;
        for (int j = 0; j < tiles.length; j++) {
          for (int k = 0; k < newTiles.length; k++) {
            if (newTiles[k][0] == i && newTiles[k][1] == j + start) {
              isNewWord = true;
            }
          }
        }
        if (isNewWord) {
          words.add(new Word(tiles, i, start, false));
        }
      }
    }

    for (int i = checkColStart; i < checkColEnd; i++) {
      int start = checkRowStart;
      int end = checkRowEnd;
      while (start > 0 && this.ts[start - 1][i] != null) {
        start--;
      }
      while (end < 15 && this.ts[end][i] != null) {
        end++;
      }
      if (end - start > 1) {
        Tile[] tiles = new Tile[end - start];
        for (int j = start; j < end; j++) {
          tiles[j - start] = this.ts[j][i];
        }
        boolean isNewWord = false;
        for (int j = 0; j < tiles.length; j++) {
          for (int k = 0; k < newTiles.length; k++) {
            if (newTiles[k][0] == j + start && newTiles[k][1] == i) {
              isNewWord = true;
            }
          }
        }
        if (isNewWord) {
          words.add(new Word(tiles, start, i, true));
        }
      }
    }
    return words;
  }

  private int getCellTileMultiplier(int row, int col) {
    if ((row == 0 || row == 14) && (col == 3 || col == 11))
      return 2;
    if ((row == 1 || row == 13) && (col == 5 || col == 9))
      return 3;
    if ((row == 2 || row == 12) && (col == 6 || col == 8))
      return 2;
    if ((row == 3 || row == 11) && (col == 0 || col == 7 || col == 14))
      return 2;
    if ((row == 5 || row == 9) && (col == 1 || col == 5 || col == 9 || col == 13))
      return 3;
    if ((row == 6 || row == 8) && (col == 2 || col == 6 || col == 8 || col == 12))
      return 2;
    if (row == 7 && (col == 3 || col == 11))
      return 2;
    return 1;
  }

  private int getCellWordMultiplier(int row, int col) {
    if ((row == 0 || row == 14) && (col == 0 || col == 7 || col == 14))
      return 3;
    if ((row == 1 || row == 13) && (col == 1 || col == 13))
      return 2;
    if ((row == 2 || row == 12) && (col == 2 || col == 12))
      return 2;
    if ((row == 3 || row == 11) && (col == 3 || col == 11))
      return 2;
    if ((row == 4 || row == 10) && (col == 4 || col == 10))
      return 2;
    if ((row == 7) && (col == 0 || col == 14))
      return 3;
    return 1;
  }

  private int getWordScore(Word word) {
    int score = 0;
    int wordMultiplier = 1;
    for (int i = 0; i < word.getTiles().length; i++) {
      int letterMultiplier = 1;
      int col = word.isVertical() ? word.getCol() : i + word.getCol();
      int row = word.isVertical() ? i + word.getRow() : word.getRow();
      letterMultiplier = getCellTileMultiplier(row, col);
      wordMultiplier *= getCellWordMultiplier(row, col);
      score += word.getTiles()[i].score * letterMultiplier;
    }
    if (isOnMiddle(word)) {
      wordMultiplier *= 2;
    }
    return score * wordMultiplier;
  }

  private int getScores(ArrayList<Word> words) {
    int score = 0;
    for (Word word : words) {
      score += getWordScore(word);
    }
    return score;
  }

  public int tryPlaceWord(Word word) {
    if (!boardLegal(word)) {
      return 0;
    }

    if (word.isVertical()) {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (this.ts[word.getRow() + i][word.getCol()] != null) {
          continue;
        }
        this.ts[word.getRow() + i][word.getCol()] = word.getTiles()[i];
      }
    } else {
      for (int i = 0; i < word.getTiles().length; i++) {
        if (this.ts[word.getRow()][word.getCol() + i] != null) {
          continue;
        }
        this.ts[word.getRow()][word.getCol() + i] = word.getTiles()[i];
      }
    }

    ArrayList<Word> words = getNewWords(word);
    int score = getScores(words);

    if (this.newBoard) {
      this.newBoard = false;
    }

    return score;
  }
}
