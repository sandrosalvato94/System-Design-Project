package polito.sdp2017.DesignEnvironmentGui;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScreenStartController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;

    @FXML private TextField databasePath;
    @FXML private TextArea logTextArea;

    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
		
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
    
    @FXML
    private void generateNewDB(ActionEvent event){
    	logTextArea.appendText("[ATTENTION] function still to be implemented\n");
    }
    
    @FXML
    private void goToAddRemoveIP(ActionEvent event){
    	if (applicationModel.isConnected()) {
        	myController.setScreen(Main.screenManageIPID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
    
    @FXML
    private void goToCreateConfig(ActionEvent event){
    	if (applicationModel.isConnected()) {
        	myController.setScreen(Main.screenCreateConfigID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
    
    @FXML
    private void goToLoadConfig(ActionEvent event){
    	if (applicationModel.isConnected()) {
    		myController.setScreen(Main.screenManageConfigID);
    	} else {
    		logTextArea.appendText("[ERROR] You are not connected to any database...\n");
    	}
    }
}
