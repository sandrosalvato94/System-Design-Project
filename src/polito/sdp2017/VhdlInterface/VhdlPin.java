package polito.sdp2017.VhdlInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import polito.sdp2017.HardwareInterface.Pin;

/**
 * VhdlPin is a class representing a pin with a given parallelism in the interface of 
 * an hardware block, described using VHDL. A VhdlPin is characterized by a name, a type,
 * a direction and an optional dimension.
 */
public class VhdlPin implements Pin {
	private String name;					//	name of the pin
	private String type;					//	type of the pin
	private VhdlPinDirection direction;		//	wheater the pin is in, out, inout or buffer
	private VhdlPinWidth dimension;			//	parallelism of the pin
	
	/**
	 * Accepts a string containing the text enclose within the port clause of a VHDL entity
	 * declaration and parses it, assuming that the syntax is correct.
	 * @param txt : inner text composing the generic clause of the entity
	 * @return a List of Generic objects, whose actual type is VhdlGeneric
	 */
	static List<Pin> parseFromSource(String txt) {
		List<Pin> list = new LinkedList<Pin>();
		String regex = "\\s*(\\w+)\\s*:\\s*(\\w+)\\s+(\\w+)\\s*((\\((.*)\\))?)";	//	regular expression
																					//	used for parsing a
																					//	single pin line,
																					//	in the port clause
																					//	of a VHDL entity
		Pattern p = Pattern.compile(regex);
		for (String genericLine : txt.split(";")) {		//	in VHDL entities the port clause is a list of
														//	pins, separated by ";"
			genericLine = genericLine.trim();			//	delete leading and trailing spaces for simpler
														//	parsing
			Matcher m = p.matcher(genericLine);
			if (m.matches()) {
				String name = m.group(1);
				String direction = m.group(2);
				String type = m.group(3);				//	no check is performed on the type, which is
														//	supposed to be correct
				String dimension = m.group(6);
				
				VhdlPinDirection pinDirection = VhdlPinDirection.vhdlPinDirectionFromString(direction);
				
				if (dimension == null) {
					list.add(new VhdlPin(name,type,pinDirection));
				} else {
					Pattern pGen = Pattern.compile("(.+)\\s+(.+)\\s+(.+)");	//	if pin dimension is not null
																			//	parses it according to this
																			//	regular expression
					Matcher mGen = pGen.matcher(dimension);
					if (mGen.matches()) {
						String left = mGen.group(1);
						String dir = mGen.group(2);							//	direction is supposed to
																			//	assumed valid values, "to"
																			//	or "downto"
						String right = mGen.group(3);
						VhdlPinWidth pinWidth = new VhdlPinWidth(left,dir,right);
						list.add(new VhdlPin(name,type,pinDirection,pinWidth));
					} else {
						throw new RuntimeException("port width format is not recognised");
					}
				}
			} else {
				throw new RuntimeException("pin format is not recognised");
			}
		}
		return list;
	}
	
	/**
	 * Constructor for VhdlPin, which simply copies the parameter passed to it, without
	 * performing any check on them.
	 * @param name : pin name
	 * @param type : pin type
	 * @param direction : wheater the pin is "in", "out", "inout" or "buffer"
	 */
	public VhdlPin(String name, String type, VhdlPinDirection direction) {
		this.name = name;
		this.type = type;
		this.direction = direction;
		this.dimension = null;
	}
	
	/**
	 * Constructor for VhdlPin, which simply copies the parameter passed to it, without
	 * performing any check on them.
	 * @param name : pin name
	 * @param type : pin type
	 * @param direction : wheater the pin is "in", "out", "inout" or "buffer"
	 * @param dimension : parallelism of the pin
	 */
	public VhdlPin(String name, String type, VhdlPinDirection direction, VhdlPinWidth dimension) {
		this.name = name;
		this.type = type;
		this.direction = direction;
		this.dimension = dimension;
	}
	
	/**
	 * @return pin name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return pin type
	 */
	@Override
	public String getType() {
		return type.toString();
	}

	/**
	 * @return pin direction
	 */
	@Override
	public String getDirection() {
		return direction.toString();
	}

	/**
	 * @return pin dimension
	 */
	@Override
	public String getDimension() {
		if (dimension != null) {
			return dimension.toString();
		}
		return "";
	}

	/**
	 * @return String representation of the pin, using a valid VHDL syntax
	 */
	@Override
	public String toString() {
		StringBuilder strb= new StringBuilder(name+" : "+direction+" "+type);
		
		if (dimension == null) {
			return strb.toString();
		} else {
			strb.append(dimension.toString());
		}
		
		return strb.toString();
	}

	/**
	 * auto-generated by Eclipse
	 * @return hash code of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		VhdlPin other = (VhdlPin) obj;
		if (dimension == null) {
			if (other.dimension != null)
				return false;
		} else if (!dimension.equals(other.dimension))
			return false;
		if (direction != other.direction)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}