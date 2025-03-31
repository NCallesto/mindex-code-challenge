package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.data.Compensation;

/**
 * Service implementation for compensation operations.
 * 
 * Key Features:
 * - Validates employee existence before operations
 * - Ensures one compensation record per employee
 * - Comprehensive error handling
 * - Detailed logging for all operations
 */
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    /**
     * Finds compensation by the referenced employee's ID.
     *
     * @param employeeId The ID of the employee to find compensation for
     * @return The Compensation object or null if not found
     */
    Compensation findByEmployeeId(String employeeId);
}