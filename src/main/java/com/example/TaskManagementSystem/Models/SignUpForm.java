package com.example.TaskManagementSystem.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpForm {

    @NotBlank(message = "Numele complet este obligatoriu.")
    private String fullname;

    @NotBlank(message = "Adresa de email este obligatorie.")
    @Email(message = "Format de email invalid.")
    private String email;

    @NotBlank(message = "Parola este obligatorie.")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere.")
    private String password;

    @NotBlank(message = "Confirmarea parolei este obligatorie.")
    private String confirmPassword;

    // Doar pentru boss
    private String companyName;

    // Pentru manager și angajat
    private String companyCode;

    @NotBlank(message = "Rolul este obligatoriu.")
    private String role;

    // Getteri și setteri
    public String getFullname() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignUpForm{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                // Excludem parolele pentru securitate
                ", companyName='" + companyName + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
