import java.util.*;

class Video {
    String id;
    String data;

    Video(String id, String data) {
        this.id = id;
        this.data = data;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

class MultiLevelCache {
    private LRUCache<String, Video> L1;
    private LRUCache<String, Video> L2;
    private HashMap<String, Video> L3;
    private HashMap<String, Integer> accessCount;

    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public MultiLevelCache() {
        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>();
        accessCount = new HashMap<>();
    }

    public void addToDB(Video v) {
        L3.put(v.id, v);
    }

    public String getVideo(String id) {
        long start = System.currentTimeMillis();

        if (L1.containsKey(id)) {
            l1Hits++;
            return "L1 HIT: " + id + " (" + (System.currentTimeMillis() - start) + "ms)";
        }

        if (L2.containsKey(id)) {
            l2Hits++;
            Video v = L2.get(id);
            promoteToL1(id, v);
            return "L2 HIT → Promoted to L1: " + id;
        }

        if (L3.containsKey(id)) {
            l3Hits++;
            Video v = L3.get(id);
            L2.put(id, v);
            accessCount.put(id, 1);
            return "L3 HIT → Added to L2: " + id;
        }

        return "Video not found";
    }

    private void promoteToL1(String id, Video v) {
        int count = accessCount.getOrDefault(id, 0) + 1;
        accessCount.put(id, count);

        if (count > 2) {
            L1.put(id, v);
        }
    }

    public void updateVideo(String id, String newData) {
        Video v = new Video(id, newData);
        L3.put(id, v);
        L2.remove(id);
        L1.remove(id);
    }

    public void getStats() {
        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0 / total);

        System.out.println("L1 Hit Rate: " + l1Rate + "%");
        System.out.println("L2 Hit Rate: " + l2Rate + "%");
        System.out.println("L3 Hit Rate: " + l3Rate + "%");
        System.out.println("Overall Requests: " + total);
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        MultiLevelCache cache = new MultiLevelCache();

        cache.addToDB(new Video("video1", "data1"));
        cache.addToDB(new Video("video2", "data2"));

        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));

        System.out.println(cache.getVideo("video2"));

        cache.getStats();
    }
}