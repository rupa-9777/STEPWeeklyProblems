import java.util.*;

class InventoryService {
    private HashMap<String, Integer> stock = new HashMap<>();
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    public synchronized void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    public synchronized int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {
        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {
            stock.put(productId, available - 1);
            return "Success, " + (available - 1) + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public synchronized Queue<Integer> getWaitingList(String productId) {
        return waitingList.get(productId);
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        InventoryService service = new InventoryService();

        service.addProduct("IPHONE15_256GB", 5);

        System.out.println(service.checkStock("IPHONE15_256GB"));

        System.out.println(service.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(service.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(service.purchaseItem("IPHONE15_256GB", 11111));
        System.out.println(service.purchaseItem("IPHONE15_256GB", 22222));
        System.out.println(service.purchaseItem("IPHONE15_256GB", 33333));

        System.out.println(service.purchaseItem("IPHONE15_256GB", 99999));
        System.out.println(service.purchaseItem("IPHONE15_256GB", 88888));
    }
}