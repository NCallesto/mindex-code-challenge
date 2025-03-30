package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;

/**
 * REST controller for employee-related operations.
 * Exposes endpoints for CRUD operations and reporting structure.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final ReportingStructureService reportingStructureService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ReportingStructureService reportingStructureService) {
        this.employeeService = employeeService;
        this.reportingStructureService = reportingStructureService;
    }

    /**
     * Creates a new employee.
     * 
     * @param employee Employee data to create
     * @return The created employee with generated ID
     */
    @PostMapping("")
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
    @GetMapping("/{id}")
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
    @PutMapping("/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) throws EmployeeNotFoundException {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * Retrieves the complete reporting structure hierarchy for an employee.
     * 
     * @param id The ID of the employee to get the reporting structure for
     * @return Fully populated reporting structure with all direct and indirect reports
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @apiNote Consider this part of the employee resource, hence the URL structure
     * @example GET /employee/16a596ae-edd3-4847-99fe-c4518e82c86f/reporting-structure
     */
    @GetMapping("/{id}/reporting-structure")
    public ReportingStructure getReportingStructure(@PathVariable String id) throws EmployeeNotFoundException {
        LOG.debug("Received reporting structure request for employee id [{}]", id);
        return reportingStructureService.getReportingStructure(id);
    }
}
