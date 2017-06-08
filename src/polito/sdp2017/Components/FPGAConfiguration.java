package polito.sdp2017.Components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import polito.sdp2017.Constants.Constants;

/**
 * This class describes all possible features and functionalities that a configuration, to be loaded
 * into the FPGA of the SEcube board, should have. One FPGAConfiguration is characterized 
 * by an unique id,  a name, the list of all IPcores (it's possible that two equal cores are mapped
 * more than once),  the IPmanager, a bitstream (ABSOLUTE)path, a set of hardware properties, 
 * one contact point (an author) and a path for additional driver source.
 */
public class FPGAConfiguration {
	private String name;
	private String idConf;
	private List<MappedIP> mappedIPs;
	private IPManager manager;
	private String bitstreamPath;
	private HardwareProperties hwProperties;
	private Author contactPoint;
	private String additionalDriverSource;

/**
 * Constructor for the class FPGAConfiguration.
 * @param name
 * @param idConf
 * @param mappedIPs
 * @param manager
 * @param bitstreamPath
 * @param hwProperties
 * @param contactPoint
 * @param additionalDriverSource
 */
	public FPGAConfiguration(String name, String idConf, List<MappedIP> mappedIPs, IPManager manager, String bitstreamPath,
			HardwareProperties hwProperties, Author contactPoint, String additionalDriverSource) {

		this.name = name;
		this.idConf = idConf;
		this.mappedIPs = mappedIPs;
		this.manager = manager;
		this.bitstreamPath = bitstreamPath;
		this.hwProperties = hwProperties;
		this.contactPoint = contactPoint;
		this.additionalDriverSource = additionalDriverSource;
	}
	
	/**
	 * This method creates the top level entity vhdl code considering all IPCores and the IPManager selected
	 * by the user, considering also their priority. This method is invoked before running the synthesis.
	 * @param conf
	 */
	public static void generateTopLevelEntity(FPGAConfiguration conf) {
		Path pathTarget = Paths.get(Constants.VHDLTopLevelEntityToBeSynth);
		Path pathTemplate = Paths.get(Constants.VHDLTopLevelEntityTemplatePath);
		
		if(newFile(pathTarget.toString()) != 1) {
			throw new RuntimeException("File can't be created");
		}
		
		File f_template = new File(pathTemplate.toString());
		
		if(!f_template.exists()) {
			throw new RuntimeException("File TOPENT_TEMPLATE.vhd not found.");
		}
		
		StringBuffer strb = new StringBuffer("");
		String tmpString;
		
		try(BufferedReader in  = new BufferedReader(new FileReader(pathTemplate.toString()));
			BufferedWriter out = new BufferedWriter(new FileWriter(pathTarget.toString())))
		{
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--COMPONENTS HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write(conf.getManager().getHwInterface().toStringInstantiation());
				
			Map<String, List<MappedIP>> hmap = conf.mappedIPs.stream().collect(Collectors.groupingBy((l -> l.getIpCore().getIdIP()), Collectors.toList()));
			
			for(String s : hmap.keySet())
			{
				out.write("\n");
				out.write(hmap.get(s).get(0).getIpCore().getHwInterface().toStringInstantiation());
				out.write("\n");
			}
			
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--HERE MANAGER--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--HERE IPs--[\\s|.]*"))
			{
				if(tmpString.contains("ip_man: IP_MANAGER"))
				{
					out.write("ip_man: " + conf.getManager().getHwInterface().getEntityName() + "\n");
				}
				else
				{
					out.write(tmpString + "\n");
				}
			}
			
			List<MappedIP> tmplist = conf.getMappedIPs()
										 .stream()
										 .sorted(Comparator.comparing(MappedIP::getPriority))
										 .collect(Collectors.toList());
			
			int cnt = 0;
			
			for(MappedIP m : tmplist)
			{
				out.write("\n");
				out.write( m.getIdMappedIP() + ": " + m.getIpCore().getName() + "\n");
				out.write("\tPORT MAP(\n");
				out.write("\t\tclk\t=> clock,\n");
				out.write("\t\trst\t=> reset,\n");
				out.write("\t\tdata_in\t=> data_in_IPs(" + cnt + "),\n");
				out.write("\t\tdata_out\t=> data_out_IPs(" + cnt + "),\n");
				out.write("\t\taddress\t=> add_IPs(" + cnt + "),\n");
				out.write("\t\tW_enable\t=> W_enable_IPs(" + cnt + "),\n");
				out.write("\t\tR_enable\t=> R_enable_IPs(" + cnt + "),\n");
				out.write("\t\tgeneric_en\t=> generic_en_IPs(" + cnt + "),\n");
				out.write("\t\tenable\t=> enable_IPs(" + cnt + "),\n");
				out.write("\t\tack\t=> ack_IPs(" + cnt + "),\n");
				out.write("\t\tinterrupt\t=> interrupt_IPs(" + cnt + "));\n");
				out.write("\n");
				cnt++;
			}
			
			out.write("end architecture;");
			
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to write file");
		}
	}
	
