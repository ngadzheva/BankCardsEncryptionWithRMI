package servergui;

import courseprojectno4.User;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *  ServerInterface: Encryptable.java
 *  Define Server object interface
 */
public interface ServerInterface extends Remote{
    /**
     * Validate an user - check whether the user is registered in the application
     * @param userName
     * @param password
     * @return
     * @throws RemoteException 
     */
    public boolean validateUser(String userName, String password) throws RemoteException;
    
    /**
     * Add an user to the application
     * @param userName
     * @param password
     * @param permissions
     * @throws RemoteException 
     */
    public void addUser(String userName, String password, String permissions) throws RemoteException;
    
    /**
     * Encrypt a bank card number
     * @param userName
     * @param bankCard
     * @return
     * @throws RemoteException 
     */
    public String encrypt(String userName, String bankCard) throws RemoteException;
    
    /**
     * Decrypt a bank card number
     * @param userName
     * @param encryption
     * @return
     * @throws RemoteException 
     */
    public String decrypt(String userName, String encryption) throws RemoteException;
}
