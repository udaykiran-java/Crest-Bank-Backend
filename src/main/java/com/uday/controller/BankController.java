package com.uday.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uday.dto.ApplyATM;
import com.uday.dto.AtmTransactions;
import com.uday.dto.BalanceEnquireATM;
import com.uday.dto.CancleATM;
import com.uday.dto.CheckBalance;
import com.uday.dto.MiniStatementATM;
import com.uday.dto.SetAtmPin;
import com.uday.dto.Statement;
import com.uday.dto.TransferAmount;
import com.uday.dto.TransferByATM;
import com.uday.dto.UpdateAccount;
import com.uday.model.Bank;
import com.uday.service.BankService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class BankController {

	 @Autowired
	    private BankService bankService;

	    // EndPoint to create a new bank account
	 
	    @PostMapping("/register")
	    public ResponseEntity<?> createBankAccount(@RequestBody Bank bank) {
	        try {
	            Bank createdBank = bankService.createBankAccount(bank);
	            return ResponseEntity.ok(createdBank);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }
	    
	    // End Point to get one one account holder
	    
	    @GetMapping("/getone/{acno}")
	    public Bank getOneAccountHolder(@PathVariable long acno) {
	    	
	    	return bankService.getOneHolder(acno);
	    }
	    
	    // End Point to get one one account holder
	    
	    @GetMapping("/getall")
	    public List<Bank> getAllAccountHolders() {
	    	
	    	return bankService.getAllAccountHolders();
	    }
	    
	 // end point for withdraw amount
	    
	    @PostMapping("/withdraw")
	    public String withdraw(@RequestBody Bank bank) {
	    	return bankService.withdraw(bank);
	    }
	    
	 // end point for deposit amount
	    
	    @PostMapping("/deposit")
	    public String deposit(@RequestBody Bank bank) {
	    	return bankService.deposit(bank);
	    }
	    
	 // end point for check-balance
	    
	    @PostMapping("/checkbalance")
	    public String checkBalence(@RequestBody CheckBalance balance) {
	    	return bankService.balanceEnquire(balance);
	    }
	    
	    // end point for transfer amount 
	    
	    @PostMapping("/transfer")
	    public String transferAmount(@RequestBody TransferAmount transfer) {
	    	
	    	return bankService.transferMoney(transfer);
	    }
	    
	    // end point for get statement
	    
	    @PostMapping("/statement")
	    public void getStatement(@RequestBody Statement statement){
	    	 bankService.transactionStatement(statement);
	    }
	    
	    
	    // end point for get statement
	    
	    @PostMapping("/ministatement")
	    public void getMiniStatement(@RequestBody Statement statement){
	    	 bankService.generateMiniStatement(statement);
	    }
	    
	    // end point for update account 
	    
	    @PostMapping("/update")
	    public Bank updateAccount(@RequestBody UpdateAccount update) {
	    	return bankService.updateAccount(update);
	    }
	    
	    // end point for apply ATM
	    
	    @PostMapping("/applyatm")
	    public Bank applyAtm(@RequestBody ApplyATM apply) {
	    	
	    	return bankService.applyForAtm(apply);
	    	
	    }
	    
	    
	 // end point for Cancel ATM
	    
	    @PostMapping("/cancelatm")
	    public Bank cancelAtm(@RequestBody CancleATM apply) {
	    	
	    	return bankService.cancelAtm(apply);
	    	
	    }
	    
	    @PostMapping("/send-otp")
	    public ResponseEntity<String> sendOtp(@RequestBody SetAtmPin atmNumber) {
	        try {
	            String response = bankService.sendOtp(atmNumber.getAtmNumber());
	            return ResponseEntity.ok(response);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    @PostMapping("/set-pin")
	    public ResponseEntity<Bank> setPin(@RequestBody SetAtmPin setAtmPin) {
	        try {
	            Bank updatedBank = bankService.setPin(setAtmPin);
	            
	            return ResponseEntity.ok(updatedBank);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(null);
	        }
	    }
	 // end point for withdraw by using ATM
	    
	    @PostMapping("/withdrawATM")
	    public String withdrawByATM(@RequestBody AtmTransactions details) {
	    	
	    	return bankService.withdrawUsingATM(details);
	    }
	    
	 // end point for Deposit by using ATM
	    
	    @PostMapping("/depositATM")
	    public String depositByATM(@RequestBody AtmTransactions details) {
	    	
	    	return bankService.depositUsingATM(details);
	    }
	    
	    
	    
	  // end point for mini statement by using ATM
	    @PostMapping("/miniStatementATM")
	    public void  miniStatementByATM(@RequestBody MiniStatementATM mini) {
	    	//TODO: process POST request
	    	
	    	bankService.miniStatementByATM(mini);
	    	
	    }
	    
	    // end point for balance enquire with ATM
	    
	    @PostMapping("/balanceATM")
	    public String balanceEnquireATM(@RequestBody BalanceEnquireATM atm) {
	    	
	    	
	    	return bankService.balanceEnquireByATM(atm);
	    }
	    
	    // end point for transfer Amount by ATM 
	    
	    @PostMapping("/transferATM")
	    public String transferByATM(@RequestBody TransferByATM atm) {
	    	
	    	
	    	return bankService.transferUsingATM(atm);
	    }
	    
	    
}
