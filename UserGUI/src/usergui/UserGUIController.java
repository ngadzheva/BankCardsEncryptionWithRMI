package usergui;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import servergui.ServerInterfaceImpl;
import servergui.ServerGUI;
import servergui.ServerInterface;

/**
 * UserInterface: UsreInterfaceController.java
 * Control the user GUI.
 */
public class UserGUIController {
    /**
     * A constant representing the administrator
     */
    private final static String ADMIN = "admin";
    /**
     * Reference to the String inputted in the user name field of the Login form
     */
    private String userName;
    /**
     * Reference to the server interface
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
    private ComboBox<?> cbChoice;

    @FXML
    private AnchorPane pnlUserComponent;

    @FXML
    private Button btnSubmit;
    
    @FXML
    private TextField txtUserName;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnAdd;

    @FXML
    private TextField txtPermissions;
    
    /**
     * Retrieve the connection with the server
     * @param user 
     */
    public void setUser(ServerInterface user){
        this.user = user;
    }
    
    /**
     * By clicking on the Login button in the login form, open a new window:
     *  - if it is an administrator - open a form for adding new users
     *  - if it is a user or a guest - open a form for encrypting/decrypting
     *      bank cards
     *  - if the user is not found - open a dialog window with error message.
     * The socket is closed from the server and the application shuts down
     *  - if one of the forms or the both are empty - a dialog window with 
     * warning message is opened
     * @param event
     * @throws RemoteException
     * @throws IOException 
     */
    @FXML
    private void onLogin(ActionEvent event) throws RemoteException, IOException{
        Stage stage;
        Parent root;
        Scene scene;
        String title;
        FXMLLoader loader = new FXMLLoader();
        
        userName = txtUserName.getText();
        
        if(Pattern.matches("", userName) || 
                Pattern.matches("", txtPassword.getText())){
            messageTitle = "Warning";
            messageHeader = "Missing user name or password";
            messageContent = "Please enter user name and password.";
            
            warningMessage(messageTitle, messageHeader, messageContent);
        } else {
            stage = (Stage) btnLogin.getScene().getWindow();
        
            if(user.validateUser(userName, txtPassword.getText())){
            
                if(userName.equals(ADMIN)){
                    root = loader.load(getClass()
                            .getResource("AdminGUIFXMLDocument.fxml"));
                    title = "Add user";
                    scene = new Scene(root);
                } else {
                    loader.setLocation(UserGUIController.class
                            .getResource("UserGUIFXMLDocument.fxml"));
                    root = loader.load();
                    scene = new Scene(root);
                    UserController userContr = loader.getController();
                    userContr.setUserName(userName);
                    title = "Encrypt/Decrypt a bank card";
                } 
            
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } else {
                messageTitle = "Not found";
                messageHeader = "Wrong user name or password";
                messageContent = "There is no user with the given user name and password";
            
                errorMessage(messageTitle, messageHeader, messageContent);
            
                Platform.exit();
            } 
        } 
    }
    
    /**
     * By clicking on the Add button in the form for adding new users, add new
     * user in the application. A dialog window is opened with the information
     * for the added user.
     * If one of the fields or all of them are empty or the permissions are 
     * different than admin, user or guest, a dialog window with warning message
     * is opened.
     * @param event
     * @throws RemoteException 
     */
    @FXML
    private void onAdd(ActionEvent event) throws RemoteException{
        initializeRMI();
        
        if(!Pattern.matches("", txtUserName.getText()) && 
                !Pattern.matches("", txtPassword.getText()) &&
                Pattern.matches("admin|user|guest", txtPermissions.getText())){
            user.addUser(txtUserName.getText(), txtPassword.getText(), 
                txtPermissions.getText());
        
            messageTitle = "Successful operation!";
            messageHeader = "Successfully added user:";
            messageContent = String.format("User name: %s%nPassowrd: %s%n"
                + "Permissions: %s%n", txtUserName.getText(), txtPassword.getText(),
                txtPermissions.getText());
        
            informationMessage(messageTitle, messageHeader, messageContent);
        
            txtUserName.setText("");
            txtPassword.setText("");
            txtPermissions.setText("");
        } else {
            messageTitle = "Warning";
            messageHeader = "Missing user name, password or permissions or "
                    + "wrong permissions.";
            messageContent = "Please input user name, password and permissions."
                    + "The permissions should be one of the following: "
                    + "admin, user or guest.";
            
            warningMessage(messageTitle, messageHeader, messageContent);
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
        assert cbChoice != null : "fx:id=\"cbChoice\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert pnlUserComponent != null : "fx:id=\"pnlUserComponent\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert btnSubmit != null : "fx:id=\"btnSubmit\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert txtUserName != null : "fx:id=\"txtUserName\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
        assert txtPermissions != null : "fx:id=\"txtPermissions\" was not injected: check your FXML file 'UserInterfaceFXMLDocument.fxml'.";
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

