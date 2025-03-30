package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * Service interface for generating the reporting structures.
 * Provides methods to get complete reporting hierarchies for employees.
 */
public interface ReportingStructureService {
    /**
     * Generate the complete reporting structure for a given employee.
     * 
     * @param employeeID The ID of the employee to generate the structure for
     * @return Fully populated ReportingStructure with the employee and total reports
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    ReportingStructure getReportingStructure(String employeeID) throws EmployeeNotFoundException;
}
