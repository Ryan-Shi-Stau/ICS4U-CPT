package cpt;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

/**
 * ChartManager
 * This class is responsible for managing and rendering a ScatterChart based on player statistics. 
 * It handles creating the chart with data points, color-coding based on player rank, and updating the chart 
 * when attributes (such as X and Y axis parameters) change.
 * 
 * @param players List of Player objects to be used for chart data.
 * @author R. Shi
 */
public class ChartManager {


    // These are used in most methods, so they are class variables
    private List<Player> players;
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();

    /**
     * ChartManager Constructor
     * Initializes the ChartManager with the given list of players.
     * 
     * @param players The list of Player objects containing the data to be visualized.
     */
    public ChartManager(List<Player> players) {
        this.players = players;
    }

    /**
     * createChart
     * Creates a ScatterChart based on the specified X and Y axis parameters, with data points color-coded by rank.
     * The chart is populated with player data and displayed with axis labels corresponding to the input parameters.
     * 
     * @param xParam The attribute to be displayed on the X-axis (e.g., "TR", "APM").
     * @param yParam The attribute to be displayed on the Y-axis (e.g., "Glicko", "RD").
     * @return A ScatterChart populated with player data.
     * @throws IllegalArgumentException If the provided attributes are invalid.
     * @author R. Shi
     */
    public ScatterChart<Number, Number> createChart(String xParam, String yParam) {

        // Change axis title
        xAxis.setLabel(xParam);
        yAxis.setLabel(yParam);

        // Create ScatterChart
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("TETR.IO stat comparisons");

        // Create series by rank for color coding
        XYChart.Series<Number, Number>[] series = new XYChart.Series[9];
        for (int i = 0; i < series.length; i++) {
            series[i] = new XYChart.Series<>();
        }

        series[0].setName("D rank");
        series[1].setName("C rank");
        series[2].setName("B rank");
        series[3].setName("A rank");
        series[4].setName("S rank");
        series[5].setName("SS rank");
        series[6].setName("U rank");
        series[7].setName("X rank");
        series[8].setName("X+ rank");

        // Efficiently process data points
        int log = 1;
        for (Player player : players) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(getPlayerAttribute(player, xParam), getPlayerAttribute(player, yParam));
            switch (player.getRank()) {
                case "d":
                case "d+":
                    dataPoint.setNode(new Circle(4, Color.rgb(144, 117, 145, 0.6)));
                    series[0].getData().add(dataPoint);
                    break;
                case "c":
                case "c-":
                case "c+":
                    dataPoint.setNode(new Circle(4, Color.rgb(115, 62, 143, 0.6)));
                    series[1].getData().add(dataPoint);
                    break;
                case "b":
                case "b-":
                case "b+":
                    dataPoint.setNode(new Circle(4, Color.rgb(79, 100, 201, 0.6)));
                    series[2].getData().add(dataPoint);
                    break;
                case "a":
                case "a-":
                case "a+":
                    dataPoint.setNode(new Circle(4, Color.rgb(70, 173, 81, 0.6)));
                    series[3].getData().add(dataPoint);
                    break;
                case "s":
                case "s-":
                case "s+":
                    dataPoint.setNode(new Circle(4, Color.rgb(224, 167, 27, 0.6)));
                    series[4].getData().add(dataPoint);
                    break;
                case "ss":
                    dataPoint.setNode(new Circle(4, Color.rgb(219, 139, 31, 0.6)));
                    series[5].getData().add(dataPoint);
                    break;
                case "u":
                    dataPoint.setNode(new Circle(4, Color.rgb(255, 56, 19, 0.6)));
                    series[6].getData().add(dataPoint);
                    break;
                case "x":
                    dataPoint.setNode(new Circle(4, Color.rgb(255, 69, 255, 0.6)));
                    series[7].getData().add(dataPoint);
                    break;
                case "x+":
                    dataPoint.setNode(new Circle(4, Color.rgb(167, 99, 234, 0.6)));
                    series[8].getData().add(dataPoint);
                    break;
            }

            // Add tooltip to data points
            Tooltip tooltip = new Tooltip("Username: " + player.getUsername() + "\nRank: " + player.getRank() + 
                            "\nTR: " + player.getTr() + "\nGlicko: " + player.getGlicko() +
                            "\nRD: " + player.getRd() + "\nAPM: " + player.getApm() +
                            "\nPPS: " + player.getPps() + "\nVS: " + player.getVs());
                            tooltip.setShowDelay(Duration.seconds(0));
            Tooltip.install(dataPoint.getNode(), tooltip);  // Attach the tooltip to the data point
            
            // Log progress
            if (log % 1000 == 0 || log == players.size()) {
                System.out.println("Loaded " + log + " out of " + players.size() + " players");
            }
            log++;
        }

        System.out.println("Added points to chart"); // Debug logging

        // Add data to chart
        for (XYChart.Series<Number, Number> serie : series) {
            scatterChart.getData().add(serie);
            System.out.println("Processed series " + serie.getName());
        }

        /* Hide legend since i can't figure out how to set the scatterplot legend to have the same
        colours as the data points, by default it rotates through a couple set shapes and 
        colours. I spent 2 hours on chatGPT as well as manually digging through the documentation.
        With CSS, I figured out how to change the shape, but the colours just wouldn't change. 
        It also doesn't help that there's a grand total of 1 stackoverflow question on this topic, and
        it's for a different version of JavaFX, as well as the javafx documentation doesn't have an explicit
        example for scatter charts, since the legends for scatter charts are different from other charts.

        end of ranty anecdote lol
        */
        scatterChart.setLegendVisible(false);

