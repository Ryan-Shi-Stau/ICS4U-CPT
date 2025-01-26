package basic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Lesson03 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // First Scene
        Label label1 = new Label("This is Scene 1");
        Button button1 = new Button("Go to Scene 2");
        VBox layout1 = new VBox(10, label1, button1);
        Scene scene1 = new Scene(layout1, 300, 200);
        
        // Second Scene
        Label label2 = new Label("This is Scene 2");
        Button button2 = new Button("Go to Scene 1");
        // Note a different style of creating a VBox layout below, to same effect
        VBox layout2 = new VBox(10); // Create empty VBox 
        layout2.getChildren().addAll(label2, button2); // Add elements to layout
        Scene scene2 = new Scene(layout2, 300, 200);
        
        // Button actions to switch scenes, use lambda expression to be concise
        button1.setOnAction(e -> primaryStage.setScene(scene2));
        button2.setOnAction(e -> primaryStage.setScene(scene1));
        
        // Set up and show the stage
        primaryStage.setTitle("Scene Switching Demo");
        primaryStage.setScene(scene1);  // Start with scene1
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}