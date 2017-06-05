package polito.sdp2017.DesignEnvironmentGui;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the main screen of the application, which is showed as soon as the application
 * starts.
 */
public class ScreenStartController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;

    @FXML private TextField databasePath;
    @FXML private TextArea logTextArea;

    /**
     * Set the parent for the screen this controller manages
     * @param screenParent : container for the screens the application is made of
     */
    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

    /**
     * Set the model of the application, assuming the Model-View_controller paradigm is used
     * @param applicationModel : model for the application
     */
	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
	
	/**
	 * Listener for events generated by the button for connecting to a database
	 * @param event : event the button is sensible to
	 */
    @FXML
    private void connectDB(ActionEvent event){
    	logTextArea.appendText("Starting connection to :\n\t"+databasePath.getText()+"\n");
    	try {
			applicationModel.openDatabaseConnection(databasePath.getText());
			logTextArea.appendText("[OK] Connection enstablished, database ready...\n");
		} catch (ClassNotFoundException | SQLException e) {
			logTextArea.appendText("[ERROR] Unable to connect to database...\n");
		} catch (IllegalArgumentException e) {
			logTextArea.appendText("[ERROR] Specified path is not an SQLite3 database...\n");
		}
    }
    
	/**
	 * Listener for events generated by the button for resetting the database
	 * @param event : event the button is sensible to
	 */
    @FXML
    private void resetDB(ActionEvent event){
    	if (applicationModel.isConnected()) {
    		applicationModel.resetDatabase();
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
    
	/**
	 * Listener for events generated by the button for setting the screen for adding or removing IPs
	 * @param event : event the button is sensible to
	 */
    @FXML
    private void goToAddRemoveIP(ActionEvent event){
    	if (applicationModel.isConnected()) {
        	myController.setScreen(Main.screenManageIPID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
    
	/**
	 * Listener for events generated by the button for setting the screen for creating a configuration
	 * @param event : event the button is sensible to
	 */
    @FXML
    private void goToCreateConfig(ActionEvent event){
    	if (applicationModel.isConnected()) {
        	myController.setScreen(Main.screenCreateConfigID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
    
	/**
	 * Listener for events generated by the button for browsing the configuration database
	 * @param event : event the button is sensible to
	 */
    @FXML
    private void goToManageConfig(ActionEvent event){
    	if (applicationModel.isConnected()) {
    		myController.setScreen(Main.screenManageConfigID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
}
