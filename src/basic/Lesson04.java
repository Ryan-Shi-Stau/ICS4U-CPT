package basic;

// Import JavaFX classes needed for this application.
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * A JavaFX application to create a simple line chart with hard-coded data.
 */
public class Lesson04 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a NumberAxis for the x-axis (horizontal axis).
        // Arguments: label, lower bound, upper bound, tick unit.
        NumberAxis xAxis = new NumberAxis("X-Axis", 0, 10, 1);

        // Create a NumberAxis for the y-axis (vertical axis).
        // Arguments: label, lower bound, upper bound, tick unit.
        NumberAxis yAxis = new NumberAxis("Y-Axis", 0, 100, 10);

        // Create a LineChart using the x-axis and y-axis.
        // A LineChart connects data points with lines.
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Set the title of the chart.
        lineChart.setTitle("Simple Line Chart");

        // Create a data series to hold the data points for the chart.
        // A Series contains multiple data points (x, y values) that will be plotted.
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Set the name of the data series, which will appear in the chart legend.
        series.setName("Sample Data");

        // Add data points to the series.
        // Each data point is created using XYChart.Data<>(x, y).
        series.getData().add(new XYChart.Data<>(1, 20)); // Point at (1, 20)
        series.getData().add(new XYChart.Data<>(2, 40)); // Point at (2, 40)
        series.getData().add(new XYChart.Data<>(3, 60)); // Point at (3, 60)
        series.getData().add(new XYChart.Data<>(4, 80)); // Point at (4, 80)

        // Add the data series to the chart.
        lineChart.getData().add(series);

        // Create a Scene to hold the chart.
        // Arguments: root node (lineChart), width, height.
        Scene scene = new Scene(lineChart, 600, 400);

        // Attach the Scene to the Stage (the top-level window).
        primaryStage.setScene(scene);

        // Set the title of the Stage (window).
        primaryStage.setTitle("Line Chart Example");

        // Show the Stage (make the window visible).
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application. This calls the start() method automatically.
        launch(args);
    }
}