import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class AutocompleteSystem {
    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> frequency = new HashMap<>();

    public void insert(String query) {
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
    }

    private void dfs(TrieNode node, String prefix, List<String> results) {
        if (node.isEnd) results.add(prefix);
        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix + c, results);
        }
    }

    public List<String> search(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return new ArrayList<>();
            node = node.children.get(c);
        }

        List<String> all = new ArrayList<>();
        dfs(node, prefix, all);

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(a) - frequency.get(b)
        );

        for (String s : all) {
            pq.offer(s);
            if (pq.size() > 10) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll());
        Collections.reverse(result);

        return result;
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java tutorial");

        List<String> suggestions = system.search("jav");

        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank++ + ". " + s);
        }
    }
}