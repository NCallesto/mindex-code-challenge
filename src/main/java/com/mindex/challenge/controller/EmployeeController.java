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
        // LOG the request to create an employee with the provided fields map
        LOG.debug("Creating employee with ID: {}", employee.getEmployeeId());   
    
        // Create the employee using the EmployeeService
        Employee created = employeeService.create(employee);

        // Log the employee's full details after creation
        LOG.info("Created employee - ID: {}, FullName: {} {}, Position: {}, Department: {}",
            created.getEmployeeId() != null ? created.getEmployeeId() : "",
            created.getFirstName() != null ? created.getFirstName() : "",
            created.getLastName() != null ? created.getLastName() : "",
            created.getPosition() != null ? created.getPosition() : "",
            created.getDepartment() != null ? created.getDepartment() : "");

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
        // LOG request to fetch an employee by ID
        LOG.debug("Requesting employee - ID: {}", id);
    
        // Read the employee using the EmployeeService
        Employee employee = employeeService.read(id);

        // LOG the employee's details
        LOG.debug("Successfully fetched employee - ID: {}, Name: {} {}",
            employee.getEmployeeId(),
            employee.getFirstName(),
            employee.getLastName());

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
        // LOG to request to update an employee by ID
        LOG.debug("Update request for ID: {}. Submitted fields: {}", 
            id,
            String.join(", ", employee.getNonNullFields()));
            
        // Set the employee ID
        employee.setEmployeeId(id);

        // Update the employee data using the EmployeeService and return
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
        // LOG the request to get the reporting structure
        LOG.debug("Initiating reporting structure generation for employee ID: {}", id);

        // Generate the reporting structure using the ReportingStructureService
        ReportingStructure structure = reportingStructureService.getReportingStructure(id);

        // Return the generated reporting structure
        return structure;
    }
}
