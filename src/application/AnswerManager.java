package application;
import java.util.*;

public class AnswerManager {
    private List<Answer> answerList = new ArrayList<>(); // array list to hold the answers

    // add answer
    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }
    // delete answer
    public void removeAnswer(int id) {
        answerList.removeIf(a -> a.getId() == id);
    }
    // read answer by ID
    public List<Answer> getAnswersForQuestion(int questionId) {
        List<Answer> answersForQ = new ArrayList<>();
        for (Answer a : answerList) {
            if (a.getQuestionId() == questionId) answersForQ.add(a);
        }
        return answersForQ;
    }
    // Display all the answers
    public void displayAnswersForQuestion(int questionId) {
        List<Answer> answers = getAnswersForQuestion(questionId);
        if (answers.isEmpty()) {
            System.out.println("No answers yet for this question.");
            return;
        }
        for (Answer a : answers) {
            a.displayAnswer();
        }
    }
}
