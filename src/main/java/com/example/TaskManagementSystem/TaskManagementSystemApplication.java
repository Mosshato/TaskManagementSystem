package com.example.TaskManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class TaskManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementSystemApplication.class, args);
		//testDatabaseConnection();

	}

	private static void testDatabaseConnection() {
		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null && !conn.isClosed()) {
				System.out.println("Connection test successful!");
			} else {
				System.out.println("Failed to obtain a valid connection.");
			}
		} catch (SQLException e) {
			System.err.println("Error occurred while testing the connection: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
