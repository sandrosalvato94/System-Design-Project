package polito.sdp2017.DesignEnvironmentGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScreenCreateConfigController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;

    @FXML private TextField IPIdentifier;
    @FXML private TextField IPName;
    @FXML private TextField maxLUTs;
    @FXML private TextField maxFFs;
    @FXML private TextField maxLatency;
    @FXML private TextField maxPowerConsumption;
    @FXML private TextField maxClockFrequency;
    @FXML private TextField contactPointId;
    @FXML private TextField contactPointName;
    @FXML private TextField company;    
    @FXML private TextField physicalAddress;
    @FXML private TextField interruptPriority;
    @FXML private TextArea logArea;
    
    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
	
	@FXML
	public void goBack (ActionEvent event) {
		myController.setScreen(Main.screenStartID);
	}
	
	@FXML
	public void searchIP (ActionEvent event) {
		logArea.appendText("search\n");
	}
	
	@FXML
	public void addIPToConf (ActionEvent event) {
		logArea.appendText("add ip to configuration\n");
	}
}
