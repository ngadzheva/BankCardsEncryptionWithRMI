package usergui;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import servergui.ServerInterfaceImpl;
import servergui.ServerGUI;
import servergui.ServerGUIController;
import servergui.ServerInterface;

/**
 * UserInterface: UserInterface.java
 * Launch the user GUI.
 */
public class UserGUI extends Application {
    /**
     * Reference to the ServerInterface
     */
    private ServerInterface user;
    /**
     * Reference to the user controller
     */
    private UserGUIController userController;
    /**
     * Hold the Title, Header and Content of a dialog window
     */
    private String errorTitle, errorHeader, errorContent;
    
    @Override
    public void start(Stage stage) throws Exception {
        /**
         * Load LoginGUIFXMLDocument.fxml, containing the description of the
         * user gui
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserGUI.class.getResource("LoginGUIFXMLDocument.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        /**
         * Retrieve the user controller
         */
        userController = loader.getController();

        /**
         * Initialize RMI
         */
        initializeRMI();
        
        stage.setScene(scene);
        stage.setOnCloseRequest(evt->stop());
        stage.setTitle("Login");
        stage.show();
        
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
            userController.setUser(user);
        } catch (Exception e) {
            System.out.println(e);
            
            errorTitle = "Connection failed";
            errorHeader = "Connection problem";
            errorContent = "Connection to the server failed.";
            
            errorMessage(errorTitle, errorHeader, errorContent);
            stop();
        }
    }
    
    /**
     * Show error message in a dialog window
     * @param errorTitle
     * @param errorHeader
     * @param errorContent 
     */
    private void errorMessage(String errorTitle, String errorHeader, String errorContent){
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        
        errorMessage.setTitle(errorTitle);
        errorMessage.setHeaderText(errorHeader);
        errorMessage.setContentText(errorContent);
        
        errorMessage.showAndWait();
    }
    
    /**
     * Shutdown the application and all running threads
     */
    public void stop(){
        Platform.exit();
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
