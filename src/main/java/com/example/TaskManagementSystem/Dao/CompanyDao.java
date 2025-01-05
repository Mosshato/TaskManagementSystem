package com.example.TaskManagementSystem.Dao;

import com.example.TaskManagementSystem.DBConnection;
import com.example.TaskManagementSystem.Models.Company;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyDao {

    // Method to add a company and return the generated COMPANY_ID
    public long addCompanyAndReturnId(Company company) {
        // Updated SQL to include COMPANY_ID with a separate sequence
        String insertCompanySQL = "INSERT INTO companies (ID, COMPANY_NAME, COMPANY_CODE, COMPANY_ID) VALUES (company_id_seq.NEXTVAL, ?, ?, company_id_seq.NEXTVAL)";
        String getCompanyIdSQL = "SELECT company_id_seq.CURRVAL FROM dual";

        try (Connection connection = DBConnection.getConnection()) {
            // Disable auto-commit to ensure both statements are executed in the same session
            connection.setAutoCommit(false);

            // Insert the company
            try (PreparedStatement insertStmt = connection.prepareStatement(insertCompanySQL)) {
                insertStmt.setString(1, company.getCompanyName());
                insertStmt.setString(2, company.getCompanyCode());
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SQLException("Failed to insert company.");
                }
            }

            // Retrieve the generated COMPANY_ID
            try (PreparedStatement getIdStmt = connection.prepareStatement(getCompanyIdSQL);
                 ResultSet rs = getIdStmt.executeQuery()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    // Commit the transaction
                    connection.commit();
                    System.console().printf("Value of id is %d", generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Failed to retrieve company ID.");
                }
            } catch (SQLException e) {
                // Rollback in case of error
                connection.rollback();
                throw e;
            } finally {
                // Restore auto-commit mode
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 in case of failure
    }

    public Long getCompanyIdByCode(String companyCode) {
        String query = "SELECT ID FROM companies WHERE COMPANY_CODE = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, companyCode);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Returnează `null` dacă codul companiei nu există
    }


}
