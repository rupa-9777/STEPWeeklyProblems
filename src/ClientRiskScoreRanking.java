import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }
}

public class WeeklyProblems {

    static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        System.out.println("Bubble Sort (Ascending Risk):");
        for (Client c : arr) {
            System.out.print(c.name + ":" + c.riskScore + " ");
        }
        System.out.println("\nSwaps: " + swaps);
    }

    static void insertionSort(Client[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 &&
                    (arr[j].riskScore < key.riskScore ||
                            (arr[j].riskScore == key.riskScore &&
                                    arr[j].accountBalance < key.accountBalance))) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

        System.out.println("\nInsertion Sort (Descending Risk + Balance):");
        for (Client c : arr) {
            System.out.print(c.name + ":" + c.riskScore + " ");
        }
        System.out.println();
    }

    static void topRisks(Client[] arr, int k) {
        System.out.println("\nTop " + k + " Highest Risk Clients:");
        for (int i = 0; i < Math.min(k, arr.length); i++) {
            System.out.println(arr[i].name + "(" + arr[i].riskScore + ")");
        }
    }

    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 2000),
                new Client("clientB", 50, 3000)
        };

        Client[] bubbleArr = Arrays.copyOf(clients, clients.length);
        Client[] insertionArr = Arrays.copyOf(clients, clients.length);

        bubbleSort(bubbleArr);
        insertionSort(insertionArr);
        topRisks(insertionArr, 3);
    }
}