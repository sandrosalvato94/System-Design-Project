package polito.sdp2017.Components;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;


public class SQLiteManager implements DBManager {
	private Connection DBConn;
	private String DBPath;
	private String DBName;
	
	//*********************TABLE NAMES**********************//
	private final static String IPCoreLib = "IPCore";
	private final static String IPManagerLib = "IPManager";
	private final static String authorLib = "Author";
	private final static String mappedIPLib = "MappedIP";
	private final static String confLib = "FPGAConfiguration";
	//******************************************************//
	
	public static final List<String> attributesIPLibs = 
		    Collections.unmodifiableList(Arrays.asList(
		    "idIP", "name", "description", "hdlSourcePath", "LUTs", "FFs", "latency", 
		    "nMemories", "powerConsuption",
		    "maxClockFrequency", "driverPath", "contactPoint"));
	
	public static final List<String> attributesAuthorLib = 
			Collections.unmodifiableList(Arrays.asList(
		    "idAuthor", "name", "company", "email", "role"));
	
	public static final List<String> attributesFPGAConfLib = 
			Collections.unmodifiableList(Arrays.asList(
		    "idConf", "name", "bitstreamPath", "LUTs", "FFs",
		    "latency", "nMemories", "powerConsuption", "maxClockFrequency",
		    "contactPoint", "idIp", "additionalDriverSource"));
	
	public static final List<String> attributesMappedIPLib = 
			Collections.unmodifiableList(Arrays.asList(
		    "idConf", "idMappedIP", "idIP", "priority", "physicalAddress"));
	
	public static final List<String> searchingParametersIP = Collections.unmodifiableList(Arrays.asList(
		    "isIPCore", "idIP", "name", "LUTs", "FFs",  
		    "latency", "powerConsuption", "maxClockFrequency", "idAuthor", "name", "company"));
	
	public static final List<String> searchingParametersFPGAConf = Collections.unmodifiableList(Arrays.asList(
		    "nIPs", "idConf", "name", "maxLUTs", "minLUTs", "maxFFs", "minFFs", "maxLatency", 
		    "minLatency","maxNMemories", "minNMemories", "maxPowerConsuption",
		    "minPowerConsuption", "maxClockFrequency", "idAuthor", "nameAuthor", "company"));
	
	
	public static final String SQLScritpCreate = "scriptCREATE_SQLite3.sql";
	
	/*public static void main(String[] args) throws FileNotFoundException, SQLException
	{
		List<String> l = new LinkedList<>();
		StringBuilder s = new StringBuilder();
		StringBuilder d = new StringBuilder();
		StringBuilder s_fpga = new StringBuilder();
		StringBuilder d_conf = new StringBuilder();
		StringBuilder i_ip = new StringBuilder();
		Author a = new Author("1367", "Alessandro Salvato", "Politecnico di Torino", 
				"sandrosalvato94@live.it", "Chief");
		IP ip = new IPCore("BellissimoIP", "VHD_IPcore47389", "Mia personalissima propriet� intellettuale", 
				new HardwareProperties(4, 22, 2.2, 765, 12.4, 887.5), a, "C:\\ciao", "C:\\d");
		
		//importSQL(null, SQLScritpCreate);		//CREATE Database
		
		l.add("358"); l.add("idmanager4565"); l.add("Accumulator"); l.add("50"); l.add("20");
		l.add("150"); l.add("$"); l.add("2.3"); l.add("2.2"); l.add("$");
		l.add("0"); l.add("1.0"); l.add("$"); l.add("45.8"); l.add("$");
		l.add("$"); l.add("ARM"); //INSERT one IPcore
		
		//buildQuerySelectIP(l, s);					//SELECT one IP
		//System.out.println(s.toString());
		
		//buildQueryDeleteIP("PentiumAdder4", "idIP43878" , true, d);	//DELETE one IP
		//System.out.println(d.toString());
		
		//buildQuerySelectFPGAConf(l, s_fpga);
		//System.out.println(s_fpga.toString());
		
		//buildQueryDeleteConfiguration("PentiumAdder4", "idIP43878", d_conf);	//DELETE one IP
		//System.out.println(d_conf.toString());
		
		//buildQueryInsertIP(ip, i_ip);
		//System.out.println(i_ip);
	}*/
	
