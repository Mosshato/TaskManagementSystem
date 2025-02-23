package com.example.TaskManagementSystem.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotBlank(message = "Numele complet este obligatoriu.")
    @JsonProperty("fullname") // Mapare la cheia JSON `fullname`
    private String fullname;

    @NotBlank(message = "Adresa de email este obligatorie.")
    @Email(message = "Format de email invalid.")
    private String email;

    @NotNull(message = "ID-ul companiei este obligatoriu.")
    private Long companyId;

    @NotBlank(message = "Rolul este obligatoriu.")
    private String role;

    private String companyCode;

    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode(){
        return companyCode;
    }
    public void setCompanyCode(String companyCode){
        this.companyCode = companyCode;
    }

    @NotBlank
    private String password;

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    // Constructors

    public UserDTO() {
        // Default constructor
    }

    public UserDTO(String fullname, String email, Long companyId, String role) {
        this.fullname = fullname;
        this.email = email;
        this.companyId = companyId;
        this.role = role;
    }

    // Getters and Setters

    public String getFullName() {
        return this.fullname;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // toString Method

    @Override
    public String toString() {
        return "{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", companyId=" + companyId +
                ", role='" + role + '\'' +
                '}';
    }

    // Optional: Equals and HashCode methods
}
