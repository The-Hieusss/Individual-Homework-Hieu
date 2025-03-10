package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        QuestionController questionController = new QuestionController(); // Initialize Controller
        Scene scene = new Scene(questionController.getView(), 600, 400); // Main Scene
        
        primaryStage.setTitle("Q&A System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
