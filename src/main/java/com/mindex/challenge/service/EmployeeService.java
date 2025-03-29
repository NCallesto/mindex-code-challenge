package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * Service interface for employee operations.
 */
public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id) throws EmployeeNotFoundException;
    Employee update(Employee employee) throws EmployeeNotFoundException;

    /**
     * Retrieves the complete reporting structure for a given employee.
     * 
     * @param employeeID The ID of the employee to get the reporting structure
     * @return A ReportingStructure object with the employee and their total reports
     * @throws EmployeeNotFoundException incase the employee is not found
     */
    ReportingStructure getReportingStructure(String employeeID) throws EmployeeNotFoundException;
}
