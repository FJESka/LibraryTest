package bookSearch;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getDBConnection(){

        String databaseName = "LibraryTest";
        String databaseUser = "root";
        String databasePassword = "Belle2001";
        String url = "jdbc:mysql://localhost/LibraryTest";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        }catch (Exception e){
            e.printStackTrace();
        }

        return databaseLink;
    }


}
