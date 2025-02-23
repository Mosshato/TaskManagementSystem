package com.example.TaskManagementSystem.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class User {

    private String companyName;

    private int tasksNum;

    private String passwordSalt; // Adaugă acest câmp


    public int getTasksNum() {
        return tasksNum;
    }

    public void setTasksNum(int tasksNum) {
        this.tasksNum = tasksNum;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    private Long id; // Auto-generated ID

    @NotBlank(message = "Numele complet este obligatoriu.")
    private String fullname;

    @NotBlank(message = "Adresa de email este obligatorie.")
    @Email(message = "Format de email invalid.")
    private String email;

    @NotBlank(message = "Parola este obligatorie.")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere.")
    private String password;

    @NotBlank(message = "Rolul este obligatoriu.")
    private String userType;

    private Long companyId; // Add this field

    // Constructors
    public User() {}

    public User(String fullname, String email, String password, String userType) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    // Improved toString method
    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

}
