package com.mindex.challenge;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String employeeUrl;
    private String reportingStructureUrl;
    private String compensationUrl;

    @Before
    public void setup() {
		// Set up each urls for the different endpoints
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

	@Test
    public void testReportingStructure() {
		// Test data - use John Lennon's ID from the sample employee database json
        String testEmployeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        
        // Create the reportingStructure
        ReportingStructure reportingStructure = restTemplate.getForEntity(
            reportingStructureUrl, 
            ReportingStructure.class, 
            testEmployeeId
        ).getBody();

        // Verify the response is not null and contains a non-null employee
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());

		// Verify the employee ID is the same as the test ID
        assertEquals(testEmployeeId, reportingStructure.getEmployee().getEmployeeId());

		// Verify John Lennon has 4 direct reports
        assertEquals(4, reportingStructure.getNumberOfReports());
    }

	@Test
    public void testCompensationCreateRead() {
		// Test data - use Paul McCartney's ID from the sample employee database json
        String testEmployeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";

		// Test data - create a compensation record for Paul McCartney 
		// with a salary of $100,000.00 and an effective date of today
        Compensation testCompensation = new Compensation();
        testCompensation.setSalary(100000.00);
        testCompensation.setEffectiveDate(new Date());

        // Create the compensation
        Compensation createdCompensation = restTemplate.postForEntity(
            compensationUrl,
            testCompensation,
            Compensation.class,
            testEmployeeId
        ).getBody();

        // Verify the response is not null
        assertNotNull(createdCompensation);

		// Verify the compensation employee ID is the same as the test ID
        assertEquals(testEmployeeId, createdCompensation.getEmployee().getEmployeeId());

		// Verify the salary and effective date are the same as the test data
        assertEquals(100000.00, createdCompensation.getSalary(), 0.001);
        assertNotNull(createdCompensation.getEffectiveDate());

        // Read the compensation
        Compensation readCompensation = restTemplate.getForEntity(
            compensationUrl,
            Compensation.class,
            testEmployeeId
        ).getBody();

        // Verify the response is not null
        assertNotNull(readCompensation);

		// Verify the compensation employee ID is the same as the read employee ID
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), 
                    readCompensation.getEmployee().getEmployeeId());

		// Verify the salary and effective date are the same as the created data
        assertEquals(createdCompensation.getSalary(), readCompensation.getSalary(), 0.001);
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }

	@Test
    public void testDirectReportsNotNullForLeaves() {
		// Test data - Pete Best is a leaf node
        String leafEmployeeId = "62c1084e-6e34-4630-93fd-9153afb65309";
        
        // Execute the request to get the employee
        Employee employee = restTemplate.getForEntity(
            employeeUrl + "/{id}", 
            Employee.class, 
            leafEmployeeId
        ).getBody();

        // Verify the directReports is null for leaf nodes
        assertNotNull(employee);
        assertNull(employee.getDirectReports());
	}
}
