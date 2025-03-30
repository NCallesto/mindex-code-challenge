package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.data.Compensation;

/**
 * MongoDB repository for Compensation entities.
 * Provides basic CRUD operations and custom query methods for Compensation data.
 */
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    /**
     * Finds compensation by employee ID.
     *
     * @param employeeId The ID of the employee to find compensation for
     * @return The Compensation object or null if not found
     */
    Compensation findByEmployeeId(String employeeId);
}