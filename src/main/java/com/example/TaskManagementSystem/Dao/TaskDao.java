package com.example.TaskManagementSystem.Dao;

import com.example.TaskManagementSystem.DBConnection;
import com.example.TaskManagementSystem.Models.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class TaskDao {

    private static final Logger logger = LoggerFactory.getLogger(TaskDao.class);

    public static List<Task> getTasksByEmployeeEmail(String email) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT t.* " +
                "FROM TASKS t " +
                "JOIN USERS u ON t.ASSIGNED_USER_ID = u.ID " +
                "WHERE u.EMAIL = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getLong("ID"));
                    task.setDescription(rs.getString("DESCRIPTION"));

                    task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    task.setDueDate(rs.getTimestamp("DUE_DATE"));

                    task.setAssignedUserId(rs.getLong("ASSIGNED_USER_ID"));
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getTasksByManagerId(Long managerId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM C##TEST1.TASKS WHERE ASSIGNED_USER_ID = ?";

        Thread thread = new Thread(() -> {
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setLong(1, managerId);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    logger.debug("Executing query: {} with ASSIGNED_USER_ID: {}", query, managerId);
                    if (!rs.isBeforeFirst()) {
                        logger.warn("No tasks found for manager ID {}", managerId);
                    }
                    while (rs.next()) {
                        Task task = new Task();
                        task.setId(rs.getLong("ID"));
                        task.setDescription(rs.getString("DESCRIPTION"));
                        task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                        task.setDueDate(rs.getTimestamp("DUE_DATE"));
                        task.setAssignedUserId(rs.getLong("ASSIGNED_USER_ID"));

                        synchronized (tasks) {
                            tasks.add(task);
                        }
                    }
                }
            } catch (SQLException e) {
                logger.error("Error fetching tasks for manager ID: " + managerId, e);
            }

            // Log task-urile returnate
            logger.info("Task-uri returnate pentru manager ID {}: {}", managerId, tasks);
        });

        thread.start();
        try {
            thread.join(); // Așteaptă finalizarea thread-ului înainte de a returna rezultatele
        } catch (InterruptedException e) {
            logger.error("Thread interrupted while fetching tasks for manager ID: " + managerId, e);
        }

        return tasks;
    }

    public boolean deleteTask(Long taskId) {
        String query = "DELETE FROM C##TEST1.TASKS WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, taskId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returnează true dacă a fost șters
        } catch (SQLException e) {
            logger.error("Error deleting task with ID: " + taskId, e);
            return false;
        }
    }

    public List<Task> getTasksByEmployeeId(Long employeeId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM C##TEST1.TASKS WHERE ASSIGNED_USER_ID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, employeeId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                logger.debug("Executing query: {} with ASSIGNED_USER_ID: {}", query, employeeId);
                if (!rs.isBeforeFirst()) {
                    logger.warn("No tasks found for employee ID {}", employeeId);
                }
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getLong("ID"));
                    task.setDescription(rs.getString("DESCRIPTION"));
                    task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    task.setDueDate(rs.getTimestamp("DUE_DATE"));
                    task.setAssignedUserId(rs.getLong("ASSIGNED_USER_ID"));

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching tasks for employee ID: " + employeeId, e);
        }

        // Log task-urile returnate
        logger.info("Task-uri returnate pentru employee ID {}: {}", employeeId, tasks);

        return tasks;
    }

    public boolean removeTask(Long taskId) {
        String query = "DELETE FROM C##TEST1.TASKS WHERE ID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, taskId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returnează true dacă task-ul a fost șters cu succes
        } catch (SQLException e) {
            logger.error("Error removing task with ID: " + taskId, e);
            return false;
        }
    }

    public boolean addTask(Task task) {
        String query = "INSERT INTO C##TEST1.TASKS (DESCRIPTION, CREATED_AT, DUE_DATE, ASSIGNED_USER_ID) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, task.getDescription());
            preparedStatement.setDate(2, new Date(task.getCreatedAt().getTime())); // Created At
            preparedStatement.setDate(3, new Date(task.getDueDate().getTime()));   // Due Date
            preparedStatement.setLong(4, task.getAssignedUserId());               // Assigned User ID

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error adding task: " + e.getMessage(), e);
            return false;
        }
    }

    public Long getUserIdByEmail(String email) {
        String query = "SELECT ID FROM USERS WHERE EMAIL = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Returnează null dacă utilizatorul nu este găsit
    }

    public List<Task> getTasksByBossEmail(String email) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM C##TEST1.TASKS t JOIN C##TEST1.USERS u ON t.ASSIGNED_USER_ID = u.ID WHERE u.EMAIL = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                logger.debug("Executing query: {} with EMAIL: {}", query, email);
                if (!rs.isBeforeFirst()) {
                    logger.warn("No tasks found for boss email {}", email);
                }
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getLong("ID"));
                    task.setDescription(rs.getString("DESCRIPTION"));
                    task.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    task.setDueDate(rs.getTimestamp("DUE_DATE"));
                    task.setAssignedUserId(rs.getLong("ASSIGNED_USER_ID"));

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching tasks for boss email: " + email, e);
        }

        // Log task-urile returnate
        logger.info("Task-uri returnate pentru boss email {}: {}", email, tasks);

        return tasks;
    }

}
