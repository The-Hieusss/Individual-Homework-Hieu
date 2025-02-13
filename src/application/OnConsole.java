package application;
import java.util.Scanner;

public class OnConsole {
    public static void main(String[] args) {
        QuestionManager questions = new QuestionManager();
        AnswerManager answers = new AnswerManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Question-Answer System =====");
            System.out.println("1. Ask a question");
            System.out.println("2. View all questions");
            System.out.println("3. Answer a question");
            System.out.println("4. View answers for a question");
            System.out.println("5. Mark an answer as accepted");
            System.out.println("6. Delete a question");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: // Create a question
                    System.out.print("Enter your question: ");
                    String questionText = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String questionAuthor = scanner.nextLine();
                    Question newQuestion = new Question(questionText, questionAuthor); // initialize the question
                    questions.addQuestion(newQuestion); // adding the questions to the manager
                    System.out.println("Question added successfully!");
                    break;

                case 2: // View questions
                    questions.displayAllQuestions();
                    break;

                case 3: // Answer a question
                    System.out.print("Enter the question ID: ");
                    int qId = scanner.nextInt();
                    scanner.nextLine();
                    Question q = questions.getQuestionById(qId);
                    if (q == null) {
                        System.out.println("Question not found.");
                        break;
                    }
                    System.out.print("Enter your answer: ");
                    String answerText = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String answerAuthor = scanner.nextLine();
                    Answer newAnswer = new Answer(qId, answerText, answerAuthor); // initialize the answer with the question id
                    answers.addAnswer(newAnswer); // add to the answer manager
                    System.out.println("Answer added successfully!");
                    break;

                case 4: // View answers for a question
                    System.out.print("Enter question ID: ");
                    int searchQId = scanner.nextInt();
                    answers.displayAnswersForQuestion(searchQId);
                    break;

                case 5: // Mark an answer as accepted
                    System.out.print("Enter answer ID: ");
                    int answerId = scanner.nextInt();
                    for (Answer a : answers.getAnswersForQuestion(answerId)) {
                        if (a.getId() == answerId) {
                            a.markAsAccepted();
                            System.out.println("Answer marked as accepted!");
                        }
                    }
                    break;
                    
                case 6: // Delete answer 
                    System.out.print("Enter Question ID: ");
                    int quesId = scanner.nextInt();
                    Question c = questions.getQuestionById(quesId);
                        if (c.getId() == quesId) {
                            questions.removeQuestion(quesId);
                            System.out.println("Question Deleted");
                        }
                    break;
                    

                case 7: // Exit
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
