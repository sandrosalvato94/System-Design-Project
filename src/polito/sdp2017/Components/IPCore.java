package polito.sdp2017.Components;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IPCore extends IP {
	private String driverPath;
    
	static List<IP> getFromDomNodeList (NodeList coreNodes) {
		List<IP> listOfCores = new LinkedList<IP>();
		Map<String,String> ipAttr = new TreeMap<String,String>();
		Map<String,String> contactAttr = new TreeMap<String,String>();
		Map<String,String> hwAttr = new TreeMap<String,String>();
		
		for (int i = 0; i < coreNodes.getLength(); i++) {
			NodeList fields = coreNodes.item(i).getChildNodes();
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

    		IPCore core = new IPCore(
    									ipAttr.get("IPName"),
    									ipAttr.get("IPId"),
    									ipAttr.get("IPDescription"),
    									hwProperties,
    									contactPoint,
    									ipAttr.get("IPHdlSourcePath"),
    									ipAttr.get("IPDriverPath"));

    		listOfCores.add(core);
    		//System.out.println(contactPoint.toString());
    		//System.out.println(hwProperties.toString());
    		//System.out.println(core.toString());
		}
		
		return listOfCores;
	}
	
	public IPCore(String name, String idIP, String description, HardwareProperties hwProperties, Author contactPoint, String hdlSourcePath, String driverPath) {
		super(name, idIP, description, hwProperties, contactPoint, hdlSourcePath);
		this.driverPath = driverPath;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	@Override
	public String toString() {
		return "CORE:\n"+super.toString()+"\n\tdriverPath : "+driverPath+"\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((driverPath == null) ? 0 : driverPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPCore other = (IPCore) obj;
		if (driverPath == null) {
			if (other.driverPath != null)
				return false;
		} else if (!driverPath.equals(other.driverPath))
			return false;
		return true;
	}
}
