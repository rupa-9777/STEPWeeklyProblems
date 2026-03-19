import java.util.*;

class PlagiarismService {
    private HashMap<String, Set<String>> index = new HashMap<>();
    private int n = 5;

    private List<String> getNGrams(String text) {
        String[] words = text.split("\\s+");
        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }
        return grams;
    }

    public void addDocument(String docId, String content) {
        List<String> grams = getNGrams(content);
        for (String gram : grams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    public void analyzeDocument(String docId, String content) {
        List<String> grams = getNGrams(content);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            if (index.containsKey(gram)) {
                for (String otherDoc : index.get(gram)) {
                    matchCount.put(otherDoc, matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("Total n-grams: " + grams.size());

        for (String doc : matchCount.keySet()) {
            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / grams.size();
            System.out.println("Match with " + doc + ": " + matches + " grams, Similarity: " + similarity + "%");
        }
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        PlagiarismService service = new PlagiarismService();

        String doc1 = "this is a simple test document for plagiarism detection system";
        String doc2 = "this is a simple test document for checking plagiarism detection";
        String doc3 = "completely different content with no matching words here";

        service.addDocument("doc1", doc1);
        service.addDocument("doc2", doc2);

        service.analyzeDocument("doc3", doc3);
    }
}