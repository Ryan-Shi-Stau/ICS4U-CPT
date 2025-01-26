package cpt;

import javafx.geometry.Pos;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * UIManager is responsible for creating and managing the user interface components for the chart and axis selectors.
 * It includes methods for creating a legend, axis selectors, and merging the axis selectors into a container.
 * 
 * @author R. Shi
 */
public class UIManager {

    // These keep track of the x/y parameters from the dropdowns, so they can properly update the chart
    private String xParam;
    private String yParam;

    /**
     * Constructor to initialize the UIManager with x and y axis parameters.
     * 
     * @param xParam the initial x-axis parameter
     * @param yParam the initial y-axis parameter
     */
    public UIManager(String xParam, String yParam) {
        this.xParam = xParam;
        this.yParam = yParam;
    }

    /**
     * Creates a legend VBox to display player ranks and their corresponding colors.
     * The ranks are displayed with colored circles representing each rank.
     * 
     * @return a VBox containing the legend of player ranks
     */
    public VBox createLegend() {

        // Create VBox for legend
        VBox legend = new VBox(10);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setStyle("-fx-padding: 10px;");

        // Title
        Label legendTitle = new Label("Player Ranks");
        legendTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        legend.getChildren().add(legendTitle);

        // Define ranks and colors
        String[] ranks = {
            "X+ rank", "X rank", "U rank", "SS rank", 
            "S rank", "A rank", "B rank", "C rank", "D rank"
        };
        Color[] colors = {
            Color.rgb(167, 99, 234, 1), // X+ rank
            Color.rgb(255, 69, 255, 1), // X rank
            Color.rgb(255, 56, 19, 1),  // U rank
            Color.rgb(219, 139, 31, 1), // SS rank
            Color.rgb(224, 167, 27, 1), // S rank
            Color.rgb(70, 173, 81, 1),  // A rank
            Color.rgb(79, 100, 201, 1), // B rank
            Color.rgb(115, 62, 143, 1), // C rank
            Color.rgb(144, 117, 145, 1) // D rank
        };

        // Add rank items to the legend
        for (int i = 0; i < ranks.length; i++) {

            // Create HBox for each legend item
            HBox legendItem = new HBox(5);

            // Add color circle and rank label to the legend item
            Circle colorCircle = new Circle(10, colors[i]);
            Label rankLabel = new Label(ranks[i]);

            // Add this Hbox to the legend Vbox
            legendItem.getChildren().addAll(colorCircle, rankLabel);
            legend.getChildren().add(legendItem);
        }

        return legend;
    }

    /**
     * Creates a VBox containing a ComboBox to select an axis (X or Y), and a description of the selected axis.
     * When the ComboBox value changes, the axis parameter (xParam or yParam) is updated and the chart is redrawn.
     * 
     * @param label the label for the axis (e.g., "X Axis", "Y Axis")
     * @param defaultValue the default value to set for the ComboBox (e.g., "PPS", "APM")
     * @param isXAxis a boolean indicating whether the axis is the X-axis or Y-axis
     * @param chartManager the ChartManager instance used to update the chart
     * @param scatterChart the ScatterChart to be updated
     * @return a VBox containing the axis selector UI components
     */
    public VBox createAxisSelector(String label, String defaultValue, boolean isXAxis, ChartManager chartManager, ScatterChart<Number, Number> scatterChart) {

        // Create label and description for the axis
        Label comboBoxLabel = new Label(label);
        Label axisDescription = new Label(getDescription(defaultValue));
        axisDescription.setWrapText(true);

        // Create ComboBox with axis options
        ComboBox<String> axisComboBox = new ComboBox<>();
        axisComboBox.getItems().addAll("TR", "APM", "PPS", "Glicko", "RD", "VS");
        axisComboBox.setValue(defaultValue);

        // Set action to update description and chart when ComboBox value changes
        axisComboBox.setOnAction(event -> {
            String selectedValue = axisComboBox.getValue();
            axisDescription.setText(getDescription(selectedValue));

            // Update the respective axis parameter 
            if (isXAxis) {
                xParam = selectedValue;
            } else {
                yParam = selectedValue;
            }

            // The chart is dependent on 2 seperate ComboBoxes, so we need to update the chart when either ComboBox changes
            // This actually caused me a lot of problems trying to make a method to create the comboboxes  
            // I found this solution (track x/yparam in the class as a class variable) after 1-1.5 hours haha
            chartManager.updateChart(scatterChart, xParam, yParam);
        });

        // Create VBox to hold the label, ComboBox, and description
        VBox axisSelector = new VBox(10, comboBoxLabel, axisComboBox, axisDescription);
        axisSelector.setStyle("-fx-padding: 10px;");

        return axisSelector;
    }

    /**
     * Merges two VBox containers (xAxisBox and yAxisBox) into a single HBox.
     * This allows the x and y axis selectors to be displayed side by side.
     * 
     * @param xAxisBox the VBox containing the X-axis selector components
     * @param yAxisBox the VBox containing the Y-axis selector components
     * @return an HBox containing the merged axis selectors
     */
    public HBox mergeVBox(VBox xAxisBox, VBox yAxisBox) {

        // Alignment and other settings for the VBox containers
        xAxisBox.setPrefWidth(800);
        yAxisBox.setPrefWidth(800);
        xAxisBox.setAlignment(Pos.CENTER);
        yAxisBox.setAlignment(Pos.CENTER);
        HBox dropdownContainer = new HBox(10, xAxisBox, yAxisBox);
        HBox.setHgrow(xAxisBox, Priority.ALWAYS);
        HBox.setHgrow(yAxisBox, Priority.ALWAYS);
        dropdownContainer.setAlignment(Pos.CENTER);

        return dropdownContainer;
    }

    /**
     * Retrieves a description for the given attribute.
     * This description provides information about the meaning and purpose of each axis parameter.
     * 
     * @param attribute the parameter for which the description is required (e.g., "TR", "APM", "PPS")
     * @return a description of the given parameter
     */
    public static String getDescription(String attribute) {

        // This is for dynamically updating the description of the axis
        switch (attribute) {
            case "TR":
                return "Tetra Rating (TR) is the main rating system used in TETR.IO, going from 0-25000. It is a measure of a player's skill level, and is used to determine their rank.";
            case "APM":
                return "Attack Per Minute (APM) is a measure of how many garbage lines a player sends to their opponent per minute.";
            case "PPS":
                return "Pieces Per Second (PPS) is a measure of how fast a player plays, and how quickly a player can place pieces on the board.";
            case "Glicko":
                return "Glicko-2 is a rating system used in TETR.IO to measure a player's skill level, similar to Elo but with additional uncertainty.";
            case "RD":
                return "Rating Deviation (RD) is a measure of the uncertainty of a player's rating in the Glicko system. RD increases with inactivity.";
            case "VS":
                return "Versus Score (VS) indicates how well you performed in a round, based on pieces, lines sent, and garbage cleared.";
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }
}
