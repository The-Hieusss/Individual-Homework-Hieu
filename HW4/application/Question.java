package application;

/**
 * The Question class represents a question entity.
 * Each question includes an auto-generated question ID, 
 * the name of the person who asked it (asker), a title, and a description.
 */
public class Question {
    private int questionId;
    private String asker; 
    private String title;
    private String description;

    // Constructor: questionId is assigned by the database.
    public Question(int questionId, String asker, String title, String description) {
        if (asker == null || asker.trim().isEmpty()) {
            throw new IllegalArgumentException("Asker cannot be empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        this.questionId = questionId;
        this.asker = asker;
        this.title = title;
        this.description = description;
    }

    // Getters
    public int getQuestionId() {
        return questionId;
    }

    public String getAsker() {
        return asker;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
