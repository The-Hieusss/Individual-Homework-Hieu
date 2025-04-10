package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionsAnswersPage {

    private final DatabaseHelper dbHelper;

    public QuestionsAnswersPage(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // -------------------------
        // QUESTIONS TAB
        // -------------------------
        Tab questionsTab = new Tab("Questions");
        questionsTab.setClosable(false);
        VBox questionsPane = new VBox(10);
        questionsPane.setPadding(new Insets(10));

        TableView<Question> questionsTable = new TableView<>();
        TableColumn<Question, Integer> colQId = new TableColumn<>("ID");
        colQId.setCellValueFactory(new PropertyValueFactory<>("questionId"));

        TableColumn<Question, String> colAsker = new TableColumn<>("Asker");
        colAsker.setCellValueFactory(new PropertyValueFactory<>("asker"));

        TableColumn<Question, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Question, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        questionsTable.getColumns().addAll(colQId, colAsker, colTitle, colDescription);
        questionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Input fields for adding/updating questions
        TextField askerField = new TextField(); 
        askerField.setPromptText("Your Name");
        TextField titleField = new TextField();
        titleField.setPromptText("Question Title");
        TextField descField = new TextField();
        descField.setPromptText("Question Description");
        HBox questionInputBox = new HBox(10, askerField, titleField, descField);

        // CRUD Buttons for questions
        Button addQuestionButton = new Button("Add");
        Button updateQuestionButton = new Button("Update");
        Button deleteQuestionButton = new Button("Delete");
        Button refreshQuestionsButton = new Button("Refresh");
        HBox questionsButtons = new HBox(10, addQuestionButton, updateQuestionButton, deleteQuestionButton, refreshQuestionsButton);

        questionsPane.getChildren().addAll(questionsTable, questionInputBox, questionsButtons);
        questionsTab.setContent(questionsPane);

        // -------------------------
        // ANSWERS TAB
        // -------------------------
        Tab answersTab = new Tab("Answers");
        answersTab.setClosable(false);
        VBox answersPane = new VBox(10);
        answersPane.setPadding(new Insets(10));

        TableView<Answer> answersTable = new TableView<>();
        TableColumn<Answer, Integer> colAnsId = new TableColumn<>("Answer ID");
        colAnsId.setCellValueFactory(new PropertyValueFactory<>("ansId"));

        TableColumn<Answer, Integer> colQId1 = new TableColumn<>("Question ID");
        colQId1.setCellValueFactory(new PropertyValueFactory<>("qId"));

        TableColumn<Answer, String> colAnswerFrom = new TableColumn<>("Answer From");
        colAnswerFrom.setCellValueFactory(new PropertyValueFactory<>("answerFrom"));

        TableColumn<Answer, String> colAnswerText = new TableColumn<>("Answer Text");
        colAnswerText.setCellValueFactory(new PropertyValueFactory<>("answerText"));

        answersTable.getColumns().addAll(colAnsId, colQId1, colAnswerFrom, colAnswerText);
        answersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Input fields for adding/updating answers
        TextField questionIdField = new TextField();
        questionIdField.setPromptText("Question ID");
        TextField answerFromField = new TextField();
        answerFromField.setPromptText("Your Name");
        TextField answerTextField = new TextField();
        answerTextField.setPromptText("Answer Text");
        HBox answerInputBox = new HBox(10, questionIdField, answerFromField, answerTextField);

        // CRUD Buttons for answers
        Button addAnswerButton = new Button("Add");
        Button updateAnswerButton = new Button("Update");
        Button deleteAnswerButton = new Button("Delete");
        Button refreshAnswersButton = new Button("Refresh");
        HBox answersButtons = new HBox(10, addAnswerButton, updateAnswerButton, deleteAnswerButton, refreshAnswersButton);

        answersPane.getChildren().addAll(answersTable, answerInputBox, answersButtons);
        answersTab.setContent(answersPane);

        // -------------------------
        // REVIEWS TAB (Reviewer CRUD)
        // -------------------------
        Tab reviewsTab = new Tab("Reviews");
        reviewsTab.setClosable(false);
        VBox reviewsPane = new VBox(10);
        reviewsPane.setPadding(new Insets(10));

        TableView<Review> reviewsTable = new TableView<>();
        TableColumn<Review, Integer> colRId = new TableColumn<>("Review ID");
        colRId.setCellValueFactory(new PropertyValueFactory<>("rId"));

        TableColumn<Review, Integer> colAId = new TableColumn<>("Answer ID");
        colAId.setCellValueFactory(new PropertyValueFactory<>("aId"));

        TableColumn<Review, String> colReviewFrom = new TableColumn<>("Review From");
        colReviewFrom.setCellValueFactory(new PropertyValueFactory<>("reviewFrom"));

        TableColumn<Review, String> colReviewText = new TableColumn<>("Review Text");
        colReviewText.setCellValueFactory(new PropertyValueFactory<>("reviewText"));

        reviewsTable.getColumns().addAll(colRId, colAId, colReviewFrom, colReviewText);
        reviewsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Input fields for reviews
        TextField reviewAnswerIdField = new TextField();
        reviewAnswerIdField.setPromptText("Answer ID");
        TextField reviewFromField = new TextField();
        reviewFromField.setPromptText("Your Name");
        TextField reviewTextField = new TextField();
        reviewTextField.setPromptText("Review Text");
        HBox reviewInputBox = new HBox(10, reviewAnswerIdField, reviewFromField, reviewTextField);

        // CRUD Buttons for reviews
        Button addReviewButton = new Button("Add");
        Button updateReviewButton = new Button("Update");
        Button deleteReviewButton = new Button("Delete");
        Button refreshReviewsButton = new Button("Refresh");
        HBox reviewsButtons = new HBox(10, addReviewButton, updateReviewButton, deleteReviewButton, refreshReviewsButton);

        reviewsPane.getChildren().addAll(reviewsTable, reviewInputBox, reviewsButtons);
        reviewsTab.setContent(reviewsPane);

        // Add all tabs to the TabPane
        tabPane.getTabs().addAll(questionsTab, answersTab, reviewsTab);

        // Back button to navigate out of this UI page
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new SetupLoginSelectionPage(dbHelper).show(primaryStage));

        VBox mainLayout = new VBox(10, tabPane, backButton);
        mainLayout.setPadding(new Insets(10));
        Scene scene = new Scene(mainLayout, 850, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Questions, Answers & Reviews Management");
        primaryStage.show();

        // Load initial data
        loadQuestions(questionsTable);
        loadAnswers(answersTable);
        loadReviews(reviewsTable);

        // ------------- Event Handlers for Questions -------------
        addQuestionButton.setOnAction(e -> {
            try {
                // Use the text value directly for the asker's name.
                String asker = askerField.getText().trim();
                String qTitle = titleField.getText().trim();
                String qDesc = descField.getText().trim();
                dbHelper.addQuestion(asker, qTitle, qDesc);
                loadQuestions(questionsTable);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        updateQuestionButton.setOnAction(e -> {
            try {
                Question selected = questionsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    String newTitle = titleField.getText().trim();
                    String newDesc = descField.getText().trim();
                    dbHelper.updateQuestion(selected.getQuestionId(), newTitle, newDesc);
                    loadQuestions(questionsTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteQuestionButton.setOnAction(e -> {
            try {
                Question selected = questionsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    dbHelper.deleteQuestion(selected.getQuestionId());
                    loadQuestions(questionsTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        refreshQuestionsButton.setOnAction(e -> loadQuestions(questionsTable));

        // ------------- Event Handlers for Answers -------------
        addAnswerButton.setOnAction(e -> {
            try {
                int qId = Integer.parseInt(questionIdField.getText().trim());
                String answerFrom = answerFromField.getText().trim();
                String answerText = answerTextField.getText().trim();
                dbHelper.createAnswer(qId, answerFrom, answerText);
                loadAnswers(answersTable);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        updateAnswerButton.setOnAction(e -> {
            try {
                Answer selected = answersTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    String newAnswerFrom = answerFromField.getText().trim();
                    String newAnswerText = answerTextField.getText().trim();
                    dbHelper.updateAnswer(selected.getAnsId(), newAnswerFrom, newAnswerText);
                    loadAnswers(answersTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteAnswerButton.setOnAction(e -> {
            try {
                Answer selected = answersTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    dbHelper.deleteAnswer(selected.getAnsId());
                    loadAnswers(answersTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        refreshAnswersButton.setOnAction(e -> loadAnswers(answersTable));

        // ------------- Event Handlers for Reviews -------------
        addReviewButton.setOnAction(e -> {
            try {
                int aId = Integer.parseInt(reviewAnswerIdField.getText().trim());
                String reviewFrom = reviewFromField.getText().trim();
                String reviewText = reviewTextField.getText().trim();
                dbHelper.createReview(aId, reviewFrom, reviewText);
                loadReviews(reviewsTable);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        updateReviewButton.setOnAction(e -> {
            try {
                Review selected = reviewsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    String newReviewFrom = reviewFromField.getText().trim();
                    String newReviewText = reviewTextField.getText().trim();
                    dbHelper.updateReview(selected.getRId(), newReviewFrom, newReviewText);
                    loadReviews(reviewsTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteReviewButton.setOnAction(e -> {
            try {
                Review selected = reviewsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    dbHelper.deleteReview(selected.getRId());
                    loadReviews(reviewsTable);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        refreshReviewsButton.setOnAction(e -> loadReviews(reviewsTable));
    }

    // Load all questions from the database
    private void loadQuestions(TableView<Question> table) {
        ObservableList<Question> questionsList = FXCollections.observableArrayList();
        try {
            ResultSet rs = dbHelper.getAllQuestions();
            while (rs.next()) {
                int id = rs.getInt("id");
                String asker = rs.getString("asker");
                String title = rs.getString("qTitle");
                String desc = rs.getString("qDesc");
                questionsList.add(new Question(id, asker, title, desc));
            }
            System.out.println("Loaded " + questionsList.size() + " questions.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(questionsList);
        table.refresh();
    }

    // Load all answers from the database
    private void loadAnswers(TableView<Answer> table) {
        ObservableList<Answer> answersList = FXCollections.observableArrayList();
        try {
            ResultSet rs = dbHelper.getAllAnswers();
            while (rs.next()) {
                int ansId = rs.getInt("ansId");
                int qId = rs.getInt("qId");
                String answerFrom = rs.getString("answerFrom");
                String answerText = rs.getString("answerText");
                answersList.add(new Answer(ansId, qId, answerFrom, answerText));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(answersList);
    }

    // Load all reviews from the database
    private void loadReviews(TableView<Review> table) {
        ObservableList<Review> reviewsList = FXCollections.observableArrayList();
        try {
            ResultSet rs = dbHelper.getAllReviews();
            while (rs.next()) {
                int rId = rs.getInt("rId");
                int aId = rs.getInt("aId");
                String reviewFrom = rs.getString("reviewFrom");
                String reviewText = rs.getString("reviewText");
                reviewsList.add(new Review(rId, aId, reviewFrom, reviewText));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(reviewsList);
    }
}
