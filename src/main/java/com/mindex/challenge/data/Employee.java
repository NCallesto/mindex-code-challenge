package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindex.challenge.exception.InvalidEmployeeRequestException;

public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;

    public Employee() {}

    /**
     * Constructs a new Employee with the required fields (firstName, lastName, position, department).
     *
     * @param firstName  the employee's first name (must not be null or empty)
     * @param lastName   the employee's last name (must not be null or empty)
     * @param position   the employee's job position (must not be null or empty)
     * @param department the employee's department (must not be null or empty)
     * @throws InvalidEmployeeRequestException if any required field is null or empty
     */
    public Employee(String firstName, String lastName, String position, String department) {
        validateRequiredFields(firstName, lastName, position, department);
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
    }

    /**
     * Constructs a new Employee with the required fields and an optional list of direct reports.
     * If the provided directReports list is null, it initializes the field as an empty ArrayList.
     * Otherwise, it creates a copy of the provided list.
     *
     * @param firstName      the employee's first name (must not be null or empty)
     * @param lastName       the employee's last name (must not be null or empty)
     * @param position       the employee's job position (must not be null or empty)
     * @param department     the employee's department (must not be null or empty)
     * @param directReports  a list of employees who report directly to this employee (may be null)
     * @throws InvalidEmployeeRequestException if any required field is null or empty
     */
    public Employee(String firstName, String lastName, String position, String department, List<Employee> directReports) {
        validateRequiredFields(firstName, lastName, position, department);
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
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
     * Compares the current Employee with an updated version and finds which fields were changed.
     * Only fields that were directly set in the updated object are compared (null values in the update are ignored).
     *
     * @param updated The updated Employee object to compare against.
     * @return A map where:
     *         - Key: The name of the field that was changed ("firstName").
     *         - Value: A string showing the change ("John -> Jane").
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
     * Returns a list of field names that have non-null values in the current Employee object.
     * Used for update logging where only some fields are modified.
     *
     * @return A list of non-null field names ("firstName", "position").
     */
    @JsonIgnore
    public List<String> getNonNullFields() {
        List<String> fields = new ArrayList<>();
        if (this.firstName != null) fields.add("firstName");
        if (this.lastName != null) fields.add("lastName");
        if (this.position != null) fields.add("position");
        if (this.department != null) fields.add("department");
        return fields;
    }

    /**
     * Validates that all of the required fields (firstName, lastName, position, department) are non-null and non-empty.
     * Throws an exception if any field is invalid.
     *
     * @param fields Varargs parameter that represents the field values to validate, 
     *              in the order of: firstName, lastName, position, department.
     * @throws InvalidEmployeeRequestException if any required field is null or empty.
     */
    private void validateRequiredFields(String... fields) {
        String[] fieldNames = {"firstName", "lastName", "position", "department"};
        List<String> missingFields = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null || fields[i].trim().isEmpty()) {
                missingFields.add(fieldNames[i]);
            }
        }

        if (!missingFields.isEmpty()) {
            throw new InvalidEmployeeRequestException(
                "Required fields missing: " + String.join(", ", missingFields));
        }
    }
}
