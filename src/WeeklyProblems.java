import java.util.*;

public class WeeklyProblems {

    static void linearSearch(String[] arr, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("Linear Search:");
        System.out.println("First Occurrence: " + first);
        System.out.println("Last Occurrence: " + last);
        System.out.println("Comparisons: " + comparisons);
    }

    static int binarySearch(String[] arr, String target, Counter counter) {
        int low = 0, high = arr.length - 1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) return mid;
            else if (arr[mid].compareTo(target) < 0) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    static int countOccurrences(String[] arr, String target, Counter counter) {
        int index = binarySearch(arr, target, counter);
        if (index == -1) return 0;

        int count = 1;

        int left = index - 1;
        while (left >= 0 && arr[left].equals(target)) {
            count++;
            left--;
        }

        int right = index + 1;
        while (right < arr.length && arr[right].equals(target)) {
            count++;
            right++;
        }

        return count;
    }

    static class Counter {
        int count = 0;
    }

    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        linearSearch(logs, "accB");

        Arrays.sort(logs);

        Counter counter = new Counter();
        int index = binarySearch(logs, "accB", counter);
        int count = countOccurrences(logs, "accB", counter);

        System.out.println("\nBinary Search:");
        System.out.println("Index: " + index);
        System.out.println("Count: " + count);
        System.out.println("Comparisons: " + counter.count);
    }
}