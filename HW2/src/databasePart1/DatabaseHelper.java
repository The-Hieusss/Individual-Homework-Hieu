package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import application.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(20))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    
	    
	    String createQuestionsTable = "CREATE TABLE IF NOT EXISTS Questions ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "text TEXT, "
				+ "author VARCHAR(255), "
				+ "resolved BOOLEAN DEFAULT FALSE)";
		statement.execute(createQuestionsTable);
		
		
		String createAnswersTable = "CREATE TABLE IF NOT EXISTS Answers ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "questionId INT, "
				+ "text TEXT, "
				+ "author VARCHAR(255), "
				+ "isAccepted BOOLEAN DEFAULT FALSE, "
				+ "FOREIGN KEY (questionId) REFERENCES Questions(id) ON DELETE CASCADE)";
		statement.execute(createAnswersTable);
		
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode() {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code) VALUES (?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	// Create a question
		public void createQuestion(String text, String author) throws SQLException {
			String query = "INSERT INTO Questions (text, author) VALUES (?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, text);
				pstmt.setString(2, author);
				pstmt.executeUpdate();
				System.out.println("Question added successfully.");
			}
		}

		// Get all questions
		public List<String> getAllQuestions() throws SQLException {
			List<String> questions = new ArrayList<>();
			String query = "SELECT * FROM Questions";
			try (ResultSet rs = statement.executeQuery(query)) {
				while (rs.next()) {
					questions.add("[" + rs.getInt("id") + "] " + rs.getString("text") + " (by " + rs.getString("author") + ")");
				}
			}
			return questions;
		}

		// Get a question by ID
		public String getQuestionById(int id) throws SQLException {
			String query = "SELECT * FROM Questions WHERE id = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, id);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return "[" + rs.getInt("id") + "] " + rs.getString("text") + " (by " + rs.getString("author") + ")";
					}
				}
			}
			return null;
		}

		// Update a question
		public void updateQuestion(int id, String newText) throws SQLException {
			String query = "UPDATE Questions SET text = ? WHERE id = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, newText);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
				System.out.println("Question updated successfully.");
			}
		}

		// Delete a question
		public void deleteQuestion(int id) throws SQLException {
			String query = "DELETE FROM Questions WHERE id = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				System.out.println("Question deleted.");
			}
		}
	
		
		
		// Add an answer to a question
		public void addAnswer(int questionId, String text, String author) throws SQLException {
			String query = "INSERT INTO Answers (questionId, text, author) VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, questionId);
				pstmt.setString(2, text);
				pstmt.setString(3, author);
				pstmt.executeUpdate();
				System.out.println("Answer added successfully.");
			}
		}

		// Get all answers for a question
		public List<String> getAnswersForQuestion(int questionId) throws SQLException {
			List<String> answers = new ArrayList<>();
			String query = "SELECT * FROM Answers WHERE questionId = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, questionId);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						answers.add("[" + rs.getInt("id") + "] " + rs.getString("text") + " (by " + rs.getString("author") + ") " +
								(rs.getBoolean("isAccepted") ? "[Accepted]" : "[Pending]"));
					}
				}
			}
			return answers;
		}

		// Mark an answer as accepted
		public void markAnswerAsAccepted(int answerId) throws SQLException {
			String query = "UPDATE Answers SET isAccepted = TRUE WHERE id = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, answerId);
				pstmt.executeUpdate();
				System.out.println("Answer marked as accepted.");
			}
		}

		// Delete an answer
		public void deleteAnswer(int answerId) throws SQLException {
			String query = "DELETE FROM Answers WHERE id = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, answerId);
				pstmt.executeUpdate();
				System.out.println("Answer deleted.");
			}
		}
		
		

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

}
