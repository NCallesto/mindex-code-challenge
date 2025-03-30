package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * REST controller for employee-related operations.
 * Exposes endpoints for CRUD operations and reporting structure.
 */
@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new employee.
     * 
     * @param employee Employee data to create
     * @return The created employee with generated ID
     */
    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    /**
     * Gets an employee by ID.
     * 
     * @param id ID of the employee to get
     * @return The requested employee
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) throws EmployeeNotFoundException {    
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    /**
     * Updates an existing employee.
     * 
     * @param id ID of the employee to update
     * @param employee Updated employee data
     * @return The updated employee
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) throws EmployeeNotFoundException {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * Gets the reporting structure for an employee.
     * 
     * @param id ID of the employee to get reporting structure for
     * @return Complete reporting structure with employee and report count
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @apiNote The report count is calculated dynamically and is not persisted
     */
    @GetMapping("/employee/{id}/reporting-structure")
    public ReportingStructure getReportingStructure(@PathVariable String id) throws EmployeeNotFoundException {
        LOG.debug("Received reporting structure request for employee id [{}]", id);

        return employeeService.getReportingStructure(id);
    }
}
