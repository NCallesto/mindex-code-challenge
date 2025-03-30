package com.mindex.challenge.service.impl;

import java.util.Map;
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
        return fetchedEmployee;
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public Employee update(Employee updatedEmployee) throws EmployeeNotFoundException {
        // Get the employees existing record
        Employee existing = employeeRepository.findByEmployeeId(updatedEmployee.getEmployeeId());

        // Detect any changes with the getChangesFields method
        Map<String, String> changes = existing.getChangedFields(updatedEmployee);
        
        // Log based on what was changed
        if (!changes.isEmpty()) {
            LOG.debug("Updating employee: {}. Changes: {}",
                updatedEmployee.getEmployeeId(),
                changes.entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "))
            );
        } else {
            LOG.warn("No changes detected for employee: {}", updatedEmployee.getEmployeeId());
        }

        // Then save updated employee
        Employee saved = employeeRepository.save(updatedEmployee);

        // Log the updated employee details
        LOG.info("Employee {} updated. Modified fields: {}", 
            saved.getEmployeeId(), 
            String.join(", ", changes.keySet()));
        
        // return the saved employee
        return saved;
    }
}
