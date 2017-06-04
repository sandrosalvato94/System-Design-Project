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
		    "nIPs", "idConf", "name", "LUTs", "FFs", "Latency", "maxPowerConsuption",
		    "maxClockFrequency", "idAuthor", "nameAuthor", "company"));
	
	
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
				String tmpIdConf = RS.getString("idConfConf");
				hmap.put(tmpIdConf, new LinkedList<MappedIP>());
			}
			
			RS.close();
			RS = state.executeQuery(query.toString());
			while(RS.next()) {
				String tmpIdConf = RS.getString("idConfConf");
				MappedIP tmpMappedIP = new MappedIP(RS.getString("idMappedIP"), 
						               new IPCore(RS.getString("nameIPCore"), 
						               RS.getString("idIPCore"), RS.getString("descriptionIPCore"), 
						               new HardwareProperties(RS.getInt("LUTsIPCore"), RS.getInt("FFsIPCore"), 
						               RS.getDouble("latencyIPCore"), RS.getInt("nMemoriesIPCore"), 
						               RS.getDouble("powerConsuptionIPCore"), RS.getDouble("maxClockFrequencyIPCore")), 
						               new Author(RS.getString("id_authorCore"), 
								       RS.getString("name_authorCore"), RS.getString("company_authorCore"), 
								       RS.getString("email_authorCore"), RS.getString("role_authorCore")), 
						               RS.getString("hdlSourcePathIPCore"), RS.getString("driverPathIPCore")), 
						               RS.getInt("priorityMapped"), RS.getString("physicalAddressMapped"));
				hmap.get(tmpIdConf).add(tmpMappedIP);
			}
			RS.close();
			//RS.first();
			RS = state.executeQuery(query.toString());
			while(RS.next()) { 
				if(hmap.containsKey(RS.getString("idConfConf"))) {
					LinkedList<MappedIP> tmpList = new LinkedList<>(hmap.get(RS.getString("idConfConf")));
					result.add(new FPGAConfiguration(RS.getString("nameConf"), 
							   RS.getString("idConfConf"), tmpList, 
							   new IPManager(RS.getString("nameIPManager"), RS.getString("idIPManager"), 
							   RS.getString("descriptionIPManager"), new HardwareProperties(RS.getInt("LUTsIPManager"), 
							   RS.getInt("FFsIPManager"), RS.getDouble("latencyIPManager"), 
							   RS.getInt("nMemoriesIPManager"), RS.getDouble("powerConsuptionIPManager"), 
							   RS.getDouble("maxClockFrequencyIPManager")), new Author(RS.getString("id_authorManager"), 
							   RS.getString("name_authorManager"), RS.getString("company_authorManager"), RS.getString("email_authorManager"), 
							   RS.getString("role_authorManager")), RS.getString("hdlSourcePathIPManager")), 
							   RS.getString("bitstreamPathConf"), new HardwareProperties(RS.getInt("LUTsConf"), 
							   RS.getInt("FFsConf"), RS.getDouble("latencyConf"), 
							   RS.getInt("nMemoriesConf"), RS.getDouble("powerConsuptionConf"), 
							   RS.getDouble("maxClockFrequencyConf")), new Author(RS.getString("id_authorConf"), 
							   RS.getString("name_authorConf"), RS.getString("company_authorConf"), RS.getString("email_authorConf"), 
							   RS.getString("role_authorConf")), RS.getString("additionalDriverSourceConf")));
					hmap.remove(RS.getString("idConfConf"));
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
		query.append("SELECT A1.name AS name_authorConf, " +
	                 "A1.idAuthor AS id_authorConf, " +
				     "A1.company AS company_authorConf, " +
	                 "A1.email AS email_authorConf, " +
				     "A1.role AS role_authorConf, " +
	                 "A2.name AS name_authorCore, " +
	                 "A2.idAuthor AS id_authorCore, " +
				     "A2.company AS company_authorCore, " +
	                 "A2.email AS email_authorCore, " +
				     "A2.role AS role_authorCore, " +
				     "A3.name AS name_authorManager, " +
	                 "A3.idAuthor AS id_authorManager, " +
				     "A3.company AS company_authorManager, " +
	                 "A3.email AS email_authorManager, " +
				     "A3.role AS role_authorManager, " +
	                 confLib + ".idConf AS idConfConf, " + 
	                 confLib + ".name AS nameConf, " +
				     confLib + ".LUTs AS LUTsConf, " + 
				     confLib + ".FFs AS FFsConf, " + 
				     confLib + ".latency AS latencyConf, " + 
				     confLib + ".nMemories AS nMemoriesConf, " + 
				     confLib + ".powerConsuption AS powerConsuptionConf, " + 
				     confLib + ".maxClockFrequency AS maxClockFrequencyConf, " + 
				     confLib + ".contactPoint AS contactPointConf, " +
				     confLib + ".idIP AS idManagerConf, " +
				     confLib + ".additionalDriverSource AS additionalDriverSourceConf, " +
				     confLib + ".bitstreamPath AS bitstreamPathConf, " +
				     mappedIPLib + ".idConf AS idConfMapped, " +
				     mappedIPLib + ".idMappedIP AS idMappedIP, " +
				     mappedIPLib + ".idIP AS idIPMapped, " +
				     mappedIPLib + ".priority AS priorityMapped, " +
				     mappedIPLib + ".physicalAddress AS physicalAddressMapped, " +
				     IPCoreLib + ".idIP AS idIPCore, " +
				     IPCoreLib + ".name AS nameIPCore, " +
				     IPCoreLib + ".hdlSourcePath AS hdlSourcePathIPCore, " +
				     IPCoreLib + ".description AS descriptionIPCore, " +
				     IPCoreLib + ".LUTs AS LUTsIPCore, " +
				     IPCoreLib + ".FFs AS FFsIPCore, " +
				     IPCoreLib + ".latency AS latencyIPCore, " +
				     IPCoreLib + ".nMemories AS nMemoriesIPCore, " +
				     IPCoreLib + ".powerConsuption AS powerConsuptionIPCore, " +
				     IPCoreLib + ".maxClockFrequency AS maxClockFrequencyIPCore, " +
				     IPCoreLib + ".driverPath AS driverPathIPCore, " +
				     IPCoreLib + ".contactPoint AS contactPointIPCore, " +
				     IPManagerLib + ".idIP AS idIPManager, " +
				     IPManagerLib + ".name AS nameIPManager, " +
				     IPManagerLib + ".hdlSourcePath AS hdlSourcePathIPManager, " +
				     IPManagerLib + ".description AS descriptionIPManager, " +
				     IPManagerLib + ".LUTs AS LUTsIPManager, " +
				     IPManagerLib + ".FFs AS FFsIPManager, " +
				     IPManagerLib + ".latency AS latencyIPManager, " +
				     IPManagerLib + ".nMemories AS nMemoriesIPManager, " +
				     IPManagerLib + ".powerConsuption AS powerConsuptionIPManager, " +
				     IPManagerLib + ".maxClockFrequency AS maxClockFrequencyIPManager, " +
				     IPManagerLib + ".contactPoint AS contactPointIPManager\n " 
	                 + " FROM " + confLib + ", " + authorLib + " A1, "
					     + authorLib + " A2, " + authorLib + " A3, " + IPManagerLib + 
				         ", " + mappedIPLib + ", " + IPCoreLib + "\n WHERE " +
		              	 confLib + ".idConf <> 'pollo'\n AND " + 
					     confLib + ".contactPoint = " + "A1.idAuthor\n" +
		                 " AND " + confLib + ".idIP = " + IPManagerLib + ".idIP\n" +
					     " AND " + confLib + ".idConf = " + mappedIPLib + ".idConf\n" +
		                 " AND " + IPCoreLib + ".idIP = " + mappedIPLib + ".idIP\n" +
					     " AND " + IPCoreLib + ".contactPoint = "  + "A2.idAuthor\n" +
		                 " AND " + IPManagerLib + ".contactPoint = " + "A3.idAuthor"); 
		
		for(int i=0; i<listOfParameters.size(); i++)
		{
			if(!listOfParameters.get(i).equals("$")) //$ is our character for a uninitialized searching parameter 
			{
				
				if(i==0) //nIPs
				{
					query.append("\n AND " + confLib + ".idConf IN (SELECT idConf FROM " + mappedIPLib +
							      " GROUP BY idConf HAVING COUNT(*) >= " + listOfParameters.get(i) + ")");
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
				if(i>=8 && i<=10) //author data CONFIGURATION
				{
					query.append("\n AND " + "A1." + searchingParametersFPGAConf.get(i) + 
							     " = " + "'"+ listOfParameters.get(i) + "'");
				}
				if(i>=3 && i<=7) //max LUTs, FFs, latency, powerConsuption, frequency
				{
					query.append("\n AND " + confLib + "." + searchingParametersFPGAConf.get(i) + 
							     " <= " + listOfParameters.get(i));
				}	
			}
		}
		query.append("\nORDER BY " + confLib + ".name ASC;");
	}
}
