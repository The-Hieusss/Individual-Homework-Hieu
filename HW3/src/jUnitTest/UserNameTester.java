package jUnitTest;

import static org.junit.Assert.*;
import application.UserNameRecognizer;

import org.junit.Test;



/**
 * Validates a username string.
 * <p>
 * The validation rules are:
 * <ul>
 *   <li>Must not be empty</li>
 *   <li>Must start with a letter (A-Z, a-z)</li>
 *   <li>Can include letters, digits, underscores (_), hyphens (-), and periods (.)</li>
 *   <li>Special characters must be followed by a letter or digit</li>
 *   <li>Username must be between 4 and 16 characters long</li>
 *   <li>Cannot end with a special character</li>
 * </ul>
 * </p>
 *
 * @param input the username string to validate
 * @return an empty string if valid; otherwise, a specific error message
 */


/**
 * Unit tests for the {@code checkForValidUserName} method in {@code UserNameRecognizer}.
 * <p>
 * These tests cover various validation scenarios including:
 * valid usernames, empty input, incorrect starting characters,
 * length violations, and improper use of special characters.
 * </p>
 */

public class UserNameTester {

    /**
     * Tests that a valid username with letters, digits, and special characters
     * returns an empty string (indicating successful validation).
     */
    @Test
	    public void testValidUsername() {
	        String input = "User_Name123";
	        String result = UserNameRecognizer.checkForValidUserName(input);
	        assertEquals("", result); // Empty string means it's valid
	    }

    /**
     * Tests that an empty string input returns the appropriate error message.
     */
    @Test
	    public void testEmptyUsername() {
	        String input = "";
	        String result = UserNameRecognizer.checkForValidUserName(input);
	        assertEquals("\n*** ERROR *** The input is empty", result);
	    }

    /**
     * Tests that a username starting with a digit returns an error message
     * indicating that usernames must begin with a letter.
     */
    @Test	    
    public void testStartsWithNonLetter() {
	        String input = "1Username";
	        String result = UserNameRecognizer.checkForValidUserName(input);
	        assertEquals("Error: Username must start with a letter (A-Z, a-z).", result);
	    }

    /**
     * Tests that a username exceeding 16 characters is rejected
     * with a length error message.
     */
    @Test
	    public void testTooLongUsername() {
	        String input = "VeryLongUsername123"; // 18 characters
	        String result = UserNameRecognizer.checkForValidUserName(input);
	        assertEquals("Error: Username must not exceed 16 characters.", result);
	    }

    /**
     * Tests that a username ending in a special character returns an error
     * indicating that usernames cannot end with special characters.
     */
    @Test
	    public void testEndsWithSpecialChar() {
	        String input = "user.name.";
	        String result = UserNameRecognizer.checkForValidUserName(input);
	        assertEquals("Error: Username cannot end with a special character.", result);
	    }
}
