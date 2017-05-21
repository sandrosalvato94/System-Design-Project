package polito.sdp2017.Components;

public class HardwareProperties {

	private int LUTs;
	private int FFs;
	private double latency;
	private int nMemories;
	private double powerConsumption;
	private double maxClkFreq;

	public static void main(String[] args)
	{
		HardwareProperties hp1, hp2;
		
		hp1 = new HardwareProperties(44, 88, 2.0, 0, 0.006, 650000);
		hp2 = new HardwareProperties(126, 7, 29.0, 4, 0.106, 650000);
		
		System.out.println(hp1.toString());
		System.out.println(hp2.toString());
		
	}

	public HardwareProperties(int lUTs, int fFs, double latency, int nMemories, double powerConsumption,
			double maxClkFreq) {
		this.LUTs = lUTs;
		this.FFs = fFs;
		this.latency = latency;
		this.nMemories = nMemories;
		this.powerConsumption = powerConsumption;
		this.maxClkFreq = maxClkFreq;
	}

	public int getLUTs() {
		return LUTs;
	}

	public void setLUTs(int lUTs) {
		LUTs = lUTs;
	}

	public int getFFs() {
		return FFs;
	}

	public void setFFs(int fFs) {
		FFs = fFs;
	}

	public double getLatency() {
		return latency;
	}

	public void setLatency(double latency) {
		this.latency = latency;
	}

	public int getNMemories() {
		return nMemories;
	}

	public void setNMemories(int nMemories) {
		this.nMemories = nMemories;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public double getMaxClkFreq() {
		return maxClkFreq;
	}

	public void setMaxClkFreq(double maxClkFreq) {
		this.maxClkFreq = maxClkFreq;
	}

	public String toString() {
		return "LUTs              : " + this.LUTs + "\n" +
				"FFs               : " + this.FFs + "\n" +
				"Latency           : " + this.latency + " ns\n" +
				"Memories          : " + this.nMemories + "\n" +
				"Power consuption  : " + this.powerConsumption + " W\n" +
				"Max clk frequency : " + this.maxClkFreq + " Hz\n";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FFs;
		result = prime * result + LUTs;
		long temp;
		temp = Double.doubleToLongBits(latency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxClkFreq);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + nMemories;
		temp = Double.doubleToLongBits(powerConsumption);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		HardwareProperties other = (HardwareProperties) obj;
		if (FFs != other.FFs)
			return false;
		if (LUTs != other.LUTs)
			return false;
		if (Double.doubleToLongBits(latency) != Double.doubleToLongBits(other.latency))
			return false;
		if (Double.doubleToLongBits(maxClkFreq) != Double.doubleToLongBits(other.maxClkFreq))
			return false;
		if (nMemories != other.nMemories)
			return false;
		if (Double.doubleToLongBits(powerConsumption) != Double.doubleToLongBits(other.powerConsumption))
			return false;
		return true;
	}

}
