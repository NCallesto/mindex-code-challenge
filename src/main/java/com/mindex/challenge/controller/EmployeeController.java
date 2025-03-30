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
    public Employee create(@RequestBody Employee request) {
        // For logging purposes
        boolean reports = false;

        // LOG the request to create an employe
        LOG.debug("Requesting to create employee {}", request);  

        Employee employee;

        // Use full constructor if reports are provided
        if (request.getDirectReports() != null && !request.getDirectReports().isEmpty()) {
            reports = true;
            employee = new Employee(
                request.getFirstName(),
                request.getLastName(),
                request.getPosition(),
                request.getDepartment(),
                request.getDirectReports()
            );
        } else {
            // Default to minimal constructor of 4 fields without reports
            employee = new Employee(
                request.getFirstName(),
                request.getLastName(),
                request.getPosition(),
                request.getDepartment()
            );
        }
        
        // Create the employee using the EmployeeService
        Employee created = employeeService.create(employee);

        // Log the employee's full details after creation
        if (reports) { // With reports
            LOG.info("Created employee - ID: {}, FullName: {} {}, Position: {}, Department: {}, DirectReports: {}",
                created.getEmployeeId(),
                created.getFirstName(),
                created.getLastName(),
                created.getPosition(),
                created.getDepartment(),
                created.getDirectReports());
        } else { // Without reports
            LOG.info("Created employee - ID: {}, FullName: {} {}, Position: {}, Department: {}",
                created.getEmployeeId(),
                created.getFirstName(),
                created.getLastName(),
                created.getPosition(),
                created.getDepartment());
        }

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

        // Check that the request has at least one field to update
        if (employee.getNonNullFields().isEmpty()) {
            throw new IllegalArgumentException("No fields provided for update");
        }
            
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
