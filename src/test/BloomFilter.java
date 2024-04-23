package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {
  private BitSet bitSet;
  private List<String> algorithms;
  private int size;

  public BloomFilter(int size, String... algorithms) {
    this.bitSet = new BitSet(size);
    this.algorithms = new ArrayList<>();
    this.size = 0;
    for (String alg : algorithms) {
      this.algorithms.add(alg);
    }
  }

  public void add(String word) {
    for (String alg : this.algorithms) {
      try {
        MessageDigest md = MessageDigest.getInstance(alg);
        byte[] bts = md.digest(word.getBytes());
        BigInteger bi = new BigInteger(bts);
        int i = Math.abs(bi.intValue()) % this.bitSet.size();
        if (i > this.size) {
          this.size = i;
        }
        this.bitSet.set(i);
      } catch (NoSuchAlgorithmException e) {
        System.err.println("Algorithm not found");
      }
    }
  }

  public boolean contains(String word) {
    for (String alg : this.algorithms) {
      try {
        MessageDigest md = MessageDigest.getInstance(alg);
        byte[] bts = md.digest(word.getBytes());
        BigInteger bi = new BigInteger(bts);
        int i = Math.abs(bi.intValue()) % this.bitSet.size();
        if (!this.bitSet.get(i)) {
          return false;
        }
      } catch (NoSuchAlgorithmException e) {
        System.err.println("Algorithm not found");
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i <= this.size; i++) {
      sb.append(this.bitSet.get(i) ? "1" : "0");
    }
    return sb.toString();
  }
}
