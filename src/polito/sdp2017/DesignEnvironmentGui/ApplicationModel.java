package polito.sdp2017.DesignEnvironmentGui;

import java.sql.SQLException;

import polito.sdp2017.Components.DBManager;
import polito.sdp2017.Components.SQLiteManager;

public class ApplicationModel {
	boolean databaseConnectionUp;
	DBManager database;
	
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

}
