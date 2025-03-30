package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.CompensationService;

/**
 * REST controller for compensation operations.
 * 
 * Endpoints:
 * - POST /employee/{id}/compensation - Create compensation
 * - GET /employee/{id}/compensation - Read compensation
 * 
 * Features:
 * - Proper REST resource naming
 * - Clear parameter documentation
 * - Consistent error responses
 */
@RestController
@RequestMapping("/employee")
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    private final CompensationService compensationService;

    /**
     * Constructs a new CompensationController with required dependencies.
     *
     * @param compensationService The service for compensation operations
     */
    @Autowired
    public CompensationController(CompensationService compensationService) {
        this.compensationService = compensationService;
    }

    /**
     * Creates a new compensation record for an employee.
     *
     * @param employeeId The ID of the employee to create compensation for
     * @param request The request body containing salary and effectiveDate
     * @return The created Compensation object
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @PostMapping("/{id}/compensation")
    public Compensation create(@PathVariable("id") String employeeId, @RequestBody CompensationRequest request) 
            throws EmployeeNotFoundException {
        LOG.debug("Received compensation create request for employee ID: {}", employeeId);

        return compensationService.create(
                employeeId,
                request.getSalary(),
                request.getEffectiveDate());
    }

    /**
     * Retrieves the compensation record for an employee.
     *
     * @param employeeId The ID of the employee to find compensation for
     * @return The Compensation object
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     * @throws CompensationNotFoundException if no compensation exists for the employee
     */
    @GetMapping("/{id}/compensation")
    public Compensation read(@PathVariable("id") String employeeId) 
            throws EmployeeNotFoundException, CompensationNotFoundException {
        LOG.debug("Received compensation read request for employee ID: {}", employeeId);

        return compensationService.read(employeeId);
    }
}