package com.gamehub;

import java.sql.*;

public class DatabaseConnector {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/gamehub", "root", "root");
    }
    public ResultSet executeLoginQuery(Connection connection, String userEmail, String userPassword) throws SQLException {
        String statement = "SELECT pseudo FROM user WHERE email=? AND password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, userEmail);
        preparedStatement.setString(2, userPassword);
        return preparedStatement.executeQuery();
    }

    public int executeSignupQuery(Connection connection, String userPseudo, String userEmail, String userPassword) throws SQLException {
        String statement = "insert into user (pseudo, email, password) values(?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, userPseudo);
        preparedStatement.setString(2, userEmail);
        preparedStatement.setString(3, userPassword);
        return preparedStatement.executeUpdate();
    }


}
