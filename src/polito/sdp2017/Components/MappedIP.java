package polito.sdp2017.Components;

public class MappedIP {
	private String idMappedIP;
	private IPCore ipCore;
	private int priority;
	private String physicalAddress;

	public MappedIP(String idMappedIP, IPCore ipCore, int priority, String physicalAddress) {
		this.idMappedIP = idMappedIP;
		this.ipCore = ipCore;
		this.priority = priority;
		this.physicalAddress = physicalAddress;
	}

	public String getIdMappedIP() {
		return idMappedIP;
	}

	public void setIdMappedIP(String idMappedIP) {
		this.idMappedIP = idMappedIP;
	}

	public IPCore getIpCore() {
		return ipCore;
	}

	public void setIpCore(IPCore ipCore) {
		this.ipCore = ipCore;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder(ipCore.toString()+"\n");
		
		strb.append("id mapped IP : "+idMappedIP+"\n");
		strb.append("priority     : "+priority+"\n");
		strb.append("phys address : "+physicalAddress+"\n");
		return strb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMappedIP == null) ? 0 : idMappedIP.hashCode());
		result = prime * result + ((ipCore == null) ? 0 : ipCore.hashCode());
		result = prime * result + ((physicalAddress == null) ? 0 : physicalAddress.hashCode());
		result = prime * result + priority;
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
		MappedIP other = (MappedIP) obj;
		if (idMappedIP == null) {
			if (other.idMappedIP != null)
				return false;
		} else if (!idMappedIP.equals(other.idMappedIP))
			return false;
		if (ipCore == null) {
			if (other.ipCore != null)
				return false;
		} else if (!ipCore.equals(other.ipCore))
			return false;
		if (physicalAddress == null) {
			if (other.physicalAddress != null)
				return false;
		} else if (!physicalAddress.equals(other.physicalAddress))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}
}
