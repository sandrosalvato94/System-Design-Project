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

public class IP {
	private String name;
	private String idIP;
	private String description;
	private HardwareProperties hwProperties;
	private Author contactPoint;
	private String hdlSourcePath;
	private HardwareInterface hwInterface;
	
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
	
	public IP(String name, String idIP, String description, HardwareProperties hwProperties,
			Author contactPoint, String hdlSourcePath) {		
		try {
			String hdlStringCode = idIP.split("_")[0];
			Hdl ipHdl = Hdl.hdlFromString(hdlStringCode.toLowerCase());
			
			this.name = name;
			this.idIP = idIP;
			this.description = description;
			this.hwProperties = hwProperties;
			this.contactPoint = contactPoint;
			this.hdlSourcePath = hdlSourcePath;
			
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdIP() {
		return idIP;
	}

	public void setIdIP(String idIP) {
		this.idIP = idIP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HardwareProperties getHwProperties() {
		return hwProperties;
	}

	public void setHwProperties(HardwareProperties hwProperties) {
		this.hwProperties = hwProperties;
	}

	public Author getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(Author author) {
		this.contactPoint = author;
	}

	public String getHdlSourcePath() {
		return hdlSourcePath;
	}

	public void setHdlSourcePath(String hdlSourcePath) {
		this.hdlSourcePath = hdlSourcePath;
	}

	public HardwareInterface getHwInterface() {
		return hwInterface;
	}

	public void setHwInterface(HardwareInterface hwInterface) {
		this.hwInterface = hwInterface;
	}
	
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
}
