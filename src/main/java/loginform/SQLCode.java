package loginform;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;

public class SQLCode {
    //Initialization of the connection to the database
    private static final String DATABASE_URL = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5492952";
    private static final String DATABASE_USERNAME = "sql5492952";
    private static final String DATABASE_PASSWORD = "KYaPVTwKWe";
    private static final String SELECT_QUERY = "SELECT * FROM Member WHERE username = ? and password = ?";
    private static Integer MemberID = 5;
    private static final String memberType = "public";
    private static final Integer allowedToBorrow = 0;
    private static final Integer maxLoanforMemberType = 5;
    //Lägg till variabel för MemberType

    public boolean validate(String usernameID, String passwordID) throws SQLException {
    //Start with establishing the connection to the database
        try (
                Connection databaseConnection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        //Creates a statement by using the connection objects
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, usernameID);
            preparedStatement.setString(2, passwordID);

            //Below snippet is used for troubleshooting.
            //System.out.println(preparedStatement);

            //Following snippet of code pulls the memberID from the database based on username and password
            // if they match and if there is no match then no memberID will be pulled.
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //When a user is identified based on username and password the session is provided with the ID
                //This ID is then used in during the rest of the users session to assist them and the system in
                //Identifying how much they can loan, what they have on loan and other information.
                memberID(resultSet.getInt("memberID"));

                return true;
            }
        } catch (SQLException exception){
            //Prints SQL Exception information
            printSQLException(exception);
        }
        return false;
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

    //Uses the incoming information from registration form to push an insert to the SQL database.
    public void registerUser(String fName, String lName, String email, String phoneNumber, Long socialSecurity, String password, String userName) throws SQLException, IOException
    {
        try (Connection databaseConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             Statement statement = databaseConnection.createStatement();
             PreparedStatement registerPrepared = databaseConnection.prepareStatement("INSERT INTO Member(socialSecurityNumber, telNo, fName, lName, email, memberType, maxLoanLimit, allowedToBorrow, username, password ) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");)
        {
                 registerPrepared.setLong(1, socialSecurity);
            registerPrepared.setString(2, phoneNumber);
            registerPrepared.setString(3, fName);
            registerPrepared.setString(4, lName);
            registerPrepared.setString(5, email);
            registerPrepared.setString(6, memberType);
            registerPrepared.setInt(7, maxLoanforMemberType);
            registerPrepared.setInt(8, allowedToBorrow);
            registerPrepared.setString(9, userName);
            registerPrepared.setString(10, password);
            registerPrepared.executeUpdate();
        }
    }

//Sets the MemberID with the ID from the database, default value is null.
    public Integer memberID(Integer id)
    {
        MemberID = id;
        return MemberID;
    }

    //A way for other methods to get the memberID
    public static Integer getMember() {return MemberID;}
}
