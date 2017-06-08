package polito.sdp2017.Components;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface is a description of a database manager regardless of the used DBMS. 
 */
public interface DBManager {
	public void openConnection(String dbPath) throws ClassNotFoundException, SQLException;
	public void closeConnection() throws SQLException;
	
	public String getDBPath();
	public void setDBName(String DBName);
	public String getDBName();
	public void resetDatabase();
	public LinkedList<IP> searchIP(LinkedList<String> listOfParameters);
	public void addIP(IP ipToBeAdded) throws SQLException;
	public boolean removeIP(String name, String id, boolean isCore);
	public LinkedList<FPGAConfiguration> searchConfiguration(List<String> listOfParameters);
	public boolean removeConfiguration(String name, String id);
	public String toString();
	void addConfiguration(FPGAConfiguration conf);
}
