package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChatPage {

    private final DatabaseHelper dbHelper;
    private final String currentUsername;
    private final int currentUserId;

    private final Map<String, Integer> userMap = new HashMap<>();

    public ChatPage(DatabaseHelper dbHelper, String currentUsername, int currentUserId) {
        this.dbHelper = dbHelper;
        this.currentUsername = currentUsername;
        this.currentUserId = currentUserId;
    }

    public void show(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Private Messages");
        ComboBox<String> userSelector = new ComboBox<>();
        userSelector.setPromptText("Select a user to chat with");

        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefHeight(300);

        HBox inputBox = new HBox(10);
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message...");
        Button sendButton = new Button("Send");

        inputBox.getChildren().addAll(inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button backButton = new Button("Return to Home");
        backButton.setOnAction(e -> {
            new UserHomePage(dbHelper, currentUsername).show(primaryStage);
        });

        root.getChildren().addAll(title, userSelector, messageArea, inputBox, backButton);

        // Populate the user selector
        try {
            ResultSet rs = dbHelper.getUsersExcept(currentUsername);
            while (rs.next()) {
                String name = rs.getString("userName");
                int id = Integer.parseInt(dbHelper.getUserId(name));
                userMap.put(name, id);
                userSelector.getItems().add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userSelector.setOnAction(e -> {
            messageArea.clear();
            String selectedUser = userSelector.getValue();
            if (selectedUser == null) return;

            int otherUserId = userMap.get(selectedUser);
            try {
                ResultSet rs = dbHelper.getMessagesBetweenUsers(currentUserId, otherUserId);
                while (rs.next()) {
                    String msg = rs.getString("messageText");
                    int senderId = rs.getInt("senderId");
                    String sender = senderId == currentUserId ? "You" : selectedUser;
                    messageArea.appendText("[" + sender + "]: " + msg + "\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        sendButton.setOnAction(e -> {
            String selectedUser = userSelector.getValue();
            if (selectedUser == null || inputField.getText().isEmpty()) return;

            int receiverId = userMap.get(selectedUser);
            String msg = inputField.getText().trim();
            try {
                dbHelper.sendMessage(currentUserId, receiverId, msg);
                inputField.clear();

                // Reload messages
                userSelector.getOnAction().handle(null);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        Scene chatScene = new Scene(root, 800, 500);
        primaryStage.setScene(chatScene);
        primaryStage.setTitle("Chat Page");
    }
}
