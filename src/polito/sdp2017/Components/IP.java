package polito.sdp2017.Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import polito.sdp2017.HardwareInterface.HardwareInterface;
import polito.sdp2017.HardwareInterface.Hdl;
import polito.sdp2017.VhdlInterface.VhdlInterface;

/**
 * This class describes all possible features and functionalities that an IP, should have. One IP is characterized 
 * by an unique id,  a name, a short description of the device, the hdl code (ABSOLUTE) path, a set of hardware properties, 
 * one contact point (an author) and an hardware interface.
 */
public class IP {
	private String name;
	private String idIP;
	private String description;
	private HardwareProperties hwProperties;
	private Author contactPoint;
	private String hdlSourcePath;
	private HardwareInterface hwInterface;
	
	/**
	 * This method creates a list of object IPs, parsing an XML file containing their description. This file lists all the main
	 * properties, such as whether the object is a Cora or a Manager, the name, the id, the description, all data on the contact point,
	 * all the hardware properties.
	 * @param xmlPath : The absolute path of the xml file
	 * @return the list of parsed IP. This list can contain both Cores and Manager at the same time.
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static List<IP> getFromXML(String xmlPath) throws SAXException, ParserConfigurationException, IOException {
    	File fXmlFile = new File(xmlPath);
    	if (!fXmlFile.exists() || !fXmlFile.isFile()) {
    		throw new FileNotFoundException();
    	}

    	List<IP> listIPs = new LinkedList<IP>();
		List<IP> listCores;
		List<IP> listManagers;
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new StreamSource(new File("xmlSchema.xml")));
		builderFactory.setSchema(schema);

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
	    Document doc = builder.parse(fXmlFile);
	    	
	    doc.getDocumentElement().normalize();
	    	
	   	NodeList cores = doc.getElementsByTagName("IPCore");
	    NodeList managers = doc.getElementsByTagName("IPManager");
	    	
	    listCores = IPCore.getFromDomNodeList(cores);
	    listManagers = IPManager.getFromDomNodeList(managers);
	    if (listCores != null) {
		   	listIPs.addAll(listCores);
		}
	    if (listManagers != null) {
		    listIPs.addAll(listManagers);
	    }

		return listIPs;
	}
	
	/**
	 * Constructor of the class IP
	 * @param name          : IP name
	 * @param idIP          : IP identifier
	 * @param description   : IP description
	 * @param hwProperties  : IP hardware properties
	 * @param contactPoint  : IP contact point
	 * @param hdlSourcePath : IP hdl source path
	 */
	public IP(String name, String idIP, String description, HardwareProperties hwProperties,
			Author contactPoint, String hdlSourcePath) {		
		try {
			
			this.name = name;
			this.idIP = idIP;
			this.description = description;
			this.hwProperties = hwProperties;
			this.contactPoint = contactPoint;
			this.hdlSourcePath = hdlSourcePath;
			
			String hdlStringCode = this.idIP.split("_")[0];
			Hdl ipHdl = Hdl.hdlFromString(hdlStringCode.toLowerCase());
			
			switch (ipHdl) {
			case VHDL:
				this.hwInterface = new VhdlInterface(hdlSourcePath);
				break;
			case VERILOG:
				//	this.hwInterface = new VerilogInterface(hdlSourcePath);
				//	TODO still not implemented
				break;
			case SYSTEMC:
				//	this.hwInterface = new SystemCInterface(hdlSourcePath);
				//	TODO still not implemented
				break;
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("creation of IP was not possible");
		}
	}
	
	/**
	 * Getter of the name for IP
	 * @return the name of that IP.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *  Setter of the name for IP
	 * @param name : IP name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter of the id for IP
	 * @return the id of the IP
	 */
	public String getIdIP() {
		return idIP;
	}
	
	/**
	 * Setter of the id for IP
	 * @param idIP : IP identifier
	 */
	public void setIdIP(String idIP) {
		this.idIP = idIP;
	}
	
	/**
	 * Getter of the description for IP
	 * @return the description of the IP
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Setter of the description for IP
	 * @param description : IP description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Getter of the hardware properties for IP
	 * @return the hardware properties
	 */
	public HardwareProperties getHwProperties() {
		return hwProperties;
	}
	
	/**
	 * Setter of the hardware properites for IP
	 * @param hwProperties : IP hardware properties
	 */
	public void setHwProperties(HardwareProperties hwProperties) {
		this.hwProperties = hwProperties;
	}
	
	/**
	 * Getter of the contact point of IP
	 * @return the contact point for the IP
	 */
	public Author getContactPoint() {
		return contactPoint;
	}
	
	/**
	 * Setter of the contact point for IP
	 * @param author : IP contact point
	 */
	public void setContactPoint(Author author) {
		this.contactPoint = author;
	}
	
	/**
	 * Getter of the hdl source path for IP
	 * @return the hdl source path of that IP
	 */
	public String getHdlSourcePath() {
		return hdlSourcePath;
	}
	
	/**
	 * Setter of the hdl source path for IP
	 * @param hdlSourcePath : IP hdl source path
	 */
	public void setHdlSourcePath(String hdlSourcePath) {
		this.hdlSourcePath = hdlSourcePath;
	}
	
	/**
	 * Getter of the hardware interface for IP
	 * @return the hardware interface object for that IP
	 */
	public HardwareInterface getHwInterface() {
		return hwInterface;
	}
	
	/**
	 * Setter of the hardware interface for IP
	 * @param hwInterface : IP hardware interface
	 */
	public void setHwInterface(HardwareInterface hwInterface) {
		this.hwInterface = hwInterface;
	}
	
	/**
	 * Custumized toString()
	 */
	@Override
	public String toString() {
		StringBuffer strb = new StringBuffer("");
		
		strb.append("\tname            : "+name+"\n");
		strb.append("\tid IP           : "+idIP+"\n");
		strb.append("\thds source path : "+hdlSourcePath+"\n");
		strb.append("\tcontact point   : "+contactPoint.toString()+"\n");
		strb.append("\n"+description+"\n");
		strb.append(hwProperties.toString()+"\n");
		strb.append(hwInterface.toString()+"\n");
		
		return  strb.toString();
	}
	
	/**
	 * auto-generated by Eclipse
	 * @return hash code of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactPoint == null) ? 0 : contactPoint.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((hdlSourcePath == null) ? 0 : hdlSourcePath.hashCode());
		result = prime * result + ((hwInterface == null) ? 0 : hwInterface.hashCode());
		result = prime * result + ((hwProperties == null) ? 0 : hwProperties.hashCode());
		result = prime * result + ((idIP == null) ? 0 : idIP.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	/**
	 * auto-generated by Eclipse
	 * @return boolean indicating if the two objects are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IP other = (IP) obj;
		if (contactPoint == null) {
			if (other.contactPoint != null)
				return false;
		} else if (!contactPoint.equals(other.contactPoint))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (hdlSourcePath == null) {
			if (other.hdlSourcePath != null)
				return false;
		} else if (!hdlSourcePath.equals(other.hdlSourcePath))
			return false;
		if (hwInterface == null) {
			if (other.hwInterface != null)
				return false;
		} else if (!hwInterface.equals(other.hwInterface))
			return false;
		if (hwProperties == null) {
			if (other.hwProperties != null)
				return false;
		} else if (!hwProperties.equals(other.hwProperties))
			return false;
		if (idIP == null) {
			if (other.idIP != null)
				return false;
		} else if (!idIP.equals(other.idIP))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * This method is a simpler version of toString(). 
	 * @return a shorter set of information referred to IP object
	 */
	public String getBrief() {
		StringBuffer strb = new StringBuffer("");
		
		strb.append("id   : "+idIP+"\n");
		strb.append("name : "+name+"\n");
		strb.append("\n");
		strb.append(description+"\n");
		strb.append("\n");
		strb.append(hwProperties.toString()+"\n");
		
		return strb.toString();
	}
}
