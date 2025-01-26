package basic;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Lesson02 extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, JavaFX!");
        Button button = new Button("Click Me");
         
        // Event handler using lambda expression
        // Read more about using lambda expressions at https://www.w3schools.com/java/java_lambda.asp
        button.setOnAction(e -> label.setText("Button Clicked!"));

        // For comparison, this is a traditional event handler instead of lambda expression
        //
        // button.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent e) {
        //         label.setText("Button Clicked!");
        //     }
        // });
        
        VBox layout = new VBox(10, label, button);
        layout.setAlignment(Pos.CENTER); // Align elements to center
        
        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Basic JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}