package library.library;

import java.sql.*;

public class JDBCConnection {
    private static  Connection connection;

    public static Connection jdbcConnection() throws SQLException {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "Hannapanna13");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
