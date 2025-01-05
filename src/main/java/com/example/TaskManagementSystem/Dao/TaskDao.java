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

public class TaskDao {

    private static final Logger logger = LoggerFactory.getLogger(TaskDao.class);

    public List<Task> getTasksByManagerId(Long managerId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM C##TEST1.TASKS WHERE ASSIGNED_USER_ID = ?";

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

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching tasks for manager ID: " + managerId, e);
        }

        // Log task-urile returnate
        logger.info("Task-uri returnate pentru manager ID {}: {}", managerId, tasks);

        return tasks;
    }

}
