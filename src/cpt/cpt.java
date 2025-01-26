package cpt;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.*;

/**
 * Main class for the JavaFX application that visualizes dynamic chart axes based on user selection.
 * It includes loading data from a CSV file, setting up the chart, and managing UI interactions.
 * 
 * @author R. Shi
 */
public class cpt extends Application {

    // defined as static since most methods in this file need to access this
    




    /**
     * The main method that launches the JavaFX application 
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Launch JavaFX
        launch(args);
    }

    /**
     * The start method that loads the csv file, initializes the primary stage, sets up the chart, 
     * UI components, and defines the layout.
     * 
     * @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) { // JavaFX code

        // Load players from smaller CSV file
        String filePath = "src/cpt/mini.csv";

        // uncomment following line for CSV with all of players, warning may lag computer
        // filePath = "src/cpt/leaderboard.csv";

        List<Player> players = DataManager.loadPlayersFromCsv(filePath);
        System.out.println("Loaded csv");

        // x and y axis initial parameters
        String xParam = "PPS";
        String yParam = "TR";

        // Create a ChartManager instance and a chart with initial x and y axis, and create a scatter plot
        ChartManager chartManager = new ChartManager(players);
        ScatterChart<Number, Number> scatterChart = chartManager.createChart(xParam, yParam);

        // Create a UIManager instance with x and y axis parameters, and create the legend
        UIManager uiManager = new UIManager(xParam, yParam);
        VBox legend = uiManager.createLegend();

        // Game description
        Label gameDescription = new Label(
                "TETR.IO is a free online multiplayer puzzle game inspired by Tetris, developed by osk. " +
                "It features a competitive, fast-paced gameplay experience with ranked matches, casual lobbies, " +
                "and single-player modes. Players can compete worldwide to climb the leaderboards, customize " +
                "their gameplay experience, and participate in tournaments. Its modern design, smooth mechanics, " +
                "and active community make it a popular choice for both casual and competitive Tetris enthusiasts.");
        gameDescription.setWrapText(true);
        gameDescription.setStyle("-fx-padding: 30px;");

        // Creates the x and y axis combobox, as well as the description of the axis which dynamically updates
        VBox xAxisBox = uiManager.createAxisSelector("X Axis", xParam, true, chartManager, scatterChart);
        VBox yAxisBox = uiManager.createAxisSelector("Y Axis", yParam, false, chartManager, scatterChart);

        // Merge X and Y axis selector containers into one HBox
        HBox dropdownContainer = uiManager.mergeVBox(xAxisBox, yAxisBox);

        // Create chart container with the scatter chart and legend
        HBox chartContainer = new HBox(10, scatterChart, legend);
        HBox.setHgrow(scatterChart, Priority.ALWAYS); // Chart takes 80% of the space
        HBox.setHgrow(legend, Priority.NEVER); // Legend takes 20% of the space

        // Create the final VBox layout
        VBox layout = new VBox(10, chartContainer, gameDescription, dropdownContainer);

        // Create and set the scene
        System.out.println("Scene setup");
        Scene scene = new Scene(layout, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dynamic Axes Chart");
        primaryStage.show();
    }
}
