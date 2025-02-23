package com.example.TaskManagementSystem.Models;

public class Company {
    private Long id;
    private String companyName;
    private String companyCode;

    // Constructori
    public Company() {}

    public Company(String companyName, String companyCode) {
        this.companyName = companyName;
        this.companyCode = companyCode;
    }

    // Getters și Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
