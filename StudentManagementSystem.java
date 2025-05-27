import java.sql.*;

public class StudentManagementSystem {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/studentDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";  
        String password = "admin"; 

        String query = "SELECT * FROM Students";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (!checkIfTableExists(conn, "Students")) {
                System.out.println("The Students table does not exist.");
                return;
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Student Records:");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                int age = rs.getInt("Age");
                String major = rs.getString("Major");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Major: " + major);
            }

            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("\nTable Metadata:");
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                String columnType = rsmd.getColumnTypeName(i);
                System.out.println("Column " + i + ": " + columnName + " (" + columnType + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkIfTableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, tableName, null);
        return rs.next();
    }
}