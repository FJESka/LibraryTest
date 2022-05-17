package loginform;

import bookSearch.DatabaseConnection;

import java.sql.*;

public class SQLLoginCode {
    //Initialization of the connection to the database
//    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306?useSSL=false";
//    private static final String DATABASE_USERNAME = "Username for database";
//    private static final String DATABASE_PASSWORD = "Password for database";


    private static final String SELECT_QUERY = "SELECT * FROM Member WHERE username = ? and password = ?";
    private static Integer MemberID;

    public boolean validate(String usernameID, String passwordID) throws SQLException {
    //Start with establishing the connection to the database


//        try (Connection databaseConnection = DriverManager
//                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        //Creates a statement by using the connection objects
//        PreparedStatement preparedStatement = databaseConnection.prepareStatement(SELECT_QUERY))
        try {
            PreparedStatement preparedStatement = DatabaseConnection.getConnection().getDBConnection().prepareStatement(SELECT_QUERY);
            {
                preparedStatement.setString(1, usernameID);
                preparedStatement.setString(2, passwordID);

                System.out.println(preparedStatement);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    memberID(resultSet.getInt("memberID"));
                    return true;
                }
            }
        } catch (SQLException exception){
            //Prints SQL Exception information
            printSQLException(exception);
        }
        return true;
}
//Error handling for SQL connection, it shows what is wrong, codes and states of SQL connection
public static void printSQLException (SQLException exceptions){
        for (Throwable e: exceptions){
            if (e instanceof SQLException){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error code: "+ ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable throwab = exceptions.getCause();
                while (throwab != null){
                    System.out.println("Cause: " + throwab);
                    throwab = throwab.getCause();
                }
            }
        }
    }
    public Integer memberID(int id){
        Integer memberIDs = id;
        MemberID = memberIDs;
        System.out.println(MemberID);
        return memberIDs;
    }
    public static Integer Member(){
        return MemberID;
    }
}
