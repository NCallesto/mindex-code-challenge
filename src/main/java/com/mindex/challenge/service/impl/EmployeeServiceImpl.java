package com.mindex.challenge.service.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;

/**
 * Implementation of the EmployeeService interface.
 * Provides business logic for employee operations including CRUD.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Employee create(Employee employee) {
        // LOG that the employee is being created
        LOG.debug("Generating new employee record...");

        // Set the employee ID to a randomly generated UUID
        employee.setEmployeeId(UUID.randomUUID().toString());

        // Insert the employee into the repository
        Employee createdEmployee = employeeRepository.insert(employee);
        
        // LOG the assigned employee ID
        LOG.info("Assigned employee ID: {}", createdEmployee.getEmployeeId());

        // Return the created employee
        return createdEmployee;
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public Employee read(String id) throws EmployeeNotFoundException {
        // LOG that the employee is being fetched
        LOG.debug("Fetching employee record - ID: {}", id);

        // Try to fetch the employee from the repository by their employee ID
        Employee fetchedEmployee = employeeRepository.findByEmployeeId(id);
        
        // LOG the retrieved employee details
        LOG.info("Retrieved employee - ID: {}, Position: {}, Department: {}",
            fetchedEmployee.getEmployeeId(),
            fetchedEmployee.getPosition(),
            fetchedEmployee.getDepartment());

        // Return the retrieved employee
        return resolveEmployeeHierarchy(fetchedEmployee);
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public Employee update(Employee updatedEmployee) throws EmployeeNotFoundException {
        boolean changed = false;

        // Get the existing employee record
        Employee existing = employeeRepository.findByEmployeeId(updatedEmployee.getEmployeeId());

        // Detect any changes with the getChangedFields method
        Map<String, String> changes = existing.getChangedFields(updatedEmployee);

        // Merge changes from updatedEmployee into existing employee
        if (updatedEmployee.getFirstName() != null) {
            existing.setFirstName(updatedEmployee.getFirstName());
        }
        if (updatedEmployee.getLastName() != null) {
            existing.setLastName(updatedEmployee.getLastName());
        }
        if (updatedEmployee.getPosition() != null) {
            existing.setPosition(updatedEmployee.getPosition());
        }
        if (updatedEmployee.getDepartment() != null) {
            existing.setDepartment(updatedEmployee.getDepartment());
        }
        if (updatedEmployee.getDirectReports() != null) {
            existing.setDirectReports(updatedEmployee.getDirectReports());
        }

        // Log based on what was changed
        if (!changes.isEmpty()) {
            changed = true;
            LOG.debug("Updating employee: {}. Changes: {}",
                updatedEmployee.getEmployeeId(),
                changes.entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "))
            );
        } else {
            LOG.warn("No changes detected for employee: {}", updatedEmployee.getEmployeeId());
        }

        // Save the merged employee
        Employee saved = employeeRepository.save(existing);

        // Log the updated employee details if there were changes
        if(changed) {
            LOG.info("Employee {} updated. Modified fields: {}", 
                saved.getEmployeeId(), 
                String.join(", ", changes.keySet()));
        }
        
        return saved;
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

        // Resolve only the direct reports recursively if they exist
        if (employee.getDirectReports() != null) {
            resolved.setDirectReports(
                employee.getDirectReports().stream()
                    .map(report -> {
                        try {
                            Employee directReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
                            if (directReport == null) {
                                LOG.warn("Missing direct report: {}", report.getEmployeeId());
                                return null;
                            }
                            Employee simplified = new Employee();
                            simplified.setEmployeeId(directReport.getEmployeeId());
                            simplified.setFirstName(directReport.getFirstName());
                            simplified.setLastName(directReport.getLastName());
                            simplified.setPosition(directReport.getPosition());
                            simplified.setDepartment(directReport.getDepartment());
                            
                            // Match reporting structure behavior:
                            // - [] if employee has reports (doesn't actually show them because the reporting structure is not needed)
                            // - null if no reports
                            if (directReport.getDirectReports() != null && !directReport.getDirectReports().isEmpty()) {
                                simplified.setDirectReports(Collections.emptyList());
                            }
                            // else remains null

                            return simplified;
                        } catch (Exception e) {
                            LOG.warn("Error resolving direct report: {}", report.getEmployeeId(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList()
            );
        } else {
            resolved.setDirectReports(Collections.emptyList());
        }
        

        return resolved;
    }
}
