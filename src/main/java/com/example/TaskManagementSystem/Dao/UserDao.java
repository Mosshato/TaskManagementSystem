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

            preparedStatement.setString(1, user.getFullName());
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
        String query = "SELECT ID, FULL_NAME, EMAIL, PASSWORD_HASH, PASSWORD_SALT, USER_TYPE, COMPANY_ID, TASKS_NUM FROM C##TEST1.USERS WHERE EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("ID"));
                    user.setFullname(resultSet.getString("FULL_NAME"));
                    user.setEmail(resultSet.getString("EMAIL"));
                    user.setPassword(resultSet.getString("PASSWORD_HASH")); // Hash-ul parolei
                    user.setPasswordSalt(resultSet.getString("PASSWORD_SALT")); // Salt-ul parolei
                    user.setUserType(resultSet.getString("USER_TYPE"));
                    user.setCompanyId(resultSet.getLong("COMPANY_ID"));
                    user.setTasksNum(resultSet.getInt("TASKS_NUM"));
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email: " + email, e);
        }
        return null;
    }


    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password.
     * @return The hashed password as a hexadecimal string.
     */
    public String hashPassword(String password) {
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

    public List<User> findManagersByBoss(String bossEmail) {
        List<User> managers = new ArrayList<>();
        if (bossEmail == null || bossEmail.isEmpty()) {
            logger.error("Provided boss email is null or empty.");
            return managers;
        }

        String getBossCompanyQuery = "SELECT COMPANY_ID FROM USERS WHERE LOWER(EMAIL) = ? AND USER_TYPE = 'boss'";
        String getManagersQuery = """
        SELECT u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE, u.COMPANY_ID,
               COUNT(t.ID) AS TASKS_NUM
        FROM USERS u
        LEFT JOIN TASKS t ON u.ID = t.ASSIGNED_USER_ID
        WHERE u.USER_TYPE = 'manager' AND u.COMPANY_ID = ?
        GROUP BY u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE, u.COMPANY_ID
    """;

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(true); // Ensure changes are visible
            Long companyId = null;

            try (PreparedStatement bossStmt = connection.prepareStatement(getBossCompanyQuery)) {
                bossStmt.setString(1, bossEmail.toLowerCase());
                try (ResultSet rs = bossStmt.executeQuery()) {
                    if (rs.next()) {
                        companyId = rs.getLong("COMPANY_ID");
                    } else {
                        logger.warn("Boss with email {} not found.", bossEmail);
                        return managers; // Early return if no boss found
                    }
                }
            }

        try (PreparedStatement managersStmt = connection.prepareStatement(getManagersQuery)) {
            managersStmt.setLong(1, companyId);
            try (ResultSet rsManagers = managersStmt.executeQuery()) {
                while (rsManagers.next()) {
                    User manager = new User();
                    manager.setId(rsManagers.getLong("ID"));
                    manager.setFullname(rsManagers.getString("FULL_NAME"));
                    manager.setEmail(rsManagers.getString("EMAIL"));
                    manager.setUserType(rsManagers.getString("USER_TYPE"));
                    manager.setCompanyId(rsManagers.getLong("COMPANY_ID"));
                    manager.setTasksNum(rsManagers.getInt("TASKS_NUM")); // Default safe handling
                    managers.add(manager);
                }
            }
        }

        } catch (SQLException e) {
            logger.error("Error fetching managers for boss with email {}: {}", bossEmail, e.getMessage(), e);
        }

        logger.info("Managers reporting to boss {}: {}", bossEmail, managers);
        return managers;
    }

    public String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Combinăm parola cu salt-ul
            String saltedPassword = salt + password;
            byte[] hash = md.digest(saltedPassword.getBytes());
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


    public List<User> findEmployeesByBoss(String bossEmail) {
        List<User> employees = new ArrayList<>();
        if (bossEmail == null || bossEmail.isEmpty()) {
            logger.error("Provided boss email is null or empty.");
            return employees;
        }

        String getBossCompanyQuery = "SELECT COMPANY_ID FROM USERS WHERE LOWER(EMAIL) = ? AND USER_TYPE = 'boss'";
        String getEmployeesQuery = """
        SELECT u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE, u.COMPANY_ID,
               COUNT(t.ID) AS TASKS_NUM
        FROM USERS u
        LEFT JOIN TASKS t ON u.ID = t.ASSIGNED_USER_ID
        WHERE u.USER_TYPE = 'employee' AND u.COMPANY_ID = ?
        GROUP BY u.ID, u.FULL_NAME, u.EMAIL, u.USER_TYPE, u.COMPANY_ID
    """;

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(true); // Ensure changes are visible
            Long companyId = null;

            // Obține ID-ul companiei șefului
            try (PreparedStatement bossStmt = connection.prepareStatement(getBossCompanyQuery)) {
                bossStmt.setString(1, bossEmail.toLowerCase());
                try (ResultSet rs = bossStmt.executeQuery()) {
                    if (rs.next()) {
                        companyId = rs.getLong("COMPANY_ID");
                    } else {
                        logger.warn("Boss with email {} not found.", bossEmail);
                        return employees; // Early return dacă șeful nu este găsit
                    }
                }
            }

            // Obține lista de angajați
            try (PreparedStatement employeesStmt = connection.prepareStatement(getEmployeesQuery)) {
                employeesStmt.setLong(1, companyId);
                try (ResultSet rsEmployees = employeesStmt.executeQuery()) {
                    while (rsEmployees.next()) {
                        User employee = new User();
                        employee.setId(rsEmployees.getLong("ID"));
                        employee.setFullname(rsEmployees.getString("FULL_NAME"));
                        employee.setEmail(rsEmployees.getString("EMAIL"));
                        employee.setUserType(rsEmployees.getString("USER_TYPE"));
                        employee.setCompanyId(rsEmployees.getLong("COMPANY_ID"));
                        employee.setTasksNum(rsEmployees.getInt("TASKS_NUM")); // Default safe handling
                        employees.add(employee);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching employees for boss with email {}: {}", bossEmail, e.getMessage(), e);
        }

        logger.info("Employees reporting to boss {}: {}", bossEmail, employees);
        return employees;
    }

    public boolean doesEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM C##TEST1.USERS WHERE EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Return true dacă email-ul există
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if email exists: " + email, e);
        }
        return false; // Returnăm false dacă ceva nu merge bine
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO C##TEST1.USERS (FULL_NAME, EMAIL, PASSWORD_HASH, PASSWORD_SALT, USER_TYPE, COMPANY_ID, TASKS_NUM) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Setează valorile
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPasswordWithSalt(user.getPassword(), user.getPasswordSalt())); // Hash-ul parolei
            preparedStatement.setString(4, user.getPasswordSalt());
            preparedStatement.setString(5, user.getUserType());
            preparedStatement.setLong(6, user.getCompanyId()); // ID-ul companiei șefului logat

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returnăm true dacă utilizatorul a fost inserat
        } catch (SQLException e) {
            logger.error("Error creating user: " + e.getMessage(), e);
            return false;
        }
    }

    public Long getCompanyIdByEmail(String email) {
        String sql = "SELECT COMPANY_ID FROM USERS WHERE EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("COMPANY_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null dacă email-ul nu este găsit
    }

    public String getCompanyCodeByEmail(String email) {
        String sql = "SELECT c.COMPANY_CODE " +
                "FROM USERS u " +
                "JOIN COMPANIES c ON u.COMPANY_ID = c.ID " +
                "WHERE u.EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("COMPANY_CODE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null dacă email-ul nu este găsit
    }

    public String getCompanyNameByEmail(String email) {
        String sql = "SELECT c.COMPANY_NAME " +
                "FROM USERS u " +
                "JOIN COMPANIES c ON u.COMPANY_ID = c.ID " +
                "WHERE u.EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("COMPANY_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null dacă email-ul nu este găsit sau compania nu există
    }



}
