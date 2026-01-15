import java.sql.*;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/securechatdb"; // Change this to your database name
        String user = "root"; // Change to your MySQL username
        String password = "theglory095"; // Change to your MySQL password

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095"))
         {
            System.out.println("Database connection successful!");
        } 
        catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
