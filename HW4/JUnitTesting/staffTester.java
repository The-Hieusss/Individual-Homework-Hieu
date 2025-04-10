package JUnitTesting;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import databasePart1.DatabaseHelper;

public class staffTester {

    private DatabaseHelper dbHelper;

    @Before
    public void setUp() throws Exception {
        dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();
        
        // Clear tables before each test.
        dbHelper.clearTables();
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.closeConnection();
    }
    
    @Test
    public void testReviewCRUD() throws SQLException {
        // Step 1: Add a question.
        String asker = "Alice";
        String qTitle = "What is JUnit?";
        String qDesc = "I need to understand how JUnit works.";
        dbHelper.addQuestion(asker, qTitle, qDesc);
        
        // Retrieve the question's ID.
        ResultSet rsQ = dbHelper.getAllQuestions();
        int questionId = -1;
        if (rsQ.next()) {
            questionId = rsQ.getInt("id");
        }
        assertTrue("Question ID should be valid", questionId != -1);
        
        // Step 2: Add an answer for that question.
        String answerFrom = "Bob";
        String answerText = "JUnit is a testing framework for Java.";
        dbHelper.createAnswer(questionId, answerFrom, answerText);
        
        // Retrieve the answer's ID.
        ResultSet rsA = dbHelper.getAllAnswers();
        int answerId = -1;
        if (rsA.next()) {
            answerId = rsA.getInt("ansId");
        }
        assertTrue("Answer ID should be valid", answerId != -1);
        
        // Step 3: Add a review for the answer.
        String reviewFrom = "Carol";
        String reviewText = "Great answer, very helpful!";
        dbHelper.createReview(answerId, reviewFrom, reviewText);
        
        // Verify the review was added.
        ResultSet rsR = dbHelper.getAllReviews();
        int reviewId = -1;
        boolean found = false;
        while (rsR.next()) {
            if (rsR.getString("reviewFrom").equals(reviewFrom)) {
                reviewId = rsR.getInt("rId");
                found = true;
            }
        }
        assertTrue("Review should be added", found);
        assertTrue("Review ID should be valid", reviewId != -1);
        
        // Step 4: Update the review.
        String updatedReviewText = "This answer is outstanding!";
        dbHelper.updateReview(reviewId, reviewFrom, updatedReviewText);
        
        // Verify the update.
        rsR = dbHelper.getAllReviews();
        boolean updated = false;
        while(rsR.next()){
            if(rsR.getInt("rId") == reviewId && rsR.getString("reviewText").equals(updatedReviewText)){
                updated = true;
            }
        }
        assertTrue("Review should be updated", updated);
        
        // Step 5: Delete the review.
        dbHelper.deleteReview(reviewId);
        rsR = dbHelper.getAllReviews();
        boolean exists = false;
        while(rsR.next()){
            if(rsR.getInt("rId") == reviewId)
                exists = true;
        }
        assertFalse("Review should be deleted", exists);
    }
}
