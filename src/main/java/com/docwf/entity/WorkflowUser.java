package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_USER")
@Audited
public class WorkflowUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_USER")
    @SequenceGenerator(name = "SEQ_WORKFLOW_USER", sequenceName = "SEQ_WORKFLOW_USER", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userId;
    
    @NotBlank
    @Column(name = "USERNAME", length = 100, unique = true, nullable = false)
    private String username;
    
    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;
    
    @Column(name = "LAST_NAME", length = 100)
    private String lastName;
    
    @NotBlank
    @Email
    @Column(name = "EMAIL", length = 255, nullable = false)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCALATION_TO")
    private WorkflowUser escalationTo;
    
    @NotNull
    @Column(name = "IS_ACTIVE", length = 1, nullable = false)
    private String isActive = "Y";
    
    @NotBlank
    @Column(name = "CREATED_BY", length = 100, nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowUser() {}
    
    public WorkflowUser(String username, String firstName, String lastName, String email, String createdBy) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdBy = createdBy;
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
    
    public WorkflowUser getEscalationTo() {
        return escalationTo;
    }
    
    public void setEscalationTo(WorkflowUser escalationTo) {
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
        return "WorkflowUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
