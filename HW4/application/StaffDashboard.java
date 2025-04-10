package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDashboard {

    private final DatabaseHelper dbHelper;
    private final String staffUsername;

    public StaffDashboard(DatabaseHelper dbHelper, String staffUsername) {
        this.dbHelper = dbHelper;
        this.staffUsername = staffUsername;
    }

    public void show(Stage primaryStage) {
        // Main container with inline styling for background and padding
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f7f9fc;");

        // Title label with inline styles
        Label title = new Label("Staff Review Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 10 0 20 0;");

        // Create buttons with inline styling
        Button reviewQuestionsButton = new Button("Review Questions");
        reviewQuestionsButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");

        Button reviewAnswersButton = new Button("Review Answers");
        reviewAnswersButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");

        Button reviewMessagesButton = new Button("Review Private Messages");
        reviewMessagesButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");

        Button backButton = new Button("Return");
        backButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");

        // When clicked, show a popup listing all questions
        reviewQuestionsButton.setOnAction(e -> {
            try {
                ResultSet rs = dbHelper.getAllQuestions();
                showDataPopup("Questions", rs, "qTitle", "qDesc");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // When clicked, show a popup listing all answers
        reviewAnswersButton.setOnAction(e -> {
            try {
                ResultSet rs = dbHelper.getAllAnswers();
                showDataPopup("Answers", rs, "answerFrom", "answerText");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // When clicked, show a popup listing all direct messages
        reviewMessagesButton.setOnAction(e -> {
            try {
                ResultSet rs = dbHelper.getAllMessages();
                showMessagesPopup(rs);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Return to the home/login page
        backButton.setOnAction(e -> {
            new SetupLoginSelectionPage(dbHelper).show(primaryStage);
        });

        // Add components to the root container
        root.getChildren().addAll(title, reviewQuestionsButton, reviewAnswersButton, reviewMessagesButton, backButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
    }

    // Helper method to show generic data in a popup (for questions/answers)
    private void showDataPopup(String title, ResultSet rs, String field1, String field2) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        StringBuilder content = new StringBuilder();
        while (rs.next()) {
            content.append(rs.getString(field1)).append(": ")
                   .append(rs.getString(field2)).append("\n\n");
        }
        String output = content.toString().isEmpty() ? "No data found." : content.toString();
        alert.setContentText(output);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    // Helper method to show messages in a popup
    private void showMessagesPopup(ResultSet rs) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Private Messages");
        alert.setHeaderText(null);

        StringBuilder content = new StringBuilder();
        while (rs.next()) {
            // Assuming your getAllMessages() returns senderName, receiverName, messageText, timestamp
            String sender = rs.getString("senderName");
            String receiver = rs.getString("receiverName");
            String message = rs.getString("messageText");
            String time = rs.getTimestamp("timestamp").toString();

            content.append("[").append(time).append("] ")
                   .append(sender).append(" ➝ ").append(receiver)
                   .append(": ").append(message).append("\n\n");
        }
        String output = content.toString().isEmpty() ? "No messages found." : content.toString();
        alert.setContentText(output);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
