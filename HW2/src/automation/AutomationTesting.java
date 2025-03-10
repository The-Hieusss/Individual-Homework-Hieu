
// This is the automation testing file for the application
// I am testing the main 4 features of CRUD
// First is Create 
// Second is Read 
// Third is Update
// Fourth is Delete

package automation;

import application.Question;
import application.Answer;
import application.QuestionManager;
import application.AnswerManager;

public class AutomationTesting {

    static int numPassed = 0;
    static int numFailed = 0;

    static QuestionManager questionRepo = new QuestionManager();
    static AnswerManager answerRepo = new AnswerManager();

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("\nAutomation Testing for Q&A CRUD Operations (In-Memory)");

        // Question CRUD Tests
        System.out.println("\n--- Question Tests ---");
        performQuestionTestCase(1, "What is Java?", "Alice", true);
        performQuestionTestCase(2, "", "Alice", false); // Invalid question text
        performQuestionTestCase(3, "What is OOP?", "", false); // Invalid author
        performQuestionUpdateTestCase(4, 1, "What is Java Programming?", true);
        performQuestionUpdateTestCase(5, 999, "Invalid Update", false);
        performQuestionDeleteTestCase(6, 1, true);
        performQuestionDeleteTestCase(7, 999, false);

        // Answer CRUD Tests
        System.out.println("\n--- Answer Tests ---");
        Question seedQuestion = new Question("What is a class in Java?", "Bob");
        questionRepo.addQuestion(seedQuestion);
        performAnswerTestCase(8, seedQuestion.getId(), "A class is a blueprint", "Carol", true);
        performAnswerTestCase(9, 999, "Invalid answer to non-existent question", "Dan", false);
        performAnswerMarkAcceptedTestCase(10, 1, true);
        performAnswerMarkAcceptedTestCase(11, 999, false);
        performAnswerDeleteTestCase(12, 1, true);
        performAnswerDeleteTestCase(13, 999, false);

        System.out.println("____________________________________________________________________________");
        System.out.println("\nNumber of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // Test Creating a Question
    private static void performQuestionTestCase(int testCase, String text, String author, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Creating Question -> Text: \"" + text + "\", Author: \"" + author + "\"");

        boolean actualPass;
        try {
            Question q = new Question(text, author);
            questionRepo.addQuestion(q);
            actualPass = true;
        } catch (IllegalArgumentException e) {
            actualPass = false;
        }

        processTestResult("Question Creation", expectedPass, actualPass);
    }

    // Test Updating a Question
    private static void performQuestionUpdateTestCase(int testCase, int id, String newText, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Updating Question -> ID: " + id + ", New Text: \"" + newText + "\"");

        Question question = questionRepo.getQuestionById(id);
        boolean actualPass;
        if (question != null) {
            try {
                question.updateText(newText);
                actualPass = true;
            } catch (IllegalArgumentException e) {
                actualPass = false;
            }
        } else {
            actualPass = false;
        }

        processTestResult("Question Update", expectedPass, actualPass);
    }

    // Test Deleting a Question
    private static void performQuestionDeleteTestCase(int testCase, int id, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Deleting Question -> ID: " + id);

        int initialSize = questionRepo.getUnresolvedQuestions().size();
        questionRepo.removeQuestion(id);
        int finalSize = questionRepo.getUnresolvedQuestions().size();

        boolean actualPass = initialSize != finalSize;
        processTestResult("Question Deletion", expectedPass, actualPass);
    }

    // Test Creating an Answer
    private static void performAnswerTestCase(int testCase, int questionId, String text, String author, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Creating Answer -> QID: " + questionId + ", Text: \"" + text + "\", Author: \"" + author + "\"");

        boolean actualPass;
        Question question = questionRepo.getQuestionById(questionId);
        if (question != null) {
            try {
                Answer answer = new Answer(questionId, text, author);
                answerRepo.addAnswer(answer);
                actualPass = true;
            } catch (IllegalArgumentException e) {
                actualPass = false;
            }
        } else {
            actualPass = false;
        }

        processTestResult("Answer Creation", expectedPass, actualPass);
    }

    // Test Marking an Answer as Accepted
    private static void performAnswerMarkAcceptedTestCase(int testCase, int answerId, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Marking Answer as Accepted -> ID: " + answerId);

        boolean actualPass = false;
        for (Answer a : answerRepo.getAnswersForQuestion(answerId)) {
            if (a.getId() == answerId) {
                a.markAsAccepted();
                actualPass = true;
                break;
            }
        }

        processTestResult("Mark Answer Accepted", expectedPass, actualPass);
    }

    // Test Deleting an Answer
    private static void performAnswerDeleteTestCase(int testCase, int answerId, boolean expectedPass) {
        System.out.println("\nTest Case " + testCase + ": Deleting Answer -> ID: " + answerId);

        int initialSize = answerRepo.getAnswersForQuestion(answerId).size();
        answerRepo.removeAnswer(answerId);
        int finalSize = answerRepo.getAnswersForQuestion(answerId).size();

        boolean actualPass = initialSize != finalSize;
        processTestResult("Answer Deletion", expectedPass, actualPass);
    }

    // Showing Test Results
    private static void processTestResult(String testType, boolean expectedPass, boolean actualPass) {
        if (actualPass == expectedPass) {
            System.out.println("***Success*** " + testType + " Test Passed.");
            numPassed++;
        } else {
            System.out.println("***Failure*** " + testType + " Test Failed.");
            numFailed++;
        }
    }
}