	/**
	 * Setter for name of FPGAConfiguration
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for name of FPGAConfiguration
	 * @return name, chosen by the user
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Setter for idConf of FPGAConfiguration
	 * @param idConf
	 */
	public void setIdConf(String idConf) {
		this.idConf = idConf;
	}
	
	/**
	 * Getter for idConf of FPGAConfiguration
	 * @return the id of the configuration. It's the primary key for the configuration table
	 */
	public String getIdConf() {
		return idConf;
	}
	
	/**
	 * Setter for all mappedIPs of FPGAConfiguration. It's possible having same IPCores.
	 * @param mappedIPs
	 */
	public void setMappedIPs(List<MappedIP> mappedIPs) {
		this.mappedIPs = mappedIPs;
	}
	
	/**
	 * Getter for all mappedIPs of FPGAConfiguration.
	 * @return the list of all mappedIPs
	 */
	public List<MappedIP> getMappedIPs() {
		return this.mappedIPs;
	}
	
	/**
	 * This method allows the adding of one IPCore to be mapped in that configuration.
	 * @param mIP
	 */
	public void addMappedIp(MappedIP mIP)
	{
		this.mappedIPs.add(mIP);
	}
	
	/**
	 * This method returns the current number of mappedIPs. The IPManager is not considered in the counting.
	 * @return Size of List<MappedIP>
	 */
	public int getNumberMappedIPs()
	{
		return this.mappedIPs.size();
	}
	
	/**
	 * Setter of the manager for FPGAConfiguration
	 * @param manager
	 */
	public void setManager(IPManager manager) {
		this.manager = manager;
	}
	
	/**
	 * Getter of the IPManager for FPGAConfiguration
	 * @return the object IPManager
	 */
	public IPManager getManager() {
		return this.manager;
	}
	
    /**
     * Returns the contactPoint (class Author) of the current IPmanager for this configuration.
     * @return The contactéoint of IPManager
     */
	public Author getContactPointIPManager()
	{
		return this.manager.getContactPoint();
	}
	
	/**
	 * Setter of bitstream path for FPGAConfiguration
	 * @param bitstreamPath
	 */
	public void setBitstreamPath(String bitstreamPath) {
		this.bitstreamPath = bitstreamPath;
	}
	
	/**
	 * Getter of bitstream path for FPGAConfiguration
	 * @return The absolute path for the file containing the bitstream, got after the synthesis.
	 */
	public String getBitstreamPath() {
		return this.bitstreamPath;
	}
	
	/**
	 * Setter of hardware properties for FPGAConfiguration. They are computed after the synthesis.
	 * @param hwProperties
	 */
	public void setHwProperties(HardwareProperties hwProperties) {
		this.hwProperties = hwProperties;
	}
	
