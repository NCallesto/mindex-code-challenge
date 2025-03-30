package com.mindex.challenge.service;

import java.util.Date;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * Service interface for managing employee compensation.
 * Defines operations for creating and retrieving compensation records.
 */
public interface CompensationService {
    /**
     * Creates a new compensation record for an employee.
     *
     * @param employeeId The ID of the employee to create compensation for
     * @param salary The annual salary amount
     * @param effectiveDate The date when the compensation becomes effective
     * @return The created Compensation object
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @throws IllegalArgumentException if salary is not positive or date is null
     */
    Compensation create(String employeeId, double salary, Date effectiveDate) throws EmployeeNotFoundException;

    /**
     * Retrieves the compensation record for an employee.
     *
     * @param employeeId The ID of the employee to find compensation for
     * @return The Compensation object
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @throws CompensationNotFoundException if no compensation exists for the employee
     */
    Compensation read(String employeeId) throws EmployeeNotFoundException, CompensationNotFoundException;
}