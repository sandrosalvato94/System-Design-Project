package polito.sdp2017.Components;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is the derived one from IP. An IPManager has all the attributes of IP. Its task is to manage several IPCores
 * inside the FPGA.
 */
public class IPManager extends IP {
	
	/**
	 * This method is invoked during the parsing of the xml file when an IPManager is met.
	 * @param coreNodes : managers inside the DOM parser
	 * @return a list of parsed object IPManagers.
	 */
	static List<IP> getFromDomNodeList (NodeList managerNodes) {
		List<IP> listOfManagers = new LinkedList<IP>();
		Map<String,String> ipAttr = new TreeMap<String,String>();
		Map<String,String> contactAttr = new TreeMap<String,String>();
		Map<String,String> hwAttr = new TreeMap<String,String>();
		
		for (int i = 0; i < managerNodes.getLength(); i++) {
			NodeList fields = managerNodes.item(i).getChildNodes();
			for (int j = 0; j < fields.getLength(); j++) {
    			if (fields.item(j).getNodeType() == Node.ELEMENT_NODE) {
    				Node current = fields.item(j);
    				String nodeName = current.getNodeName();
    				String textContent = current.getTextContent();
    				
    				switch (nodeName) {
    				case "ContactPoint":
    					NodeList contactNList = current.getChildNodes();
    					for (int z = 0; z < contactNList.getLength(); z++) {
    						if (contactNList.item(z).getNodeType() == Node.ELEMENT_NODE) {
    							contactAttr.put(contactNList.item(z).getNodeName(), contactNList.item(z).getTextContent());
    						}
						}
    					break;
    				case "HardwareProperties":
    					NodeList hwNList = current.getChildNodes();
    					for (int z = 0; z < hwNList.getLength(); z++) {
    						if (hwNList.item(z).getNodeType() == Node.ELEMENT_NODE) {
    							hwAttr.put(hwNList.item(z).getNodeName(), hwNList.item(z).getTextContent());
    						}
						}
    					break;
    				default:
    					ipAttr.put(nodeName, textContent);
    				}
    			}
			}
    		
    		Author contactPoint = new Author(
    									contactAttr.get("AuthorId"),
    									contactAttr.get("AuthorName"),
    									contactAttr.get("AuthorCompany"),
    									contactAttr.get("AuthorEmail"),
    									contactAttr.get("AuthorRole"));
    		HardwareProperties hwProperties = new HardwareProperties(
    									Integer.parseInt(hwAttr.get("HardwarePropertiesLUTs")),
    									Integer.parseInt(hwAttr.get("HardwarePropertiesFFs")),
    									Double.parseDouble(hwAttr.get("HardwarePropertiesLatency")),
    									Integer.parseInt(hwAttr.get("HardwarePropertiesNMemories")),
    									Double.parseDouble(hwAttr.get("HardwarePropertiesPowerConsumption")),
    									Double.parseDouble(hwAttr.get("HardwarePropertiesMaxClkFreq")));
    		
    		String tmp = ipAttr.get("IPHdlSourcePath");
    		String[] splitted = tmp.split("\\.");
    		if(splitted[1].equals("vhd") || splitted.equals("vhdl")) {
    			tmp = "VHDL";
    		}
    		if(splitted[1].equals("v") || splitted.equals("verilog")) {
    			tmp = "VRLG";
    		}
    		if(splitted[1].equals("cpp")) {
    			tmp = "SYSC";
    		}
    		
    		IPManager core = new IPManager(
    									ipAttr.get("IPName"),
    									tmp+"_"+ipAttr.get("IPName"),
    									ipAttr.get("IPDescription"),
    									hwProperties,
    									contactPoint,
    									ipAttr.get("IPHdlSourcePath"));

    		listOfManagers.add(core);
		}	
		return listOfManagers;
	}
	
	/**
	 * Constructor of the class IPCManager. It simply invokes the super constructor from IP.
	 * @param name          : Manager name
	 * @param idIP          : Manager identifier
	 * @param description   : Manager description
	 * @param hwProperties  : Manager hardware properties
	 * @param author        : Manager contact point
	 * @param hdlSourcePath : Manager hdl source path
	 */
	public IPManager(String name, String idIP, String description, HardwareProperties hwProperties,
						Author author, String hdlSourcePath) {
		super(name, idIP, description, hwProperties, author, hdlSourcePath);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Customized toString() for the class IPManager
	 */
	@Override
	public String toString() {
		return "MANAGER:\n"+super.toString();
	}

}
