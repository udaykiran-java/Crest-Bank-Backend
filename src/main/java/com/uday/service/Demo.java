package com.uday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.model.Bank;
import com.uday.repo.BankRepo;

@Service
public class Demo {
	
	 @Autowired
	    private BankRepo repo; 
	
	
    // Method to get One Account Holder Details
    
    public Bank getOneHolder(long acno) {
    	
    	return repo.findById(acno).get();
    }
    
    
    
    // Method to get All Account Holders Details
    
    public List<Bank> getAllAccountHolders() {
    	
    	return repo.findAll();
    }
    

}
