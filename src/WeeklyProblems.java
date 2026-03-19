import java.util.*;

class ParkingSpot {
    String plate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        this.plate = null;
        this.occupied = false;
    }
}

class ParkingLot {
    private ParkingSpot[] table;
    private int size;
    private int count = 0;
    private int totalProbes = 0;

    public ParkingLot(int size) {
        this.size = size;
        table = new ParkingSpot[size];
        for (int i = 0; i < size; i++) table[i] = new ParkingSpot();
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    public String parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % size;
            probes++;
        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        count++;
        totalProbes += probes;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String plate) {
        int index = hash(plate);

        while (table[index].occupied) {
            if (plate.equals(table[index].plate)) {
                long duration = System.currentTimeMillis() - table[index].entryTime;
                table[index].occupied = false;
                count--;

                double hours = duration / (1000.0 * 60 * 60);
                double fee = hours * 5;

                return "Freed spot #" + index + ", Duration: " + String.format("%.2f", hours) + "h, Fee: $" + String.format("%.2f", fee);
            }
            index = (index + 1) % size;
        }

        return "Vehicle not found";
    }

    public void getStatistics() {
        double occupancy = (count * 100.0) / size;
        double avgProbes = count == 0 ? 0 : (totalProbes * 1.0 / count);

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(10);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        System.out.println(lot.exitVehicle("ABC-1234"));

        lot.getStatistics();
    }
}