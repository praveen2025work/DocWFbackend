package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Admin Dashboard
 */
public class AdminDashboardDto {
    
    private Long adminId;
    private String adminName;
    private SystemOverviewDto systemOverview;
    private List<WorkflowInstanceDto> systemWorkflows;
    private List<WorkflowUserDto> systemUsers;
    private List<EscalationItemDto> systemEscalations;
    private SystemPerformanceDto systemPerformance;
    private List<SystemAlertDto> systemAlerts;
    private LocalDateTime lastUpdated;

    // Default constructor
    public AdminDashboardDto() {}

    // Constructor with required fields
    public AdminDashboardDto(Long adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public SystemOverviewDto getSystemOverview() {
        return systemOverview;
    }

    public void setSystemOverview(SystemOverviewDto systemOverview) {
        this.systemOverview = systemOverview;
    }

    public List<WorkflowInstanceDto> getSystemWorkflows() {
        return systemWorkflows;
    }

    public void setSystemWorkflows(List<WorkflowInstanceDto> systemWorkflows) {
        this.systemWorkflows = systemWorkflows;
    }

    public List<WorkflowUserDto> getSystemUsers() {
        return systemUsers;
    }

    public void setSystemUsers(List<WorkflowUserDto> systemUsers) {
        this.systemUsers = systemUsers;
    }

    public List<EscalationItemDto> getSystemEscalations() {
        return systemEscalations;
    }

    public void setSystemEscalations(List<EscalationItemDto> systemEscalations) {
        this.systemEscalations = systemEscalations;
    }

    public SystemPerformanceDto getSystemPerformance() {
        return systemPerformance;
    }

    public void setSystemPerformance(SystemPerformanceDto systemPerformance) {
        this.systemPerformance = systemPerformance;
    }

    public List<SystemAlertDto> getSystemAlerts() {
        return systemAlerts;
    }

    public void setSystemAlerts(List<SystemAlertDto> systemAlerts) {
        this.systemAlerts = systemAlerts;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Inner class for system overview
     */
    public static class SystemOverviewDto {
        private int totalUsers;
        private int activeUsers;
        private int totalWorkflows;
        private int activeWorkflows;
        private int totalTasks;
        private int pendingTasks;
        private int systemAlerts;
        private double systemUptime;

        // Default constructor
        public SystemOverviewDto() {}

        // Getters and Setters
        public int getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(int totalUsers) {
            this.totalUsers = totalUsers;
        }

        public int getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(int activeUsers) {
            this.activeUsers = activeUsers;
        }

        public int getTotalWorkflows() {
            return totalWorkflows;
        }

        public void setTotalWorkflows(int totalWorkflows) {
            this.totalWorkflows = totalWorkflows;
        }

        public int getActiveWorkflows() {
            return activeWorkflows;
        }

        public void setActiveWorkflows(int activeWorkflows) {
            this.activeWorkflows = activeWorkflows;
        }

        public int getTotalTasks() {
            return totalTasks;
        }

        public void setTotalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
        }

        public int getPendingTasks() {
            return pendingTasks;
        }

        public void setPendingTasks(int pendingTasks) {
            this.pendingTasks = pendingTasks;
        }

        public int getSystemAlerts() {
            return systemAlerts;
        }

        public void setSystemAlerts(int systemAlerts) {
            this.systemAlerts = systemAlerts;
        }

        public double getSystemUptime() {
            return systemUptime;
        }

        public void setSystemUptime(double systemUptime) {
            this.systemUptime = systemUptime;
        }
    }

    /**
     * Inner class for system performance
     */
    public static class SystemPerformanceDto {
        private double averageResponseTime;
        private double throughput;
        private int errorRate;
        private double cpuUsage;
        private double memoryUsage;
        private double diskUsage;

        // Default constructor
        public SystemPerformanceDto() {}

        // Getters and Setters
        public double getAverageResponseTime() {
            return averageResponseTime;
        }

        public void setAverageResponseTime(double averageResponseTime) {
            this.averageResponseTime = averageResponseTime;
        }

        public double getThroughput() {
            return throughput;
        }

        public void setThroughput(double throughput) {
            this.throughput = throughput;
        }

        public int getErrorRate() {
            return errorRate;
        }

        public void setErrorRate(int errorRate) {
            this.errorRate = errorRate;
        }

        public double getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public double getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(double memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public double getDiskUsage() {
            return diskUsage;
        }

        public void setDiskUsage(double diskUsage) {
            this.diskUsage = diskUsage;
        }
    }

    /**
     * Inner class for system alerts
     */
    public static class SystemAlertDto {
        private Long alertId;
        private String alertType; // SYSTEM, PERFORMANCE, SECURITY, WORKFLOW
        private String severity; // LOW, MEDIUM, HIGH, CRITICAL
        private String title;
        private String message;
        private LocalDateTime createdAt;
        private String status; // ACTIVE, ACKNOWLEDGED, RESOLVED

        // Default constructor
        public SystemAlertDto() {}

        // Getters and Setters
        public Long getAlertId() {
            return alertId;
        }

        public void setAlertId(Long alertId) {
            this.alertId = alertId;
        }

        public String getAlertType() {
            return alertType;
        }

        public void setAlertType(String alertType) {
            this.alertType = alertType;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
