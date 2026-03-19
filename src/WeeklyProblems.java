import java.util.*;

class DNSEntry {
    String ip;
    long expiry;

    DNSEntry(String ip, long ttl) {
        this.ip = ip;
        this.expiry = System.currentTimeMillis() + ttl;
    }
}

class DNSCache {
    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    public synchronized String resolve(String domain) {
        long now = System.currentTimeMillis();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);
            if (entry.expiry > now) {
                hits++;
                return "Cache HIT: " + entry.ip;
            } else {
                cache.remove(domain);
            }
        }

        misses++;
        String ip = fetchFromUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 5000));
        return "Cache MISS: " + ip;
    }

    private String fetchFromUpstream(String domain) {
        return "172." + (int)(Math.random()*255) + "." + (int)(Math.random()*255) + "." + (int)(Math.random()*255);
    }

    public void getStats() {
        int total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0 / total);
        System.out.println("Hit Rate: " + rate + "%");
    }
}

public class WeeklyProblems {
    public static void main(String[] args) throws Exception {
        DNSCache cache = new DNSCache(3);

        System.out.println(cache.resolve("google.com"));
        System.out.println(cache.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(cache.resolve("google.com"));

        cache.getStats();
    }
}