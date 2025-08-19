package com.docwf.dto;

import java.util.Map;

/**
 * Data Transfer Object for User Preferences and Settings
 */
public class UserPreferencesDto {
    
    private Long userId;
    private String username;
    
    // Notification Preferences
    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean pushNotifications;
    private boolean taskReminders;
    private boolean workflowUpdates;
    private boolean escalationAlerts;
    
    // Display Preferences
    private String theme; // LIGHT, DARK, AUTO
    private String language; // EN, ES, FR, etc.
    private String timezone;
    private String dateFormat;
    private String timeFormat;
    
    // Dashboard Preferences
    private boolean showTaskCount;
    private boolean showWorkflowProgress;
    private boolean showPerformanceMetrics;
    private boolean showTeamOverview;
    private int dashboardRefreshInterval; // in minutes
    
    // Workflow Preferences
    private boolean autoAssignTasks;
    private boolean showDueDateWarnings;
    private int dueDateWarningDays;
    private boolean enableBulkOperations;
    
    // Custom Preferences
    private Map<String, String> customPreferences;

    // Default constructor
    public UserPreferencesDto() {}

    // Constructor with required fields
    public UserPreferencesDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        // Set default values
        this.emailNotifications = true;
        this.taskReminders = true;
        this.workflowUpdates = true;
        this.theme = "LIGHT";
        this.language = "EN";
        this.timezone = "UTC";
        this.dateFormat = "MM/dd/yyyy";
        this.timeFormat = "HH:mm";
        this.showTaskCount = true;
        this.showWorkflowProgress = true;
        this.dashboardRefreshInterval = 5;
        this.dueDateWarningDays = 3;
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

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public boolean isPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public boolean isTaskReminders() {
        return taskReminders;
    }

    public void setTaskReminders(boolean taskReminders) {
        this.taskReminders = taskReminders;
    }

    public boolean isWorkflowUpdates() {
        return workflowUpdates;
    }

    public void setWorkflowUpdates(boolean workflowUpdates) {
        this.workflowUpdates = workflowUpdates;
    }

    public boolean isEscalationAlerts() {
        return escalationAlerts;
    }

    public void setEscalationAlerts(boolean escalationAlerts) {
        this.escalationAlerts = escalationAlerts;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean isShowTaskCount() {
        return showTaskCount;
    }

    public void setShowTaskCount(boolean showTaskCount) {
        this.showTaskCount = showTaskCount;
    }

    public boolean isShowWorkflowProgress() {
        return showWorkflowProgress;
    }

    public void setShowWorkflowProgress(boolean showWorkflowProgress) {
        this.showWorkflowProgress = showWorkflowProgress;
    }

    public boolean isShowPerformanceMetrics() {
        return showPerformanceMetrics;
    }

    public void setShowPerformanceMetrics(boolean showPerformanceMetrics) {
        this.showPerformanceMetrics = showPerformanceMetrics;
    }

    public boolean isShowTeamOverview() {
        return showTeamOverview;
    }

    public void setShowTeamOverview(boolean showTeamOverview) {
        this.showTeamOverview = showTeamOverview;
    }

    public int getDashboardRefreshInterval() {
        return dashboardRefreshInterval;
    }

    public void setDashboardRefreshInterval(int dashboardRefreshInterval) {
        this.dashboardRefreshInterval = dashboardRefreshInterval;
    }

    public boolean isAutoAssignTasks() {
        return autoAssignTasks;
    }

    public void setAutoAssignTasks(boolean autoAssignTasks) {
        this.autoAssignTasks = autoAssignTasks;
    }

    public boolean isShowDueDateWarnings() {
        return showDueDateWarnings;
    }

    public void setShowDueDateWarnings(boolean showDueDateWarnings) {
        this.showDueDateWarnings = showDueDateWarnings;
    }

    public int getDueDateWarningDays() {
        return dueDateWarningDays;
    }

    public void setDueDateWarningDays(int dueDateWarningDays) {
        this.dueDateWarningDays = dueDateWarningDays;
    }

    public boolean isEnableBulkOperations() {
        return enableBulkOperations;
    }

    public void setEnableBulkOperations(boolean enableBulkOperations) {
        this.enableBulkOperations = enableBulkOperations;
    }

    public Map<String, String> getCustomPreferences() {
        return customPreferences;
    }

    public void setCustomPreferences(Map<String, String> customPreferences) {
        this.customPreferences = customPreferences;
    }

    /**
     * Check if any notifications are enabled
     */
    public boolean hasAnyNotifications() {
        return emailNotifications || smsNotifications || pushNotifications;
    }

    /**
     * Get custom preference value
     */
    public String getCustomPreference(String key) {
        return customPreferences != null ? customPreferences.get(key) : null;
    }

    /**
     * Set custom preference value
     */
    public void setCustomPreference(String key, String value) {
        if (customPreferences == null) {
            customPreferences = new java.util.HashMap<>();
        }
        customPreferences.put(key, value);
    }

    /**
     * Check if user prefers dark theme
     */
    public boolean prefersDarkTheme() {
        return "DARK".equals(this.theme);
    }

    /**
     * Check if user prefers light theme
     */
    public boolean prefersLightTheme() {
        return "LIGHT".equals(this.theme);
    }

    /**
     * Check if user prefers auto theme
     */
    public boolean prefersAutoTheme() {
        return "AUTO".equals(this.theme);
    }
}
