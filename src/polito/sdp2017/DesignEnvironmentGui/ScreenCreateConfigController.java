package polito.sdp2017.DesignEnvironmentGui;

import java.io.IOException;
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

/**
 * Controller for the createConfig screen of the application, which is in charge of create a new
 * configuration to be added in the database.
 */
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

    /**
     * Called by the FXML loader automatically. It is in charge of instantiating a listener for
     * showing a description for the selected item in the TextArea named reportArea.
     */
    @FXML
    public void initialize() {
		searchedIP.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				focusedIP = map.get(newValue);
				logArea.setText(focusedIP.getBrief());
			}
		});
    }
    
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
	 * Listener for events generated by the button for returning to the start screen.
	 * @param event : event the button is sensible to
	 */
	@FXML
	public void goBack (ActionEvent event) {
		myController.setScreen(Main.screenStartID);
	}
	
	/**
	 * Listener for events generated by the button for starting an IP search.
	 * @param event : event the button is sensible to
	 */
	@FXML
	public void searchIP (ActionEvent event) {
		List<IP> foundIPs;
		LinkedList<String> pars = new LinkedList<String>();
		String s;
	
		if (isCore.isSelected()) {		//	checks if the checkBox is selected
			pars.add("true");			//	string for signaling the Model that a core is searched
		} else {
			pars.add("false");			//	string for signaling the Model that a manager is searched
		}
		
		s = IPIdentifier.getText();		//	get the IP identifier if present
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = IPName.getText();			//	get the IP name if present
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = maxLUTs.getText();			//	get the maximum number of LUTs if present
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
		
		s = maxFFs.getText();			//	get the maximum number of FFs if present
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
		
		s = maxLatency.getText();		// get the maximum latency allowed if present
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
		
		s = maxPowerConsumption.getText();	//	get the maximum power consumption if present
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
	
		s = maxClockFrequency.getText();	//	get the maximum clock frequency if present
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
		
		s = contactPointId.getText();		//	get the contact point identifier if present
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = contactPointName.getText();		//	get the contact point name if present
		if (s.trim().equals("")) {
			pars.add("$");
		} else {
			pars.add(s);
		}
		
		s = company.getText();				//	get the company name if present
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
	
	/**
	 * Listener for events generated by the button for resetting the configuration build up to now.
	 * @param event : event the button is sensible to
	 */
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
	
	/**
	 * Listener for events generated by the button for adding an IP to a configuration.
	 * @param event : event the button is sensible to
	 */
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
				MappedIP mip = new MappedIP("mapIP_"+String.valueOf(mappedIpCnt),(IPCore)focusedIP,
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
	
	/**
	 * Listener for events generated by the button for printing a brief report for the configuration
	 * built up to now.
	 * @param event : event the button is sensible to
	 */
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
	
	/**
	 * Listener for events generated by the button for creating a new configuration.
	 * @param event : event the button is sensible to
	 */
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
		String confConfName = confName.getText();
		
		createConfLogArea.setText("reading configuration name...");
		if (confConfName.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tconfiguration name set to unknown\n");
			confConfName = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		fpgaConfiguration.setName(confConfName);
		progressBar.setProgress(0.15);
		
		createConfLogArea.appendText("reading contact point id from fields...");
		if (confContactId.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point id set to unknown...\n");
			confContactId = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
		createConfLogArea.appendText("reading contact point name from fields...");
		if (confContactName.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point name set to unknown...\n");
			confContactName = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
		createConfLogArea.appendText("reading contact point mail from fields...");
		if (confContactMail.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point mail set to unknown\n");
			confContactMail = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
		createConfLogArea.appendText("reading contact point company from fields...");
		if (confContactCompany.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point company set to unknown\n");
			confContactCompany = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
		createConfLogArea.appendText("reading contact point role from fields...");
		if (confContactRole.trim().equals("")) {
			createConfLogArea.appendText(" [WARNING]\n\tcontact point role set to unknown\n");
			confContactRole = "unknown";
		} else {
			createConfLogArea.appendText(" [OK]\n");
		}
		progressBar.setProgress(progressBar.getProgress()+0.05);
		
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
		progressBar.setProgress(progressBar.getProgress()+0.2);
		
		createConfLogArea.appendText("retrieving IPs...");
		if (applicationModel.getManager() == null || applicationModel.getMapped().isEmpty()) {
			createConfLogArea.appendText(" [ERROR]\n\tmanager or cores missing...\n");
			progressBar.setProgress(0);
			return;
		} else {
			int distinctAddresses = applicationModel.getMapped().stream()
							 									.map(m -> m.getPhysicalAddress())
							 									.distinct()
							 									.collect(Collectors.toList())
							 									.size();
			if (distinctAddresses != applicationModel.getMapped().size()) {
				createConfLogArea.appendText(" [ERROR]\n\tsome addresses may be repeated...\n");
				return;
			} else {
				fpgaConfiguration.setManager(applicationModel.getManager());				
				fpgaConfiguration.setMappedIPs(applicationModel.getMapped());	
			}
		}
		createConfLogArea.appendText(" [OK]\n");
		progressBar.setProgress(progressBar.getProgress()+0.2);

		createConfLogArea.appendText("additional driver source is null... [WARNING]\n");

		createConfLogArea.appendText("retrieving HDL sources...\n");
		fpgaConfiguration.getMappedIPs().stream()
										.map(m -> m.getIpCore().getHdlSourcePath())
										.forEach(l -> createConfLogArea.appendText("\t"+l+" [OK]\n"));
		
		try {
			FPGAConfiguration.createConfiguration(fpgaConfiguration,applicationModel.getDB());
			createConfLogArea.appendText("finish... [OK]\n");
		} catch (Exception e) {
			createConfLogArea.appendText("error, unable to create configuration... [ERROR]\n");
		}
		progressBar.setProgress(1);

		applicationModel.resetIPs();
	}
}
