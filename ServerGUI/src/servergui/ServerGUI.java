package servergui;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * ServerInterface: ServerInterface.java
 * Launch the server GUI
 */
public class ServerGUI extends Application {
    /**
     * Hold the Title, Header and Content of a dialog window
     */
    private String errorTitle, errorHeader, errorContent;
    /**
     * Reference to the server controller
     */
    private ServerGUIController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        /**
         * Load ServerGUIFXMLDocument.fxml, containing the description of
         * the server gui
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ServerGUI.class.getResource("ServerGUIFXMLDocument.fxml"));
        Parent root = loader.load();
               
        Scene scene = new Scene(root);
        
        /**
         * Retrieve the server controller
         */
        controller = loader.getController();
        
        /**
         * Initialize RMI
         */
        initialize();
        
        stage.setScene(scene);
        stage.setOnCloseRequest(evt->stop());
        stage.setTitle("Server");
        stage.show();
    }

    /**
     * Initialize RMI
     * Create and register a server object
     */
    private void initialize(){
        try {
            controller.setServerLog("Server is connecting...\n");
            
            ServerInterfaceImpl server = new ServerInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("encryptable", server);
            server.setRegister(registry);
            
            controller.setServerLog("Server is ready!\n");
        } catch (Exception e) {
            System.out.println(e);
            
            errorTitle = "Connection failed";
            errorHeader = "Connection problem";
            errorContent = "Connection to the server failed.";
            
            errorMessage(errorTitle, errorHeader, errorContent);
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
