package com.mindex.challenge.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

/**
 * Implementation of the CompensationService interface.
 * Handles business logic for compensation operations including validation and persistence.
 */
@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    private final CompensationRepository compensationRepository;
    private final EmployeeService employeeService;

    /**
     * Constructs a new CompensationServiceImpl with required dependencies.
     *
     * @param compensationRepository The repository for compensation data access
     * @param employeeService The service for employee operations
     */
    @Autowired
    public CompensationServiceImpl(CompensationRepository compensationRepository, EmployeeService employeeService) {
        this.compensationRepository = compensationRepository;
        this.employeeService = employeeService;
    }

    @Override
    public Compensation create(String employeeId, double salary, Date effectiveDate) throws EmployeeNotFoundException {
        LOG.debug("Creating compensation for employee ID: {}", employeeId);

        // Validate employee exists
        Employee employee = employeeService.read(employeeId);

        // Check if compensation already exists
        Compensation existing = compensationRepository.findByEmployeeId(employeeId);
        if (existing != null) {
            LOG.warn("Compensation already exists for employee ID: {}", employeeId);
            throw new IllegalArgumentException("Compensation already exists for this employee");
        }

        Compensation compensation = new Compensation(employee, salary, effectiveDate);
        Compensation created = compensationRepository.insert(compensation);

        LOG.info("Created compensation for employee {} {} - Salary: {}, Effective: {}",
                employee.getFirstName(),
                employee.getLastName(),
                salary,
                effectiveDate);

        return created;
    }

    @Override
    public Compensation read(String employeeId) throws EmployeeNotFoundException, CompensationNotFoundException {
        LOG.debug("Retrieving compensation for employee ID: {}", employeeId);

        // Validate employee exists
        employeeService.read(employeeId);

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
        if (compensation == null) {
            LOG.warn("No compensation found for employee ID: {}", employeeId);
            throw new CompensationNotFoundException("No compensation found for employee: " + employeeId);
        }

        LOG.debug("Found compensation for employee ID: {} - Salary: {}, Effective: {}",
                employeeId,
                compensation.getSalary(),
                compensation.getEffectiveDate());

        return compensation;
    }
}