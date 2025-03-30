package com.mindex.challenge.data;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    // Stores just the ID of the employee
    private String id;

    @DBRef
    // Ref to the full employee object
    private final Employee employee;
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

        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = new Date(effectiveDate.getTime()); // Defensive copy
    }

    /**
     * Gets the unique identifier for this compensation record.
     * @return The compensation ID
     */
    public String getId() {
        return id;
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