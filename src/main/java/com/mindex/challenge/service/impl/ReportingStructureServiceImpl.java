package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Service implementation for generating employee reporting structures.
 * Uses EmployeeService to properly retrieve employees while keeping track of
 * service layer abstraction and business logic consistency.
 */
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    private final EmployeeService employeeService;

    /**
     * Constructs a new ReportingStructureServiceImpl with required dependencies.
     *
     * @param employeeService The employee service to use for employee retrieval
     */
    @Autowired
    public ReportingStructureServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * {@inheritDoc}
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Override
    public ReportingStructure getReportingStructure(String employeeId) throws EmployeeNotFoundException {
        LOG.debug("Generating reporting structure for employee id [{}]", employeeId);
        
        // Use employeeService.read() instead of repository directly
        Employee employee = employeeService.read(employeeId);
        Employee resolvedEmployee = resolveEmployeeHierarchy(employee);
        int numberOfReports = calculateTotalReports(resolvedEmployee);

        return new ReportingStructure(resolvedEmployee, numberOfReports);
    }

    /**
     * Recursively resolves the complete employee hierarchy starting from the given employee.
     * Uses EmployeeService to properly retrieve each employee in the hierarchy.
     *
     * @param employee The root employee to start resolution from
     * @return The fully resolved employee with complete direct reports hierarchy
     * @throws EmployeeNotFoundException if any employee in the hierarchy cannot be found
     * @implNote This method performs a depth-first traversal of the reporting structure
     */
    private Employee resolveEmployeeHierarchy(Employee employee) throws EmployeeNotFoundException {
        Employee resolved = new Employee();
        // Copy all of the basic fields
        resolved.setEmployeeId(employee.getEmployeeId());
        resolved.setFirstName(employee.getFirstName());
        resolved.setLastName(employee.getLastName());
        resolved.setPosition(employee.getPosition());
        resolved.setDepartment(employee.getDepartment());

        if (employee.getDirectReports() != null) {
            resolved.setDirectReports(
                employee.getDirectReports().stream()
                    .map(report -> {
                        try {
                            //  resolve each direct report using employeeService
                            return resolveEmployeeHierarchy(employeeService.read(report.getEmployeeId()));
                        } catch (EmployeeNotFoundException e) {
                            LOG.warn("Missing employee reference in reporting chain: {}", report.getEmployeeId());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList()
            );
        }
        return resolved;
    }

    /**
     * Calculates the total number of reports under an employee.
     *
     * @param employee The employee to calculate reports for
     * @return Total count of reports in the hierarchy
     * @implNote Uses a Set to ensure each employee is only counted once
     */
    private int calculateTotalReports(Employee employee) {
        Set<String> distinctReportIds = new HashSet<>();
        collectReportIds(employee, distinctReportIds);
        return distinctReportIds.size();
    }

    /**
     * Recursively collects all of the unique report IDs in the hierarchy.
     *
     * @param employee The current employee in the hierarchy
     * @param collectedIds Set containing all of the collected report IDs
     * @implNote Uses depth-first to go through the reporting structure
     */
    private void collectReportIds(Employee employee, Set<String> collectedIds) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return;
        }

        for (Employee report : employee.getDirectReports()) {
            if (!collectedIds.contains(report.getEmployeeId())) {
                collectedIds.add(report.getEmployeeId());
                collectReportIds(report, collectedIds);
            }
        }
    }
}