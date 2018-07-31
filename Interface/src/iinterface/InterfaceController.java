package iinterface;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Interface: InterfaceController.java
 * Control the basic interface of the application
 */
public class InterfaceController extends AnchorPane{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtUserName;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;
    
    /**
     * Default constructor
         * Load the basic GUI of the application - InterfaceFXMLDocument.fxml
     */
    public InterfaceController(){
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource(
                "/interface/InterfaceFXMLDocument.fxml"));
        
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private void onLogin(ActionEvent event) {
        
    }

    @FXML
    private void onCancel(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'LoginInterfaceFXMLDocument.fxml'.";
        assert txtUserName != null : "fx:id=\"txtUserName\" was not injected: check your FXML file 'LoginInterfaceFXMLDocument.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'LoginInterfaceFXMLDocument.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'LoginInterfaceFXMLDocument.fxml'.";

    }
}

