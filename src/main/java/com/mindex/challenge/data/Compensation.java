package com.mindex.challenge.data;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * Represents an employee's compensation details stored in MongoDB.
 * 
 * Key Features:
 * - @DBRef for proper employee reference
 * - Immutable effectiveDate with defensive copying
 * - Comprehensive validation in constructor
 * - Clear documentation of all fields and methods
 */
public class Compensation {
    @Id
    private String id; 
    private String employeeId; // Store employeeId directly instead of the Employee reference
    private final Employee employee; // full employee data when needed
    private final double salary;
    private final Date effectiveDate;

    /**
     * Constructs a new Compensation record.
     *
     * @param employee The employee this compensation belongs to (must not be null)
     * @param salary The annual salary amount (must be positive)
     * @param effectiveDate The date when this compensation becomes effective (must not be null)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Compensation(Employee employee, double salary, Date effectiveDate) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }
        if (effectiveDate == null) {
            throw new IllegalArgumentException("Effective date cannot be null");
        }

        this.employeeId = employee.getEmployeeId();
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = new Date(effectiveDate.getTime());
    }

    /**
     * Gets the unique identifier for this compensation record.
     * @return The compensation ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the unique identifier for the employee record.
     * @return The employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Gets the employee associated with this compensation.
     * @return The employee object (never null)
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Gets the annual salary amount.
     * @return The salary value
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Gets the effective date of this compensation.
     * @return A defensive copy of the effective date
     */
    public Date getEffectiveDate() {
        return new Date(effectiveDate.getTime());
    }
}