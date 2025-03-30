package com.mindex.challenge.service.impl;

import java.util.UUID;

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
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public Employee read(String id) throws EmployeeNotFoundException {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public Employee update(Employee employee) throws EmployeeNotFoundException {
        LOG.debug("Updating employee [{}]", employee);

        // Check that the employee exists before updating
        if (!employeeRepository.existsById(employee.getEmployeeId())) {
            throw new EmployeeNotFoundException("Employee not found for update: " + employee.getEmployeeId());
        }

        return employeeRepository.save(employee);
    }
}
