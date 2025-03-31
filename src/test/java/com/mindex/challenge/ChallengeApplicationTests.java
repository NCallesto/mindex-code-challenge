package com.mindex.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest
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

    }

	@Test
    public void testCompensationCreateRead() {

    }

	@Test
    public void testDirectReportsNotNullForLeaves() {
		
	}
}
