package application;

/**
 * The Answer class represents an answer entity.
 * Each answer includes an auto-generated answer ID, the related question’s ID,
 * the name of the person who answered (answerFrom), and the answer text.
 */
public class Answer {
    private int ansId;
    private int qId;
    private String answerFrom;
    private String answerText;

    public Answer(int ansId, int qId, String answerFrom, String answerText) {
        if (answerFrom == null || answerFrom.trim().isEmpty()) {
            throw new IllegalArgumentException("AnswerFrom cannot be empty.");
        }
        if (answerText == null || answerText.trim().isEmpty()) {
            throw new IllegalArgumentException("AnswerText cannot be empty.");
        }
        this.ansId = ansId;
        this.qId = qId;
        this.answerFrom = answerFrom;
        this.answerText = answerText;
    }

    public int getAnsId() {
        return ansId;
    }

    public int getQId() {
        return qId;
    }

    public String getAnswerFrom() {
        return answerFrom;
    }

    public String getAnswerText() {
        return answerText;
    }
}
