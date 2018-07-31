package usergui;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import servergui.ServerInterface;

/**
 * UserGUI: UserController.java
 * Control the user form for encryption and decryption
 */
public class UserController {
    /**
     * Hold the String inputted to the User name field
     */
    private String userName;
    /**
     * Reference to the ServerInterface
     */
    private ServerInterface user;
    /**
     * Hold the Title, Header and Content of a dialog window
     */
    private String messageTitle, messageHeader, messageContent;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCancel;
    
    @FXML
    private TextField txtAction;

    @FXML
    private TextField txtCardNumber;

    @FXML
    private AnchorPane pnlUserComponent;

    @FXML
    private Button btnSubmit;
    
    /**
     * Retrieve the user name of the user who has logged in
     * @param userName 
     */
    @FXML
    public void setUserName(String userName){
        this.userName = userName != null ? userName : "";
    }
    
    /**
     * By clicking on the Submit button on the form for encrypting/decrypting
     * bank cards, encrypt or decrypt a bank card if the user has such rights
     * (if the user is not a guest)
     * If the user is a guest, a dialog window is opened with error message.
     * If one of the fields or all of them are empty or incorrect, a dialog
     * window with warning message is opened.
     * @param event 
     */
    @FXML
    private void onSubmit(ActionEvent event) {
        initializeRMI();
        
        String result;
        
        if(Pattern.matches(".*encrypt.*", txtAction.getText())){
            validateBankCardFieldForEncryption();
            
            try {
                result = user.encrypt(userName, txtCardNumber.getText());
                
                if(result.matches("[0-9]+")){
                   messageTitle = "Encryption";
                   messageHeader = "The encryption code for your bank card is:";
                   messageContent = "Bank card number: " + txtCardNumber.getText() +
                        "\nEncryption code: " + result;
                   
                   informationMessage(messageTitle, messageHeader, messageContent); 
                   
                   txtAction.setText("");
                   txtCardNumber.setText("");
                } else {
                    messageTitle = "Error";
                    messageHeader = "Problem found";
                    messageContent = result;
                    
                    errorMessage(messageTitle, messageHeader, messageContent); 
                }
            } catch (RemoteException ex) {
                messageTitle = "Encryption error";
                messageHeader = "Error occured.";
                messageContent = "Error with encrypting your bank card number "
                        + "occured!\nPlease, try again!";
                
                errorMessage(messageTitle, messageHeader, messageContent);
            }
        } else if(Pattern.matches(".*decrypt.*", txtAction.getText())){
            validateBankCardFieldForDecryption();
            
            try {
                result = user.decrypt(userName, txtCardNumber.getText());
                
                if(Pattern.matches("[0-9]+", result)){
                   messageTitle = "Decryption";
                   messageHeader = "The decryption code for your bank card is:";
                   messageContent = "Bank card number: " + txtCardNumber.getText() +
                        "\nEncryption code: " + result;
                   
                   informationMessage(messageTitle, messageHeader, messageContent);
                   
                   txtAction.setText("");
                   txtCardNumber.setText("");
                } else {
                    messageTitle = "Error";
                    messageHeader = "Problem found";
                    messageContent = result;
                    
                    errorMessage(messageTitle, messageHeader, messageContent);
                } 
            } catch (RemoteException ex) {
                messageTitle = "Decryption error";
                messageHeader = "Error occured.";
                messageContent = "Error with decrypting your bank card number "
                        + "occured!\nPlease, try again!";
                
                errorMessage(messageTitle, messageHeader, messageContent);
            }
        } else {
            messageTitle = "Wrong operation";
            messageHeader = "Problem occured.";
            messageContent = "The operation you entered is incorrect!"
                    + "\nPlease, try again!";
                
            warningMessage(messageTitle, messageHeader, messageContent);
        }
    }
    
    /**
     * Validate whether the inputted String in the bank card field for 
     * encryption is valid - containing 16 digits
     */
    private void validateBankCardFieldForEncryption(){
        if(!Pattern.matches("[0-9]{16}", txtCardNumber.getText())){
            messageTitle = "Warning";
            messageHeader = "Wrong card number";
            messageContent = "The card number must be a number with 16 digits."
                    + "Please try again.";
            
            warningMessage(messageTitle, messageHeader, messageContent);
            return;
        }
    }
    
    /**
     * Validate whether the inputted String in the bank card field for decryption
     * is valid - containing at least 16 digits
     */
    private void validateBankCardFieldForDecryption(){
        if(!Pattern.matches("[0-9]{16,}", txtCardNumber.getText())){
            messageTitle = "Warning";
            messageHeader = "Wrong encryption number";
            messageContent = "The encryption number must be a number with at "
                    + "least 16 digits. Please try again.";
            
            warningMessage(messageTitle, messageHeader, messageContent);
            return;
        }
    }

    /**
     * Close the client GUI
     * @param event 
     */
    @FXML
    private void onCancel(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Initialize RMI
     * Connect to the server
     */
    protected void initializeRMI(){
        String host = "localhost";
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            user = (ServerInterface) registry.lookup("encryptable");
        } catch (Exception e) {
            System.out.println(e);
            
            messageTitle = "Connection failed";
            messageHeader = "Connection problem";
            messageContent = "Connection to the server failed.";
            
            errorMessage(messageTitle, messageHeader, messageContent);
            Platform.exit();
        }
    }

    /**
     * Initialization
     */
    @FXML
    void initialize() {

        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert txtCardNumber != null : "fx:id=\"txtCardNumber\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert pnlUserComponent != null : "fx:id=\"pnlUserComponent\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert btnSubmit != null : "fx:id=\"btnSubmit\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
       
    }

    /**
     * Show error message in a dialog window
     * @param errorTitle
     * @param errorHeader
     * @param errorContent 
     */
    private void errorMessage(String errorTitle, String errorHeader, String errorContent){
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        
        setAlertProperties(errorMessage, errorTitle, errorHeader, errorContent);
        
        errorMessage.showAndWait();
    }
    
    /**
     * Show warning message in a dialog window
     * @param infoTitle
     * @param infoHeader
     * @param infoContent 
     */
    private void warningMessage(String infoTitle, String infoHeader, String infoContent){
        Alert errorMessage = new Alert(Alert.AlertType.INFORMATION);
        
        setAlertProperties(errorMessage, infoTitle, infoHeader, infoContent);
        
        errorMessage.showAndWait();
    }
    
    /**
     * Show information message in a dialog window
     * @param infoTitle
     * @param infoHeader
     * @param infoContent 
     */
    private void informationMessage(String infoTitle, String infoHeader, String infoContent){
        Alert errorMessage = new Alert(Alert.AlertType.INFORMATION);
        
        setAlertProperties(errorMessage, infoTitle, infoHeader, infoContent);
        
        errorMessage.showAndWait();
    }
    
    /**
     * Set Alert object's title, header and content
     * @param message
     * @param infoTitle
     * @param infoHeader
     * @param infoContent 
     */
    private void setAlertProperties(Alert message, String infoTitle, 
            String infoHeader, String infoContent){
        message.setTitle(infoTitle);
        message.setHeaderText(infoHeader);
        message.setContentText(infoContent);
    }
}
