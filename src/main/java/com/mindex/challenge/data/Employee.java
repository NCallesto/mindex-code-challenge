package com.mindex.challenge.data;

import java.util.ArrayList;
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

    public Employee() {}


    public Employee(String firstName, String lastName, String position, String department) {
        validateRequiredFields(firstName, lastName, position, department);
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
    }


    public Employee(String firstName, String lastName, String position, String department, List<Employee> directReports) {
        validateRequiredFields(firstName, lastName, position, department);
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;

        // if null, initialize to empty list
        // if not null, create a new list with the same elements
        this.directReports = directReports != null ? new ArrayList<>(directReports) : new ArrayList<>();
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
     * @param updated The updated employee object
     * @return Map of changed fields (key: field name, value: old→new values)
     */
    public Map<String, String> getChangedFields(Employee updated) {
        Map<String, String> changes = new HashMap<>();
        
        // Only compare fields that were explicitly set in the update
        if (updated.getFirstName() != null && !Objects.equals(this.firstName, updated.getFirstName())) {
            changes.put("firstName", this.firstName + " -> " + updated.getFirstName());
        }
        if (updated.getLastName() != null && !Objects.equals(this.lastName, updated.getLastName())) {
            changes.put("lastName", this.lastName + " -> " + updated.getLastName());
        }
        if (updated.getPosition() != null && !Objects.equals(this.position, updated.getPosition())) {
            changes.put("position", this.position + " -> " + updated.getPosition());
        }
        if (updated.getDepartment() != null && !Objects.equals(this.department, updated.getDepartment())) {
            changes.put("department", this.department + " -> " + updated.getDepartment());
        }
        
        return changes;
    }

    /**
     * Returns names of non-null fields in the current object.
     * Useful for logging partial updates in the controller.
     */
    public List<String> getNonNullFields() {
        List<String> fields = new ArrayList<>();
        if (this.firstName != null) fields.add("firstName");
        if (this.lastName != null) fields.add("lastName");
        if (this.position != null) fields.add("position");
        if (this.department != null) fields.add("department");
        return fields;
    }

    // Shared validation logic
    private void validateRequiredFields(String... fields) {
        String[] fieldNames = {"firstName", "lastName", "position", "department"};
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null || fields[i].trim().isEmpty()) {
                throw new IllegalArgumentException(fieldNames[i] + " is required");
            }
        }
    }
}
