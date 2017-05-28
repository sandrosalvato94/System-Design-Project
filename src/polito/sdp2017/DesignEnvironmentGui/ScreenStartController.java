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
			logTextArea.appendText("Connection enstablished, database ready...");
		} catch (ClassNotFoundException | SQLException e) {
			logTextArea.appendText("Unable to connect to database...\n");
		}
    }
    
    @FXML
    private void goToAddRemoveIP(ActionEvent event){
    	myController.setScreen(Main.screenManageIPID);
    }
    
    @FXML
    private void goToCreateConfig(ActionEvent event){
    	myController.setScreen(Main.screenCreateConfigID);
    }
    
    @FXML
    private void goToLoadConfig(ActionEvent event){
    	myController.setScreen(Main.screenLoadConfigID);
    }
}
