package polito.sdp2017.DesignEnvironmentGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.util.List;

import org.xml.sax.SAXException;

public class ScreenManageIPController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;

    @FXML private TextField XMLPath;
    @FXML private TextField IPIdentifier;
    @FXML private TextField IPName;
    @FXML private TextArea logTextAreaAdd;
    @FXML private TextArea logTextAreaRemove;
    @FXML private CheckBox isCore;
    
    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
	
	@FXML
	public void addIPByXML (ActionEvent event) {
		logTextAreaAdd.appendText("Retrieving XML file:\n\t"+XMLPath.getText()+"\n");
		try {
			List<String> insertedIP;
			insertedIP = applicationModel.insertIPByXml(XMLPath.getText());
			for (String s : insertedIP) {
				logTextAreaAdd.appendText(s);
			}
		} catch (FileNotFoundException e) {
			logTextAreaAdd.appendText("[ERROR] XML file does not exists...\n");
		} catch (SAXException e) {
			logTextAreaAdd.appendText("[ERROR] Provided XML does not match the requested schema...\n");
		} catch (Exception e) {
			logTextAreaAdd.appendText("[ERROR] Unable to retrieve IPs from XML file... "+e.toString()+"\n");
		}
	}
	
	@FXML
	public void removeIP (ActionEvent event) {
		String id = new String("$");
		String name = new String("$");
		boolean core = false;
		logTextAreaRemove.appendText("Removing IP:\n");
		if (!IPIdentifier.getText().trim().equals("")) {
			id = IPIdentifier.getText();
			logTextAreaRemove.appendText("\tIP identifier : "+id+"\n");
		}
		if (!IPName.getText().trim().equals("")) {
			name = IPName.getText();
			logTextAreaRemove.appendText("\tIP name : "+name+"\n");
		}
		if (isCore.isSelected()) {
			core = true;
			logTextAreaRemove.appendText("\tlooking for IP core...\n");
		} else {
			core = false;
			logTextAreaRemove.appendText("\tlooking for IP manager...\n");
		}
		
		if (applicationModel.removeIP(id, name, core)) {
			logTextAreaRemove.appendText("[OK] removed...\n");
		} else {
			logTextAreaRemove.appendText("[ERROR] not removed...\n");
		}
	}
	
	@FXML
	public void goBack (ActionEvent event) {
		myController.setScreen(Main.screenStartID);
	}
}
