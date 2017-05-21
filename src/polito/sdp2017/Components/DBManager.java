package polito.sdp2017.Components;

import java.util.LinkedList;
import java.util.List;

public interface DBManager {
	public void setDBPath(String DBPath);
	public String getDBPath();
	public void setDBName(String DBName);
	public String getDBName();
	public void generateNewDatabase(String path);
	public LinkedList<IP> searchIP(List<String> listOfParameters);
	public void addIP(IP ipToBeAdded);
	public boolean removeIP(String name, String id, boolean isCore);
	public LinkedList<FPGAConfiguration> searchConfiguration(List<String> listOfParameters);
	public boolean removeConfiguration(String name, String id);
	public String toString();
	public boolean compareTo(Object o);
	public int hashCode();
	void addConfiguration(FPGAConfiguration conf);
}
