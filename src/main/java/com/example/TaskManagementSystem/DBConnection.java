package com.example.TaskManagementSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    // Connection details from application.properties (adjusted here for demonstration)
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521";
    private static final String USER = "C##test1";
    private static final String PASSWORD = "test1";

    static {
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            logger.info("Oracle JDBC Driver successfully loaded.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load Oracle JDBC Driver.", e);
        }
    }

    /**
     * Returns a connection to the database.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established successfully.");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to establish database connection.", e);
            throw e; // Re-throw the exception for further handling
        }
    }
}
