package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class UserHomePage {

	private final DatabaseHelper databaseHelper;
	private String userName;
	  public UserHomePage(DatabaseHelper databaseHelper, String userName) {
	        this.databaseHelper = databaseHelper;
	        this.userName = userName;
	    } 
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Student!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Button returnHome = new Button("Return Home");
	    
	    Button chatPage = new Button("Go to private messages");
	    	chatPage.setOnAction( a -> {
	    		 int userId = Integer.parseInt(databaseHelper.getUserId(userName));
	    		    new ChatPage(databaseHelper, userName, userId).show(primaryStage);
	    	});
	    
	    Button questionPage = new Button("Question Page");
	    
	    returnHome.setOnAction( a -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
	    
	    questionPage.setOnAction(a -> { 
	    	try {
				databaseHelper.connectToDatabase();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	System.out.print("To question page");
	    	new QuestionsAnswersPage(databaseHelper).show(primaryStage);
	    });

	    layout.getChildren().addAll(userLabel, returnHome, questionPage, chatPage);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}