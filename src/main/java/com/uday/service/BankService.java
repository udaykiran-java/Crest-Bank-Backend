package com.uday.service;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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
import com.uday.model.TransactionHistory;
import com.uday.repo.BankRepo;
import com.uday.utility.AccountNumberGenerator;

import jakarta.mail.internet.MimeMessage;

@Service
public class BankService {

    @Autowired
    private BankRepo repo;
    
    @Autowired
    private JavaMailSender mailSender;
    
   
    
    // Inject Twilio properties from application.properties
    
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    
    // Get current date and time
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    String currentDate = now.format(dateFormatter);
    String currentTime = now.format(timeFormatter);

    

    
    

    // Method to create a new bank account with email,PHno,adharNo validation
    
    public Bank createBankAccount(Bank bank) {
    	
        // Check if email already exists
    	
        if (repo.findByEmail(bank.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This Email already exists: " + bank.getEmail());
        }
        
       // Check if mobile number already exists
        
       if(repo.findByMobileNumber(bank.getMobileNumber()).isPresent())  {
    	   throw new IllegalArgumentException("This Mobile Number is already exists: " + bank.getMobileNumber());
       }
       
       // Check if mobile number already exists
       
       if(repo.findByAdharnumber(bank.getAdharnumber()).isPresent()) {
    	   throw new IllegalArgumentException("This Adhar Number is already exists: " + bank.getAdharnumber());
       }
       
       Bank savedBank = repo.save(bank);
       
       // Send email after created account successful 
       
       try {
           SimpleMailMessage message = new SimpleMailMessage();
           message.setTo(bank.getEmail());
           message.setSubject("Account Created Successfully");

           // creating of the email
           String emailBody = "Dear " + savedBank.getName() + ",\n\n" +
                              "Your account has been successfully created!\n\n" +
                              "Account Holder Name: " + savedBank.getName() + "\n" +
                              "Account Number: " + savedBank.getAccountNumber() + "\n" +
                              "Account type:"+savedBank.getAccountType()+"\n"+
                              "IFSC Code: " + savedBank.getIfsc() + "\n" +
                              "Branch: " + savedBank.getBranch() + "\n" +
                              "District: " + savedBank.getDistrict() + "\n" +
                              "State: " + savedBank.getState() + "\n" +
                              "Mobile Number: " + savedBank.getMobileNumber() + "\n" +
                              "Address: " + savedBank.getAddress() + "\n" +
                              "Father Name: " + savedBank.getFatherName() + "\n" +
                              "Pin Code: " + savedBank.getPinCode() + "\n" +
                              "Please keep this information secure.\n\n" +
                              "Best regards,\n" +
                              "USA Bank";

           message.setText(emailBody);
           mailSender.send(message);  // Send email
           
           // Check if an ATM was created and  send the ATM details also
           
           if ("yes".equals(savedBank.getNeedsAtm()) && savedBank.getAtmNumber() != 0) {
               Bank atmDetails = savedBank;
              String emailForATM = "\nATM Card Details:\n" +
                            "ATM Card Number: " + atmDetails.getAtmNumber() + "\n" +
                            "ATM PIN: " + atmDetails.getPin() + "\n" + 
                            "Please keep this information secure.\n\n" +
                            "Best regards,\n" +
                            "USA Bank";
              
              message.setText(emailForATM);
              mailSender.send(message);  // Send email
           }
           
       } catch (Exception e) {
    	   
           //  throw exception if email sending fails
           throw new RuntimeException("Error sending email: " + e.getMessage());
       }

       
        
        return savedBank;
    }
    
    
    
    // Method to get One Account Holder Details
    
    public Bank getOneHolder(long acno) {
    	
    	return repo.findById(acno).get();
    }
    
    
    
    // Method to get All Account Holders Details
    
    public List<Bank> getAllAccountHolders() {
    	
    	return repo.findAll();
    }
    
    
    
    
    
    // method for withdraw amount 
    
      public String withdraw(Bank bank) {
    	
        // Check if the account exists or not
    	  
        if (repo.findById(bank.getAccountNumber()).isPresent()) {
            
            Bank holder = repo.findById(bank.getAccountNumber()).get();
            
            // Check  the withdrawal amount is greater than or equal to 500
            
            if (bank.getAmount() >= 500) {
            	
                // Check the account has enough balance
            	
                if (holder.getAmount() >= bank.getAmount()) {
                    
                    // Deduct the withdrawal amount from the balance
                	
                    holder.setAmount(holder.getAmount() - bank.getAmount());
                    
                    
                 // Create a new withdrawal transaction entry
                    TransactionHistory transactionDetail = new TransactionHistory();
                    transactionDetail.setTransactionType("Debited");
                    transactionDetail.setAmount(bank.getAmount());  
                    transactionDetail.getTransactionDate();
                    transactionDetail.setAvailableBalance(holder.getAmount());  
                    transactionDetail.setDescription("Credited Rs. " + bank.getAmount());
                    
                    // Add transaction to the holder's transaction history
                    holder.addTransaction(transactionDetail);  

                    
                    // Save the updated account information
                    repo.save(holder);
                 
                    
                    // Initialize Twilio with your Account SID and Auth Token
                    Twilio.init(accountSid, authToken);
                    
                    // Create and send the SMS message using Twilio
                    
                    try {
                        // Mask the account number (last 4 digits)
                    	
                        String maskedAccountNumber = "A/c *" + String.valueOf(holder.getAccountNumber()).substring(String.valueOf(holder.getAccountNumber()).length() - 4);
                        
                        // Get current date and time
                        LocalDate currentDate = LocalDate.now();
                        LocalTime currentTime = LocalTime.now();
                        
                        // Format date and time for the message
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        
                        String formattedDate = currentDate.format(dateFormatter);
                        String formattedTime = currentTime.format(timeFormatter);
                        
                        // Create and send the SMS message
                        Message message = Message.creator(
                        		
                        		new PhoneNumber("+919963446813"),
                        		
                        		//new PhoneNumber("+91" + String.valueOf(holder.getMobileNumber())),
                                new PhoneNumber(fromPhoneNumber),  
                                maskedAccountNumber + " Debited for Rs:" + bank.getAmount() +
                                " on " + formattedDate + " " + formattedTime +
                                " Avl Bal Rs:" + holder.getAmount() +
                                ". If not you, Call " + fromPhoneNumber + " - USA Bank"
                        ).create();
                        
                    } catch (ApiException e) {
                       
                    	
                    	
                        return "Withdrawal successful, but we couldn't send you an SMS notification."+e.getMessage();
                    }
                    
                    
                    return "Amount withdrawal is completed. Your Balance is: " + holder.getAmount();
                    
                } 
                else {
                	
                    
                    return "Insufficient balance for the withdrawal. Available balance: " + holder.getAmount();
                }
            } 
            else {
                
                return "Your withdrawal amount must be above Rs. 500";
            }
        } 
        else {
           
            return "This account number does not exist: " + bank.getAccountNumber();
        }
    }
    
    
    
    
    // Method for Deposit amount 

    public String deposit(Bank bank) {
    	
    	 // Check if the account exists in the Bank Holder Accounts
        if (repo.findById(bank.getAccountNumber()).isPresent()) {
            
            // Check if the Deposit amount is greater than or equal to 500
            if (bank.getAmount() >= 500) {
                
                Bank holder = repo.findById(bank.getAccountNumber()).get();
                
                // Deduct the Deposit amount from the balance
                holder.setAmount(holder.getAmount() + bank.getAmount());
                
                // Create a TransactionHistory object for the deposit transaction
                
                TransactionHistory transaction = new TransactionHistory();
                transaction.setTransactionType("Credit");
                transaction.setAmount(bank.getAmount());
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setAvailableBalance(holder.getAmount());
                transaction.setDescription("Credited Rs. " + bank.getAmount());

                // Add the transaction to the holder's transaction list
                holder.addTransaction(transaction);
                
                // Save the updated account information
                repo.save(holder);
                
             // Initialize Twilio with your Account SID and Auth Token
                Twilio.init(accountSid, authToken);
                
             // Create and send the SMS message using Twilio
                try {
                    // Mask the account number (last 4 digits)
                    String maskedAccountNumber = "A/c *" + String.valueOf(holder.getAccountNumber()).substring(String.valueOf(holder.getAccountNumber()).length() - 4);
                    
                   
                    LocalDate currentDate = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();
                    
                    // Format date and time for the message
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    
                    String formattedDate = currentDate.format(dateFormatter);
                    String formattedTime = currentTime.format(timeFormatter);
                    
                    // Create and send the SMS message
                    Message message = Message.creator(
                    		
                    		new PhoneNumber("+919963446813"),
                    		//new PhoneNumber("+91" + String.valueOf(holder.getMobileNumber())),
                            new PhoneNumber(fromPhoneNumber), 
                            maskedAccountNumber + " Credited for Rs:" + bank.getAmount() +
                            " on " + formattedDate + " " + formattedTime +
                            " Avl Bal Rs:" + holder.getAmount() +
                            ". If not you, Call " + fromPhoneNumber + " - USA Bank"
                    ).create();
                    
                }
                catch (ApiException e) {
                   
                	
                	
                    return "Deposited successful, but we couldn't send you an SMS notification."+e.getMessage();
                    
                }
                
                
                return "Amount Deposit is completed. Your Balance is: " + holder.getAmount();
            
            }
            else {
            	
                return "Your Deposit amount must be above Rs. 500";
            }
            
        }
        else {
        	
        	return "This account number does not exist: " + bank.getAccountNumber();
        }
    }
    
    
 // Balance Enquire
    
    public String balanceEnquire(CheckBalance bal) {
    	
        // Check if the account number exists in the repository
        Optional<Bank> holderOptional = repo.findById(bal.getAccountNumber());

        if (holderOptional.isPresent()) {
            Bank holder = holderOptional.get();

            // Validate the mobile number
            if (holder.getMobileNumber() == bal.getMobileNumber()) {
            	
                
                return "The available balance is Rs. " + holder.getAmount();
            }
            else {
                return "The provided mobile number does not match the registered mobile number.";
            }
        }
        else {
            return "The account number does not exist.";
        }
    }
    
    
// Method for transfer money 
    
    public String transferMoney(TransferAmount transfer) {
        
        // Check if 'from' account exists
        if (repo.findById(transfer.getFromAccountNumber()).isPresent()) {
            
            // Check if 'to' account exists
            if (repo.findById(transfer.getToAccountNumber()).isPresent()) {
                
                Bank from = repo.findById(transfer.getFromAccountNumber()).get();
                
                // Validate sender's name and mobile number
                if (from.getName().equals(transfer.getName())) {
                    
                    if (from.getMobileNumber()==(transfer.getMobileNumber())) {  
                        
                        Bank to = repo.findById(transfer.getToAccountNumber()).get();
                        
                        
                        if (from.getAmount() >= transfer.getAmount()) {
                            
                            
                            from.setAmount(from.getAmount() - transfer.getAmount());  
                            to.setAmount(to.getAmount() + transfer.getAmount());  
                            
                            
                         // Create a TransactionHistory object for the sender (from) account
                            TransactionHistory fromTransaction = new TransactionHistory();
                            fromTransaction.setTransactionType("Transfer");
                            fromTransaction.setAmount(transfer.getAmount());
                            fromTransaction.setTransactionDate(LocalDateTime.now());
                            fromTransaction.setAvailableBalance(from.getAmount());
                            fromTransaction.setDescription("Transferred Rs. " + transfer.getAmount() + " to Account " + transfer.getToAccountNumber());

                            // Add the transaction to the sender's transaction history
                            from.addTransaction(fromTransaction);

                            // Create a TransactionHistory object for the receiver (to) account
                            TransactionHistory toTransaction = new TransactionHistory();
                            toTransaction.setTransactionType("Credit");
                            toTransaction.setAmount(transfer.getAmount());
                            toTransaction.setTransactionDate(LocalDateTime.now());
                            toTransaction.setAvailableBalance(to.getAmount());
                            toTransaction.setDescription("Credited Rs. " + transfer.getAmount() + " from Account " + transfer.getFromAccountNumber());

                            // Add the transaction to the receiver's transaction history
                            to.addTransaction(toTransaction);
                            
                            // Save the updated accounts
                            repo.save(from);
                            repo.save(to);
                            
                         // Initialize Twilio with your Account SID and Auth Token
                            Twilio.init(accountSid, authToken);
                            
                            // Send SMS notification to both the sender and receiver
                            sendSmsNotification(from, transfer.getAmount(), "debited");
                            sendSmsNotification(to, transfer.getAmount(), "credited");
                            
                            return "Your transaction is successfully completed.";
                            
                        } 
                        else {
                            return "Insufficient balance in the sender's account.";
                        }
                        
                    } 
                    else {
                        return "You entered the wrong mobile number. Please check your registered mobile number.";
                    }
                    
                } 
                else {
                    return "Your name does not match the account holder name. Please enter the valid name.";
                }
                
            }
            else {
                return "The target account number does not exist in our bank.";
            }
            
        }
        else {
            return "Please check your account number. It does not exist in our bank.";
        }
    }

    // Helper method to send SMS notifications 
    
    private void sendSmsNotification(Bank accountHolder, double amount, String transactionType) {
    	
        try {
        	
            // Mask the account number (last 4 digits)
            String maskedAccountNumber = "A/c *" + String.valueOf(accountHolder.getAccountNumber()).substring(String.valueOf(accountHolder.getAccountNumber()).length() - 4);
            
            
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            String formattedDate = currentDate.format(dateFormatter);
            String formattedTime = currentTime.format(timeFormatter);
            
            // Create and send the SMS message using Twilio
            Message message = Message.creator(
            		
            		new PhoneNumber("+919963446813"),
                    //new PhoneNumber("+91" + accountHolder.getMobileNumber()),
                    new PhoneNumber(fromPhoneNumber), 
                    maskedAccountNumber + " " + transactionType + " for Rs: " + amount +
                    " on " + formattedDate + " " + formattedTime +
                    " Avl Bal Rs: " + accountHolder.getAmount() +
                    ". If not you, Call " + fromPhoneNumber + " - USA Bank"
            ).create();
            
        }
        catch (ApiException e) {
        	
            
        }
    }
    
    
    
 // Method to generate transaction statement PDF and send via email
    
    public void transactionStatement(Statement statement) throws IOException {
    	
        // Fetch bank details and transaction history
        Bank bank = getBankDetails(statement);
        List<TransactionHistory> transactions = bank.getTransactionHistory();
        String recipientEmail = bank.getEmail(); 

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
        		
             Document document = new Document(pdfDoc)) {

            pdfDoc.setDefaultPageSize(PageSize.A4); // Set the page size

            // Add logo and heading
            addLogoAndHeading(document);

            // Add account details section
            addAccountDetails(document, bank);

            // Add transactions table
            addTransactionsTable(document, transactions);

            // Close the document
            document.close();

            // Send the generated PDF as an email attachment
            sendEmailWithAttachment(baos.toByteArray(), recipientEmail);
        } catch (Exception e) {
            System.err.println("Error generating transaction statement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addLogoAndHeading(Document document) {
    	
        String logoPath = "C:\\Users\\UDAY KIRAN\\Pictures\\bank\\logo.png";

        try {
            // Create the logo image
            Image logo = new Image(ImageDataFactory.create(logoPath))
                    .setWidth(100)
                    .setHeight(50)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Create a table for logo and heading
            
            float[] columnWidths = {1, 3};
            Table logoAndHeadingTable = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100));

            // Add logo
            logoAndHeadingTable.addCell(new Cell()
                    .add(logo)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    
                    .setHeight(150)
                    .setWidth(100)
            		); 

            // Add heading
            logoAndHeadingTable.addCell(new Cell()
                    .add(new Paragraph("Details of Statement")
                            .setFontSize(16)
                            .setBold()
                            .setTextAlignment(TextAlignment.LEFT))
                    .setFontColor(new DeviceRgb(0, 0, 139)) 
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10)
        
            		); 
            
            // Add the table to the document
            document.add(logoAndHeadingTable);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            
        }
    }

    private void addAccountDetails(Document document, Bank bank) {
    	
        // Create a table for account details
        Table detailsTable = new Table(1);

        // Add account holder name
        detailsTable.addCell(createStyledCell("Account Holder  : " + bank.getName())
        		.setBackgroundColor(new DeviceRgb(35, 101, 139))
        		.setFontColor(new DeviceRgb(255, 255, 255))
        		);

        // Add account number
        detailsTable.addCell(createStyledCell("Account Number  : " + bank.getAccountNumber())
        		.setBackgroundColor(new DeviceRgb(35, 101, 139))
        		.setFontColor(new DeviceRgb(255, 255, 255))
        		);

        // Add email
        detailsTable.addCell(createStyledCell("Email  : " + bank.getEmail())
        		.setBackgroundColor(new DeviceRgb(35, 101, 139))
        		.setFontColor(new DeviceRgb(255, 255, 255))
        		);

        // Add the table to the document
        document.add(detailsTable);
    }

    
    private Cell createStyledCell(String content) {
        return new Cell()
                .add(new Paragraph(content)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT))
                .setPadding(5)
                .setBorder(new SolidBorder(1));
    }

    private void addTransactionsTable(Document document, List<TransactionHistory> transactions) {
    	
        // Define column widths for the table
        float[] columnWidths = {1, 2, 2, 1, 2, 3};
        Table table = new Table(columnWidths);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        
        
        // Add table headers
        String[] headers = {"S.No", "Date","Transaction ID", "Transaction Type", "Amount", "Available Balance"};
        for (String header : headers) {
            table.addCell(new Cell()
                    .add(new Paragraph(header))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb( 35, 101, 139)) 
                    .setFontColor(new DeviceRgb(255, 255, 255)));
        }

        // Add transaction rows
        int serialNumber = 1;
        for (TransactionHistory transaction : transactions) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(serialNumber++)).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph(
                    transaction.getTransactionDate() != null ? transaction.getTransactionDate().toString() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(" " + transaction.getTransactionId())));
            table.addCell(new Cell().add(new Paragraph(transaction.getTransactionType() != null ? transaction.getTransactionType() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(" " + transaction.getAmount())));
            table.addCell(new Cell().add(new Paragraph("Rs. " + transaction.getAvailableBalance())));
            
        }

        // Add the table to the document
        document.add(table);
    }


    // Method to send the email with PDF attachment
    
    private void sendEmailWithAttachment(byte[] pdfBytes, String recipientEmail) {
        try {
            // Create a MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set email properties
            helper.setTo(recipientEmail);
            helper.setSubject("Your Transaction Statement");
            helper.setText("Dear Customer,\n\nPlease find attached your transaction statement in PDF format.\n\nBest regards,\nYour Bank");

            // Attach the PDF
            helper.addAttachment("Transaction_Statement.pdf", new ByteArrayResource(pdfBytes));

            // Send the email
            mailSender.send(mimeMessage);

            System.out.println("Transaction statement email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    // Placeholder method for fetching bank details
    
    public Bank getBankDetails(Statement statement) {
        Optional<Bank> optionalBank = repo.findById(statement.getAccountNumber());
        if (optionalBank.isPresent()) {
            return optionalBank.get();  
        } else {
            throw new IllegalArgumentException("Account with number " + statement.getAccountNumber() + " not found.");
        }
    }
    
    
    
 // Method to generate a Mini Statement PDF and send via email
    
    public void generateMiniStatement(Statement statement) throws IOException {
    	
        // Fetch bank details and transaction history
        Bank bank = getBankDetails(statement);
        List<TransactionHistory> transactions = bank.getTransactionHistory();
        String recipientEmail = bank.getEmail(); 

        // Fetch last 5 transactions or fewer
        List<TransactionHistory> miniTransactions = getMiniTransactions(transactions);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            pdfDoc.setDefaultPageSize(PageSize.A4); 

            // Add logo and heading
            addMiniStatementLogoAndHeading(document);

            // Add account details section
            addAccountDetails(document, bank);

            // Add mini transactions table
            addMiniTransactionsTable(document, miniTransactions);

            // Close the document
            document.close();

            // Send the generated PDF as an email attachment
            ministatementMail(baos.toByteArray(), recipientEmail);
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }

    // Fetch last 5 transactions or fewer
    private List<TransactionHistory> getMiniTransactions(List<TransactionHistory> transactions) {
        int transactionCount = transactions.size();
        return transactionCount <= 5 ? transactions : transactions.subList(transactionCount - 5, transactionCount);
    }

    
    // Add a logo and heading specific to the Mini Statement
    
    private void addMiniStatementLogoAndHeading(Document document) {
    	
        String logoPath = "C:\\Users\\UDAY KIRAN\\Pictures\\bank\\logo.png";

        try {
            // Create the logo image
            Image logo = new Image(ImageDataFactory.create(logoPath))
                    .setWidth(100)
                    .setHeight(50)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Create a table for logo and heading
            float[] columnWidths = {1, 3};
            Table logoAndHeadingTable = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100));

            // Add logo
            logoAndHeadingTable.addCell(new Cell()
                    .add(logo)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            // Add heading
            logoAndHeadingTable.addCell(new Cell()
                    .add(new Paragraph("Mini Transaction Statement")
                            .setFontSize(16)
                            .setBold()
                            .setTextAlignment(TextAlignment.LEFT))
                    .setFontColor(new DeviceRgb(0, 0, 139)) // Dark blue color
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10));

            // Add the table to the document
            document.add(logoAndHeadingTable);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: Unable to load the logo image from the specified path.");
        }
    }

    // Add a transactions table for Mini Statement
    
    private void addMiniTransactionsTable(Document document, List<TransactionHistory> transactions) {
    	
        // Define column widths for the table
        float[] columnWidths = {1, 2, 2, 1, 2, 3};
        Table table = new Table(columnWidths);

        document.add(new Paragraph("\n"));

        // Add table headers
        String[] headers = {"S.No", "Date","Transaction ID", "Transaction Type", "Amount", "Available Balance"};
        for (String header : headers) {
            table.addCell(new Cell()
                    .add(new Paragraph(header))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(35, 101, 139)) 
                    .setFontColor(new DeviceRgb(255, 255, 255))); 
        }

        // Add transaction rows
        int serialNumber = 1;
        for (TransactionHistory transaction : transactions) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(serialNumber++)).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph(
                    transaction.getTransactionDate() != null ? transaction.getTransactionDate().toString() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(" " + transaction.getTransactionId())));
            table.addCell(new Cell().add(new Paragraph(transaction.getTransactionType() != null ? transaction.getTransactionType() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(" " + transaction.getAmount())));
            table.addCell(new Cell().add(new Paragraph("Rs. " + transaction.getAvailableBalance())));
           
        }

        // Add the table to the document
        document.add(table);
    }

    
    // Method to send the email with PDF attachment
    private void ministatementMail(byte[] pdfBytes, String recipientEmail) {
        try {
            // Create a MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set email properties
            helper.setTo(recipientEmail);
            helper.setSubject("Your Mini Transaction Statement");
            helper.setText("Dear Customer,\n\nPlease find attached your mini transaction statement in PDF format.\n\nBest regards,\nYour Crest Bank");

            // Attach the PDF
            helper.addAttachment("Mini_Transaction_Statement.pdf", new ByteArrayResource(pdfBytes));

            // Send the email
            mailSender.send(mimeMessage);

            System.out.println("Mini transaction statement email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

   

    
    
 // Method to update account details
    
    public Bank updateAccount(UpdateAccount account) {
        
        // Verify that the account exists
        Optional<Bank> holder = repo.findById(account.getAccountNumber());
        if (!holder.isPresent()) {
            throw new IllegalArgumentException("Account number " + account.getAccountNumber() + " does not exist.");
        }

        // Verify that the email exists and matches
        if (!repo.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email " + account.getEmail() + " does not exist. Please provide the correct email.");
        }

        // Verify that the Aadhaar number exists and matches
        if (!repo.findByAdharnumber(account.getAdharnumber()).isPresent()) {
            throw new IllegalArgumentException("Aadhaar number " + account.getAdharnumber() + " does not exist. Please provide the correct Aadhaar number.");
        }

        // Update the account details
        Bank person = holder.get();
        person.setName(account.getName());
        person.setFatherName(account.getFatherName());
        person.setAge(account.getAge());
        person.setEmail(account.getEmail());
        person.setAddress(account.getAddress());
        person.setDateOfBirth(account.getDateOfBirth());
        person.setDistrict(account.getDistrict());
        person.setState(account.getState());
        person.setMobileNumber(account.getMobileNumber());

        Bank savedBank = repo.save(person); 

        // Send email to confirm the update
        
        try {
        	
            // Initialize the email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(account.getEmail());
            message.setSubject("Your Account Has Been Updated Successfully");

            // Build the primary email body for account update confirmation
            String emailBody = "Dear " + savedBank.getName() + ",\n\n" +
                    "Your account details have been successfully updated.\n\n" +
                    "Updated Account Information:\n" +
                    "Account Holder Name: " + savedBank.getName() + "\n" +
                    "Account Number: " + savedBank.getAccountNumber() + "\n" +
                    "IFSC Code: " + savedBank.getIfsc() + "\n" +
                    "Branch: " + savedBank.getBranch() + "\n" +
                    "District: " + savedBank.getDistrict() + "\n" +
                    "State: " + savedBank.getState() + "\n" +
                    "Mobile Number: " + savedBank.getMobileNumber() + "\n" +
                    "Address: " + savedBank.getAddress() + "\n" +
                    "Father's Name: " + savedBank.getFatherName() + "\n" +
                    "Pin Code: " + savedBank.getPinCode() + "\n" +
                    "Please keep this information secure.\n\n" +
                    "Best regards,\n" +
                    "USA Bank";

            message.setText(emailBody);
            mailSender.send(message); 

            // If an ATM card was created, send ATM details
            
            if ("yes".equals(savedBank.getNeedsAtm()) && savedBank.getAtmNumber() != 0) {
            	
                // Create ATM details email content
            	
                String emailForATM = "Dear " + savedBank.getName() + ",\n\n" +
                        "Your ATM card has been successfully created.\n\n" +
                        "ATM Card Details:\n" +
                        "ATM Card Number: " + savedBank.getAtmNumber() + "\n" +
                        "ATM PIN: Not set (please set your PIN at the ATM machine)\n" +
                        "Please keep this information secure.\n\n" +
                        "Best regards,\n" +
                        "USA Bank";

                // Set new email content and send ATM details
                message.setText(emailForATM);
                mailSender.send(message);
            }

        } catch (Exception e) {
            
            throw new RuntimeException("Account updated, but there was an error sending the confirmation email.");
        }

        return savedBank; 
    }
    
    

                                  /* ------------> ATM Implementation <-------------- */
    
    
 // Method to apply for an ATM card
    
    public Bank applyForAtm(ApplyATM app) {
        
        // Retrieve the bank account based on account number
        Optional<Bank> optionalBank = repo.findById(app.getAccountNumber());
        
        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();

            // Validate the provided name and mobile number to ensure account security
            if (!bank.getName().equals(app.getName()) || bank.getMobileNumber() != app.getMobileNumber()) {
                throw new IllegalArgumentException("Account details do not match. Please verify the account information.");
            }

            // Check if ATM card has not been issued previously
            if (bank.getAtmNumber() == 0) {  
                
                bank.setNeedsAtm("yes");
                bank.setAtmNumber(AccountNumberGenerator.atmNumberGenarater());

                Bank savedBank = repo.save(bank);  
                
                // Send email notification about the new ATM card
                
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(bank.getEmail());
                    message.setSubject("Your ATM Card is Created Successfully");

                    // Construct the email content
                    String emailForATM = "Dear " + bank.getName() + ",\n\n" +
                                         "Congratulations! Your ATM card has been successfully created.\n\n" +
                                         "ATM Card Details:\n" +
                                         "ATM Card Number: " + savedBank.getAtmNumber() + "\n" +
                                         "ATM PIN: Not set (please set your PIN at the ATM machine)\n" +
                                         "Please keep this information secure.\n\n" +
                                         "Best regards,\n" +
                                         "USA Bank";

                    message.setText(emailForATM);
                    mailSender.send(message);  

                } catch (Exception e) {
                    
                    throw new RuntimeException("ATM created successfully, but there was an error sending the email notification.");
                }

                return savedBank;
            } 
            else {

            	throw new IllegalStateException("An ATM card already exists for this account.");
            }
        } 
        else {

        	throw new IllegalArgumentException("Account with number " + app.getAccountNumber() + " does not exist.");
        }
    }

    
    
    // Method to cancel ATM card
    
    public Bank cancelAtm(CancleATM cancle) {
        Optional<Bank> optionalBank = repo.findById(cancle.getAccountNumber());

        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();

            // Validate account number, name, and mobile number
            if (!bank.getName().equals(cancle.getName()) || bank.getMobileNumber() != cancle.getMobileNumber()) {
                throw new IllegalArgumentException("Account details do not match. Please verify the account information.");
            }

            if (bank.getAtmNumber() != 0) { 
                bank.setAtmNumber(0);;
                bank.setNeedsAtm("no");

                return repo.save(bank);  
            }
            else {
                throw new IllegalStateException("No ATM assigned to this account to cancel.");
            }
        }
        else {
            throw new IllegalArgumentException("Account with number " + cancle.getAccountNumber() + " does not exist.");
        }
    }
      
    
    
 // Temporary storage for OTPs
    
    private final Map<Long, Integer> otpStorage = new ConcurrentHashMap<>(); 

    
    
 // Method to send OTP
    
 public String sendOtp(long atmNumber) {
	 
     Optional<Bank> optionalBank = repo.findByAtmNumber(atmNumber);

     if (optionalBank.isPresent()) {
         Bank bank = optionalBank.get();

         // Generate and send OTP
         int otp = generateAndSendOtp(bank.getMobileNumber());
         
         // Store the OTP temporarily
         otpStorage.put(atmNumber, otp);
         return "OTP sent successfully to the registered mobile number.";
     } else {
         throw new IllegalArgumentException("ATM number not found.");
     }
 }

 
 // method for set pin
 
    public Bank setPin(SetAtmPin atm) {
    	
	    // Retrieve the bank account based on the provided ATM number
	    Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());

	    if (optionalBank.isPresent()) {
	        Bank bank = optionalBank.get();

	       

	        // Validate OTP
	        int storedOtp = otpStorage.get(atm.getAtmNumber());

	        if (storedOtp == 0) {
	          
	            throw new IllegalArgumentException("OTP not found or expired. Please request a new OTP.");
	        }

	        // Compare the OTP values
	        if (storedOtp != atm.getOtp()) {
	            
	            throw new IllegalArgumentException("Invalid OTP. Please try again.");
	        }

	        // If OTP is valid, validate the PIN
	        if (atm.getPin() < 1000 || atm.getPin() > 9999) {
	            throw new IllegalArgumentException("PIN must be a 4-digit number.");
	        }

	        
	        // Set the new PIN for the bank account
	        bank.setPin(atm.getPin());

	        // Remove OTP from storage after successful validation
	        otpStorage.remove(atm.getAtmNumber());


	        // Save and return the updated bank account
	        return repo.save(bank);
	    } else {
	        throw new IllegalArgumentException("ATM number not found.");
	    }
	}



 // Helper method to generate and send OTP
    
 private int generateAndSendOtp(long mobileNumber) {
	 
	// Generate 4-digit OTP
     int otp = 1000 + new Random().nextInt(9999); 
     Twilio.init(accountSid, authToken);
     try {
         Message.creator(
                 new PhoneNumber("+91" + mobileNumber),
                 new PhoneNumber(fromPhoneNumber),
                 "Your OTP for setting ATM PIN is: " + otp + ". Please use it within 5 minutes."
         ).create();
         System.out.println("OTP sent to: " + mobileNumber);
     } catch (Exception e) {
         System.err.println("Error sending OTP: " + e.getMessage());
         throw new IllegalStateException("Failed to send OTP. Please try again.");
     }
     return otp;
 }

                                /* ----------> Transactions By Using ATM Card <------------ */
    
    
 // Method for withdrawal using ATM card
 
    public String withdrawUsingATM(AtmTransactions atm) {
        
        // Retrieve the bank account using the ATM number
        Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());

        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();

            // Validate the PIN entered by the user
            if (bank.getPin() == atm.getPin()) {

                // Check if the account has sufficient balance for the withdrawal
                if (bank.getAmount() >= atm.getAmount()) {

                    // Create a new Bank object with the withdrawal amount
                    Bank withdrawalBank = new Bank();
                    withdrawalBank.setAccountNumber(bank.getAccountNumber());
                    withdrawalBank.setAmount(atm.getAmount()); 
                    

                    return withdraw(withdrawalBank);
                } else {

                	return "Insufficient balance for the withdrawal. Available balance: " + bank.getAmount();
                }
            } else {

            	return "Invalid PIN. Please try again.";
            }
        } 
        else {

        	return "Account with the given ATM number does not exist.";
        }
    }


    
    
 // Method for depositing amount using ATM card
    
    public String depositUsingATM(AtmTransactions atm) {
    	
        // Retrieve the bank account using the ATM number
    	 Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());

        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();

            // Validate the PIN entered by the user
            if ((bank.getPin() == atm.getPin())&&(atm.getPin() > 1000 || atm.getPin() < 9999)) { 
            	
                // Create a new Bank object with the deposit amount
            	
                Bank depositBank = new Bank();
                depositBank.setAccountNumber(bank.getAccountNumber());
                depositBank.setAmount(atm.getAmount()); 
                
                // Call the deposit method and return the message
                return deposit(depositBank); 
            }
            else {
                return "Invalid PIN. Please try again.";
            }
        } 
        else {
            return "Account with the given ATM number does not exist.";
        }
    }
    
 // Generate Mini Statement using ATM credentials
    
    public void miniStatementByATM(MiniStatementATM atm) throws IOException {
    	
        // Validate ATM number
        Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());
        if (!optionalBank.isPresent()) {
            throw new IllegalArgumentException("ATM number not found.");
        }

        Bank bank = optionalBank.get();

        // Validate PIN
        if ((bank.getPin() != atm.getPin())&&(atm.getPin() < 1000 || atm.getPin() > 9999)) {
            throw new IllegalArgumentException("Invalid PIN. Please try again.");
        }

        // Prepare the statement object
        Statement miniStatement = new Statement();
        miniStatement.setAccountNumber(bank.getAccountNumber()); 

        // Generate the mini statement
        generateMiniStatement(miniStatement);
    }

    
    // Method for Balance Enquire By Using ATM
    
     public String balanceEnquireByATM(BalanceEnquireATM atm) {
    	
    	 Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());
         if (!optionalBank.isPresent()) {
             throw new IllegalArgumentException("ATM number not found.");
         }

         Bank bank = optionalBank.get();

         // Validate PIN
         if ((bank.getPin() != atm.getPin())&&(atm.getPin() < 1000 || atm.getPin() > 9999)) {
             throw new IllegalArgumentException("Invalid PIN. Please try again.");
         }

         // Prepare the statement object
         CheckBalance check = new CheckBalance();
         check.setAccountNumber(bank.getAccountNumber()); 
         check.setMobileNumber(bank.getMobileNumber());

         // Generate the mini statement
         return balanceEnquire(check);
    }

     
     
  // Method for Transfer amount using ATM card
     
     public String transferUsingATM(TransferByATM atm) {
     	
         // Retrieve the bank account using the ATM number
     	 Optional<Bank> optionalBank = repo.findByAtmNumber(atm.getAtmNumber());

         if (optionalBank.isPresent()) {
             Bank bank = optionalBank.get();

             // Validate the PIN entered by the user
             if ((bank.getPin() == atm.getPin())&&(atm.getPin() > 1000 || atm.getPin() < 9999)) { 
             	
            	 
                 TransferAmount transfer = new TransferAmount();
                 transfer.setFromAccountNumber(bank.getAccountNumber());
                 transfer.setName(bank.getName());
                 transfer.setMobileNumber(bank.getMobileNumber());
                 transfer.setToAccountNumber(atm.getToAccountNumber());
                 transfer.setAmount(atm.getAmount()); // Set the amount to transfer
                 
                 // Call the Transfer  method and return the message
                 
                 return transferMoney(transfer); 
             }
             else {
                 return "Invalid PIN. Please try again.";
             }
         } 
         else {
             return "Account with the given ATM number does not exist.";
         }
     }


}