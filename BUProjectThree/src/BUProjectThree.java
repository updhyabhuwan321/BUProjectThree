
/**
 * @author acer Bhuwan Upadhyaya
 * Date: 5/5/2024
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class BUProjectThree {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the file name for CellTower definitions:");
        String cellTowersFileName = scanner.nextLine();

        System.out.println("Enter the file name for Waypoint definitions:");
        String waypointsFileName = scanner.nextLine();

        System.out.println("Enter the file name for the set of waypoint names for potential journeys:");
        String journeysFileName = scanner.nextLine();
        System.out.println("");

        HashMap<String, CellTower> towers = readCellTowers(cellTowersFileName);
        HashMap<String, Waypoint> waypoints = readWaypoints(waypointsFileName);
        ArrayList<String[]> journeys = readJourneys(journeysFileName);

        try {
            PrintWriter printWriter = new PrintWriter("Report.txt");
            Date now = new Date();
            printWriter.println("   Cellphone Tower Coverage Report");
            printWriter.println("Prepared on: " + now.toString());

            int journeyNumber = 1;
            for (String[] journey : journeys) {
                printWriter.println();
                printWriter.println(
                        "The " + ordinal(journeyNumber) + " journey consists of " + journey.length + " waypoints:");
                for (String waypointName : journey) {
                    printWriter.print(waypointName + " ");
                }
                printWriter.println();
                printWriter.println("                           Mid-point:");
                printWriter.printf("%6s %6s %8s %8s %8s%n", "From", "To", "Distance", "X-coord", "Y-coord");
                printWriter.printf("%5s %5s %7s %7s %9s%n", "------", "------", "--------", " -------", "-------");

                reportWaypointDistances(waypoints, journey, printWriter);
                reportCoverage(waypoints, towers, journey, printWriter);
                journeyNumber++;
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Report file not found.");
        }
        System.out.println("");
    }

    /**
     * Reads cell tower data from a file and stores it in a HashMap.
     *
     * @param fileName The name of the file containing cell tower data.
     * @return A HashMap containing cell tower names as keys and corresponding
     *         CellTower objects as values.
     */
    private static HashMap<String, CellTower> readCellTowers(String fileName) {
        HashMap<String, CellTower> towers = new HashMap<>();
        int skippedRecords = 0;
        try (Scanner keyboard = new Scanner(new File(fileName))) {
            int recordNumber = 0;
            while (keyboard.hasNextLine()) {
                String line = keyboard.nextLine();
                recordNumber++;
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("    Skipping celltower record " + recordNumber + ": " + line);
                    System.out.println("        Incomplete data in celltower record " + recordNumber + ".");
                    skippedRecords++;
                    continue;
                }
                try {
                    String name = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double range = Double.parseDouble(parts[3]);
                    if (x >= 0 && y >= 0 && range > 0) {
                        towers.put(name, new CellTower(name, x, y, range));
                    } else {
                        if (x < 0 || y < 0) {
                            System.out.println("    Skipping celltower record " + recordNumber + ": " + line);
                            System.out.println(
                                    "        Celltower record " + recordNumber + " has invalid coordinate(s).");
                        }
                        if (range <= 0) {
                            System.out.println("    Skipping celltower record " + recordNumber + ": " + line);
                            System.out.println("        Celltower record " + recordNumber + " has an invalid radius.");
                        }
                        skippedRecords++;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("    Skipping celltower record " + recordNumber + ": " + line);
                    System.out.println("        Number Format Exception in celltower record " + recordNumber + ".");
                    System.out.println("        System message: " + e.getMessage());
                    skippedRecords++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Celltower file not found.");
        }
        System.out.println(
                "Information for " + towers.size() + " cell towers defined. " + skippedRecords + " Records skipped.");
        System.out.println("");
        return towers;
    }

    /**
     * Reads Waypoints data from a file and stores it in a HashMap.
     *
     * @param fileName The name of the file containing Waypoints data.
     * @return A HashMap containing Waypoints names as keys and corresponding
     *         Waypoints objects as values.
     */
    private static HashMap<String, Waypoint> readWaypoints(String fileName) {
        HashMap<String, Waypoint> waypoints = new HashMap<>();
        int skippedRecords = 0;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int recordNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                recordNumber++;
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    continue;
                }
                try {
                    String name = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    waypoints.put(name, new Waypoint(name, x, y));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("    Skipping waypoint record " + recordNumber + ": " + line);
                    System.out.println("    Number Format Exception in waypoint record " + recordNumber + ".");
                    System.out.println("    System message: " + e.getMessage());
                    skippedRecords++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Waypoint file not found.");
        }
        System.out.println(
                "    Information for " + waypoints.size() + " waypoints defined. " + skippedRecords + " Records skipped.");
        return waypoints;
    }

    /**
     * Reads journey data from a file and stores it in an ArrayList of String
     * arrays. Each String array represents a journey.
     *
     * @param fileName The name of the file containing journey data.
     * @return An ArrayList of String arrays representing journeys.
     */
    private static ArrayList<String[]> readJourneys(String fileName) {
        ArrayList<String[]> journeys = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                journeys.add(scanner.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Journeys file not found.");
        }
        return journeys;
    }

    /**
     * Reports distances between consecutive Waypoints along a journey.
     *
     * @param waypoints A map of Waypoints names to their corresponding objects.
     * @param journey   An array representing the sequence of traversed Waypoints.
     * @param writer    The PrintWriter object to write the report.
     */
    private static void reportWaypointDistances(HashMap<String, Waypoint> waypoints, String[] journey,
            PrintWriter writer) {
        int totalWaypoints = 0;
        for (int i = 0; i < journey.length - 1; i++) {
            Waypoint from = waypoints.get(journey[i]);
            Waypoint to = waypoints.get(journey[i + 1]);
            if (from != null && to != null) {
                double distance = calculateDistance(from, to);
                double midX = (from.getX() + to.getX()) / 2;
                double midY = (from.getY() + to.getY()) / 2;
                writer.printf("%6s %6s %8.2f %8.2f %9.2f%n", from.getName(), to.getName(), distance, midX, midY);
                totalWaypoints++;
            }
        }

    }

    /**
     * Generates a coverage report based on the journey through waypoints and
     * their proximity to cell towers.
     *
     * @param waypoints A map of waypoint names to their corresponding objects.
     * @param towers    A map of cell tower names to their corresponding objects.
     * @param journey   An array representing the sequence of traversed waypoints.
     * @param writer    The PrintWriter object to write the coverage report.
     */
    private static void reportCoverage(HashMap<String, Waypoint> waypoints, HashMap<String, CellTower> towers,
            String[] journey, PrintWriter writer) {

    // Print header for the proximity report
    writer.println("Location Tower Proximity");
    writer.println("-------- ------- ---------");

    // Iterate through the journey waypoints
    for (int i = 0; i < journey.length - 1; i++) {
        Waypoint from = waypoints.get(journey[i]);
        Waypoint to = waypoints.get(journey[i + 1]);

        // Check if both 'from' and 'to' waypoints exist
        if (from != null && to != null) {
            // Find the closest tower to the current waypoint and its midpoint
            CellTower closestTowerFrom = findClosestTower(from, towers);
            CellTower closestTowerMid = findClosestTower(midpoint(from, to), towers);

            // Check if there is coverage for the current waypoint
            boolean hasCoverageFrom = closestTowerFrom != null &&
                calculateDistance(from, new Waypoint(closestTowerFrom.getName(), closestTowerFrom.getX(), closestTowerFrom.getY())) <= closestTowerFrom.getRange();

            // Check if there is coverage for the midpoint
            boolean hasCoverageMid = closestTowerMid != null &&
                calculateDistance(midpoint(from, to), new Waypoint(closestTowerMid.getName(), closestTowerMid.getX(), closestTowerMid.getY())) <= closestTowerMid.getRange();

            // Print proximity information for the current waypoint
            if (hasCoverageFrom) {
                writer.printf("%-8s %-8s %-8.1f%n", from.getName(), closestTowerFrom.getName(),
                    calculateDistance(from, new Waypoint(closestTowerFrom.getName(), closestTowerFrom.getX(), closestTowerFrom.getY())));
            } else {
                writer.printf("%-8s %-8s %-8s%n", from.getName(), "No Coverage.", "");
            }

            // Print proximity information for the midpoint
            if (hasCoverageMid) {
                writer.printf("%-8s %-8s %-8.1f%n", "Mid-Pt", closestTowerMid.getName(),
                    calculateDistance(midpoint(from, to), new Waypoint(closestTowerMid.getName(), closestTowerMid.getX(), closestTowerMid.getY())));
            } else {
                writer.printf("%-8s %-8s %-8s%n", "Mid-Pt", "No Coverage.", "");
            }
        } else {
            // Print a message if a waypoint is not defined
            if (from == null) {
                System.out.println("");
                System.out.println("Skipping undefined waypoint " + journey[i] + " in " + (journey.length - 1)
                        + "rd journey.");
                System.out.println("There are " + (journey.length - 1) + " waypoints in this journey.");
            }
        }
    }
}




    /**
     * Checks if any cell tower in the provided HashMap is within range of
     * the given waypoint.
     *
     * @param waypoint The waypoint to check coverage for.
     * @param towers   A map of cell tower names to their corresponding
     *                 objects.
     * @return True if at least one tower is within range of the waypoint,
     *         false otherwise.
     */
    private static boolean towersInRange(Waypoint waypoint, HashMap<String, CellTower> towers) {
        for (CellTower tower : towers.values()) {
            double distance = calculateDistance(waypoint, new Waypoint(tower.getName(), tower.getX(), tower.getY()));
            if (distance <= tower.getRange()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the distance between two waypoints.
     *
     * @param from The starting waypoint.
     * @param to   The destination waypoint.
     * @return The Euclidean distance between the two waypoints.
     */
    private static double calculateDistance(Waypoint from, Waypoint to) {
        return Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
    }

    /**
     * Generates the ordinal representation of a given number.
     *
     * @param number The number for which to generate the ordinal
     *               representation.
     * @return The ordinal representation of the given number.
     */
    private static String ordinal(int number) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        int mod100 = number % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return number + "th";
        } else {
            return number + suffixes[number % 10];
        }
    }

    private static Waypoint midpoint(Waypoint from, Waypoint to) {
        double midX = (from.getX() + to.getX()) / 2;
        double midY = (from.getY() + to.getY()) / 2;
        return new Waypoint("Midpoint", midX, midY);
    }

    private static CellTower findClosestTower(Waypoint waypoint, HashMap<String, CellTower> towers) {
        CellTower closestTower = null;
        double minDistance = Double.MAX_VALUE;
    
        for (CellTower tower : towers.values()) {
            double distance = calculateDistance(waypoint, new Waypoint(tower.getName(), tower.getX(), tower.getY()));
            if (distance <= tower.getRange() && distance < minDistance) {
                minDistance = distance;
                closestTower = tower;
            }
        }
    
        return closestTower;
    }
}
