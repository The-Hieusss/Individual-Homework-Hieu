package application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class QuestionController {
    private QuestionManager questionRepo = new QuestionManager(); // Stores questions
    private AnswerManager answerRepo = new AnswerManager(); // Stores answers
    private ListView<String> questionListView; // Displays questions
    private TextField questionInput;
    private TextField authorInput;

    public VBox getView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("Question & Answer System");

        // Input Fields
        questionInput = new TextField();
        questionInput.setPromptText("Enter your question...");
        authorInput = new TextField();
        authorInput.setPromptText("Your Name...");

        // Buttons
        Button addQuestionBtn = new Button("Ask Question");
        addQuestionBtn.setOnAction(e -> addQuestion());

        // ListView to show questions
        questionListView = new ListView<>();
        questionListView.setOnMouseClicked(e -> showAnswers());

        layout.getChildren().addAll(title, questionInput, authorInput, addQuestionBtn, questionListView);
        return layout;
    }

    // Adds a new question
    private void addQuestion() {
        String text = questionInput.getText();
        String author = authorInput.getText();
        
        if (text.isEmpty() || author.isEmpty()) {
            showAlert("Error", "Please enter a question and your name.");
            return;
        }

        Question q = new Question(text, author);
        questionRepo.addQuestion(q);
        updateQuestionList();
        questionInput.clear();
        authorInput.clear();
    }

    // Updates the list of questions
    private void updateQuestionList() {
        questionListView.getItems().clear();
        for (Question q : questionRepo.getUnresolvedQuestions()) {
            questionListView.getItems().add("[" + q.getId() + "] " + q.getText());
        }
    }

    // Displays answers for the selected question
    private void showAnswers() {
        String selected = questionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        int questionId = Integer.parseInt(selected.split("]")[0].replace("[", ""));
        Question q = questionRepo.getQuestionById(questionId);

        new AnswerController(q, answerRepo).display(); // Opens answer window
    }

    // Alert Dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
