# Q&A CRUD Application

## Overview
This is a simple **Q&A CRUD Application** built with **JavaFX** for the graphical user interface (GUI) and **H2 Database** for data persistence. The application allows users to:

- **Create Questions**
- **View All Questions**
- **Answer Questions**
- **View Answers for a Question**
- **Mark an Answer as Accepted**

## Key Features
- **JavaFX GUI** for an intuitive user interface.
- **H2 Database** integration for data persistence.
- **CRUD Operations** on Questions and Answers.
- **Input validation** to prevent empty submissions.

## Technologies Used
- **Java (JDK 17+)**
- **JavaFX**
- **H2 Database (Embedded)**

## Project Structure
```
src/
│├── application/
│   ├── Main.java
│   ├── QuestionController.java
│   ├── AnswerController.java
│├── databasePart1/
│   └── DatabaseHelper.java
└── models/
    ├── Question.java
    └── Answer.java
```

## Database Schema
**Questions Table:**
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `text` (TEXT)
- `author` (VARCHAR)
- `resolved` (BOOLEAN)

**Answers Table:**
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `questionId` (INT, FOREIGN KEY)
- `text` (TEXT)
- `author` (VARCHAR)
- `isAccepted` (BOOLEAN)

## Installation & Setup
1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   ```
2. **Open the project in your preferred IDE (IntelliJ/VS Code/Eclipse).**
3. **Add JavaFX and H2 dependencies:**
   - Download **JavaFX SDK** and set up `--module-path`.
   - Add **H2 Database** to your classpath.

## Running the Application
### **VM Options Configuration:**
```bash
--module-path <path_to_javafx> --add-modules javafx.controls,javafx.fxml
```

### **Run the Application:**
- Run `Main.java` from the `application` package.

## How to Use
### **1. Ask a Question:**
- Enter your question text and author name.
- Click **"Ask Question"**.

### **2. View All Questions:**
- Questions will be listed in a ListView.
- Click a question to **view/add answers**.

### **3. Answer a Question:**
- Enter your answer and author name.
- Click **"Submit Answer"**.

### **4. Mark an Answer as Accepted:**
- Select an answer from the list.
- Click **"Mark as Accepted"**.

## Screenshots
_Add screenshots of the UI if available._

## Future Improvements
- **Delete Questions/Answers.**
- **User authentication system.**
- **Styling with CSS for a better look.**

## Troubleshooting
- **H2 Driver Not Found:** Ensure `h2.jar` is in the classpath.
- **JavaFX Module Error:** Set `--module-path` correctly.

## License
This project is licensed under the MIT License.

## Contact
For questions, contact: [Your Email or GitHub Profile]

---
**Happy Coding!**

