import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

class AnalyticsService {
    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> sourceCount = new HashMap<>();

    public synchronized void processEvent(Event e) {
        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);

        sourceCount.put(e.source, sourceCount.getOrDefault(e.source, 0) + 1);
    }

    public void getDashboard() {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");
        int k = 10;
        int rank = 1;

        while (!pq.isEmpty() && k-- > 0) {
            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank++ + ". " + url + " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");
        int total = sourceCount.values().stream().mapToInt(i -> i).sum();

        for (String src : sourceCount.keySet()) {
            int count = sourceCount.get(src);
            double percent = (count * 100.0) / total;
            System.out.println(src + ": " + String.format("%.2f", percent) + "%");
        }
    }
}

public class WeeklyProblems {
    public static void main(String[] args) throws Exception {
        AnalyticsService service = new AnalyticsService();

        service.processEvent(new Event("/article/breaking-news", "user1", "google"));
        service.processEvent(new Event("/article/breaking-news", "user2", "facebook"));
        service.processEvent(new Event("/sports/championship", "user3", "google"));
        service.processEvent(new Event("/sports/championship", "user1", "direct"));
        service.processEvent(new Event("/article/breaking-news", "user1", "google"));

        service.getDashboard();
    }
}