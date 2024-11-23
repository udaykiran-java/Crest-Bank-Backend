package com.uday.utility;

import java.util.Random;

public class AccountNumberGenerator {

       // Method to generate a 10-digit number starting with 9963
        public static long generateAccountNumber() {
        String startwith = "9963";  // Fixed starting number
        Random random = new Random();

        // Generate a random number that will complete the account number to 10 digits
        int randomNumber = random.nextInt(1000000);  // Random number between 0 and 9999999
        
        // Combine the StartWith and random number to form a 10-digit number
        String accountNumberStr = startwith + String.format("%06d", randomNumber); 
        
        /* Ensures six digits, pads with 0 if necessary
         * 
         * "%06d": This is a format specifier.
           %: Indicates the start of a format specifier.
           0: This means to pad the output with leading zeros.
           6: This indicates the total width of the output. In this case, it means that the resulting string should be at least 6 characters long.
           d: This stands for "decimal integer," meaning the input (randomNumber) is treated as an integer.
           
           
         */
        
        return Long.parseLong(accountNumberStr);  // Convert the string to long
    }
        
        
       // method for generate ATM Card Number
        
        public static long atmNumberGenarater() {
        	
        	 String startWith = "8500";  // Fixed starting number
        	    Random atm = new Random();

        	    // Generate two 4-digit random numbers
        	    int middleDigits = atm.nextInt(10000); // Generates a 4-digit number (0 to 9999)
        	    int lastDigits = atm.nextInt(10000);   // Generates another 4-digit number (0 to 9999)

        	    // Format numbers as 4 digits, padding with zeros if necessary
        	    String atmNumber = startWith + String.format("%04d", middleDigits) + String.format("%04d", lastDigits);

        	    return Long.parseLong(atmNumber);  // Convert the string to long
        }
}
