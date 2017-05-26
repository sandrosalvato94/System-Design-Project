package polito.sdp2017.DesignEnvironmentGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScreenStartController implements ControlledScreen {
    ScreensController myController;

    @FXML private TextField databasePath;
    @FXML private TextArea logTextArea;

    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }
    
    @FXML
    private void openDatabaseConnection(ActionEvent event){
    	logTextArea.appendText("Enstablish connection to :\n\t"+databasePath.getText()+"\n");
    }
    
    @FXML
    private void goToAddRemoveIP(ActionEvent event){

    }
    
    @FXML
    private void goToCreateConfig(ActionEvent event){

    }
    
    @FXML
    private void goToLoadConfig(ActionEvent event){

    }
}
