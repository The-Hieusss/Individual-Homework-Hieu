package application;
import java.util.*;

public class Answer {
    private static int idCounter = 1;
    private int id;
    private int questionId;
    private String text;
    private String author;
   // private Date timestamp;
    private boolean isAccepted;

    public Answer(int questionId, String text, String author) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer text cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        this.id = idCounter++;
        this.questionId = questionId;
        this.text = text;
        this.author = author;
     //   this.timestamp = new Date();
        this.isAccepted = false;
    }

    // Getters
    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getText() { return text; }
    public String getAuthor() { return author; }
    public boolean isAccepted() { return isAccepted; }
    
    // Setters
    public void markAsAccepted() { this.isAccepted = true; }

    // Display method
    public void displayAnswer() {
        System.out.println("[" + id + "] " + text + " (by " + author + ") - " +
                           (isAccepted ? "Accepted" : "Pending"));
    }
}
