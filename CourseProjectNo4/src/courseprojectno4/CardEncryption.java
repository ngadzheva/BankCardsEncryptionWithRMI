package courseprojectno4;

/**
 * CourseProjectNo4: EcryptCard.java
 * Encrypting a bank card using the Route Cipher algorithm
 */
public class CardEncryption {
    /**
     * Key for encrypting
     */
    private static int key;
    
    /**
     * General purpose constructor
     * @param key 
     */
    public CardEncryption(int key){
        this.key = key % 16;
    }
    
    /**
     * Create the grid for encoding
     * @param grid
     * @param plainCardNumber
     * @param currentDigit 
     */
    private void createGrid(char[][] grid, char[] plainCardNumber, int currentDigit){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                /**
                 * while there are digits in the plain card number, 
                 * we add them to the grid
                 * if there are no more digits in the plain card number
                 * and it is allocated for the grid space greater
                 * than the length of the plain card number,
                 * we add '0' to the free cells of the grid
                 */
                if (currentDigit < plainCardNumber.length) {
                    grid[i][j] = plainCardNumber[currentDigit++];  
                } else {
                    grid[i][j] = '0';
                }
            }
        }
    }
    
    /**
     * Spiral iterating through the grid from the top left corner 
     * counter clockwise
     * @param grid
     * @param cipherCardNumber
     * @param currentDigit 
     */
    private void topLeftCorner(char[][] grid, char[] cipherCardNumber, 
                                                               int currentDigit){
        int rowStart = 0,
            rowEnd = grid.length-1,
            colStart = 0,
            colEnd = grid[0].length-1;
            
            while(rowStart <= rowEnd && colStart <= colEnd){
                 
                /**
                 * move top-down
                 */
                for (int i = rowStart; i <= rowEnd; i++) {
                    cipherCardNumber[currentDigit++] = grid[i][colStart];
                }
                
                /**
                 * if the key is equal to 1, we have only one column,
                 * so we have already iterated through the whole grid
                 */
                if(colStart == colEnd) {
                    break;
                }
                
                colStart++;
                
                /**
                 * move left-right
                 */
                for (int j = colStart; j <= colEnd; j++) {
                    cipherCardNumber[currentDigit++] = grid[rowEnd][j];
                }
                
                 /**
                 * we have only one raw left and
                 * we have already iterated through the whole grid
                 */
                if(rowStart == rowEnd) {
                    break;
                }
                
                rowEnd--;
                
                /**
                 * we have only one row left
                 * and one column to iterate through
                 */
                if(rowStart == rowEnd){
                    for (int j = colStart; j <= colEnd; j++) {
                        cipherCardNumber[currentDigit++] = grid[rowEnd][j];
                    }
                        break;
                }
                
                /**
                 * move bottom-top
                 */
                for (int i = rowEnd; i >= rowStart; i--) {
                    cipherCardNumber[currentDigit++] = grid[i][colEnd];
                }
                colEnd--;
                
                /**
                 * we have only one column left 
                 * and one row to iterate through
                 */
                if(colStart == colEnd){
                    for (int i = rowStart; i <= rowEnd; i++) {
                        cipherCardNumber[currentDigit++] = grid[i][colStart];
                    } 
                    break;
                }
                
                /**
                 * move right-left
                 */
                for (int j = colEnd; j >= colStart; j--) {
                    cipherCardNumber[currentDigit++] = grid[rowStart][j];
                }
                rowStart++;
                
            }
    }
    
    /**
     * Encrypt the plain card number, using the route cipher algorithm
     * @param plainCardNumber
     * @return new String(cipherCardNumberChars) - the encoded card number
     */
    public String encrypt(String plainCardNumber){
        char[] plainCardNumberChars = plainCardNumber.toCharArray();
        char[] cipherCardNumberChars;
        char[][] grid;
        
        int cols = key,
            rows = (int) Math.ceil((double) plainCardNumberChars.length / cols),
            currentChar = 0;

        cipherCardNumberChars = new char[rows * cols];
        grid = new char[rows][cols];

        /**
         * create the grid
         */
        createGrid(grid, plainCardNumberChars, currentChar);
        
        /**
         * iterate through the grid and get the cipher card number
         */
        currentChar = 0;
        topLeftCorner(grid, cipherCardNumberChars, currentChar);
        
        return new String(cipherCardNumberChars);
    }
}