	/**
	 * Getter of hardware properties for FPGAConfiguration.
	 * @return the object HardwareProperties for that configuration.
	 */
	public HardwareProperties getHwProperties() {
		return this.hwProperties;
	}
	
	/**
	 * Setter of the contactPoint for FPGAConfiguration
	 * @param contactPoint
	 */
	public void setContactPoint(Author contactPoint) {
		this.contactPoint = contactPoint;
	}
	
	/**
	 * Getter of the contactPoint for FPGAConfiguration
	 * @return the contactPoint of who designed that configuration.
	 */
	public Author getContactPoint() {
		return this.contactPoint;
	}

	/*public void setAdditionalDriverSources(List additionalDriverSources) {

	}*/
	
	/**
	 * Getter of the additional driver source path for FPGAConfiguration
	 * @return the path for further drivers to be used by the processors in order to realize some functionalities
	 * considering that configuration.
	 */
	public String getAdditionalDriverSource() {
		return this.additionalDriverSource;
	}
	
	/**
	 * Extracts all source paths from all mapped IPCores.
	 * @return linkedlist of all source paths.
	 */
	public LinkedList<String> getAllSourcePaths()
	{
		LinkedList<String> l = new LinkedList<String>();
		for(MappedIP m : this.mappedIPs)
		{
			l.add(m.getIpCore().getHdlSourcePath());
		}
		
		return l;
	}
	
	/**
	 * auto-generated by Eclipse
	 * @return hash code of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalDriverSource == null) ? 0 : additionalDriverSource.hashCode());
		result = prime * result + ((bitstreamPath == null) ? 0 : bitstreamPath.hashCode());
		result = prime * result + ((contactPoint == null) ? 0 : contactPoint.hashCode());
		result = prime * result + ((hwProperties == null) ? 0 : hwProperties.hashCode());
		result = prime * result + ((idConf == null) ? 0 : idConf.hashCode());
		result = prime * result + ((manager == null) ? 0 : manager.hashCode());
		result = prime * result + ((mappedIPs == null) ? 0 : mappedIPs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	/**
	 * auto-generated by Eclipse
	 * @return  boolean indicating if the two objects are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FPGAConfiguration other = (FPGAConfiguration) obj;
		if (additionalDriverSource == null) {
			if (other.additionalDriverSource != null)
				return false;
		} else if (!additionalDriverSource.equals(other.additionalDriverSource))
			return false;
		if (bitstreamPath == null) {
			if (other.bitstreamPath != null)
				return false;
		} else if (!bitstreamPath.equals(other.bitstreamPath))
			return false;
		if (contactPoint == null) {
			if (other.contactPoint != null)
				return false;
		} else if (!contactPoint.equals(other.contactPoint))
			return false;
		if (hwProperties == null) {
			if (other.hwProperties != null)
				return false;
		} else if (!hwProperties.equals(other.hwProperties))
			return false;
		if (idConf == null) {
			if (other.idConf != null)
				return false;
		} else if (!idConf.equals(other.idConf))
			return false;
		if (manager == null) {
			if (other.manager != null)
				return false;
		} else if (!manager.equals(other.manager))
			return false;
		if (mappedIPs == null) {
			if (other.mappedIPs != null)
				return false;
		} else if (!mappedIPs.equals(other.mappedIPs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * @return String representation of all information on the configuration.
	 */
	@Override
	public String toString() {
		return "FPGAConfiguration [name=" + name + ", idConf=" + idConf + ", mappedIPs=" + mappedIPs + ", manager="
				+ manager + ", bitstreamPath=" + bitstreamPath + ", hwProperties=" + hwProperties + ", contactPoint="
				+ contactPoint + ", additionalDriverSource=" + additionalDriverSource + "]";
	}
	
