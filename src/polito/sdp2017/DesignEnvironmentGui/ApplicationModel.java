package polito.sdp2017.DesignEnvironmentGui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import polito.sdp2017.Components.DBManager;
import polito.sdp2017.Components.FPGAConfiguration;
import polito.sdp2017.Components.IP;
import polito.sdp2017.Components.IPManager;
import polito.sdp2017.Components.MappedIP;
import polito.sdp2017.Components.SQLiteManager;

public class ApplicationModel {
	private boolean databaseConnectionUp;
	private DBManager database;
	private List<MappedIP> mapped = new LinkedList<MappedIP>();
	private IPManager manager;
	
	public ApplicationModel() {
		super();
		database = new SQLiteManager();
		databaseConnectionUp = false;
	}
	
	public void openDatabaseConnection(String dbPath) throws ClassNotFoundException, SQLException {
		database.openConnection(dbPath);
		databaseConnectionUp = true;
	}
	
	public void closeDatabaseConnection(String dbPath) throws SQLException {
		database.closeConnection();
		databaseConnectionUp = false;
	}
	
	public boolean isConnected() {
		return databaseConnectionUp;
	}

	public List<String> insertIPByXml(String xmlPath) throws SAXException, ParserConfigurationException, IOException {
		List<String> logs = new LinkedList<String>();
		List<IP> l = IP.getFromXML(xmlPath);
		
		for (IP i : l) {
			try {
				database.addIP(i);
				logs.add("[OK] IP "+i.getName()+" inserted\n");
			} catch (SQLException e) {
				logs.add("[ERROR] Unable to add IP "+i.getName()+", already exists...\n");
			}
		}
		
		return logs;
	}

	public boolean removeIP(String id, String name, boolean isCore) {
		return database.removeIP(name, id, isCore);
	}

	public List<IP> searchIPs(LinkedList<String> pars) {
		return database.searchIP(pars);
	}

	public void addMappedIp(MappedIP mip) {
		mapped.add(mip);	
	}

	public void setManager(IPManager focusedIP) {
		manager = focusedIP;	
	}
	
	public List<MappedIP> getMapped() {
		return mapped;
	}

	public IPManager getManager() {
		return manager;
	}

	public void resetIPs() {
		manager = null;
		mapped.clear();
	}

	public List<FPGAConfiguration> searchConfs(LinkedList<String> pars) {
		return database.searchConfiguration(pars);
	}

	public boolean deleteConf(String name, String idConf) {
		return database.removeConfiguration(name, idConf);
	}

	public void resetDatabase() {
		database.resetDatabase();
	}
}
