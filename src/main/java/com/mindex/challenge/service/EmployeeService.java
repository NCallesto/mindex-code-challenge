package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * Service interface for create, read, and update employee operations.
 * Defines the contract for employee management including the CRUD operations.
 */
public interface EmployeeService {
    /**
     * Creates a new employee record in the system.
     *
     * @param employee The employee data to create. Must not be null.
     * @return The created employee with generated ID and any of the system-populated fields
     * @throws IllegalArgumentException if the employee data is invalid
     * @implNote The employeeID will be made automatically if not given
     * @example Employee with firstName, lastName, position, and department
     */
    Employee create(Employee employee);

    /**
     * Retrieves an employee by their unique id.
     * 
     * @param id The employee ID to search for. Must not be null or empty.
     * @return The found employee
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @throws IllegalArgumentException if the ID format is invalid
     * @apiNote This performs a complete read of the employee record including the direct reports
     */
    Employee read(String id) throws EmployeeNotFoundException;

    /**
     * Updates an existing employee record.
     *
     * @param employee The employee data to update. Must not be null.
     * @return The updated employee
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @throws IllegalArgumentException if the employee data is invalid
     * @implNote The employeeID in the object must match an existing one
     * @see #read(String) 
     */
    Employee update(Employee employee) throws EmployeeNotFoundException;
}
