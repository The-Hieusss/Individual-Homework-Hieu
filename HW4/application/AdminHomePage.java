package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.HBox;

/**
 * Admin Page - Displays OTP requests for the admin to process.
 */
public class AdminHomePage {
	private final String adminUserName;
    private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper, String adminUserName) {
        this.databaseHelper = databaseHelper;
        this.adminUserName = adminUserName;
    }

    // This shows the admin who needs their password to be reset
    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label adminLabel = new Label("Hello, " + adminUserName + "!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // TableView to display OTP requests
        TableView<OtpRequest> otpTable = new TableView<>();
        otpTable.setPrefWidth(400);
        otpTable.setPrefHeight(200);

        
        TableColumn<OtpRequest, String> otpUserNameColumn = new TableColumn<>("Requests");
        otpUserNameColumn.setCellValueFactory(data -> data.getValue().userNameProperty());
        otpTable.getColumns().add(otpUserNameColumn);
        otpTable.setItems(getOtpRequests());

        // --- New Users Table ---
        TableView<UserRecord> userTable = new TableView<>();
        userTable.setPrefWidth(400);
        userTable.setPrefHeight(200);
        
        //username column
        TableColumn<UserRecord, String> userNameColumn = new TableColumn<>("Username");
        userNameColumn.setCellValueFactory(data -> data.getValue().userNameProperty());
        
        // Role column
        TableColumn<UserRecord, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> data.getValue().roleProperty());
        
        //Change Role Column
        TableColumn<UserRecord, Void> actionCol = new TableColumn<>("Action");

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button changeRoleBtn = new Button("Change Role");

            {
                changeRoleBtn.setOnAction(event -> {
                	UserRecord user = getTableView().getItems().get(getIndex());

                    if (user != null) {
                        Stage roleStage = new Stage();
                        roleStage.setTitle("Change Role for " + user.getUserName());

                        Button userBtn = new Button("user");
                        Button reviewerBtn = new Button("reviewer");
                        Button instructorBtn = new Button("instructor");
                        Button staffBtn = new Button("staff");

                        userBtn.setOnAction(e -> {
                            try {
                                databaseHelper.changeUserRole(user.getUserName(), "user");
                                roleStage.close();
                                showAlert("Success", user.getUserName() + "'s role changed to user");
                                userTable.setItems(getUserRecords()); // Refresh the table
                            } catch (SQLException ex) {
                                showAlert("Error", "Failed to change role: " + ex.getMessage());
                            }
                        });

                        reviewerBtn.setOnAction(e -> {
                            try {
                                databaseHelper.changeUserRole(user.getUserName(), "reviewer");
                                roleStage.close();
                                showAlert("Success", user.getUserName() + "'s role changed to reviewer");
                                userTable.setItems(getUserRecords());
                            } catch (SQLException ex) {
                                showAlert("Error", "Failed to change role: " + ex.getMessage());
                            }
                        });

                        instructorBtn.setOnAction(e -> {
                            try {
                                databaseHelper.changeUserRole(user.getUserName(), "instructor");
                                roleStage.close();
                                showAlert("Success", user.getUserName() + "'s role changed to instructor");
                                userTable.setItems(getUserRecords());
                            } catch (SQLException ex) {
                                showAlert("Error", "Failed to change role: " + ex.getMessage());
                            }
                        });
                        
                        staffBtn.setOnAction(e -> {
                            try {
                                databaseHelper.changeUserRole(user.getUserName(), "staff");
                                roleStage.close();
                                showAlert("Success", user.getUserName() + "'s role changed to staff");
                                userTable.setItems(getUserRecords());
                            } catch (SQLException ex) {
                                showAlert("Error", "Failed to change role: " + ex.getMessage());
                            }
                        });

                        HBox buttonRow = new HBox(10, userBtn, reviewerBtn, instructorBtn, staffBtn);
                        buttonRow.setStyle("-fx-padding: 10; -fx-alignment: center;");

                        Scene scene = new Scene(buttonRow);
                        roleStage.setScene(scene);
                        roleStage.setResizable(false);
                        roleStage.initOwner(changeRoleBtn.getScene().getWindow()); // Attach to parent window
                        roleStage.show();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(changeRoleBtn);
                }
            }
        });

        // Add both columns to the table
        userTable.getColumns().addAll(userNameColumn, roleColumn, actionCol);
        userTable.setItems(getUserRecords());
        
        userTable.setOnMouseClicked(event -> {
            UserRecord selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showAlert("User Selected", "Username: " + selectedUser.getUserName() + "\nRole: " + databaseHelper.getUserRole(selectedUser.getUserName()));
            }
        });

        // Place both tables side by side using an HBox
        HBox tablesContainer = new HBox(20);
        tablesContainer.getChildren().addAll(otpTable, userTable);

        // Admin select the user and make an OTP for them 
        Button processButton = new Button("Generate OTP");
        processButton.setOnAction(e -> {
            OtpRequest selectedRequest = otpTable.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                try {
                    String otp = databaseHelper.setOneTimePassword(selectedRequest.getUserName()); //OTP generated call here
                    if (otp != null) {
                        showAlert("OTP Generated", "Generated OTP for " + selectedRequest.getUserName() + ": " + otp);
                        otpTable.setItems(getOtpRequests()); // Refresh table
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Could not generate OTP: " + ex.getMessage());
                }
            } else {
                showAlert("No Request Selected", "Please select a request.");
            }
        });

        
        Button returnHome = new Button("Return Home");

	    
	    returnHome.setOnAction( a -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
	    
        layout.getChildren().addAll(adminLabel, tablesContainer, processButton, returnHome);
        //otpTable.setItems(getOtpRequests());

        primaryStage.setScene(new Scene(layout, 850, 450));
        primaryStage.setTitle("Admin Page");
        primaryStage.show();
    }

    // Displaying the user requests
    private ObservableList<OtpRequest> getOtpRequests() {
        ObservableList<OtpRequest> requests = FXCollections.observableArrayList();
        try {
            ResultSet rs = databaseHelper.getResetRequests();
            while (rs.next()) {
                String userName = rs.getString("userName");
                requests.add(new OtpRequest(userName));
            }
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
        return requests;
    }
    private ObservableList<UserRecord> getUserRecords() {
        ObservableList<UserRecord> users = FXCollections.observableArrayList();
        try {
            ResultSet rs = databaseHelper.getUsersExcept(adminUserName);
            while (rs.next()) {
                String userName = rs.getString("userName");
                String role = rs.getString("role");
                users.add(new UserRecord(userName,role));
              
            }
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
        return users;
    }

    	// Pop up windows
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
