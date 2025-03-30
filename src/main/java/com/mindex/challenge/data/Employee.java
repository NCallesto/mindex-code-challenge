package com.mindex.challenge.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    /**
     * Identifies any changed fields between this and another employee
     * @param other The updated employee object
     * @return Map of changed fields (key: field name, value: old→new values)
     */
    public Map<String, String> getChangedFields(Employee other) {
        Map<String, String> changes = new HashMap<>();

        if (!Objects.equals(this.firstName, other.firstName)) {
            changes.put("firstName", this.firstName + "→" + other.firstName);
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            changes.put("lastName", this.lastName + "→" + other.lastName);
        }
        if (!Objects.equals(this.position, other.position)) {
            changes.put("position", this.position + "→" + other.position);
        }
        if (!Objects.equals(this.department, other.department)) {
            changes.put("department", this.department + "→" + other.department);
        }
        
        return changes;
    }
}