	static public boolean isSQLite3 (String dbPath) {
		final String SQLite3Signature = "SQLite format 3"+'\0';
		byte[] buffer = new byte[16];
		File dbFile = new File(dbPath);
		
		if (!dbFile.exists() || !dbFile.isFile()) {
			return false;
		}
		
		if (dbFile.length() < 16) {
			return false;
		}
		
		try {
			InputStream is = new FileInputStream(dbPath);
			is.read(buffer);
			is.close();
		} catch (IOException e) {
			return false;
		}

		return SQLite3Signature.equals(new String(buffer, StandardCharsets.UTF_8));
	}
	
	@Override
	public void openConnection(String dbPath) throws ClassNotFoundException, SQLException {
		if (!isSQLite3(dbPath)) {
			throw new IllegalArgumentException("path do not belong to an SQLite3 database");
		}

		Class.forName("org.sqlite.JDBC");
		DBPath = "jdbc:sqlite:"+dbPath;
		System.out.println(DBPath);
		//SQLiteConfig config = new SQLiteConfig();  
        //config.enforceForeignKeys(true);
		//DBConn = DriverManager.getConnection(DBPath/*, config.toProperties()*/);
		
		DBConn = DriverManager.getConnection(DBPath);

		Statement state = DBConn.createStatement();
		ResultSet rs = state.executeQuery("SELECT * FROM IPCore"); // tests if connection is active
		rs.close();
	}
	
	@Override
	public void closeConnection() throws SQLException {
		DBConn.close();
	}
	
	@Override
	public String getDBPath() {
		return this.DBPath;
	}

	@Override
	public void setDBName(String DBName) {
		this.DBName = DBName;
	}

	@Override
	public String getDBName() {
		return this.DBName;
	}

