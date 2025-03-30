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
        // LOG all employee details when creating a new employee
        LOG.debug("Creating new employee - FirstName: {}, LastName: {}, Position: {}, Department: {}", 
            employee.getFirstName(),
            employee.getLastName(),
            employee.getPosition(),
            employee.getDepartment());
    
        // Create the employee using the EmployeeService
        Employee created = employeeService.create(employee);

        // LOG the created employee's details
        LOG.info("Successfully created employee - ID: {}, FullName: {} {}", 
            created.getEmployeeId(),
            created.getFirstName(),
            created.getLastName());

        // Return the created employee
        return created;
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
        // LOG the ID when fetching an employee
        LOG.debug("Fetching employee with ID: {}", id);
    
        // Read the employee using the EmployeeService
        Employee employee = employeeService.read(id);

        // LOG the employee's details after fetching
        LOG.debug("Found employee - ID: {}, FullName: {} {}, Position: {}",
            employee.getEmployeeId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getPosition());

        // Return the fetched employee
        return employee;
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
        // LOG the ID and updated data when updating an employee
        LOG.debug("Updating employee ID: {} with data - FirstName: {}, Position: {}, Department: {}",
            id,
            employee.getFirstName(),
            employee.getPosition(),
            employee.getDepartment());
        
        // Set the employee ID in the request body to make sure it matches the path variable
        employee.setEmployeeId(id);

        // Update the employee using the EmployeeService
        Employee updated = employeeService.update(employee);

        // LOG the updated employee's details after updating
        LOG.info("Successfully updated employee - ID: {}, New Position: {}",
            id,
            updated.getPosition());

        // Return the updated employee
        return updated;
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
        // LOG the ID when generating the reporting structure
        LOG.debug("Generating reporting structure for employee ID: {}", id);

        // Generate the reporting structure using the ReportingStructureService
        ReportingStructure structure = reportingStructureService.getReportingStructure(id);

        // LOG the employee's details and total number of reports in the structure
        LOG.info("Generated reporting structure - Employee: {} {}, Total Reports: {}",
            structure.getEmployee().getFirstName(),
            structure.getEmployee().getLastName(),
            structure.getNumberOfReports());

        // Return the generated reporting structure
        return structure;
    }
}