        return scatterChart;
    }

    /**
     * updateChart
     * Updates the ScatterChart with new data points based on the selected X and Y parameters.
     * Clears the existing data and re-renders the chart with updated attribute selections.
     * 
     * @param scatterChart The ScatterChart to be updated.
     * @param xParam The new X-axis attribute (e.g., "TR", "APM").
     * @param yParam The new Y-axis attribute (e.g., "Glicko", "RD").
     * @author R. Shi
     */
    public void updateChart(ScatterChart<Number, Number> scatterChart, String xParam, String yParam) {

        // Disable animations for faster loading
        scatterChart.setAnimated(false);

        // Update axis labels
        xAxis.setLabel(xParam);
        yAxis.setLabel(yParam);

        System.out.println("Clearing data");
        scatterChart.getData().clear(); // Clear existing data
        System.out.println("Cleared data");

        // Create new series for each rank
        XYChart.Series<Number, Number>[] series = new XYChart.Series[9];
        for (int i = 0; i < series.length; i++) {
            series[i] = new XYChart.Series<>();
        }

        series[0].setName("D rank");
        series[1].setName("C rank");
        series[2].setName("B rank");
        series[3].setName("A rank");
        series[4].setName("S rank");
        series[5].setName("SS rank");
        series[6].setName("U rank");
        series[7].setName("X rank");
        series[8].setName("X+ rank");

        // this is a variable instead of player.indexOf() because it's less resource intensive
        int log = 1;

        // Process player data points
        for (Player player : players) {

            // it's variables for readability, you can just plug it in to the 3rd line there

            double xValue = getPlayerAttribute(player, xParam);
            double yValue = getPlayerAttribute(player, yParam);
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(xValue, yValue);

            // Color code data points based on rank
            switch (player.getRank()) {
                case "d":
                case "d+":
                    dataPoint.setNode(new Circle(4, Color.rgb(144, 117, 145, 0.6)));
                    series[0].getData().add(dataPoint);
                    break;
                case "c":
                case "c-":
                case "c+":
                    dataPoint.setNode(new Circle(4, Color.rgb(115, 62, 143, 0.6)));
                    series[1].getData().add(dataPoint);
                    break;
                case "b":
                case "b-":
                case "b+":
                    dataPoint.setNode(new Circle(4, Color.rgb(79, 100, 201, 0.6)));
                    series[2].getData().add(dataPoint);
                    break;
                case "a":
                case "a-":
                case "a+":
                    dataPoint.setNode(new Circle(4, Color.rgb(70, 173, 81, 0.6)));
                    series[3].getData().add(dataPoint);
                    break;
                case "s":
                case "s-":
                case "s+":
                    dataPoint.setNode(new Circle(4, Color.rgb(224, 167, 27, 0.6)));
                    series[4].getData().add(dataPoint);
                    break;
                case "ss":
                    dataPoint.setNode(new Circle(4, Color.rgb(219, 139, 31, 0.6)));
                    series[5].getData().add(dataPoint);
                    break;
                case "u":
                    dataPoint.setNode(new Circle(4, Color.rgb(255, 56, 19, 0.6)));
                    series[6].getData().add(dataPoint);
                    break;
                case "x":
                    dataPoint.setNode(new Circle(4, Color.rgb(255, 69, 255, 0.6)));
                    series[7].getData().add(dataPoint);
                    break;
                case "x+":
                    dataPoint.setNode(new Circle(4, Color.rgb(167, 99, 234, 0.6)));
                    series[8].getData().add(dataPoint);
                    break;
            }

            // Add tooltip to data points
            Tooltip tooltip = new Tooltip("Username: " + player.getUsername() + "\nRank: " + player.getRank() + 
                            "\nTR: " + player.getTr() + "\nGlicko: " + player.getGlicko() +
                            "\nRD: " + player.getRd() + "\nAPM: " + player.getApm() +
                            "\nPPS: " + player.getPps() + "\nVS: " + player.getVs());
                            tooltip.setShowDelay(Duration.seconds(0));
            Tooltip.install(dataPoint.getNode(), tooltip);  // Attach the tooltip to the data point
                
            // Log progress, especially helps to show somethings happening when using the large csv
            if (log % 1000 == 0 || log == players.size()) {
                System.out.println("Loaded " + log + " out of " + players.size() + " players");
            }
            log++;
        }

        // Re-enable animations
        scatterChart.setAnimated(true);

        // Add series to the chart
        scatterChart.getData().addAll(series);
        scatterChart.setStyle("-fx-padding: 10px;");
    }

    /**
     * getPlayerAttribute
     * Retrieves the value of a specific attribute for a given player.
     * 
     * @param player The Player object whose attribute is to be retrieved.
     * @param attribute The name of the attribute (e.g., "TR", "APM").
     * @return The value of the specified attribute for the player.
     * @throws IllegalArgumentException If the provided attribute is invalid.
     * @author R. Shi
     */
    private double getPlayerAttribute(Player player, String attribute) {

        // This is a thing instead of doing player.getTr() because the thing the calls this
        // takes the combobox value as a variable

        // Return the attribute value based on the input parameter
        switch (attribute.toLowerCase()) {
            case "tr":
                return player.getTr();
            case "glicko":
                return player.getGlicko();
            case "rd":
                return player.getRd();
            case "apm":
                return player.getApm();
            case "pps":
                return player.getPps();
            case "vs":
                return player.getVs();
            default: // This will never be used since all the combobox values are already included
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }
}
