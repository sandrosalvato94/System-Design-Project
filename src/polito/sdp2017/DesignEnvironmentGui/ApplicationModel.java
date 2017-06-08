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

/**
 * A Model class for the application, provides all the functionalities the View and the Controllers
 * need for accessing the data managed by the application logic.
 */
public class ApplicationModel {
	private boolean databaseConnectionUp;	//	flag for monitoring if a database connection is
											//	set up
	private DBManager database;				//	reference to the database
	private List<MappedIP> mapped;			//	list of mapped IPs
	private IPManager manager;				//	manager selected
	
	/**
	 * constructor for the application model, simply instantiate some variables.
	 */
	public ApplicationModel() {
		super();
		database = new SQLiteManager();
		mapped = new LinkedList<MappedIP>();
		databaseConnectionUp = false;
	}
	
	/**
	 * Try to open a connection toward a database whose path is specified as parameter. If any
	 * exception is launched by the methods called it is simply passed to the calling function.
	 * @param dbPath : path of the database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void openDatabaseConnection(String dbPath) throws ClassNotFoundException, SQLException {
		database.openConnection(dbPath);
		databaseConnectionUp = true;
	}
	
	/**
	 * Try to close a connection toward a database whose path is specified as parameter. If any
	 * exception is launched by the methods called it is simply passed to the calling function.
	 * @param dbPath : path of the database
	 * @throws SQLException
	 */
	public void closeDatabaseConnection(String dbPath) throws SQLException {
		database.closeConnection();
		databaseConnectionUp = false;
	}
	
	/**
	 * Checks if a database connection is up.
	 * @return if the database connection is up
	 */
	public boolean isConnected() {
		return databaseConnectionUp;
	}

	/**
	 * Insert a new IPs in the database, given the XML file specified as input. The application
	 * produces a list of logs string to be delivered to the calling function, for tracing if
	 * the method acted correctly. Any exception due to a wrong XML schema are passed to the calling
	 * function, while the exceptions caused by adding to the database an already existing IP are
	 * kept in the list of String.
	 * @param xmlPath : XML file path to be parsed for retrieving IPs
	 * @return a list of String as set of logs
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
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

	/**
	 * Attempt to remove an IP from the database.
	 * @param id     : identifier for the IP to be removed
	 * @param name   : name of the IP to be removed
	 * @param isCore : a flag for signaling if the specified id refers to a Core or to a Manager
	 * @return a flag specifying if the IP was correctly removed
	 */
	public boolean removeIP(String id, String name, boolean isCore) {
		return database.removeIP(name, id, isCore);
	}

	/**
	 * Search an IP in the database, given a set of parameter passed as list of String.
	 * @param pars : list of String containing the search parameter for building the SQL query
	 * @return a list containing all the IPs which matched the searching parameters
	 */
	public List<IP> searchIPs(LinkedList<String> pars) {
		return database.searchIP(pars);
	}

	/**
	 * Add an IP to the list of MappedIP used for building up a configuration.
	 * @param mip : MappedIP to be added
	 */
	public void addMappedIp(MappedIP mip) {
		mapped.add(mip);	
	}

	/**
	 * Set an IPManager as a manager for the configuration to be built.
	 * @param focusedIP : IPManager to be added
	 */
	public void setManager(IPManager focusedIP) {
		manager = focusedIP;	
	}
	
	/**
	 * Retrieve the list of MappedIPs selected until now.
	 * @return the list of MappedIPs
	 */
	public List<MappedIP> getMapped() {
		return mapped;
	}

	/**
	 * Retrieve the last manager selected.
	 * @return the last manager selected for the configuration to be built
	 */
	public IPManager getManager() {
		return manager;
	}

	/**
	 * Empty the list of MappedIPs and the reference to the manager.
	 */
	public void resetIPs() {
		manager = null;
		mapped.clear();
	}

	/**
	 * Search a configuration in the database, given a set of parameter passed as list of String.
	 * @param pars : list of String containing the search parameter for building the SQL query
	 * @return a list containing all the configurations which matched the searching parameters
	 */
	public List<FPGAConfiguration> searchConfs(LinkedList<String> pars) {
		return database.searchConfiguration(pars);
	}

	/**
	 * Attempt to remove a configuration from the database.
	 * @param name   : name of the configuration to be removed
	 * @param idConf : id for the configuration to be deleted
	 * @return a flag specifying if the IP was correctly removed
	 */
	public boolean deleteConf(String name, String idConf) {
		return database.removeConfiguration(name, idConf);
	}

	/**
	 * Reset the database to the factory state, everything that was previously inserted is
	 * deleted.
	 */
	public void resetDatabase() {
		database.resetDatabase();
	}

	public DBManager getDB() {
		return database;
	}
}
