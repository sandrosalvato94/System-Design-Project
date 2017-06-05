package polito.sdp2017.DesignEnvironmentGui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import polito.sdp2017.Components.Author;
import polito.sdp2017.Components.FPGAConfiguration;
import polito.sdp2017.Components.IP;
import polito.sdp2017.Components.IPCore;
import polito.sdp2017.Components.IPManager;
import polito.sdp2017.Components.MappedIP;

public class ScreenCreateConfigController implements ControlledScreen {
	ApplicationModel applicationModel;
    ScreensController myController;
    Map<String,IP> map = new HashMap<String,IP>();
    int mappedIpCnt = 0;
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
    @FXML private TextField confContactPointId;
    @FXML private TextField confContactPointName;
    @FXML private TextField confCompany;    
    @FXML private TextField confEmail;
    @FXML private TextField confRole;
    @FXML private TextField confName;
    @FXML private CheckBox isCore;
    @FXML private TextArea logArea;
    @FXML private TextArea reportArea;
    @FXML private TextArea createConfLogArea;
    @FXML private ListView<String> searchedIP;
    @FXML private ProgressBar progressBar;

    @FXML
    public void initialize() {
		searchedIP.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				focusedIP = map.get(newValue);
				logArea.setText(focusedIP.getBrief());
			}
		});
    }
    
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
			map.clear();
			
			for (IP i : foundIPs) {
				map.put(i.getIdIP(), i);
			}
			
			searchedIP.getItems().clear();
			ObservableList<String> items = FXCollections.observableArrayList (map.keySet());
			searchedIP.setItems(items);
		}	
	}
	
	@FXML
	public void resetConf (ActionEvent event) {	
	    IPIdentifier.setText("");
	    IPName.setText("");
	    maxLUTs.setText("");
	    maxFFs.setText("");
	    maxLatency.setText("");
	    maxPowerConsumption.setText("");
	    maxClockFrequency.setText("");
	    contactPointId.setText("");
	    contactPointName.setText("");
	    company.setText("");
	    physicalAddress.setText("");
	    interruptPriority.setText("");
	    confContactPointId.setText("");
	    confContactPointName.setText("");
	    confCompany.setText("");
	    confEmail.setText("");
	    confRole.setText("");
	    confName.setText("");
	    isCore.setSelected(false);
	    logArea.setText("");
	    reportArea.setText("");
	    createConfLogArea.setText("");
	    progressBar.setProgress(0);
		
		focusedIP = null;
		map.clear();
		mappedIpCnt = 0;
		searchedIP.getItems().clear();
		
		applicationModel.resetIPs();
	}
	
	@FXML
	public void addIPToConf (ActionEvent event) {
		int priority = -1, phyAddress = -1;
		
		try {			
			if(focusedIP.getClass().getName().equals("polito.sdp2017.Components.IPCore")) {
				priority = Integer.parseInt(interruptPriority.getText());
				phyAddress = Integer.decode("0x"+physicalAddress.getText());
				if (priority < 0 || phyAddress < 0) {
					throw new NumberFormatException();
				}
				MappedIP mip = new MappedIP(String.valueOf(mappedIpCnt),(IPCore)focusedIP,
												priority,"0x"+physicalAddress.getText());
				mappedIpCnt++;
				applicationModel.addMappedIp(mip);
				logArea.appendText("[OK] IPCore ("+focusedIP.getIdIP()+") added with:\n");
				logArea.appendText("\tpriority    : "+priority+"\n");
				logArea.appendText("\tphy address : "+phyAddress+"\n");
			} else {
				if (applicationModel.getManager() != null) {
					logArea.appendText("[WARNING] previous IPManager overwritten...\n");
				}
				applicationModel.setManager((IPManager)focusedIP);
				logArea.appendText("[OK] IPManager ("+focusedIP.getIdIP()+") added...\n");
			}
		} catch (NumberFormatException nfe) {
			logArea.appendText("[ERROR] priority or physical address not valid...\n");
		} catch (NullPointerException npe) {
			logArea.appendText("[ERROR] nothing is selected...\n");
		}
		interruptPriority.setText("");
		physicalAddress.setText("");
	}
	
	@FXML
	public void printReport (ActionEvent event) {
		String confContactId = confContactPointId.getText();
		String confContactName = confContactPointName.getText();
		String confContactMail = confEmail.getText();
		String confContactCompany = confCompany.getText();
		String confContactRole = confRole.getText();
		String confUserName = confName.getText();
				
		if (confContactId.trim().equals("")) {
			confContactId = "unknown";
		}
		if (confContactName.trim().equals("")) {
			confContactName = "unknown";
		}
		if (confContactMail.trim().equals("")) {
			confContactMail = "unknown";
		}
		if (confContactCompany.trim().equals("")) {
			confContactCompany = "unknown";
		}
		if (confContactRole.trim().equals("")) {
			confContactRole = "unknown";
		}
		if (confUserName.trim().equals("")) {
			confUserName = "unknown";
		}
		
		reportArea.setText("CONFIGURATION "+confUserName+" REPORT\n");
		reportArea.appendText("contact point id   : "+confContactId+"\n");
		reportArea.appendText("contact point name : "+confContactName+"\n");
		reportArea.appendText("contact point mail : "+confContactMail+"\n");
		reportArea.appendText("company            : "+confContactCompany+"\n");
		reportArea.appendText("role               : "+confContactRole+"\n");
		reportArea.appendText("\n");
		for (MappedIP m : applicationModel.getMapped()) {
			reportArea.appendText(m.getIdMappedIP()+" - "+m.getIpCore().getIdIP()+"\n");
			reportArea.appendText("    priority    : "+m.getPriority()+"\n");
			reportArea.appendText("    phy address : "+m.getPhysicalAddress()+"\n\n");
		}
		
		if (applicationModel.getManager() == null) {
			reportArea.appendText("[WARNING] manager missing\n");
		} else {
			reportArea.appendText("manager - "+applicationModel.getManager()+"\n");
		}
	}
	
	@FXML
	public void createConfiguration (ActionEvent event) {
		FPGAConfiguration fpgaConfiguration = new FPGAConfiguration(null, null, null, null,
																	null, null, null, null);
		Author contactPoint;
		String confContactId = confContactPointId.getText();
		String confContactName = confContactPointName.getText();
		String confContactMail = confEmail.getText();
		String confContactCompany = confCompany.getText();
		String confContactRole = confRole.getText();
		String confUserName = confName.getText();
		
		createConfLogArea.setText("reading configuration name...");
		if (confUserName.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tconfiguration name set to unknown\n");
			confUserName = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(0.05);
		
		createConfLogArea.appendText("reading contact point id from fields...");
		if (confContactId.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point id set to unknown...\n");
			confContactId = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.01);
		
		createConfLogArea.appendText("reading contact point name from fields...");
		if (confContactName.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point name set to unknown...\n");
			confContactName = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.01);
		
		createConfLogArea.appendText("reading contact point mail from fields...");
		if (confContactMail.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point mail set to unknown\n");
			confContactMail = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.01);
		
		createConfLogArea.appendText("reading contact point company from fields...");
		if (confContactCompany.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point company set to unknown\n");
			confContactCompany = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.01);
		
		createConfLogArea.appendText("reading contact point role from fields...");
		if (confContactRole.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point role set to unknown\n");
			confContactRole = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.01);
		
		contactPoint = new Author(confContactId, confContactName, confContactCompany,
				confContactMail, confContactRole);
		fpgaConfiguration.setContactPoint(contactPoint);
		createConfLogArea.appendText("contact point set [OK]\n");
		
		SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date localDate = new Date();
		
		String confId;
		String rndAlphabet = "0123456789abcdefghijklmnopqrtsuwxyz";
		Random rnd = new Random(System.currentTimeMillis());
		StringBuilder strb = new StringBuilder();
		for (int i=0;i<15;i++) {
			strb.append(rndAlphabet.charAt(rnd.nextInt(rndAlphabet.length()))); 
		}
		confId = dtf.format(localDate)+strb.toString();
		fpgaConfiguration.setIdConf(confId);
		createConfLogArea.appendText("configuration id created: "+confId+"... [OK]\n");
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
		createConfLogArea.appendText("retrieving IPs...");
		if (applicationModel.getManager() == null || applicationModel.getMapped().isEmpty()) {
			createConfLogArea.appendText(" [ERROR]\n\tmanager or cores missing...\n");
			progressBar.setProgress(0);
			return;
		} else {
			fpgaConfiguration.setManager(applicationModel.getManager());
			fpgaConfiguration.setMappedIPs(applicationModel.getMapped());
		}
		createConfLogArea.appendText(" [OK]\n");
		progressBar.setProgress(progressBar.getProgress()+0.1);

		createConfLogArea.appendText("additional driver source is null... [WARNING]\n");

		createConfLogArea.appendText("creating top level entity...");
		try {
			FPGAConfiguration.generateTopLevelEntity(fpgaConfiguration);
			createConfLogArea.appendText(" [OK]\n");
			progressBar.setProgress(progressBar.getProgress()+0.1);
		} catch (RuntimeException rte) {
			createConfLogArea.appendText(" [ERROR]\n\t"+rte.getMessage()+"\n");
			progressBar.setProgress(0);
			return;
		}

		createConfLogArea.appendText("retrieving HDL sources...\n");
		List<String> listOfSources = fpgaConfiguration.getMappedIPs().stream()
										.map(m -> m.getIpCore().getHdlSourcePath())
										.collect(Collectors.toList());
		listOfSources.stream()
						.forEach(l -> createConfLogArea.appendText("\t"+l+" [OK]\n"));

		// ------- END OF OUR TASKS !!!!!!
		// spawn synth process
		// wait
		// retrieve infos hardware properties
		// print ok!
		
		createConfLogArea.appendText("finish...\n");
		applicationModel.resetIPs();
	}
}
