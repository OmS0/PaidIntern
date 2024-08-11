import java.util.*;

public class TrainAllocationSystem {

    // Class to store user profile information
    static class UserProfile {
        String name;
        int age;
        String trainName;
        int boogieNumber;
        int seatNumber;

        UserProfile(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Name: %s, Age: %d, Train: %s, Boogie: %d, Seat: %d",
                    name, age, trainName, boogieNumber, seatNumber);
        }
    }

    // Class to represent a Train with seats
    static class Train {
        String name;
        boolean[][] seats; // 3 boogies, each with 2 seats

        Train(String name) {
            this.name = name;
            this.seats = new boolean[3][2]; // Initialize seats as false (not booked)
        }

        boolean allocateSeat(int boogie, int seat) {
            // Check if the boogie and seat numbers are valid
            if (boogie < 1 || boogie > 3 || seat < 1 || seat > 2) return false;
            // Check if the seat is available (false means not booked)
            if (!seats[boogie - 1][seat - 1]) {
                seats[boogie - 1][seat - 1] = true; // Mark seat as booked
                return true;
            }
            return false;
        }

        int getRemainingSeats() {
            int count = 0;
            for (boolean[] boogie : seats) {
                for (boolean seat : boogie) {
                    if (!seat) count++; // Count available seats
                }
            }
            return count;
        }

        int getBookedSeats() {
            int count = 0;
            for (boolean[] boogie : seats) {
                for (boolean seat : boogie) {
                    if (seat) count++; // Count booked seats
                }
            }
            return count;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final Map<String, UserProfile> userProfiles = new HashMap<>();
    private static final Map<String, Train> trains = new HashMap<>();
    private static int profileCounter = 1;

    public static void main(String[] args) {
        // Initialize trains
        trains.put("Train 1", new Train("Train 1"));
        trains.put("Train 2", new Train("Train 2"));
        trains.put("Train 3", new Train("Train 3"));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your choice:");
            System.out.println("1. Enter Profile");
            System.out.println("2. Enter Train Choice");
            System.out.println("3. Display Person Information");
            System.out.println("4. Display Complete Information");
            System.out.println("5. Display Train Status");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline after integer input

            switch (choice) {
                case 1:
                    enterProfile(scanner);
                    break;
                case 2:
                    enterTrainChoice(scanner);
                    break;
                case 3:
                    displayPersonInformation(scanner);
                    break;
                case 4:
                    displayCompleteInformation();
                    break;
                case 5:
                    displayTrainStatus();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void enterProfile(Scanner scanner) {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your age:");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline after integer input

        // Generate unique login ID
        String loginId = "user" + profileCounter++;
        userProfiles.put(loginId, new UserProfile(name, age));

        System.out.println("Profile registered. Your login ID is: " + loginId);
    }

    private static void enterTrainChoice(Scanner scanner) {
        System.out.println("Enter your login ID:");
        String loginId = scanner.nextLine();

        // Check if the login ID exists
        if (!userProfiles.containsKey(loginId)) {
            System.out.println("Invalid login ID.");
            return;
        }

        System.out.println("Available trains:");
        int index = 1;
        for (String trainName : trains.keySet()) {
            System.out.println(index++ + ". " + trainName);
        }

        System.out.println("Choose a train:");
        int trainChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline after integer input

        // Validate train choice
        if (trainChoice < 1 || trainChoice > trains.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String selectedTrainName = (String) trains.keySet().toArray()[trainChoice - 1];
        Train selectedTrain = trains.get(selectedTrainName);

        // Find and allocate the first available seat
        int boogieNumber = 0;
        int seatNumber = 0;
        for (int b = 0; b < 3; b++) {
            for (int s = 0; s < 2; s++) {
                if (!selectedTrain.seats[b][s]) {
                    boogieNumber = b + 1;
                    seatNumber = s + 1;
                    selectedTrain.allocateSeat(boogieNumber, seatNumber);
                    UserProfile profile = userProfiles.get(loginId);
                    profile.trainName = selectedTrainName;
                    profile.boogieNumber = boogieNumber;
                    profile.seatNumber = seatNumber;
                    System.out.println("Seat allocated: Boogie " + boogieNumber + ", Seat " + seatNumber);
                    return;
                }
            }
        }

        System.out.println("No seats available on this train.");
    }

    private static void displayPersonInformation(Scanner scanner) {
        System.out.println("Enter your login ID:");
        String loginId = scanner.nextLine();

        UserProfile profile = userProfiles.get(loginId);
        if (profile != null) {
            System.out.println(profile);
        } else {
            System.out.println("Invalid login ID.");
        }
    }

    private static void displayCompleteInformation() {
        if (userProfiles.isEmpty()) {
            System.out.println("No user profiles found.");
            return;
        }
        for (Map.Entry<String, UserProfile> entry : userProfiles.entrySet()) {
            System.out.println("Login ID: " + entry.getKey() + ", " + entry.getValue());
        }
    }

    private static void displayTrainStatus() {
        for (Train train : trains.values()) {
            System.out.println(train + " - Total Remaining Seats: " + train.getRemainingSeats() +
                    ", Total Booked Seats: " + train.getBookedSeats());
        }
    }
}
