package com.securedevops.employeemanagement.model;

public class DepartmentSummary {

    private String department;
    private long employeeCount;

    public DepartmentSummary(String department, long employeeCount) {
        this.department = department;
        this.employeeCount = employeeCount;
    }

    public String getDepartment() {
        return department;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }
}
