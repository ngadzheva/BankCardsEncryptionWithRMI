package servergui;

import courseprojectno4.BankCardsSortedByEncryption;
import courseprojectno4.BankCardsSortedByCardNumber;
import courseprojectno4.UsersInfoXML;
import courseprojectno4.CardEncryption;
import courseprojectno4.User;
import courseprojectno4.UserType;
import courseprojectno4.ValidateCard;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * ServerInterface: EncryptableImplementation.java
 * Define Server implementation object
 */
public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface{
    /**
     * Reference to the registered server object
     */
    private Registry register;
    /**
     * List of the users of the application
     */
    private  ArrayList<User> users;
    /**
     * Hold the encryptions and their bank card numbers
     */
    private  TreeMap<String, String> encryptions;
    /**
     * Reference to a BankCardsSortedByCardNumber object
     * Used for writing the bank cards, sorted by card number, to a file
     */
    private BankCardsSortedByCardNumber sortedByCardNumber;
    /**
     * Reference to a BankCardsSortedByEncryption object
     * Used for writing the bank cards, sorted by encryption, to a file
     */
    private BankCardsSortedByEncryption sortedByEncryption;
    /**
     * Reference to an UsersInfoXML object
     * Used to write the users to a XML file
     */
    private UsersInfoXML usersInfo;
    /**
     * Count of the encryptions of one bank card number
     */
    private int encryptionNumber;
    /**
     * Offset for encryption
     */
    private static final int OFFSET = 5;
    
    /**
     * Default constructor
     * @throws RemoteException 
     */
    public ServerInterfaceImpl() throws RemoteException{  
        users = new ArrayList<>();
        encryptions = new TreeMap<>();
        sortedByCardNumber = new BankCardsSortedByCardNumber();
        sortedByEncryption = new BankCardsSortedByEncryption();
        usersInfo = new UsersInfoXML();
        encryptionNumber = 0;
        initializeUsers();
    }
    
    /**
     * Retrieve the registered server object
     * @param reg 
     */
    public void setRegister(Registry reg){
        register = reg;
    }

    /**
     * Validate the user who wants to login - check whether the user is 
     * registered in the application. If the user is not found, close the socket
     * @param userName
     * @param password
     * @return true if the user is found, else return false
     * @throws RemoteException 
     */
    @Override
    public boolean validateUser(String userName, String password) throws RemoteException {
        boolean found = false;
        
        for (User user : users) {
            if(user.getUserName().equals(userName) && 
                    user.getPassword().equals(password)){
                found = true;
                break;
            }
        }
        
        if(!found){
           try {
               register.unbind("encryptable");
           } catch (NotBoundException ex) {
               Logger.getLogger(ServerInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
           } catch (AccessException ex) {
               Logger.getLogger(ServerInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        
        return found;
    }

    /**
     * Register new user in the application
     * @param userName
     * @param password
     * @param permissions
     * @throws RemoteException 
     */
    @Override
    public void addUser(String userName, String password, String permissions) throws RemoteException {
        UserType userType;
        
        if(permissions.equals("admin") || permissions.equals("administrator")){
            userType = UserType.ADMIN;
        } else if(permissions.equals("user")){
            userType = UserType.USER;
        } else {
            userType = UserType.GUEST;
        }
        
        User newUser = new User(userName, password, userType);
        users.add(newUser);
        
        saveUsersInfoToXML();
    }

    /**
     * Encrypt a bank card and return the encrypted bank card number.
     *  1. Check whether the user has rights for encryption. If the user is
     * a guest and doesn't have rights for encryption, return an error message.
     *  2. If the user has rights for encryption, validate the bank card number.
     * If it is incorrect, return an error message.
     *  3. If the bank card number is correct, encrypt it and return the encryption.
     * If the bank card is already encrypted, increase the encryption key with 1.
     * If there are 12 encryptions for the bank card, return an error message.
     * @param userNumber
     * @param bankCard
     * @return encryptedCard
     * @throws RemoteException 
     */
    @Override
    public String encrypt(String userName, String bankCard) throws RemoteException {
        User foundUser = new User();
        ValidateCard validation = new ValidateCard();
        CardEncryption encryption;
        String encryptedCard = "";
        
        foundUser = getUser(userName);

        if(foundUser.getUserLevel().toString().equals("guest")){
            encryptedCard = "You don't have permissions for encryption!";
            return encryptedCard;
        }

        if(!validation.isValid(bankCard)){
            encryptedCard = "The bank card is invalid! Please, try again";
            return encryptedCard;
        }
        
        encryptionNumber = numberOfEncryptions(bankCard);
        
        if(encryptionNumber == 0){
            encryption = new CardEncryption(OFFSET);
            encryptedCard = encryption.encrypt(bankCard);
            encryptions.put(encryptedCard, bankCard);
            writeSortedByCardNumber();
            writeSortedByEncryption();
        } else if(encryptionNumber >= 1 && encryptionNumber <= 10){
            encryption = new CardEncryption(OFFSET + encryptionNumber);
            encryptedCard = encryption.encrypt(bankCard);
            encryptions.put(encryptedCard, bankCard);
            writeSortedByCardNumber();
            writeSortedByEncryption();
        } else if(encryptionNumber == 11){
            encryption = new CardEncryption(OFFSET - 1);
            encryptedCard = encryption.encrypt(bankCard);
            encryptions.put(encryptedCard, bankCard);
            writeSortedByCardNumber();
            writeSortedByEncryption();
        }else if(encryptionNumber > 11) {
            encryptedCard = "You can not make encryptions for one bank card "
                    + "more than 12 times!";
        } 
        
        return encryptedCard;
    }

    /**
     * Find the user in the list of the users and return the user
     * @param userName
     * @return foundUser
     */
    private User getUser(String userName){
        User foundUser = new User();
        
        for (User user : users) {
            if(user.getUserName().equals(userName)){
                foundUser = user;
                break;
            }
        }
        
        return foundUser;
    }
    
    /**
     * Get the number of encryptions of the bank card number
     * @param userName
     * @param bankCard
     * @return count
     */
    private int numberOfEncryptions(String bankCard){
        int count = 0;
        
        Set<Entry<String, String>> encryptionsEntry = encryptions.entrySet();
        
        for (Entry<String, String> cardsEntry : encryptionsEntry) {
           if(cardsEntry.getValue().equals(bankCard)){
               count++;
           }
        }
        
        return count;
    }

    /**
     * Decrypt a bank card and return the decrypted bank card number
     *   1. Check whether the user has rights for decryption. If the user is
     * a guest and doesn't have rights for decryption, return an error message.
     *  2. Decrypt it and return the encryption. If there is no such encryption,
     * return an error message.
     * @param userNumber
     * @param encryption
     * @return decryptedCard
     * @throws RemoteException 
     */
    @Override
    public String decrypt(String userName, String encryption) throws RemoteException {
        String decryptedCard = "";
        User currentUser = getUser(userName);
        
        if(currentUser.getUserLevel().toString().equals("guest")){
            decryptedCard = "You don't have permissions for decryption!";
            return decryptedCard;
        }
       
       decryptedCard = findBankCard(encryption);
        
        if(decryptedCard.equals("")){
            decryptedCard = "The wrong encryption number! Please, try again!";
        }
        
        return decryptedCard;
    }
    
    /**
     * Find the encryption and return the bank card number
     * @param userName
     * @return foundCard - the decrypted bank card
     */
    private String findBankCard(String encryption){
        String foundCard = "";
        
        Set<Entry<String, String>> encryptionsEntry = encryptions.entrySet();
        
        for (Entry<String, String> cardsEntry : encryptionsEntry) {
           if(cardsEntry.getKey().equals(encryption)){
               foundCard = cardsEntry.getValue();
               break;
           }
        }
        
        return foundCard;
    }
    
    /**
     * Write the bank cards and their encryptions to a file, containing a table,
     * sorted by the bank card number
     */
    public void writeSortedByCardNumber(){
        TreeSet<String> bankCards = new TreeSet<>();
        Set<Entry<String, String>> cards = encryptions.entrySet();
        
        for (Entry<String, String> card : cards) {
            bankCards.add(card.getValue());
        }
        
        sortedByCardNumber.writeRecords(bankCards, cards);
    }
    
    /**
     * Write the bank cards and their encryptions to file, containing a
     * table, sorted by the encryption code
     */
    public void writeSortedByEncryption(){
        Set<Entry<String, String>> cardsEntry = encryptions.entrySet();
        sortedByEncryption.writeRecords(cardsEntry);
    }
    
    /**
     * Save the information for the users in XML file
     */
    public void saveUsersInfoToXML(){
        usersInfo.writeXML(users);
    }

    /**
     * Register basic users in the application:
     *  - administrator
     *  - user
     *  - guest
     */
    private void initializeUsers(){
        users.add(new User("admin", "admin", UserType.ADMIN));
        users.add(new User("user", "user", UserType.USER));
        users.add(new User("guest", "guest", UserType.GUEST));
        encryptions.put("46090000929365001912", "4563960122001999");
        encryptions.put("47470000085883610701", "4388576018410707");
    }
}
