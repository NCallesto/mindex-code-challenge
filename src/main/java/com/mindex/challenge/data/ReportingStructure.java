package com.mindex.challenge.data;

/**
 * Reporting Structure represents the reporting structure for an employee, including the employee details
 * and total number of reports under them in the hierarchy.
 */
public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    /**
     * Default constructor.
     */
    public ReportingStructure() {}

    /**
     * Constructs a ReportingStructure with the given employee and report count.
     *
     * @param employee The employee for the reporting structure
     * @param numberOfReports The total number of reports under this employee
     */
    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }


    // Getters and Setters with JavaDoc

    /**
     * @return The employee the reporting structure refers to
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee The employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return The total number of reports under this employee
     */
    public int getNumberOfReports() {
        return numberOfReports;
    }

    /**
     * @param numberOfReports The number of reports to set
     */
    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
