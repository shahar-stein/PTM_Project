package test;
import java.util.LinkedHashSet;



public class LRU implements CacheReplacementPolicy {
  private LinkedHashSet<String> cache;

  public LRU() {
    this.cache = new LinkedHashSet<>();
  }

  public void add(String key) {
    if (this.cache.contains(key)) {
      this.cache.remove(key);
    }
    this.cache.add(key);
  }

  public String remove() {
    String firstKey = this.cache.iterator().next();
    this.cache.remove(firstKey);
    return firstKey;
  }
}
