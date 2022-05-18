package com.example.loginform;

import java.sql.*;

public class SQLLoginCode {
    //Initialization of the connection to the database
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306?useSSL=false";
    private static final String DATABASE_USERNAME = "Username for database";
    private static final String DATABASE_PASSWORD = "Password for database";
    private static final String SELECT_QUERY = "SELECT * FROM Members WHERE username_ID = ? and password_ID = ?";
    private static Integer MemberID = 2;
    private static String firstName = "Lars";
    private static String lastName = "Lars";
    private static Integer maxLoans = 3;
    private static Integer currentLoans = 1;

    public boolean validate(String usernameID, String passwordID) throws SQLException {
    //Start with establishing the connection to the database
        try (Connection databaseConnection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        //Creates a statement by using the connection objects
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, usernameID);
            preparedStatement.setString(2, passwordID);

            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                memberID(resultSet.getInt("memberID"));
                fName(resultSet.getString("FirstName"));
                lName(resultSet.getString("LastName"));
                maxAmountLoans(resultSet.getInt("MaxLoan"));
                currentAmountLoans(resultSet.getInt("CurrentLoans"));
                return true;
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
        MemberID = id;
        return MemberID;
    }

    public String fName(String fname){
        firstName = fname;
        return firstName;
    }

    public String lName (String lname){
        lastName = lname;
        return lastName;
    }

    public Integer maxAmountLoans(int maxLoan){
        maxLoans = maxLoan;
        return maxLoans;
    }

    public Integer currentAmountLoans(int currentloan){
        currentLoans = currentloan;
        return currentLoans;
    }


    public static int getMember(){
        return 2;
    }

    public static String getFName(){return firstName;}

    public static String getLName(){return lastName;}

    public static int getMaxLoan(){return maxLoans;}

    public static int getCurrLoan(){return currentLoans;}


}
