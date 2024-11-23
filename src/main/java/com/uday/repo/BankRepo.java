package com.uday.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uday.model.Bank;

public interface BankRepo extends JpaRepository<Bank, Long> {
	
	// Custom method to find a Bank by email
    Optional<Bank> findByEmail(String email);
    
   // Custom method to find a Bank by mobile number
    Optional<Bank> findByMobileNumber(long mobileNumber);

   // Custom method to find a Bank by adhar number number
    Optional<Bank> findByAdharnumber(long adharnumber);
    
   // Custom method to find a bank by atmNumber
    Optional<Bank> findByAtmNumber(long atmNumber);
}
