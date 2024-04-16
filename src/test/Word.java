package test;


public class Word {
  private Tile[] ts;
  private int r;
  private int c;
  private boolean isVertical;
	
  public Word(Tile[] ts, int r, int c, boolean isVertical) {
    this.ts = ts;
    this.r = r;
    this.c = c;
    this.isVertical = isVertical;
  }

  public Tile[] getTiles() {
    return this.ts;
  }

  public int getRow() {
    return this.r;
  }

  public int getCol() {
    return this.c;
  }

  public boolean isVertical() {
    return this.isVertical;
  }

  public boolean equals(Word other) {
    if (this.r != other.r || this.c != other.c || this.isVertical != other.isVertical) {
      return false;
    }
    for (int i = 0; i < this.ts.length; i++) {
      if (!this.ts[i].equals(other.ts[i])) {
        return false;
      }
    }
    return true;
  }
}
