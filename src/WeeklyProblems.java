import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    long time;
    String account;

    Transaction(int id, int amount, String merchant, long time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

class TransactionAnalyzer {

    public List<int[]> twoSum(List<Transaction> list, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : list) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }

    public List<int[]> twoSumWithTime(List<Transaction> list, int target, long window) {
        list.sort(Comparator.comparingLong(t -> t.time));
        List<int[]> result = new ArrayList<>();
        int left = 0;

        for (int right = 0; right < list.size(); right++) {
            while (list.get(right).time - list.get(left).time > window) {
                left++;
            }

            HashMap<Integer, Transaction> map = new HashMap<>();
            for (int i = left; i < right; i++) {
                Transaction t = list.get(i);
                int complement = target - list.get(right).amount;
                if (map.containsKey(complement)) {
                    result.add(new int[]{map.get(complement).id, list.get(right).id});
                }
                map.put(t.amount, t);
            }
        }
        return result;
    }

    public List<List<Integer>> kSum(List<Transaction> list, int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        kSumHelper(list, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void kSumHelper(List<Transaction> list, int k, int target, int start,
                            List<Integer> current, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k <= 0 || start >= list.size()) return;

        for (int i = start; i < list.size(); i++) {
            current.add(list.get(i).id);
            kSumHelper(list, k - 1, target - list.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public void detectDuplicates(List<Transaction> list) {
        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            String key = t.amount + "-" + t.merchant;
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {
            List<Transaction> group = map.get(key);
            Set<String> accounts = new HashSet<>();

            for (Transaction t : group) {
                accounts.add(t.account);
            }

            if (accounts.size() > 1) {
                System.out.println("Duplicate: " + key + " Accounts: " + accounts);
            }
        }
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        List<Transaction> list = new ArrayList<>();

        list.add(new Transaction(1, 500, "StoreA", 1000, "acc1"));
        list.add(new Transaction(2, 300, "StoreB", 1100, "acc2"));
        list.add(new Transaction(3, 200, "StoreC", 1200, "acc3"));
        list.add(new Transaction(4, 500, "StoreA", 1300, "acc2"));

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        List<int[]> pairs = analyzer.twoSum(list, 500);
        for (int[] p : pairs) {
            System.out.println("Pair: " + p[0] + ", " + p[1]);
        }

        analyzer.detectDuplicates(list);

        List<List<Integer>> ksum = analyzer.kSum(list, 3, 1000);
        for (List<Integer> combo : ksum) {
            System.out.println("KSum: " + combo);
        }
    }
}