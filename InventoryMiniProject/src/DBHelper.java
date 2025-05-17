import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "Topology@79"; // replace with your MySQL password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}