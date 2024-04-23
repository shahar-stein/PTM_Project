package test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LFU implements CacheReplacementPolicy {
  private List<String> cache;
  private Map<String, Integer> freq;
  private int minFreq;

  public LFU() {
    this.cache = new ArrayList<>();
    this.freq = new HashMap<>();
    this.minFreq = 1;
  }

  public void add(String key) {
    if (cache.contains(key)) {
      cache.remove(key);
      freq.put(key, freq.get(key) + 1);
    } else {
      freq.put(key, 1);
    }
    cache.add(key);
    if (freq.get(key) < minFreq) {
      minFreq = freq.get(key);
    }
  }

  public String remove() {
    String minKey = "";
    for (String key : cache) {
      if (freq.get(key) == minFreq) {
        minKey = key;
        break;
      }
    }
    cache.remove(minKey);
    freq.remove(minKey);
    return minKey;
  }
}