	@Override
	public void generateNewDatabase(String path) {
		try {
			importSQL(DBConn, SQLScritpCreate);
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
	}

	@Override
	public LinkedList<IP> searchIP(LinkedList<String> listOfParameters) {
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		LinkedList<IP> result = new LinkedList<IP>();
		String libIP, libAuth;
		
		try {
			state = DBConn.createStatement();
			buildQuerySelectIP(listOfParameters, query);
			RS = state.executeQuery(query.toString());
			if(listOfParameters.get(0).equals("true")) {
				libIP = IPCoreLib;
				libAuth = authorLib;
				while(RS.next()) { 
					//System.out.println(RS.getMetaData());
					result.add(new IPCore(RS.getString("nameIP"), RS.getString("idIP"), 
							   RS.getString("description"), 
							   new HardwareProperties(RS.getInt("LUTs"), 
							   RS.getInt("FFs"), RS.getDouble("latency"), 
							   RS.getInt("nMemories"), RS.getDouble("powerConsuption"), 
							   RS.getDouble("maxClockFrequency")), new Author(RS.getString("idAuthor"), 
							   RS.getString("nameA"), 
							   RS.getString("company"), RS.getString("email"), 
							   RS.getString("role")), 
							   RS.getString("hdlSourcePath"), RS.getString("driverPath")));
				}
			} else {
				libIP = IPManagerLib;
				libAuth = authorLib;
				while(RS.next()) { 
					//System.out.println(RS.getMetaData());
					result.add(new IPManager(RS.getString("nameIP"), RS.getString("idIP"), 
							   RS.getString("description"), 
							   new HardwareProperties(RS.getInt("LUTs"), 
							   RS.getInt("FFs"), RS.getDouble("latency"), 
							   RS.getInt("nMemories"), RS.getDouble("powerConsuption"), 
							   RS.getDouble("maxClockFrequency")), new Author(RS.getString("idAuthor"), 
							   RS.getString("nameA"), 
							   RS.getString("company"), RS.getString("email"), 
							   RS.getString("role")), 
							   RS.getString("hdlSourcePath")));
				}
			}
			
			
			RS.close();
		}
		catch(Exception e) {
			System.out.println("Error: " + e);
		}
		
		System.out.println(" ");
		return result;
	}

	@Override
	public void addIP(IP ipToBeAdded) throws SQLException {
		Statement state;
		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();
		buildQueryInsertIP(ipToBeAdded, query1, query2);
		
		
			state = DBConn.createStatement();
			try {
				state.executeUpdate(query1.toString());
			} catch(SQLException se) {
				System.out.println(ipToBeAdded.getContactPoint().getIdAuthor() + " already exists inside Author");
			}
			System.out.println(query2.toString());
			state.executeUpdate(query2.toString());
	}

	@Override
	public boolean removeIP(String name, String id, boolean isCore) {
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		buildQueryDeleteIP(name, id, isCore, query);
		
		try
		{
			state = DBConn.createStatement();
			if(state.executeUpdate(query.toString()) == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch(SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}

	@Override
	public LinkedList<FPGAConfiguration> searchConfiguration(List<String> listOfParameters) {
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		LinkedList<FPGAConfiguration> result = new LinkedList<FPGAConfiguration>();
		HashMap<String, LinkedList<MappedIP>> hmap = new HashMap<>();
		
		try {
			state = DBConn.createStatement();
			buildQuerySelectFPGAConf(listOfParameters, query);
			RS = state.executeQuery(query.toString());
			while(RS.next()) {
				String tmpIdConf = RS.getString(confLib + ".idConf");
				MappedIP tmpMappedIP = new MappedIP(RS.getString(mappedIPLib + ".idMappedIP"), 
						               new IPCore(RS.getString(IPCoreLib + ".name"), 
						               RS.getString(IPCoreLib + ".idIP"), RS.getString(IPCoreLib + ".description"), 
						               new HardwareProperties(RS.getInt(IPCoreLib + ".LUTs"), RS.getInt(IPCoreLib + ".FFs"), 
						               RS.getDouble(IPCoreLib + ".latency"), RS.getInt(IPCoreLib + ".nMemories"), 
						               RS.getDouble(IPCoreLib + ".powerConsuption"), RS.getDouble(IPCoreLib + ".maxClockFrequency")), 
						               new Author(RS.getString("A2.idAuthor"), 
								       RS.getString("A2.name"), RS.getString("A2.company"), 
								       RS.getString("A2.email"), RS.getString("A2.role")), 
						               RS.getString(IPCoreLib + ".hdlSourcePath"), RS.getString(IPCoreLib + ".driverPath")), 
						               RS.getInt(mappedIPLib + ".priority"), RS.getString(mappedIPLib + ".physicalAddress"));
				hmap.get(tmpIdConf).add(tmpMappedIP);
			}
			
			RS.first();
			
			while(RS.next()) { 
				if(hmap.containsKey(RS.getString(confLib + ".idConf"))) {
					LinkedList<MappedIP> tmpList = new LinkedList<>(hmap.get(RS.getString(confLib + ".idConf")));
					result.add(new FPGAConfiguration(RS.getString(confLib + ".name"), 
							   RS.getString(confLib + ".idConf"), tmpList, 
							   new IPManager(RS.getString(IPManagerLib + ".name"), RS.getString(IPManagerLib + "idIP"), 
							   RS.getString(IPManagerLib + ".description"), new HardwareProperties(RS.getInt(IPManagerLib + ".LUTs"), 
							   RS.getInt(IPManagerLib + ".FFs"), RS.getDouble(IPManagerLib + ".latency"), 
							   RS.getInt(IPManagerLib + ".nMemories"), RS.getDouble(IPManagerLib + ".powerConsuption"), 
							   RS.getDouble(IPManagerLib + ".maxClockFrequency")), new Author(RS.getString("A3.idAuthor"), 
							   RS.getString("A3.name"), RS.getString("A3.company"), RS.getString("A3.email"), 
							   RS.getString("A3.role")), RS.getString(IPManagerLib + ".hdlSourcePath")), 
							   RS.getString(confLib + "bistreamPath"), new HardwareProperties(RS.getInt(confLib + ".LUTs"), 
							   RS.getInt(confLib + ".FFs"), RS.getDouble(confLib + ".latency"), 
							   RS.getInt(confLib + ".nMemories"), RS.getDouble(confLib + ".powerConsuption"), 
							   RS.getDouble(confLib + ".maxClockFrequency")), new Author(RS.getString("A1.idAuthor"), 
							   RS.getString("A1.name"), RS.getString("A1.company"), RS.getString("A1.email"), 
							   RS.getString("A1.role")), RS.getString(confLib + ".additionalDriverSource")));
					hmap.remove(RS.getString(confLib + ".idConf"));
				}
			}
			RS.close();
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}

		return result;
	}

	@Override
	public void addConfiguration(FPGAConfiguration conf) {
		//devo fare l'insert dell'autor, l'insert dei mapped IP, NO l'insert dell'IPManager
		//NO l'insert degli author degli IP
		Statement state;
		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();
		StringBuilder query3 = new StringBuilder();
		LinkedList<MappedIP> result = new LinkedList<MappedIP>();
		String libIP, libAuth;
		
		try {
			state = DBConn.createStatement();
			buildQueryInsertConfiguration(conf, query1, query2, query3);
			try
			{
				state.executeUpdate(query1.toString());
			}
			catch(SQLException se)
			{
				System.out.println(conf.getContactPoint().getIdAuthor() + " already exists inside Author");
			}
			try
			{
				state.executeUpdate(query2.toString());
			}
			catch(SQLException se)
			{
				//something
			}
			try
			{
				state.executeUpdate(query3.toString());
			}
			catch(SQLException se)
			{
				System.out.println(conf.getIdConf() + " already exists inside FPGAConfiguration");
			}
	
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
	}

	@Override
	public boolean removeConfiguration(String name, String id) {
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		buildQueryDeleteConfiguration(name, id, query);
		
		try {
			state = DBConn.createStatement();
			RS = state.executeQuery(query.toString());
			
			if(RS.wasNull()) {
				RS.close();
				return false;
			} else {
				RS.close();
				return true;
			}
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}

		return false;
	}
	
	private static void buildQueryInsertConfiguration(FPGAConfiguration conf, StringBuilder query1, StringBuilder query2, StringBuilder query3)
	{
		//INSERT per Autore Configurazione
		query1.append("INSERT INTO Author (idAuthor, name, company, email, role)\n" +
				  "VALUES (" +
				  "'" + conf.getContactPoint().getIdAuthor() + "', "  +
				  "'" + conf.getContactPoint().getName() + "', " +
				  "'" + conf.getContactPoint().getCompany() + "', " +
				  "'" + conf.getContactPoint().getEmail() +"', " +
				  "'" + conf.getContactPoint().getRole() + "');");
		
		
		LinkedList<MappedIP> tmp = new LinkedList<MappedIP>();
		tmp = (LinkedList<MappedIP>) conf.getMappedIPs();
		
		
		//INSERT per Mapped IP
		for(MappedIP mIP : tmp)
		{
			query2.append("INSERT INTO MappedIP (idConf, idMappedIP, idIP, priority, physicalAddress)\n");
			query2.append("VALUES (" + "'" + conf.getIdConf() + "', "  +
					      "'" + mIP.getIdMappedIP() + "', " +
						  "'" + mIP.getIpCore().getIdIP() + "', " +
					          + mIP.getPriority() + ", " + //priority is integer 
						  "'" + mIP.getPhysicalAddress() + "');\n\n");
		}
		
		//INSERT per FPGAConfiguration
		query3.append("INSERT INTO FPGAConfiguration (idConf, name, bitstreamPath, LUTs, FFs," +
		              "latency, nMemories, powerConsuption, maxClockFrequency, contactPoint, idIP, additionalDriverSource)\n");
		query3.append("VALUES (" + 
		              "'" + conf.getIdConf() + "', " +
		              "'" + conf.getName() + "', " +
		              "'" + conf.getBitstreamPath() + "', " +
		                  + conf.getHwProperties().getLUTs() + ", " +
		                  + conf.getHwProperties().getFFs() + ", " +
		                  + conf.getHwProperties().getLatency() + ", " +
		                  + conf.getHwProperties().getNMemories() + ", " +
		                  + conf.getHwProperties().getPowerConsumption() + ", " +
		                  + conf.getHwProperties().getMaxClkFreq() + ", " +
		              "'" + conf.getContactPoint().getIdAuthor() + "', " +
		              "'" + conf.getManager().getIdIP() + "', " +
		              "'" + conf.getAdditionalDriverSource() + "');");
	}
	
	private static void buildQueryInsertIP(IP ip, StringBuilder query1, StringBuilder query2)
	{
		if(ip.getClass().getName().equals("polito.sdp2017.Components.IPCore"))
		{
			query1.append("INSERT INTO Author (idAuthor, name, company, email, role)\n" +
					  "VALUES (" +
					  "'" + ip.getContactPoint().getIdAuthor() + "', "  +
					  "'" + ip.getContactPoint().getName() + "', " +
					  "'" + ip.getContactPoint().getCompany() + "', " +
					  "'" + ip.getContactPoint().getEmail() +"', " +
					  "'" + ip.getContactPoint().getRole() + "');");
			//query.append("\n");
			query2.append("INSERT INTO IPCore (idIP, name, description, hdlSourcePath, LUTs, " +
			             "FFs, latency, nMemories, powerConsuption, maxClockFrequency, driverPath, contactPoint)\n" +
					     "VALUES (" + "'" + ip.getIdIP() + "', " + "'" + ip.getName() +"'," +
			             "'" + ip.getDescription() + "', " + "'" + ip.getHdlSourcePath() + "'," +
					         + ip.getHwProperties().getLUTs() + ", " +
					         + ip.getHwProperties().getFFs() + ", " +
					         + ip.getHwProperties().getLatency() + ", " +
					         + ip.getHwProperties().getNMemories() + ", " +
					         + ip.getHwProperties().getPowerConsumption() + ", " +
					         + ip.getHwProperties().getMaxClkFreq() + ", " +
					     "'" + ((IPCore) ip).getDriverPath() + "', " +
					     "'" + ip.getContactPoint().getIdAuthor() +"');");
			System.out.println("Sono entrato nella build query IPCOre");
		}
		if(ip.getClass().getName().equals("polito.sdp2017.Components.IPManager"))
		{
			query1.append("INSERT INTO Author (idAuthor, name, company, email, role)\n" +
					  "VALUES (" +
					  "'" + ip.getContactPoint().getIdAuthor() + "', "  +
					  "'" + ip.getContactPoint().getName() + "', " +
					  "'" + ip.getContactPoint().getCompany() + "', " +
					  "'" + ip.getContactPoint().getEmail() +"', " +
					  "'" + ip.getContactPoint().getRole() + "');");
			query2.append("INSERT INTO IPManager (idIP, name, description, hdlSourcePath, LUTs, " +
			             "FFs, latency, nMemories, powerConsuption, maxClockFrequency, contactPoint)\n" +
					     "VALUES (" + "'" + ip.getIdIP() + "', " + "'" + ip.getName() +"'," +
			             "'" + ip.getDescription() + "', " + "'" + ip.getHdlSourcePath() + "'," +
					         + ip.getHwProperties().getLUTs() + ", " +
					         + ip.getHwProperties().getFFs() + ", " +
					         + ip.getHwProperties().getLatency() + ", " +
					         + ip.getHwProperties().getNMemories() + ", " +
					         + ip.getHwProperties().getPowerConsumption() + ", " +
					         + ip.getHwProperties().getMaxClkFreq() + ", " +
					     "'" + ip.getContactPoint().getIdAuthor() +"');");
			System.out.println("Sono entrato nella build query IPManager");
		}
	}
	
	private static void buildQuerySelectIP(LinkedList<String> listOfParameters, StringBuilder query)
	{
		String libIP;
		String libAuth;
		if(listOfParameters.get(0).equals("true")) //0 -> looking for IP cores
		{
			query.append("SELECT " + IPCoreLib + ".name AS nameIP, " + authorLib + ".name AS nameA" + ", *\n" 
		                 + " FROM " + IPCoreLib  + ", " + authorLib + 
		                 "\n WHERE " + IPCoreLib + ".idIP <> 'pollo'\n AND " + 
					     authorLib + ".idAuthor = " + IPCoreLib + ".contactPoint");
			libIP = IPCoreLib;
			libAuth = authorLib;
		}
		else //1 -> looking for IP managers
		{
			query.append("SELECT " + IPManagerLib + ".name AS nameIP, " + authorLib + ".name AS nameA" + ", *\n" +
		                 " FROM " + IPManagerLib  + ", " + 
		                 authorLib + "\n WHERE " + IPManagerLib + ".idIP <> 'pollo'\n AND " +
					     authorLib + ".idAuthor = " + IPManagerLib + ".contactPoint");
			libIP = IPManagerLib;
			libAuth = authorLib;
		}
		
		for(int i=1; i<listOfParameters.size(); i++)
		{
			if(!listOfParameters.get(i).equals("$")) //$ is our character for a uninitialized searching parameter 
			{
				
				if(i==1) //idIP
				{
					query.append("\n AND " + libIP + "." + searchingParametersIP.get(i) + " = " + "'" + listOfParameters.get(i) +"'");
				}
				if(i==2) //nameIP
				{
					query.append("\n AND " + libIP + "." + searchingParametersIP.get(i) + " = " + "'" + listOfParameters.get(i) +"'");
				}
				if(i==13) //max frequency
				{
					query.append("\n AND " + libIP + "." + searchingParametersIP.get(i) + " = " + listOfParameters.get(i));
				}
				if(i>=8 && i<=10)
				{
					query.append("\n AND " + libAuth + "." + searchingParametersIP.get(i) + " = " + "'"+ listOfParameters.get(i) + "'");
				}
				if(i>=3 && i<=7) //LUTs, FFs, latency, #Memories, powerConsuption
				{
					query.append("\n AND " + libIP + "." + searchingParametersIP.get(i) + " <= " + listOfParameters.get(i));
				}
				
			}
			
		}
		
		query.append("\nORDER BY " + libIP + ".name ASC;");
	}
	
	private static void buildQueryDeleteIP(String name, String id, boolean isCore, StringBuilder query)
	{
		
		String libIP;
		String libAuthorIP;
		
		if(isCore == true) //0 -> looking for IP cores
		{
			query.append("DELETE \n" + " FROM " + IPCoreLib  + "\n WHERE " );
			libIP = IPCoreLib;
			libAuthorIP = authorLib;
		}
		else //1 -> looking for IP managers
		{
			query.append("DELETE \n" + " FROM " + IPManagerLib  + "\n WHERE "  );
			libIP = IPManagerLib;
			libAuthorIP = authorLib;
		}
		
		if(name.compareTo("$") != 0) // 0 if equal
		{
			if(id.compareTo("$") != 0) //name & id
			{
				query.append(libIP +  ".idIP = " + "'" + id + "'\n"
						     + "\t AND "+ libIP +  ".name = " + "'" + name + "'" + ";");
			}
			else					   //name
			{
				query.append(libIP +  ".name = " + "'" + name + "'" + ";");
			}
		}
		else
		{
			if(id.compareTo("$") != 0) //id
			{
				query.append(libIP +  ".idIP = " + "'" + id + "'" + ";");
			}
			else					   //nothing
			{
				query.append("idIP = 'Prinetto;'");
			}
		}
		
	}
	
	private static void buildQueryDeleteConfiguration(String name, String id, StringBuilder query)
	{
		
		query.append("DELETE \n" + " FROM " + confLib  + "\n WHERE " );
		
		if(name.compareTo("$") != 0) // 0 if equal
		{
			if(id.compareTo("$") != 0) //name & id
			{
				query.append(confLib +  ".idConf = " + "'" + id + "'\n"
						     + "\t AND "+ confLib +  ".name = " + "'" + name + "'" + ";");
			}
			else					   //name
			{
				query.append(confLib +  ".name = " + "'" + name + "'" + ";");
			}
		}
		else
		{
			if(id.compareTo("$") != 0) //id
			{
				query.append(confLib +  ".idConf = " + "'" + id + "'" + ";");
			}
			else					   //nothing
			{
				query.append("idConf = 'Prinetto;'");
			}
		}
		
	}
	
	private static void importSQL(Connection conn, String in) throws SQLException, FileNotFoundException
	{
		InputStream inputstream = new FileInputStream(in);
	    Scanner s = new Scanner(inputstream);
	    s.useDelimiter("(;(\r)?\n)|((\r)?\n)?(--)?.*(--(\r)?\n)");
	    Statement st = null;
	    try
	    {
	        st = conn.createStatement();
	        while (s.hasNext())
	        {
	            String line = s.next();
	            if (line.startsWith("/*!") && line.endsWith("*/"))
	            {
	                int i = line.indexOf(' ');
	                line = line.substring(i + 1, line.length() - " */".length());
	            }

	            if (line.trim().length() > 0)
	            {
	               st.execute(line);
	               System.out.println(line);
	               System.out.println("Eseguito!");
	            }
	        }
	    }
	    finally
	    {
	        if (st != null) st.close();
	    }
	    s.close();
	}
	
	private static void buildQuerySelectFPGAConf(List<String> listOfParameters, StringBuilder query)
	{
		query.append("SELECT *\n" + " FROM " + confLib + ", " + authorLib + " A1, "
					     + authorLib + " A2, " + authorLib + " A3, " + IPManagerLib + 
				         ", " + mappedIPLib + ", " + IPCoreLib + "\n WHERE " +
		              	 confLib + ".idConf <> 'pollo'\n AND " + 
					     confLib + ".contactPoint = " + "A1.idAuthor\n" +
		                 " AND " + confLib + ".idIP = " + IPManagerLib + ".idIP\n" +
					     " AND " + confLib + ".idConf = " + mappedIPLib + ".idConf\n" +
		                 " AND " + IPCoreLib + ".idIP = " + mappedIPLib + ".idIP\n" +
					     " AND " + IPCoreLib + ".contactPoint = "  + "A2.idAuthor\n" +
		                 " AND " + IPManagerLib + "id.contactPoint = " + "A3.idAuthor"); 
		
		boolean flag = false;
		//in this method, listOfParameters[0] is not used
		for(int i=0; i<listOfParameters.size(); i++)
		{
			if(!listOfParameters.get(i).equals("$")) //$ is our character for a uninitialized searching parameter 
			{
				
				if(i==0) //nIPs
				{
					query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
							     " = " + listOfParameters.get(i));
				}
				if(i==1) //idConf
				{
					query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
							     " = " + "'" + listOfParameters.get(i) +"'");
				}
				if(i==2) //nameIP
				{
					query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
							     " = " + "'" + listOfParameters.get(i) +"'");
				}
				if(i==13) //max frequency
				{
					query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
							     " = " + listOfParameters.get(i));
				}
				if(i>=14 && i<=16)
				{
					query.append("\n AND " + "A1." + searchingParametersFPGAConf.get(i) + 
							     " = " + "'"+ listOfParameters.get(i) + "'");
				}
				if(i>=3 && i<=12) //max & min LUTs, FFs, latency, #Memories, powerConsuption
				{
					if(i%2 == 1)
					{
						flag = true;
					}
					else
					{
						if(flag == true)
						{
							if(Double.parseDouble(listOfParameters.get(i)) <= Double.parseDouble(listOfParameters.get(i-1)))
							{
								query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i-1) + 
										     " >= " + listOfParameters.get(i-1));
								query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
										     " <= " + listOfParameters.get(i));
							}
						}
						flag = false;		
					}
				}	
			}
		}
		query.append("\nORDER BY " + confLib + ".name ASC;");
	}
}
