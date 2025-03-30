package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.mindex.challenge.exception.EmployeeNotFoundException;

/**
 * Implementation of the EmployeeService interface.
 * Provides business logic for employee operations including CRUD and reporting structure.
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
        LOG.debug("Creating employee with id [{}]", id);

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

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public ReportingStructure getReportingStructure(String employeeID) throws EmployeeNotFoundException {
        LOG.debug("Creating reporting structure for employee with id [{}]", employeeID);
        
        // Get the employee or throw EmployeeNotFoundException if not found
        Employee employee = read(employeeID);
        
        // Calculate total reports recursively with the calculateNumberOfReports helper method
        int numberOfReports = calculateNumberOfReports(employee);
        
        return new ReportingStructure(employee, numberOfReports);
    }

    /**
     * Recursively calculates the total number of reports under an employee.
     * 
     * @param employee The employee to calculate reports for
     * @return Total number of reports under this employee
     * @implNote This method uses depth-first when going through the reporting hierarchy
     */
    private int calculateNumberOfReports(Employee employee) {
        // Base case: there are no direct reports
        if (employee.getDirectReports() == null) {
            return 0;
        }
        
        int count = 0;
        for (Employee directReport : employee.getDirectReports()) {
            // Count this employee plus all of their reports
            count += 1 + calculateNumberOfReports(directReport);
        }
        return count;
    }
}
