package polito.sdp2017.VhdlInterface;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The VhdlPinDirection is an enumerative type, containing the four kind of
 * pin directions supported by VHDL. These directions are:
 * 		-	IN     : Representing an input which the module can read
 * 		-	OUT    : Representing a port on which the module can write
 * 		-	INOUT  : Representing a port on which the module can both read and write
 * 		-	BUFFER : Representing a signal inside the module, which can be used both
 * 					 as output and for feeding a circuit inside the module 
 */
public enum VhdlPinDirection {
	IN,			//	equivalent to "in" port
	OUT,		//	equivalent to "out" port
	INOUT,		//	equivalent to "inout" port
	BUFFER;		//	equivalent to "buffer" port
	
	/**
	 * matchMap is used for checking if a pin direction read from a VHDL source code is a valid
	 * keyword for VHDL. Unless changing in the VHDL specifications, this map is supposed
	 * to be unmodifiable.
	 */
	private static final Map<String,VhdlPinDirection> matchMap;
	static {
		Map<String,VhdlPinDirection> aMap = new TreeMap<String,VhdlPinDirection>();
		aMap.put("in", IN);
		aMap.put("out", OUT);
		aMap.put("inout", INOUT);
		aMap.put("buffer", BUFFER);
		matchMap = Collections.unmodifiableMap(aMap);
	}
	
	/**
	 * Transform a string parsed from a VHDL source file in its correspondent code
	 * expressed a VhdlPinDirection. This is accomplished by looking for the sType
	 * parameter in the matchMap.
	 * @param sType : a String, supposed to be the key for a valid pin type in the matchMap
	 * @return a VhdlPinDirection code
	 */
	public static VhdlPinDirection vhdlPinDirectionFromString (String sType) {
		String type = sType.toLowerCase();	//	Takes into account that the VHDL is case insensitive
		if (matchMap.containsKey(type)) {	//	Check if the String represent a valid PinDirection for VHDL
			return matchMap.get(type);		//	Returns the proper identifier is returned
		}
		throw new RuntimeException("Unknown Vhdl type : "+sType);	//	If the String is not recognised a
																	//	RuntimeException is raised
	}

	/**
	 * @return a valid VHDL String representing the PinDirection
	 */
	@Override
	public String toString() {
		switch(this) {
	    	case IN:
	    		return "in";
	    	case OUT:
	    		return "out";
	    	case INOUT:
	    		return "inout";
	    	case BUFFER:
	    		return "buffer";
	    	default: 
	    		throw new IllegalArgumentException("Non valid Vhdl pin direction");
	    }
	}
}
