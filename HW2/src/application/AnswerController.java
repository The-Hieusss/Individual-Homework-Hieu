package application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnswerController {
    private Question question;
    private AnswerManager answerRepo;
    private ListView<String> answerListView;
    private TextField answerInput;
    private TextField authorInput;

    public AnswerController(Question question, AnswerManager answerRepo) {
        this.question = question;
        this.answerRepo = answerRepo;
    }

    public void display() {
        Stage window = new Stage();
        window.setTitle("Answers for: " + question.getText());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("Answers for: " + question.getText());

        // Answer Input Fields
        answerInput = new TextField();
        answerInput.setPromptText("Enter your answer...");
        authorInput = new TextField();
        authorInput.setPromptText("Your Name...");

        // Buttons
        Button addAnswerBtn = new Button("Submit Answer");
        addAnswerBtn.setOnAction(e -> addAnswer());

        // Answer ListView
        answerListView = new ListView<>();
        updateAnswerList();

        layout.getChildren().addAll(title, answerInput, authorInput, addAnswerBtn, answerListView);
        
        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.show();
    }

    // Adds an answer
    private void addAnswer() {
        String text = answerInput.getText();
        String author = authorInput.getText();
        
        if (text.isEmpty() || author.isEmpty()) {
            showAlert("Error", "Please enter an answer and your name.");
            return;
        }

        Answer answer = new Answer(question.getId(), text, author);
        answerRepo.addAnswer(answer);
        updateAnswerList();
        answerInput.clear();
        authorInput.clear();
    }

    // Updates the answer list
    private void updateAnswerList() {
        answerListView.getItems().clear();
        for (Answer a : answerRepo.getAnswersForQuestion(question.getId())) {
            answerListView.getItems().add("[" + a.getId() + "] " + a.getText() + " (by " + a.getAuthor() + ")");
        }
    }

    // Alert Dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
