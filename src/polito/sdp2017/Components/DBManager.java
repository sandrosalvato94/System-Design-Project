package polito.sdp2017.Components;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface is a description of a database manager regardless of the used DBMS. 
 */
public interface DBManager {
	/**
	 * This method open the session with the database. Starting from now all other methods of this interface
	 * can run.
	 * @param dbPath : It's the path referred to the position of the database in the file system
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void openConnection(String dbPath) throws ClassNotFoundException, SQLException;
	
	/**
	 * It concludes the database session.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException;
	
	/**
	 * Getter of the database path in the file system.
	 * @return  A string referring to the path.
	 */
	public String getDBPath();
	
	/**
	 * Setter of the name for the database.
	 * @param DBName : The name to assign to the database.
	 */
	public void setDBName(String DBName);
	
	/**
	 * Getter of the name for the database.
	 * @return A string referring to the name of that database.
	 */
	public String getDBName();
	
	/**
	 * This method cleans the database content.
	 */
	public void resetDatabase();
	
	/**
	 * An IP selection query is executed in order to obtain all those IPs that satisfy a set of properties
	 * listed in listOfParameters.
	 * @return A linkedList of object IPs, filled by the ResultSet of the query.
	 */
	public LinkedList<IP> searchIP(LinkedList<String> listOfParameters);
	
	/**
	 * An IP insertion is performed on the database.
	 * @param ipToBeAdded is the filled IP, ready to be inserted
	 * @throws SQLException
	 */
	public void addIP(IP ipToBeAdded) throws SQLException;
	
	/**
	 * An IP remotion is performed on the database.
	 * @param name: it's the name of the IP to be removed.
	 * @param id: it's the id of the IP to be removed.
	 * @param isCore: it's referred to the fact whether the IP is a Core or a Manager
	 * @return true if everything ends well, otherwise false
	 */
	public boolean removeIP(String name, String id, boolean isCore);
	
	/**
	 * A FPGAConfiguration selection query is executed in order to obtain all those configurations that satisfy a set of properties
	 * listed in listOfParameters.
	 * @param listOfParameters: list of parameters selected by the final user.
	 * @return A linkedList of object FPGAConfigurations, filled by the ResultSet of the query.
	 */
	public LinkedList<FPGAConfiguration> searchConfiguration(List<String> listOfParameters);
	
	/**
	 * An FPGAConfiguration remotion is performed on the database.
	 * @param name: it's the name of the configuration to be removed.
	 * @param id: it's the id of the configuration to be removed.
	 * @return true if everything ends well, otherwise false
	 */
	public boolean removeConfiguration(String name, String id);
	
	/**
	 * Customized toString().
	 * @return main information on the database.
	 */
	public String toString();
	
	/**
	 * A FPGAConfiguration insertion is performed on the database.
	 * @param conf is the filled configuration, ready to be inserted
	 */
	void addConfiguration(FPGAConfiguration conf);
}