	/**
	 * Create a new file in according to the path passed as parameter. If that file already exits it is overwritten.
	 * @param path
	 * @return 1 if the operation ended well, -1 if some problems occurred.
	 */
	public static int newFile(String path) {
	 
	    try {
	        File file = new File(path);
	         
	        if (file.exists())
	        {
	            System.out.println("Il file " + path + " esiste gia'. Sovrascritto");
	            file.createNewFile();
	            return 1;
	        }
	        else 
	        {	if (file.createNewFile())
	        	{
	            	System.out.println("Il file " + path + " e' stato creato");
	            	return 1;
	        	}
	        	else
	        	{
	        		System.out.println("Il file " + path + " non puo' essere creato");
	        		return -1;
	        	}
	        }
	        
	     
	    } catch (IOException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
	/**
	 * It's a shorter version of toString() of FPGAConfiguration. 
	 * @return main informations on that configuration: id, name and hardware properties.
	 */
	public String getBrief()
	{
		StringBuffer s = new StringBuffer();
		
		s.append("ID configuration: \t" + this.getIdConf() + "\n");
		s.append("Name configuration: \t" + this.getName() + "\n");
		s.append("Hardware sroperties: \t" + this.getHwProperties().toString() + "\n");
		
		return s.toString();
	}
	
	/**
	 * This method rewrites the content of the VHDL package constants.vhd, in according to the specified template,
	 *  every time createConfiguration() is invoked. It is a necessary step before running the synthesis.
	 * @param fpgaconf
	 * @return true if everything ends well, otherwise false
	 */
	public static boolean setNumberIPCoresInConstantsVHDL(FPGAConfiguration fpgaconf)
	{
		File f_templ_constants = new File(Constants.VHDLConstantsTemplatePath);
		File f_constants = new File(Constants.JavaProjectVHDLs + "/constants.vhd");
		
		if(newFile(Constants.JavaProjectVHDLs + "/constants.vhd") != 1)
		{
			return false;
		}
		
		String tmpString = new String();
		
		try(BufferedReader in  = new BufferedReader(new FileReader(Constants.VHDLConstantsTemplatePath));
				BufferedWriter out = new BufferedWriter(new FileWriter(Constants.JavaProjectVHDLs + "/constants.vhd")))
			{
				while(!(tmpString = in.readLine()).matches("[\\s|.]*--IMPORTANT: Choose the number of IP cores[\\s|.]*"))
				{
					out.write(tmpString + "\n");
				}
				
				
				out.write("	constant NUM_IPS                : integer := " + fpgaconf.getNumberMappedIPs() + "; ");
				
				while(!(tmpString = in.readLine()).matches("[\\s|.]*--END HERE--[\\s|.]*"))
				{
					out.write(tmpString + "\n");
				}
				
				return true;
			}
			catch(Exception e)
			{
				throw new RuntimeException("Unable to write file");
			}
		
	}
	
	/**
	 * This method generates dynamically the synthesis tcl script, in according to a template, 
	 * listing all components to be added to the project. The script is generated inside the Lattice Diamond
	 * file system. 
	 * @param fpgaconf
	 * @return true if everything ends well, otherwise false
	 */
	public static boolean createTCLScript(FPGAConfiguration fpgaconf)
	{	
		if(newFile(Constants.diamondTCLScritpPath) != 1) {
			throw new RuntimeException("File can't be created");
		}
		
		File f_template = new File(Constants.TCLscriptTemplatePath);
		
		if(!f_template.exists()) {
			throw new RuntimeException("File TCLSCRIPT_TEMPLATE.tcl not found.");
		}
		String tmpString;
		
		try(BufferedReader in  = new BufferedReader(new FileReader(Constants.TCLscriptTemplatePath));
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.diamondTCLScritpPath)))
		{
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--DIAMOND PATH HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write("cd \"" + Constants.DiamondRoot + "\"\n");
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--SOURCE HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write("file mkdir " + "\"" + Constants.diamondImplSourcePath + "\"\n");
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--VHDL HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write("file copy -force -- " + "\"" + Constants.JavaApplicationRoot + "Tmp/TopLevelEntity.vhd\" "  +
			           "\"" + Constants.diamondImplSourcePath + "\"\n");
			out.write("file copy -force -- " + "\"" + Constants.JavaProjectVHDLs + "BUFFER_DATA.vhd\" "  +
			           "\"" + Constants.diamondImplSourcePath + "\"\n");
			out.write("file copy -force -- " + "\"" + Constants.JavaProjectVHDLs + "constants.vhd\" "  +
			           "\"" + Constants.diamondImplSourcePath + "\"\n");
			
			Map<String, List<MappedIP>> hmap = fpgaconf.getMappedIPs().stream().collect(Collectors.groupingBy((l -> l.getIpCore().getIdIP()), Collectors.toList()));
			
			for(String s : hmap.keySet())
			{
				out.write("file copy -force -- " + "\"" + hmap.get(s).get(0).getIpCore().getHdlSourcePath() +
						  "\" " + "\"" + Constants.diamondImplSourcePath + "\"\n");
			}
			
			out.write("file copy -force -- " + "\"" + fpgaconf.getManager().getHdlSourcePath() +
					 "\" " + "\"" + Constants.diamondImplSourcePath + "\"");
			
			out.write("\nprj_src add " + "\"" + Constants.JavaApplicationRoot + "Tmp/TopLevelEntity.vhd\"\n");
			out.write("prj_src add " + "\"" + Constants.JavaProjectVHDLs + "BUFFER_DATA.vhd\"\n");
			out.write("prj_src add " + "\"" + Constants.JavaProjectVHDLs + "constants.vhd\"\n");
			out.write("prj_src add " + "\"" + fpgaconf.getManager().getHdlSourcePath() +"\"\n");
			
			for(String s : hmap.keySet())
			{
				out.write("prj_src add " + "\"" + hmap.get(s).get(0).getIpCore().getHdlSourcePath() +"\"\n");
			}
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--REPORT HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write("pwc_writereport html -file " + "\"" + Constants.diamondImplPath + "/report.html\"\n");
			
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--END HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to write file");
		}
		
		return true;
	}
	
