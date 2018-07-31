package courseprojectno4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * CourseProjectNo4: BankCardsSortedByEncryption.java
 * Read a file with a table with bank cards and their encryptions,
 * sorted by encryption
 */
public class BankCardsSortedByEncryption {
    private File file;
    private Scanner input;
    private Formatter output;
    
    /**
     * Write the bank cards and their encryptions to the file
     * "sortedbyEncryption.txt"
     * @param cardsEntry 
     */
    public void writeRecords(Set<Map.Entry<String, String>> cardsEntry){
        /**
         * If the file doesn't exist, create it
         */
        file = new File("sortedByEncryption.txt");
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
            this.output = new Formatter("sortedByEncryption.txt");
        } catch(SecurityException securityException){
            System.err.println("Security exception");
        } catch(FileNotFoundException foundNotFound){
            System.err.println("File not found");
        }
        
        StringBuilder sb = new StringBuilder();
        
        /**
         * Iterate through the encryptions and their bank cards and
         * write them to the file
         */
        for (Map.Entry<String, String> entry : cardsEntry) {
            sb.append(String.format("%s %s%n", entry.getKey(), entry.getValue()));
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
     * Read the bank cards and their encryptions from the file
     * "sortedByEncryption.txt"
     * @return sb - a String containing the content of the file
     */
    public String readRecords(){
        /**
         * Retrieve the path to the file
         */
        this.file = new File("sortedByEncryption.txt");
        StringBuilder sb = new StringBuilder();
        
        /**
         * Try to open the file and read the records. Finally, close the file.
         */
        try{
            input = new Scanner(file);
            
            while(input.hasNext()){
                sb.append(String.format("%-50s %50s%n", 
                        "Encryption: " + input.next(),
                        "Bank card number: " + input.next()));
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
