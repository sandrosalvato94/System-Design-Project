package polito.sdp2017.DesignEnvironmentGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ScreenStartController implements ControlledScreen {
    ScreensController myController;
        
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }
}
