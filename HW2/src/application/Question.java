package application;
import java.util.*;

public class Question {
    private static int idCounter = 1;  // Auto-increment ID
    private int id;
    private String text;
    private String author;
    private boolean resolved;

    // Constructor
    public Question(String text, String author) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        this.id = idCounter++;
        this.text = text;
        this.author = author;
        this.resolved = false;
    }

    // Getters
    public int getId() { return id; }
    public String getText() { return text; }
    public String getAuthor() { return author; }
    public boolean isResolved() { return resolved; }

    // Setters
    public void updateText(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated text cannot be empty.");
        }
        this.text = newText;
    }

    public void markAsResolved() { this.resolved = true; }

    // Display method
    public void displayQuestion() {
        System.out.println("[" + id + "] " + text + " (by " + author + ") - " +
                           (resolved ? "Resolved" : "Unresolved"));
    }
}
