package polito.sdp2017.HardwareInterface;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Hdl enum is a list of the languages supported by the design environment.
 */
public enum Hdl {
	VHDL,
	VERILOG,
	SYSTEMC;
	
	/**
	 * matchMap is used for checking if a certain hdl is currently supported by the
	 * application. Whenever a new language have to be supported by the application, this
	 * map have to be modified.
	 */
	private static final Map<String,Hdl> matchMap;
	static {
		Map<String,Hdl> aMap = new TreeMap<String,Hdl>();
		aMap.put("vhdl", VHDL);
		aMap.put("vrlg", VERILOG);
		aMap.put("sysc", SYSTEMC);
		matchMap = Collections.unmodifiableMap(aMap);
	}
	
	/**
	 * Transform a string in its correspondent code expressed an Hdl. This is accomplished by
	 * looking for the hdl parameter in the matchMap.
	 * @param hdl : a String, supposed to be the key for a valid pin type in the matchMap
	 * @return a Hdl code
	 */
	public static Hdl hdlFromString (String hdl) {
		if (matchMap.containsKey(hdl)) {	//	Check if the String represent a valid PinDirection for VHDL
			return matchMap.get(hdl);		//	Returns the proper identifier is returned
		}
		throw new RuntimeException("Unknown Vhdl type : "+hdl);	//	If the String is not recognised a
																//	RuntimeException is raised
	}
	
	/**
	 * @return A textual code representing a certain Hardware Description Language
	 */
	@Override
	public String toString() {
		switch(this) {
	    	case VHDL:
	    		return "vhd";
	    	default:
	    		throw new IllegalArgumentException("hdl language not recognised");
	    }
	}
}
