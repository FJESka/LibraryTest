package bookSearch;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getDBConnection(){

        String databaseName = "sql5492952";
        String databaseUser = "sql5492952";
        String databasePassword = "KYaPVTwKWe";
        String url = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5492952";

        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        }catch (Exception e){
            e.printStackTrace();
        }

        return databaseLink;
    }

    public static DatabaseConnection getConnection(){
        DatabaseConnection connectNow = new DatabaseConnection();
        return connectNow;
    }

}
