import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class College {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/college_db";
        String username = "root";
        String password = "";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println(" Connected to college_db!\n");

            // 1️⃣ Create Table
            System.out.println(" Creating Student table (if not exists)...");
            Statement st = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS Student (" +
                    "Id INT PRIMARY KEY, " +
                    "Name VARCHAR(50), " +
                    "Age INT)";
            st.executeUpdate(createTable);
            System.out.println(" Student table ready!\n");

            // 2️⃣ Insert 5 students
            System.out.println("🔹 Inserting 5 students...");
            String insertSQL = "INSERT INTO Student (Id, Name, Age) VALUES (?, ?, ?)";
            PreparedStatement pstInsert = connection.prepareStatement(insertSQL);

            String[][] students = {
                    {"1", "Nani", "20"},
                    {"2", "dustin", "21"},
                    {"3", "Naruto", "22"},
                    {"4", "pallavi", "23"},
                    {"5", "Tyler Durden", "24"}
            };

            for (String[] s : students) {
                pstInsert.setInt(1, Integer.parseInt(s[0]));
                pstInsert.setString(2, s[1]);
                pstInsert.setInt(3, Integer.parseInt(s[2]));
                pstInsert.executeUpdate();
                System.out.println("Inserted: " + s[1] + " (Id: " + s[0] + ", Age: " + s[2] + ")");
            }
            System.out.println();

            // 3️ Update a student
            System.out.println(" Updating student Nani age to 22...");
            String updateSQL = "UPDATE Student SET Age = ? WHERE Id = ?";
            PreparedStatement pstUpdate = connection.prepareStatement(updateSQL);
            pstUpdate.setInt(1, 22);
            pstUpdate.setInt(2, 1);
            pstUpdate.executeUpdate();
            System.out.println("✅ Updated student Nani!\n");

            // 4️⃣ Retrieve all students
            System.out.println("🔹 Retrieving all students:");
            String selectSQL = "SELECT * FROM Student";
            PreparedStatement pstSelect = connection.prepareStatement(selectSQL);
            ResultSet rs = pstSelect.executeQuery();
            System.out.println("Id | Name  | Age");
            System.out.println("-----------------");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("Id") + "  | " +
                                rs.getString("Name") + " | " +
                                rs.getInt("Age")
                );
            }
            System.out.println();

            // 5️⃣ Call stored procedure GetStudentById
            System.out.println("🔹 Calling stored procedure GetStudentById for Id=3...");
            CallableStatement cst = connection.prepareCall("{call GetStudentById(?)}");
            cst.setInt(1, 3);
            ResultSet rsProc = cst.executeQuery();
            System.out.println("Id | Name | Age");
            System.out.println("----------------");
            while (rsProc.next()) {
                System.out.println(
                        rsProc.getInt("Id") + "  | " +
                                rsProc.getString("Name") + " | " +
                                rsProc.getInt("Age")
                );
            }
            System.out.println();

            // 6️⃣ Delete all students
            System.out.println("🔹 Deleting all 5 students...");
            String deleteSQL = "DELETE FROM Student WHERE Id = ?";
            PreparedStatement pstDelete = connection.prepareStatement(deleteSQL);
            for (int i = 1; i <= 5; i++) {
                pstDelete.setInt(1, i);
                pstDelete.executeUpdate();
                System.out.println("Deleted student with Id: " + i);
            }
            System.out.println("\n All students deleted!");

            connection.close();
            System.out.println("\n Database connection closed.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
