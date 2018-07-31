package courseprojectno4;

/**
 * CourseProjectNo4: CardValidation.java
 * Validating a bank card using the Luhn algorithm
 */
public class ValidateCard {    
    /**
     * Max and Min valid length of the card number
     */
    private static final int MIN_LENGTH = 13;
    private static final int MAX_LENGTH = 16;
    
    /**
     * Check whether the bank card number is valid
     * @param cardNumber
     * @return true if the bank card number is valid, else return false
     */
    public boolean isValid(String cardNumber){
        if (rightNumberOfDigits(cardNumber)) {
            if (rightPrefix(cardNumber)) {
                if (luhn(cardNumber)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Implement the Luhn algorithm for checking whether a bank card number is
     * valid or not
     * @param cardNumber
     * @return true if the bank card number is valid according to Luhn algorithm,
     * else return false
     */
    private boolean luhn(String cardNumber){
        /**
         * Sum of all digits 
         */
        int sum = 0; 
        /**
         * Length of the digits
         */
        int nDigits = cardNumber.length();
        /**
         * Represents the odd positions of the card number
         */
        boolean oddPosition = false;
        /**
         * Current digit
         */
        int digit;
        
        /**
         * Start iterating through the card number from right to left
         */
        while(0 < nDigits--) {
            digit = (int) (cardNumber.charAt(nDigits) - 48);
            
            /**
             * For every odd position - double the digit on that position
             */
            if(oddPosition){
                digit *= 2;
                
                /**
                 * If the resulting digit is greater than 9, make a single-digit
                 * number by subtracting 9 from the resulting digit
                 */
                if(digit > 9){
                    digit -= 9;
                }
            }
            
            /**
             * Sum all single-digit numbers
             */
            sum += digit;
            
            /**
             * Move on next position - odd => even or even => odd
             */
            oddPosition = !oddPosition;
        }
        
        /**
         * If the result is divisible by 10, then the card number is valid
         */
        return sum % 10 == 0;
    }
    
    /**
     * Check whether the prefix of the bank card number is valid
     * It should be one of the following:
     *  - 4 for Visa cards
     *  - 5 for Master cards
     *  - 6 for Discover cards
     *  - 37 for American Express cards
     * @param cardNumber
     * @return true if the prefix is valid, else return false
     */
    private boolean rightPrefix(String cardNumber){
        int firstDigit = (int) (cardNumber.charAt(0) - 48);
        
        if(firstDigit != CardPrefixes.VISA.getPrefix() && 
                firstDigit != CardPrefixes.MASTER.getPrefix() && 
                firstDigit != CardPrefixes.DISCOVER.getPrefix()
                && firstDigit != CardPrefixes.AMERICAN_EXPRESS_FIRST.getPrefix()){
            return false;
        } else if(firstDigit == CardPrefixes.AMERICAN_EXPRESS_FIRST.getPrefix()){
            int secondDigit = (int) cardNumber.charAt(1);
            
            if(secondDigit == CardPrefixes.AMERICAN_EXPRESS_SECOND.getPrefix()){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    
    /**
     * Check whether the length of the bank card number is valid
     * It should be between 13 and 16 digits
     * @param cardNumber
     * @return true if the length of the bank card number is valid,
     * else return false
     */
    private boolean rightNumberOfDigits(String cardNumber){
        return cardNumber.length() >= MIN_LENGTH && cardNumber.length() <= MAX_LENGTH;
    }
}


