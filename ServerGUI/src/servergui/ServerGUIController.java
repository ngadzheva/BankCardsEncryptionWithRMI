package servergui;

import courseprojectno4.BankCardsSortedByEncryption;
import courseprojectno4.BankCardsSortedByCardNumber;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * ServerInterface: ServerInterfaceController.java
 * Control the server GUI
 */
public class ServerGUIController {
    /**
     * Reference to a BankCardsSortedByCardNumber object
     */
    private BankCardsSortedByCardNumber sortedByCardNumber;
    /**
     * Reference to a BankCardsSortedByEncryption object
     */
    private BankCardsSortedByEncryption sortedByEncryption;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem mnuSortedByEncryption;

    @FXML
    private AnchorPane apnRootPanel;

    @FXML
    private MenuItem mnuSortedByCardNumber;

    @FXML
    private MenuItem mnuExit;
    
    @FXML
    private TextArea txaLog;
    
    /**
     * Write messages to the text area
     * @param log 
     */
    @FXML
    public void setServerLog(String log){
        txaLog.appendText(log);
    }
    
    
    /**
     * Load a file with table of the bank cards, sorted by their number
     * @param event 
     */
    @FXML
    private void onSortedByCardNumber(ActionEvent event){        
        txaLog.appendText("Reading the bank cards and their encryptions sorted "
                + "by bank card number...\n");
        
        txaLog.appendText(sortedByCardNumber.readRecords());
    }
    
    /**
     * Load a file with table of the bank cards, sorted by their encrypted number
     * @param event 
     */
    @FXML
    private void onSortedByEncryption(ActionEvent event){        
        txaLog.appendText("Reading the bank cards and their encryptions sorted"
                + " by encryption...\n");
        
        txaLog.appendText(sortedByEncryption.readRecords());
    }
    
    /**
     * Close the server GUI
     * @param event 
     */
    @FXML
    private void onExit(ActionEvent event){
        Platform.exit();
    }

    /**
     * Initialization
     */
    @FXML
    void initialize() throws RemoteException {
        sortedByCardNumber = new BankCardsSortedByCardNumber();
        sortedByEncryption = new BankCardsSortedByEncryption();
        
        assert mnuSortedByEncryption != null : "fx:id=\"mnuSortedByEncryption\" was not injected: check your FXML file 'ServerInterfaceFXMLDocument.fxml'.";
        assert apnRootPanel != null : "fx:id=\"apnRootPanel\" was not injected: check your FXML file 'ServerInterfaceFXMLDocument.fxml'.";
        assert mnuSortedByCardNumber != null : "fx:id=\"mnuSortedByCardNumber\" was not injected: check your FXML file 'ServerInterfaceFXMLDocument.fxml'.";
        assert mnuExit != null : "fx:id=\"mnuExit\" was not injected: check your FXML file 'ServerInterfaceFXMLDocument.fxml'.";
    }
}

