package polito.sdp2017.Components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FPGAConfiguration {
	private String name;
	private String idConf;
	private List<MappedIP> mappedIPs;
	private IPManager manager;
	private String bitstreamPath;
	private HardwareProperties hwProperties;
	private Author contactPoint;
	private String additionalDriverSource;

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
	
	public static void generateTopLevelEntity(FPGAConfiguration conf) {
		Path pathTarget = Paths.get("./src/polito/sdp2017/Tests/Tmp/TopLevelEntity.vhd");
		Path pathTemplate = Paths.get("./src/polito/sdp2017/Tests/TOPENT_TEMPLATE.vhd");
		
		if(newFile(pathTarget.toString()) != 1)
		{
			return;
		}
		
		File f_template = new File(pathTemplate.toString());
		
		if(!f_template.exists())
		{
			System.out.println("ERROR: File TOPENT_TEMPLATE.vhd not found.");
			return;
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
			
			for(MappedIP m : conf.getMappedIPs())
			{
				out.write("\n");
				out.write(m.getIpCore().getHwInterface().toStringInstantiation());
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
			
			int cnt = 0;
			
			for(MappedIP m : conf.getMappedIPs())
			{
				out.write("\n");
				out.write("ip_" + cnt + ": " + m.getIpCore().getName() + "\n");
				out.write("\tPORT MAP(\n");
				out.write("\t\tclk\t=> clock,\n");
				out.write("\t\trst\t=> reset,\n");
				out.write("\t\tdata_in\t=> data_in_IPs(" + cnt + "),\n");
				out.write("\t\tdata_out\t=> data_out_IPs(" + cnt + "),\n");
				out.write("\t\taddress\t=> addIPs_IPs(" + cnt + "),\n");
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
			
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setIdConf(String idConf) {
		this.idConf = idConf;
	}

	public String getIdConf() {
		return idConf;
	}

	public void setMappedIPs(List<MappedIP> mappedIPs) {
		this.mappedIPs = mappedIPs;
	}

	public List<MappedIP> getMappedIPs() {
		return this.mappedIPs;
	}

	public void addMappedIp(MappedIP mIP)
	{
		this.mappedIPs.add(mIP);
	}
	
	public int getNumberMappedIPs()
	{
		return this.mappedIPs.size();
	}
	
	public void setManager(IPManager manager) {
		this.manager = manager;
	}

	public IPManager getManager() {
		return this.manager;
	}
	
	public Author getContactPointIPManager()
	{
		return this.manager.getContactPoint();
	}
	
	public void setBitstreamPath(String bitstreamPath) {
		this.bitstreamPath = bitstreamPath;
	}

	public String getBitstreamPath() {
		return this.bitstreamPath;
	}

	public void setHwProperties(HardwareProperties hwProperties) {
		this.hwProperties = hwProperties;
	}

	public HardwareProperties getHwProperties() {
		return this.hwProperties;
	}

	public void setContactPoint(Author contactPoint) {
		this.contactPoint = contactPoint;
	}

	public Author getContactPoint() {
		return this.contactPoint;
	}

	/*public void setAdditionalDriverSources(List additionalDriverSources) {

	}*/

	public String getAdditionalDriverSource() {
		return this.additionalDriverSource;
	}
	
	public LinkedList<String> getAllSourcePaths()
	{
		LinkedList<String> l = new LinkedList<String>();
		for(MappedIP m : this.mappedIPs)
		{
			l.add(m.getIpCore().getHdlSourcePath());
		}
		
		return l;
	}

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

	@Override
	public String toString() {
		return "FPGAConfiguration [name=" + name + ", idConf=" + idConf + ", mappedIPs=" + mappedIPs + ", manager="
				+ manager + ", bitstreamPath=" + bitstreamPath + ", hwProperties=" + hwProperties + ", contactPoint="
				+ contactPoint + ", additionalDriverSource=" + additionalDriverSource + "]";
	}
	
	public static int newFile(String path) {
	 
	    try {
	        File file = new File(path);
	         
	        if (file.exists())
	        {
	            System.out.println("Il file " + path + " esiste gi�. Sovrascritto");
	            file.createNewFile();
	            return 1;
	        }
	        else 
	        {	if (file.createNewFile())
	        	{
	            	System.out.println("Il file " + path + " � stato creato");
	            	return 1;
	        	}
	        	else
	        	{
	        		System.out.println("Il file " + path + " non pu� essere creato");
	        		return -1;
	        	}
	        }
	        
	     
	    } catch (IOException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
	public String getBrief()
	{
		StringBuffer s = new StringBuffer();
		
		s.append("ID configuration: \t" + this.getIdConf() + "\n");
		s.append("Name configuration: \t" + this.getName() + "\n");
		s.append("Hardware sroperties: \t" + this.getHwProperties().toString() + "\n");
		
		return s.toString();
	}
}
