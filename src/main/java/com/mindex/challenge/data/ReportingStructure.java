package com.mindex.challenge.data;

/**
 * Reporting Structure represents the reporting structure for an employee, including the employee details
 * and total number of reports under them in the hierarchy.
 */
public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;
    
    /**
     * Constructs a new ReportingStructure with the specified employee and report count.
     *
     * @param employee The employee this structure refers to
     * @param numberOfReports The total number of distinct reports in the hierarchy
     * @throws IllegalArgumentException if employee is null or report count is negative
     */
    public ReportingStructure(Employee employee, int numberOfReports) {
        if (employee == null) {throw new IllegalArgumentException("Employee cannot be null");}
        if (numberOfReports < 0) {throw new IllegalArgumentException("Report count cannot be negative");}

        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    /**
     * Gets the employee this reporting structure refers to.
     *
     * @return The employee with complete reporting hierarchy
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Gets the total number of distinct reports under this employee.
     *
     * @return The count of all direct and indirect reports
     * @implNote Includes all levels of the reporting hierarchy
     */
    public int getNumberOfReports() {
        return numberOfReports;
    }
}
