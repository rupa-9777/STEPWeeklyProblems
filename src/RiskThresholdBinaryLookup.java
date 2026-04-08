import java.util.*;

public class RiskThresholdBinaryLookup {

    static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                found = true;
                break;
            }
        }

        System.out.println("Linear Search:");
        if (found) System.out.println("Found");
        else System.out.println("Not Found");
        System.out.println("Comparisons: " + comparisons);
    }

    static int binarySearchInsertion(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return low;
    }

    static int floor(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                result = arr[mid];
                low = mid + 1;
            } else high = mid - 1;
        }
        return result;
    }

    static int ceiling(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                result = arr[mid];
                high = mid - 1;
            } else low = mid + 1;
        }
        return result;
    }

    static class Counter {
        int count = 0;
    }

    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};

        linearSearch(risks, 30);

        Counter c1 = new Counter();
        int insertion = binarySearchInsertion(risks, 30, c1);

        Counter c2 = new Counter();
        int floor = floor(risks, 30, c2);

        Counter c3 = new Counter();
        int ceiling = ceiling(risks, 30, c3);

        System.out.println("\nBinary Search:");
        System.out.println("Insertion Index: " + insertion);
        System.out.println("Floor: " + floor);
        System.out.println("Ceiling: " + ceiling);
        System.out.println("Comparisons: " + (c1.count + c2.count + c3.count));
    }
}