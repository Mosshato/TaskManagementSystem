package com.example.TaskManagementSystem.Dao;

import com.example.TaskManagementSystem.DBConnection;
import com.example.TaskManagementSystem.Models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    // SQL statement to insert a new user
    private static final String INSERT_USER_SQL =
            "INSERT INTO users (ID, FULL_NAME, EMAIL, PASSWORD_HASH, USER_TYPE, COMPANY_ID) " +
                    "VALUES (user_id_seq.NEXTVAL, ?, ?, ?, ?, ?)";

    // SQL statement to check if a user exists by email
    private static final String SELECT_USER_BY_EMAIL =
            "SELECT COUNT(*) FROM users WHERE EMAIL = ?";

    // SQL statement to find user by email and password
    private static final String VALIDATE_USER_SQL =
            "SELECT * FROM users WHERE EMAIL = ? AND PASSWORD_HASH = ?";

    /**
     * Adds a new user to the database.
     *
     * @param user The user to be added.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, user.getFullname());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword())); // Hash the password
            preparedStatement.setString(4, user.getUserType());

            // Ensure companyId is handled properly
            if (user.getCompanyId() != null) {
                preparedStatement.setLong(5, user.getCompanyId());
            } else {
                preparedStatement.setNull(5, Types.BIGINT); // Set NULL for the COMPANY_ID column
            }

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("User added successfully: {}", user.getEmail());
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.error("Error adding user to database.", e);
            return false;
        }
    }

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExists(String email) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {

            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("User with email {} exists: {}", email, count > 0);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            logger.error("Error checking if user exists", e);
        }
        return false;
    }

    /**
     * Finds a user by their email and password (hashed).
     *
     * @param email The email to search for.
     * @param password The plain text password to validate.
     * @return The User object if valid, or null if authentication fails.
     */
    public User validateUser(String email, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(VALIDATE_USER_SQL)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hashPassword(password)); // Hash the password before comparing

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("ID"));
                    user.setFullname(rs.getString("FULL_NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setUserType(rs.getString("USER_TYPE"));
                    user.setCompanyId(rs.getLong("COMPANY_ID"));
                    logger.info("User validated successfully: {}", email);
                    return user;
                }
            }

        } catch (SQLException e) {
            logger.error("Error validating user by email and password", e);
        }
        return null;
    }

    /**
     * Finds a user by their email.
     *
     * @param email The email to search for.
     * @return The User object if found, or null if not found.
     */
    public User findUserByEmail(String email) {
        String query = """
        SELECT u.*, c.COMPANY_NAME 
        FROM users u
        LEFT JOIN companies c ON u.COMPANY_ID = c.ID
        WHERE u.EMAIL = ?
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("ID"));
                    user.setFullname(rs.getString("FULL_NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(rs.getString("PASSWORD_HASH"));
                    user.setUserType(rs.getString("USER_TYPE"));
                    user.setCompanyId(rs.getLong("COMPANY_ID"));

                    // Adaugăm numele companiei
                    user.setCompanyName(rs.getString("COMPANY_NAME"));
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email", e);
        }
        return null;
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public List<User> findUsersByType(String userType) {
        List<User> users = new ArrayList<>();
        String query = """
        SELECT u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE,
               COUNT(t.ID) AS TASKS_NUM
        FROM users u
        LEFT JOIN tasks t ON u.ID = t.ASSIGNED_USER_ID
        WHERE u.USER_TYPE = ?
        GROUP BY u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userType);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("ID"));
                    user.setFullname(rs.getString("FULL_NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setUserType(rs.getString("USER_TYPE"));
                    user.setTasksNum(rs.getInt("TASKS_NUM")); // Setează numărul de task-uri
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching users by type", e);
        }

        logger.info("Manageri returnați cu numărul de task-uri: {}", users);
        return users;
    }



}
