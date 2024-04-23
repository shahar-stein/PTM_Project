package test;
import java.util.HashSet;



public class CacheManager {
  private HashSet<String> cache;
  private CacheReplacementPolicy policy;
  private int size;

  public CacheManager(int size, CacheReplacementPolicy policy) {
    this.cache = new HashSet<>();
    this.policy = policy;
    this.size = size;
  }

  public boolean query(String word) {
    return cache.contains(word);
  }

  public void add(String word) {
    if (cache.size() == size) {
      String removed = policy.remove();
      cache.remove(removed);
    }
    cache.add(word);
    policy.add(word);
  }
}
