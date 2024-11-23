# Crest-Bank-Backend

**Description**


**Overview of Crest Bank's Banking System**

Crest Bank is a comprehensive banking solution designed to manage a wide array of financial services efficiently.
It provides users with functionalities such as account creation, ATM card management, balance inquiries, secure money transfers, and transaction history access.

This backend system integrates robust features for both customer account management and seamless ATM operations.
It ensures secure user access through PIN validation and OTP verification, safeguarding sensitive transactions.
Moreover, Crest Bank's system utilizes email and SMS notifications to keep customers updated on critical account activities, transfers, or security-related events.
Whether you're managing your personal finances or performing ATM transactions, Crest Bank ensures both convenience and security at every step.



**Features Overview**

1. Account Management:

Create/Update Account: Customers can create new accounts and update existing account details, such as personal information, address, and contact details.
Account creation is managed by the bank’s backend and involves security checks such as email and Aadhaar number validation.

2. ATM Card Operations:

Apply for ATM Card: Users can apply for an ATM card linked to their bank account, with the system ensuring that account details such as name and mobile number match during the process.
Cancel ATM Card: Customers can request to cancel their ATM cards. The application checks for the existence of an ATM card before proceeding with the cancellation request.
Set ATM PIN: After requesting an ATM card, customers can set a secure 4-digit PIN for ATM usage. This process requires OTP validation to ensure security.
Withdraw/Deposit Using ATM: Users can perform ATM transactions such as withdrawals and deposits. The system verifies the user's PIN, checks for sufficient funds in case of withdrawals,
and processes deposits to the respective account.

3. Transaction Management:

Transfer Using ATM: Crest Bank allows users to transfer money to other accounts using their ATM credentials. It ensures that the transaction amount is available in the user's account and that the PIN is correct.
Balance Enquiry Using ATM: Customers can check the balance of their accounts using their ATM card credentials. The system provides a secure way to access balance details without compromising user privacy.
Generate Mini Statement: Users can request a mini statement that displays a summary of recent transactions linked to their ATM card. This service is ideal for customers who need a quick overview of their account 
activity.

4. Security and OTP Handling:

OTP Generation for PIN Setup: A secure OTP is generated and sent to the user's registered mobile number to verify the authenticity of the PIN setting process.
PIN Validation: Every ATM transaction—such as withdrawal, deposit, and transfer—requires the correct PIN for verification to ensure only authorized access.

5. Email and SMS Notifications:

Email Notifications: Email alerts are sent for crucial actions such as ATM card creation, account updates, and transaction confirmations.
These emails contain important details like ATM card number, transaction status, and security-related updates.
SMS for OTPs: OTPs are sent to the registered mobile number via Twilio for secure transactions such as ATM PIN setting.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**API Endpoints**

**Account Management:**

Create Bank Account:

Endpoint: /register

Method: POST

Description: Create a new bank account for a customer.

Validations: Ensures that all required account fields are provided, including name, address, and contact details.


Update Account:
Endpoint: /update
Method: POST
Description: Update details for an existing bank account.
Validations: Verifies that the provided account information is correct.


Get One Account Holder:
Endpoint: /getone/{acno}
Method: GET
Description: Retrieve a specific account holder's information based on the account number.
Validations: Verifies that the account exists before returning the details.


Get All Account Holders:
Endpoint: /getall
Method: GET
Description: Retrieve all account holders in the system.
Validations: N/A (Returns the list of all accounts).


**Transactions and Account Operations**

Withdraw Amount:
Endpoint: /withdraw
Method: POST
Description: Withdraw money from the bank account.
Validations: Verifies the account balance and PIN.
Deposit Amount:

Endpoint: /deposit
Method: POST
Description: Deposit funds into a bank account.
Validations: Verifies the PIN and processes the deposit.
Balance Enquiry:

Endpoint: /checkbalance
Method: POST
Description: Check the balance of a specific account.
Validations: Verifies the user's PIN before showing the balance.
Transfer Amount:

Endpoint: /transfer
Method: POST
Description: Transfer funds from one account to another.
Validations: Verifies the account balance, recipient account, and user PIN.
Transaction Statement:

Endpoint: /statement
Method: POST
Description: Retrieve the full transaction statement for an account.
Validations: Verifies account details before generating the statement.
Generate Mini Statement:

Endpoint: /ministatement
Method: POST
Description: Retrieve a mini statement showing the last few transactions.
Validations: Verifies account details before generating the mini statement.


**ATM Card Operations:**

Apply for ATM Card:
Endpoint: /applyatm
Method: POST
Description: Apply for an ATM card linked to your bank account.
Validations: Verifies account holder’s name and mobile number.

Cancel ATM Card:
Endpoint: /cancelatm
Method: POST
Description: Cancel your existing ATM card.
Validations: Verifies the account holder's name, mobile number, and that an ATM card exists.


Set ATM PIN:
Endpoint: /set-pin
Method: POST
Description: Set a new ATM PIN after validating the OTP sent to the user.
Validations: OTP validation and PIN format validation.


Send OTP for ATM PIN Setup:
Endpoint: /send-otp
Method: POST
Description: Generate and send an OTP to the registered mobile number for ATM PIN setup.
Validations: Verifies the account details before sending the OTP.

**ATM Transactions:**

Withdraw Using ATM:
Endpoint: /withdrawATM
Method: POST
Description: Withdraw money from your account using your ATM card.
Validations: Verifies user’s PIN and checks for sufficient balance.


Deposit Using ATM:
Endpoint: /depositATM
Method: POST
Description: Deposit money into your bank account using your ATM card.
Validations: Verifies user’s PIN and processes deposit.


Transfer Using ATM:
Endpoint: /transferATM
Method: POST
Description: Transfer funds from your account to another account using your ATM card.
Validations: Verifies the PIN and ensures sufficient funds for the transfer.


Balance Enquiry Using ATM:
Endpoint: /balanceATM
Method: GET
Description: Check the balance of your account using ATM credentials.
Validations: Verifies the PIN to ensure secure access.


Mini Statement Using ATM:
Endpoint: /miniStatementATM
Method: GET
Description: Generate a mini statement of recent transactions.
Validations: Verifies the PIN to ensure only the authorized user can access the statement.



**Technologies and Libraries**

Java: The application is built using Java, a robust language for backend systems.
Spring Boot: The core framework for creating RESTful web services and handling business logic.
Twilio API: Used for sending OTPs to customers for secure PIN validation.
Spring Data JPA: Provides an abstraction layer for interacting with the database to fetch and update customer data.
SimpleMailMessage (Spring Mail): Used for sending email notifications to users regarding their account and ATM card activities.
ConcurrentHashMap: For temporary OTP storage to validate user actions in real-time.

**Security Considerations**

PIN Encryption: The ATM PIN is encrypted and stored securely to prevent unauthorized access.
OTP Verification: OTPs are time-sensitive and provide an additional layer of security when setting a new ATM PIN.
Transaction Validations: Every financial transaction requires PIN validation to ensure the requestor is the authorized account holder.
Access Control: Sensitive actions such as money transfers and balance checks require correct PIN verification.
