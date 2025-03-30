package com.mindex.challenge.data;

import java.util.Date;

/**
 * Represents an employee's compensation details including salary and effective date.
 * This class is immutable to ensure thread safety and data consistency.
 */
public class Compensation {
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
     * Gets the employee associated with this compensation.
     *
     * @return The employee object (never null)
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Gets the annual salary amount.
     *
     * @return The salary value
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Gets the effective date of this compensation.
     *
     * @return A defensive copy of the effective date
     */
    public Date getEffectiveDate() {
        return new Date(effectiveDate.getTime()); // Defensive copy
    }
}