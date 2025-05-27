import java.sql.*;
import java.util.Scanner;

public class CollegeManagementSystem {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "collegeDB";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + "mysql", USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            
            createDatabase(conn);
            try (Connection dbConn = DriverManager.getConnection(DATABASE_URL + DATABASE_NAME, USER, PASSWORD)) {
                createTable(dbConn);
                
                while (true) {
                    System.out.println("Choose an operation: \n1. Insert\n2. Update\n3. Delete\n4. Display\n5. Exit");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            insertCourse(dbConn, scanner);
                            break;
                        case 2:
                            updateCourse(dbConn, scanner);
                            break;
                        case 3:
                            deleteCourse(dbConn, scanner);
                            break;
                        case 4:
                            displayCourses(dbConn);
                            break;
                        case 5:
                            System.out.println("Exiting program.");
                            return;
                        default:
                            System.out.println("Invalid choice. Try again.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection conn) throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Database '" + DATABASE_NAME + "' created successfully or already exists.");
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (" +
                     "CourseID INT AUTO_INCREMENT PRIMARY KEY, " +
                     "Name VARCHAR(255) NOT NULL, " +
                     "Credits INT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' created successfully.");
        }
    }

    private static void insertCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO Courses (Name, Credits) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter CourseID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Credits value: ");
        int credits = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Record updated successfully." : "CourseID not found.");
        }
    }

    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter CourseID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Record deleted successfully." : "CourseID not found.");
        }
    }

    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Courses Table Records:");
            System.out.println("-----------------------------------------");
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") + " | Name: " + rs.getString("Name") + " | Credits: " + rs.getInt("Credits"));
            }
        }
    }
}
