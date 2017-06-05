package polito.sdp2017.DesignEnvironmentGui;

import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import polito.sdp2017.Components.FPGAConfiguration;

public class ScreenManageConfigController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;
    
    Map<String, FPGAConfiguration> map = new HashMap<String,FPGAConfiguration>();
    FPGAConfiguration focusedConf;
    
    @FXML private TextField maxIPs;
    @FXML private TextField confId;
    @FXML private TextField confName;
    @FXML private TextField maxLUTs;
    @FXML private TextField maxFFs;
    @FXML private TextField maxLatency;
    @FXML private TextField maxPowerConsumption;
    @FXML private TextField maxClockFrequency;
    @FXML private TextField contactPointId;
    @FXML private TextField contactPointName;
    @FXML private TextField company;       
    @FXML private TextArea logArea;
    @FXML private ListView<String> searchedConf;

    @Override
    public void setScreenParent(ScreensController screenParent){
    	myController = screenParent;
    }

	@Override
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
	
    @FXML
    public void initialize() {
		searchedConf.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				focusedConf = map.get(newValue);
				logArea.setText(focusedConf.getBrief());
			}
		});
    }
	
	@FXML
	public void goBack (ActionEvent event) {
		myController.setScreen(Main.screenStartID);
	}
	
	@FXML
	public void resetConf (ActionEvent event) {
	    confId.setText("");
	    confName.setText("");
	    maxLUTs.setText("");
	    maxFFs.setText("");
	    maxLatency.setText("");
	    maxPowerConsumption.setText("");
	    maxClockFrequency.setText("");
	    contactPointId.setText("");
	    contactPointName.setText("");
	    company.setText("");
	    contactPointId.setText("");
	    contactPointName.setText("");
	    company.setText("");
	    logArea.setText("");
		
		focusedConf = null;
		map.clear();
		searchedConf.getItems().clear();
	}
	
	@FXML
	public void searchConf (ActionEvent event) {
		List<FPGAConfiguration> foundConfs;
		LinkedList<String> pars = new LinkedList<String>();
		String s;
		
		s = maxIPs.getText();
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
				logArea.appendText("[ERROR] field max IPs is not valid, ignored...\n");
				pars.add("$");
			}
		}
		
		s = confId.getText();
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = confName.getText();
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
		
		foundConfs = applicationModel.searchConfs(pars);
		
		if (foundConfs.isEmpty()) {
			logArea.appendText("[ERROR] no suitable configuration found...\n");
		} else {
			map.clear();
			
			for (FPGAConfiguration c : foundConfs) {
				map.put(c.getIdConf(), c);
			}

			searchedConf.getItems().clear();
			ObservableList<String> items = FXCollections.observableArrayList (map.keySet());
			searchedConf.setItems(items);
		}	
	}
	
	@FXML
	public void loadConf (ActionEvent event) {
		if (focusedConf != null) {
			File bitstream = new File(focusedConf.getBitstreamPath());
			if (bitstream.exists() && bitstream.isFile()) {
				logArea.setText("[OK] bitstream file found :\n");
				try(BufferedReader in  = new BufferedReader(new FileReader(bitstream))) {		
					in.lines().forEach(l->logArea.appendText(l+"\n"));
				} catch(Exception e) {
					logArea.appendText("[ERROR] IO error, unable to open file...\n");
				}
			} else {
				logArea.appendText("[ERROR] no configuration selected...\n");
			}
		}
	}
	
	@FXML
	public void deleteConf (ActionEvent event) {
		boolean res = false;
		if (focusedConf != null) {
			res = applicationModel.deleteConf(focusedConf.getName(), focusedConf.getIdConf());
			if (res) {
				logArea.appendText("[OK] configuration removed\n");
			} else {
				logArea.appendText("[ERROR] unable to remove configuration...\n");
			}
		} else {
			logArea.appendText("[ERROR] no configuration selected...\n");
		}
	}
}
