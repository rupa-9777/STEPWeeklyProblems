import java.util.*;

class UsernameService {
    private HashMap<String, Integer> users = new HashMap<>();
    private HashMap<String, Integer> attempts = new HashMap<>();

    public void addUser(String username, int userId) {
        users.put(username, userId);
    }

    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> res = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String s = username + i;
            if (!users.containsKey(s)) res.add(s);
        }
        String mod = username.replace("_", ".");
        if (!users.containsKey(mod)) res.add(mod);
        return res;
    }

    public String getMostAttempted() {
        String ans = "";
        int max = 0;
        for (String key : attempts.keySet()) {
            if (attempts.get(key) > max) {
                max = attempts.get(key);
                ans = key;
            }
        }
        return ans;
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        UsernameService service = new UsernameService();

        service.addUser("john_doe", 1);
        service.addUser("admin", 2);

        System.out.println(service.checkAvailability("john_doe"));
        System.out.println(service.checkAvailability("jane_smith"));

        System.out.println(service.suggestAlternatives("john_doe"));

        service.checkAvailability("admin");
        service.checkAvailability("admin");
        service.checkAvailability("admin");

        System.out.println(service.getMostAttempted());
    }
}