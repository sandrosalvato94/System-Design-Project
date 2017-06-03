package polito.sdp2017.DesignEnvironmentGui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import polito.sdp2017.Components.IP;

public class ScreenCreateConfigController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;
    IP focusedIP;

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
    @FXML private CheckBox isCore;
    @FXML private TextArea logArea;
    @FXML private ListView<String> searchedIP;

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
		List<IP> foundIPs;
		LinkedList<String> pars = new LinkedList<String>();
		String s;
		
		searchedIP.getItems().clear();
		
		if (isCore.isSelected()) {
			pars.add("true");
		} else {
			pars.add("false");
		}
		
		s = IPIdentifier.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = IPName.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = maxLUTs.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			try {
				Integer i = Integer.parseInt(s);
				if (i < 0) {
					throw new NumberFormatException();
				}
				pars.add(s);
			} catch (NumberFormatException nfe) {
				logArea.appendText("[ERROR] field max LUTs is not valid, ignored...\n");
				pars.add("$");
			}
		}
		
		s = maxFFs.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			try {
				Integer i = Integer.parseInt(s);
				if (i < 0) {
					throw new NumberFormatException();
				}
				pars.add(s);
			} catch (NumberFormatException nfe) {
				logArea.appendText("[ERROR] field max LUTs is not valid, ignored...\n");
				pars.add("$");
			}
		}
		
		s = maxLatency.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			try {
				Double i = Double.parseDouble(s);
				if (i < 0) {
					throw new NumberFormatException();
				}
				pars.add(s);
			} catch (NumberFormatException nfe) {
				logArea.appendText("[ERROR] field max latency is not valid, ignored...\n");
				pars.add("$");
			}
		}
		
		s = maxPowerConsumption.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			try {
				Double i = Double.parseDouble(s);
				if (i < 0) {
					throw new NumberFormatException();
				}
				pars.add(s);
			} catch (NumberFormatException nfe) {
				logArea.appendText("[ERROR] field max power consumption is not valid, ignored...\n");
				pars.add("$");
			}
		}	
	
		s = maxClockFrequency.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			try {
				Double i = Double.parseDouble(s);
				if (i < 0) {
					throw new NumberFormatException();
				}
				pars.add(s);
			} catch (NumberFormatException nfe) {
				logArea.appendText("[ERROR] field max clock frequency is not valid, ignored...\n");
				pars.add("$");
			}
		}
		
		s = contactPointId.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = contactPointName.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = company.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		foundIPs = applicationModel.searchIPs(pars);
		if (foundIPs.isEmpty()) {
			logArea.appendText("[ERROR] no suitable IP found...\n");
		} else {
			Map<String,IP> map = new HashMap<String,IP>();
			for (IP i : foundIPs) {
				map.put(i.getIdIP(), i);
			}
			
			ObservableList<String> items = FXCollections.observableArrayList (map.keySet());
			searchedIP.setItems(items);
			searchedIP.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			    focusedIP = map.get(newValue);
				logArea.setText(focusedIP.getBrief());
			});
		}	
	}
		
	@FXML
	public void addIPToConf (ActionEvent event) {
		
		logArea.appendText("add ip to configuration\n");
	}
}
