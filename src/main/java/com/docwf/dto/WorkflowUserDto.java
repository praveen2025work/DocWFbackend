package com.docwf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WorkflowUserDto {
    
    private Long userId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    private String firstName;
    
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private Long escalationTo;
    
    @NotNull(message = "Active status is required")
    private String isActive;
    
    private String createdBy;
    
    private LocalDateTime createdOn;
    
    private String updatedBy;
    
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowUserDto() {}
    
    public WorkflowUserDto(String username, String firstName, String lastName, String email, String isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getEscalationTo() {
        return escalationTo;
    }
    
    public void setEscalationTo(Long escalationTo) {
        this.escalationTo = escalationTo;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Override
    public String toString() {
        return "WorkflowUserDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
