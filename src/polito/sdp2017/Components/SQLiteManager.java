package polito.sdp2017.Components;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;


public class SQLiteManager implements DBManager {
	private String DBPath;
	private String DBName;
	private String driverName;
	
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
		    "isIPCore", "idIP", "name", "maxLUTs", "minLUTs", "maxFFs", "minFFs", "maxLatency", 
		    "minLatency","maxNMemories", "minNMemories", "maxPowerConsuption",
		    "minPowerConsuption", "maxClockFrequency", "idAuthor", "nameAuthor", "company"));
	
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
	
	
	@Override
	public void setDBPath(String DBPath) {
		this.DBPath = DBPath;
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
		Connection conn;
		openConnectionToDB();
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			importSQL(conn, SQLScritpCreate);
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
	}

	@Override
	public LinkedList<IP> searchIP(List<String> listOfParameters) {
		
		Connection conn;
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		LinkedList<IP> result = new LinkedList<IP>();
		String libIP, libAuth;
		
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			buildQuerySelectIP(listOfParameters, query);
			RS = state.executeQuery(query.toString());
			if(listOfParameters.get(0).equals("true"))
			{
				libIP = IPCoreLib;
				libAuth = authorLib;
			}
			else
			{
				libIP = IPManagerLib;
				libAuth = authorLib;
			}
			
			while(RS.next())
			{ 
				result.add(new IP(RS.getString(libIP + ".name"), RS.getString(libIP + ".idIP"), 
						   RS.getString(libIP + ".description"), 
						   new HardwareProperties(RS.getInt(libIP + ".FFs"), 
						   RS.getInt(libIP + ".FFs"), RS.getDouble(libIP + ".latency"), 
						   RS.getInt(libIP + ".nMemories"), RS.getDouble(libIP + ".powerConsuption"), 
						   RS.getDouble(libIP + ".maxClockFrequency")), new Author(RS.getString(libAuth + ".idAuthor"), RS.getString(libAuth + ".name"), 
						   RS.getString(libAuth + ".company"), RS.getString(libAuth + ".email"), 
						   RS.getString(libAuth + ".role")), 
						   RS.getString(libIP + ".hdlSourcePath")));
			}
			
			RS.close();
			conn.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
		return result;
	}

	@Override
	public void addIP(IP ipToBeAdded) {
		Connection conn;
		Statement state;
		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();
		IP ip = null;
		buildQueryInsertIP(ip, query1, query2);
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			state.executeUpdate(query1.toString());
			state.executeUpdate(query2.toString());
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
	}

	@Override
	public boolean removeIP(String name, String id, boolean isCore) {
		
		Connection conn;
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		buildQueryDeleteIP(name, id, isCore, query);
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			RS = state.executeQuery(query.toString());
			
			if(RS.wasNull())
			{
				RS.close();
				conn.close();
				return false;
			}
			else
			{
				RS.close();
				conn.close();
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
		return false;
	}

	@Override
	public LinkedList<FPGAConfiguration> searchConfiguration(List<String> listOfParameters) {
		Connection conn;
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		LinkedList<FPGAConfiguration> result = new LinkedList<FPGAConfiguration>();
		HashMap<String, LinkedList<MappedIP>> hmap = new HashMap<>();
		
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			buildQuerySelectFPGAConf(listOfParameters, query);
			RS = state.executeQuery(query.toString());
			while(RS.next())
			{
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
			
			while(RS.next())
			{ 
				if(hmap.containsKey(RS.getString(confLib + ".idConf")))
				{
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
			conn.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
		return result;
	}

	@Override
	public void addConfiguration(FPGAConfiguration conf) {
		//devo fare l'insert dell'autor, l'insert dei mapped IP, NO l'insert dell'IPManager
		//NO l'insert degli author degli IP
		Connection conn;
		Statement state;
		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();
		StringBuilder query3 = new StringBuilder();
		LinkedList<MappedIP> result = new LinkedList<MappedIP>();
		String libIP, libAuth;
		
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			buildQueryInsertConfiguration(conf, query1, query2, query3);
			state.executeUpdate(query1.toString());
			state.executeUpdate(query2.toString());
			state.executeUpdate(query3.toString());
			
			conn.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
	}

	@Override
	public boolean removeConfiguration(String name, String id) {
		Connection conn;
		Statement state;
		ResultSet RS;
		StringBuilder query = new StringBuilder();
		buildQueryDeleteConfiguration(name, id, query);
		openConnectionToDB();
		
		try
		{
			conn = DriverManager.getConnection(this.DBPath);
			state = conn.createStatement();
			RS = state.executeQuery(query.toString());
			
			if(RS.wasNull())
			{
				RS.close();
				conn.close();
				return false;
			}
			else
			{
				RS.close();
				conn.close();
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		
		return false;
	}

	@Override
	public boolean compareTo(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	void openConnectionToDB()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch(Exception e)
		{
			System.out.println("Driver not available  + e");
		}
		
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
		if(ip.getClass().getName().equals("IPCore"))
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
		}
		if(ip.getClass().getName().equals("IPManager"))
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
		}
	}
	
	private static void buildQuerySelectIP(List<String> listOfParameters, StringBuilder query)
	{
		String libIP;
		String libAuth;
		if(listOfParameters.get(0).equals("true")) //0 -> looking for IP cores
		{
			query.append("SELECT *\n" + " FROM " + IPCoreLib  + ", "
					     + authorLib + "\n WHERE " + IPCoreLib + ".idIP <> 'pollo'\n AND " + 
					     authorLib + ".idAuthor = " + IPCoreLib + ".contactPoint");
			libIP = IPCoreLib;
			libAuth = authorLib;
		}
		else //1 -> looking for IP managers
		{
			query.append("SELECT *\n" + " FROM " + IPManagerLib  + ", " + 
		                 authorLib + "\n WHERE " + IPManagerLib + ".idIP <> 'pollo'\n AND " +
					     authorLib + ".idAuthor = " + IPManagerLib + ".contactPoint");
			libIP = IPManagerLib;
			libAuth = authorLib;
		}
		
		int j = 1;
		boolean flag = false;
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
				if(i>=14 && i<=16)
				{
					query.append("\n AND " + libAuth + "." + searchingParametersIP.get(i) + " = " + "'"+ listOfParameters.get(i) + "'");
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
								query.append("\n AND " + libIP + "." + searchingParametersIP.get(i-1) + " >= " + listOfParameters.get(i-1));
								query.append("\n AND " + libIP + "." + searchingParametersIP.get(i) + " <= " + listOfParameters.get(i));
							}
						}
						flag = false;
						
					}
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
