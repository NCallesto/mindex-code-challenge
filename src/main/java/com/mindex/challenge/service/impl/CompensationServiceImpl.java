package com.mindex.challenge.service.impl;

import java.util.Date;
import java.util.Objects;

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
 * Ensures proper employee reference and compensation uniqueness.
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

        // Get and fully resolve the employee hierarchy
        Employee employee = employeeService.read(employeeId);
        Employee resolvedEmployee = resolveEmployeeHierarchy(employee);

        // Check if compensation already exists
        Compensation existing = compensationRepository.findByEmployeeId(employeeId);
        if (existing != null) {
            LOG.warn("Compensation already exists for employee ID: {}", employeeId);
            throw new IllegalArgumentException("Compensation already exists for this employee");
        }

        // Create new compensation with resolved employee
        Compensation compensation = new Compensation(resolvedEmployee, salary, effectiveDate);
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

        // Get and fully resolve the employee hierarchy
        Employee employee = employeeService.read(employeeId);
        Employee resolvedEmployee = resolveEmployeeHierarchy(employee);

        // Find compensation
        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
        if (compensation == null) {
            LOG.warn("No compensation found for employee ID: {}", employeeId);
            throw new CompensationNotFoundException("No compensation found for employee: " + employeeId);
        }

        // Set the fully resolved employee
        compensation.setEmployee(resolvedEmployee);

        LOG.debug("Found compensation for employee ID: {} - Salary: {}, Effective: {}",
                employeeId,
                compensation.getSalary(),
                compensation.getEffectiveDate());

        return compensation;
    }

    /**
     * Recursively resolves the complete employee hierarchy starting from the given employee.
     * Uses the same logic as ReportingStructureService for consistent employee resolution.
     *
     * @param employee The root employee to start resolution from
     * @return The fully resolved employee with complete direct reports hierarchy
     * @throws EmployeeNotFoundException if any employee in the hierarchy cannot be found
     */
    private Employee resolveEmployeeHierarchy(Employee employee) throws EmployeeNotFoundException {
        // Create new employee instance to hold resolved data
        Employee resolved = new Employee();

        // Copy all basic fields
        resolved.setEmployeeId(employee.getEmployeeId());
        resolved.setFirstName(employee.getFirstName());
        resolved.setLastName(employee.getLastName());
        resolved.setPosition(employee.getPosition());
        resolved.setDepartment(employee.getDepartment());

        // Resolve direct reports recursively if they exist
        if (employee.getDirectReports() != null) {
            resolved.setDirectReports(
                employee.getDirectReports().stream()
                    .map(report -> {
                        try {
                            // Fetch and resolve each report
                            Employee fullReport = employeeService.read(report.getEmployeeId());
                            return resolveEmployeeHierarchy(fullReport);
                        } catch (EmployeeNotFoundException e) {
                            LOG.warn("Missing employee in reporting chain: {}", report.getEmployeeId());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList()
            );
        }

        return resolved;
    }
}