package application;
import java.util.*;

public class QuestionManager {
    private List<Question> questionList = new ArrayList<>(); //array list to hold the questions

    // add question
    public void addQuestion(Question question) {
        questionList.add(question);
    }
    // remove question
    public void removeQuestion(int id) {
        questionList.removeIf(q -> q.getId() == id);
    }
    
    public Question getQuestionById(int id) {
        return questionList.stream().filter(q -> q.getId() == id).findFirst().orElse(null);
    }

    public List<Question> getUnresolvedQuestions() {
        List<Question> unresolved = new ArrayList<>();
        for (Question q : questionList) {
            if (!q.isResolved()) unresolved.add(q);
        }
        return unresolved;
    }

    public void displayAllQuestions() {
        if (questionList.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }
        for (Question q : questionList) {
            q.displayQuestion();
        }
    }
}
