package com.mindex.challenge.controller;

import java.util.Date;

/**
 * Request DTO for creating compensation records.
 * Used to deserialize incoming JSON payloads for compensation creation.
 */
public class CompensationRequest {
    private double salary;
    private Date effectiveDate;

    /**
     * Gets the salary amount from the request.
     *
     * @return The salary value
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the salary amount.
     *
     * @param salary The salary value to set
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Gets the effective date from the request.
     *
     * @return The effective date
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the effective date.
     *
     * @param effectiveDate The effective date to set
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}