	public static boolean runSynthesis(FPGAConfiguration fpgaconf) throws InterruptedException, IOException
	{	
		try {
			Process proc = Runtime.getRuntime().exec(Constants.prv);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((reader.readLine()) != null) {}
			proc.waitFor();
			proc.destroy();
			System.out.println("Synthesis ended.");

		} catch (IOException e) {
			 System.out.println(e.getMessage() + " [ERROR]\n");
			 return false;
		}
		
		File f = new File(Constants.diamondBitstreamPath);
		System.out.println("Looking for bitstream...");
		if(f.exists()) //copy file
		{
			System.out.println("Bitstream found!");
			copyBitstream(fpgaconf);
			return true;
		}
		else
		{
			System.out.println("ERROR: bitstream not found");
			return false;
		}
	}
	
	public static void copyBitstream(FPGAConfiguration fpgaconf) throws IOException
	{
		
		
		File f = new File(Constants.diamondBitstreamPath);
		//File z = new File(Constants.JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit");
		if(newFile(Constants.JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit") != -1)
		{
			FileInputStream in = new FileInputStream(f);
			FileOutputStream out = new FileOutputStream(Constants.JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit");
			
			byte [] dati = new byte[in.available()];
			in.read(dati);
			out.write(dati);
			in.close();
			out.close();
			
			fpgaconf.setBitstreamPath(Constants.JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit");
		}
	}
	
	public static HardwareProperties getHPFromLattice(String pathDir)
	{
		//LUTs
		//FFs
		//latency
		//nMemories = random?
		//maxPowerConsuption
		//maxClockFrequency
		
		int LUTs = 0, FFs = 0, nMemories = 0;
		Double latency = 0.0, maxPowerConsuption = 0.0, maxClockFrequency = 0.0;
		
		File f_power = new File(Constants.powerReportPath);
		File f_freq = new File(Constants.frequencyPath);
		File f_area = new File(Constants.LUTsFFsPath);
		
		if(f_power.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_power)))
				{
					while(!in.readLine().matches("<td width=250><font class=\"table\">Total Power Est. Design </font></td>"))
					{
						
					}
					StringBuffer tmp = new StringBuffer(in.readLine());
					Pattern p = Pattern.compile(".*>([\\d|\\.]+)\\s+W.*", Pattern.DOTALL);
					Matcher m = p.matcher(tmp);
					m.matches();
					maxPowerConsuption = Double.parseDouble(m.group(1));
					
					in.close();
				}
				catch(Exception e)
				{
					//throw new RuntimeException("Unable to fi file");
				}
		}
		else
		{
			System.out.println("ERROR: power report not found.\nMax Power Consuption = 0");
		}
		if(f_freq.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_freq)))
			{
				String tmp = new String();
				while(!(tmp = in.readLine()).matches("\\|.*MCCLK_FREQ.*"));
				
				
				Pattern p = Pattern.compile(".*(\\d+.\\d+)\\*.*"); //ii.dddd 
				Matcher m = p.matcher(tmp);
				m.matches();
				tmp = m.group(1);
				maxClockFrequency = Double.parseDouble(m.group(1));
				
				
				double temp = Math.pow(10, 2);
			    latency = Math.ceil((1/maxClockFrequency) * temp) / temp;
				
				in.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("Unable to write file");
			}
		}
		else
		{
			System.out.println("ERROR: timing report not found.\nMax Clock Frequency = 0\nLatency = 0");
		}
		if(f_area.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_area)))
			{
				String tmp = new String();
				while(!(tmp = in.readLine()).matches("LUTS_used.*"));
				
				Pattern p = Pattern.compile(".*=\\s*(\\d{1,4}).*", Pattern.DOTALL);
				Matcher m = p.matcher(tmp);
				m.matches();
				LUTs = Integer.valueOf(m.group(1));
				
				while(!(tmp = in.readLine()).matches("FF_used.*"));
				
				Pattern p1 = Pattern.compile(".*=\\s*(\\d{1,4}).*", Pattern.DOTALL);
				Matcher m1 = p1.matcher(tmp);
				m1.matches();
				FFs = Integer.valueOf(m1.group(1));
				
				in.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		else
		{
			System.out.println("ERROR: area report not found.\nLUTs = 0\nFFs = 0");
		}
		
		HardwareProperties hp = new HardwareProperties(LUTs, FFs, latency, nMemories, maxPowerConsuption, maxClockFrequency);
		return hp;
	}
	
	public static void deleteAllFiles(String path)
	{
		File directory = new File(path);
		File[] files = directory.listFiles();
		for (File f : files) {
			if(f.isDirectory())
			{
				System.out.println(f.getAbsolutePath());
				deleteAllFiles(f.getAbsolutePath());
			}
			else
			{
				f.delete();
			}
		}	
	}
	
	public static void createConfiguration(FPGAConfiguration fpgaconf, DBManager DBM) throws IOException, InterruptedException
	{	
		FPGAConfiguration.setNumberIPCoresInConstantsVHDL(fpgaconf); //changes the value of N_IPS inside constants.vhd package
		
		FPGAConfiguration.generateTopLevelEntity(fpgaconf); //generation of the top level entity
		
		FPGAConfiguration.createTCLScript(fpgaconf); //creates new TCL script
		
		FPGAConfiguration.runSynthesis(fpgaconf);	//runs synthesis (and copy bitstream)

		fpgaconf.setHwProperties(FPGAConfiguration.getHPFromLattice(Constants.diamondImplPath)); //set HWProperties
		
		FPGAConfiguration.deleteAllFiles(Constants.diamondImplPath); //erases every files inside Diamond folder
		
		DBM.addConfiguration(fpgaconf);
	}

}
