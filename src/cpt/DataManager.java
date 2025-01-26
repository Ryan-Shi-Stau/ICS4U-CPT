package cpt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataManager is responsible for loading player data from a CSV file and parsing it into a list of Player objects.
 * It handles the reading of the CSV file and mapping the data to Player attributes.
 * 
 * @author R. Shi
 */
public class DataManager {

    /**
     * Loads players from a given CSV file and returns a list of Player objects.
     * The CSV file should have a specific structure with columns such as username, TR, rank, Glicko, RD, APM, PPS, and VS.
     * 
     * @param filePath the path to the CSV file to be loaded
     * @return a list of Player objects populated with data from the CSV file
     */
    public static List<Player> loadPlayersFromCsv(String filePath) {

        // List to hold the players
        List<Player> players = new ArrayList<>(); 

        // Try catch block to handle I/O exceptions 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // We need this boolean because continue is only allowed in loops
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    // Skip the header row
                    isHeader = false; 
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(","); 

                // Assign values to player variables
                String username = values[0];
                double tr = Double.parseDouble(values[1]);
                String rank = values[2];
                double glicko = Double.parseDouble(values[3]);
                double rd = Double.parseDouble(values[4]);
                double apm = Double.parseDouble(values[5]);
                double pps = Double.parseDouble(values[6]);
                double vs = Double.parseDouble(values[7]);

                // Create a new Player instance and add it to the players list
                Player player = new Player(username, tr, rank, glicko, rd, apm, pps, vs);
                players.add(player);
            }
        } catch (IOException | NumberFormatException e) {
            // Handle any I/O or parsing errors
            e.printStackTrace(); 
        }
        return players; 
    }
}
