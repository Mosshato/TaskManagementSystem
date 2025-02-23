package com.example.TaskManagementSystem.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "Email-ul este obligatoriu.")
    @Email(message = "Format de email invalid.")
    private String email;

    @NotBlank(message = "Parola este obligatorie.")
    private String password;

    // Getters și Setters
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
}
