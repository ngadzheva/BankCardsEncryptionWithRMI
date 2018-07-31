package courseprojectno4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * CourseProjectNo4: BankCardsSortedByCardNumber.java
 * Read a file with a table with bank cards and their encryptions,
 * sorted by bank card number
 */
public class BankCardsSortedByCardNumber {
    private File file;
    private Scanner input;
    private Formatter output;
    
    /**
     * Write the bank cards and their encryptions 
     * to the file "sortedByCardNumber.txt"
     * @param cardsEntry 
     */
    public void writeRecords(TreeSet<String> bankCards, 
            Set<Entry<String, String>> cardsEntry){
        /**
         * If the file doesn't exist, create it
         */
        file = new File("sortedByCardNumber.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.err.println("I/O exception");
            }
            file.canWrite();
            file.canRead();
        }
        
        /**
         * Try to open the file
         */
        try{
            output = new Formatter("sortedByCardNumber.txt");
        } catch(SecurityException securityException){
            System.err.println("Security exception");
        } catch(FileNotFoundException foundNotFound){
            System.err.println("File not found");
        }
        
        StringBuilder sb = new StringBuilder();
        
        /**
         * Iterate through the bank cards and their encryptions and
         * write them to the file
         */
        for (String bankCard : bankCards) {
            for (Entry<String, String> card : cardsEntry) {
                if(bankCard.equals(card.getValue())){
                    sb.append(String.format("%s\t%s%n", bankCard, card.getKey()));
                }
            }
        }
        
        try{
            output.format("%s%n", sb);
        }catch(FormatterClosedException formatterClosedException){
            System.err.println("Formatter closed");
        } catch(NoSuchElementException elementException){
            output.close();
            System.err.println("No such element");
        }
        
        /**
         * Close the file
         */
        if(output!=null){
            output.close();
        }
    }
    
    /**
     * Read the bank cards and their encryptions 
     * from the file "sortedByCardNumber.txt"
     * @return sb - a String containing the content of the file
     */
    public String readRecords(){
        /**
         * Retrieve the path to the file
         */
        file = new File("sortedByCardNumber.txt");
        StringBuilder sb = new StringBuilder();
        
        /**
         * Try to open the file and read the records. Finally, close the file.
         */
        try{
            input = new Scanner(file);
            
            while(input.hasNext()){
                sb.append("Bank card number: " + input.next() + "\t" +
                        "Encryption: " + input.next() + "\n");
            }
        } catch (NoSuchElementException elementException) {
            sb.append( "File improperly formed.\n" );
            input.close();
            System.exit( 1 );
        } catch (IllegalStateException stateException){
            sb.append( "Error reading from file." );
            System.exit( 1 );
        } catch (FileNotFoundException fileNotFoundException){
            sb.append( "File cannot be found." );
            System.exit( 1 );
        } finally {
            if ( input != null ){
                input.close(); 
            }
        }
        
        return sb.toString();
    }
}
