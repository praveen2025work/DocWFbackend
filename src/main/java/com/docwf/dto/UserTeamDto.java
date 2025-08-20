package com.docwf.dto;

import java.util.List;

/**
 * Data Transfer Object for User Team Information
 */
public class UserTeamDto {
    
    private Long teamId;
    private String teamName;
    private String teamDescription;
    private Long teamLeadId;
    private String teamLeadName;
    private List<WorkflowUserDto> teamMembers;
    private int totalMembers;
    private int activeMembers;
    private String teamStatus; // ACTIVE, INACTIVE, ARCHIVED
    private String department;
    private String location;

    // Default constructor
    public UserTeamDto() {}

    // Constructor with required fields
    public UserTeamDto(Long teamId, String teamName, Long teamLeadId, String teamLeadName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLeadId = teamLeadId;
        this.teamLeadName = teamLeadName;
        this.teamStatus = "ACTIVE";
    }

    // Getters and Setters
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public Long getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(Long teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public String getTeamLeadName() {
        return teamLeadName;
    }

    public void setTeamLeadName(String teamLeadName) {
        this.teamLeadName = teamLeadName;
    }

    public List<WorkflowUserDto> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<WorkflowUserDto> teamMembers) {
        this.teamMembers = teamMembers;
        if (teamMembers != null) {
            this.totalMembers = teamMembers.size();
            this.activeMembers = (int) teamMembers.stream()
                .filter(member -> "Y".equals(member.getIsActive()))
                .count();
        }
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(int activeMembers) {
        this.activeMembers = activeMembers;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Check if team is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.teamStatus);
    }

    /**
     * Get team member by ID
     */
    public WorkflowUserDto getTeamMember(Long userId) {
        if (teamMembers != null) {
            return teamMembers.stream()
                .filter(member -> member.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    /**
     * Check if user is team lead
     */
    public boolean isTeamLead(Long userId) {
        return this.teamLeadId != null && this.teamLeadId.equals(userId);
    }

    /**
     * Get team member count by status
     */
    public int getMemberCountByStatus(String status) {
        if (teamMembers != null) {
            return (int) teamMembers.stream()
                .filter(member -> status.equals(member.getIsActive()))
                .count();
        }
        return 0;
    }
}
