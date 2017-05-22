package polito.sdp2017.VhdlInterface;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import polito.sdp2017.HardwareInterface.Generic;
import polito.sdp2017.HardwareInterface.HardwareInterface;
import polito.sdp2017.HardwareInterface.Pin;
import polito.sdp2017.HardwareInterface.Hdl;

/**
 * A VhdlInterface is a description of a certain hardware block in VHDL. It is
 * composed of three main elements: an entity name, a set of pins and, optionally
 * a list of generic parameters.
 */
public class VhdlInterface implements HardwareInterface {
	private String entityName;			//	entity name
	private List<Generic> generics;		//	list of generics
	private List<Pin> pins;				//	list of pins
	
	/**
	 * Accepts a VHDL source file describing a pair entity-architecture, and extract
	 * a String representing the entity declaration from such a file.
	 * @param vhdlSourcePath : path of the VHDL source file
	 * @return the entity declaration as a String
	 */
	private static String extractEntityFromSource(String vhdlSourcePath) {
		String StartEntityRegexp  = "entity.*";		//	start reading the entity the first time the word
													//	"entity" appears on the source file
		String EndEntityRegexp = "end.*";			//	finish reading the entity the first time the word
													//	"end" appears on the source file

		StringBuffer strb = new StringBuffer("");
		String tmpString;

		try (Scanner scanner = new Scanner(new File(vhdlSourcePath))) {	//	start reading the file
			while (scanner.hasNext()) {
				tmpString = scanner.nextLine().toLowerCase();			//	as long as VHDL is case
																		//	insensitive every line is
																		//	made lower-case
				if (tmpString.matches(StartEntityRegexp)) {
					strb.append(tmpString+"\n");
					while (scanner.hasNext()) {
						tmpString = scanner.nextLine().toLowerCase();
						strb.append(tmpString+"\n");
						if (tmpString.matches(EndEntityRegexp)) {
							break;
						}
					}
					break;
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();	//	IOExceptions are not managed, simply the file is not read and
									//	the default error string is printed out
		}
		return strb.toString();
	}
	
	/**
	 * Build a VhdlInterface from the source file in which it is contained.
	 * @param vhdlSourcePath : VHDL file containing the source for the hardware block to be modeled
	 */
	public VhdlInterface(String vhdlSourcePath) {
		String entitySource = extractEntityFromSource(vhdlSourcePath);	//	first of all the entity block
																		//	is extracted from the source
		String reStartEnt = "\\s*entity\\s+(.*)\\s+is\\s*";				//	regex matching the entity name
		String reGeneric = "((generic\\s*\\((.*)\\);)?)\\s*";			//	regex matching the generics
																		//	if they are present
		String rePort = "port\\s*\\((.*)\\);\\s*";						//	regex matching the ports
		String reEndEnt = "\\s*end\\s+(.*);\\s*";						//	regex matchig the entity end
		String reFinal = reStartEnt+reGeneric+rePort+reEndEnt;			//	the final regex is got by the
																		//	concatenation of all the
																		//	previous ones
		entitySource = entitySource.toLowerCase();	// TODO is this toLowerCase() necessary ???

		Pattern p = Pattern.compile(reFinal, Pattern.DOTALL);
		Matcher m = p.matcher(entitySource);
		
		if (m.matches()) {
			this.entityName = m.group(1);
			if (m.group(3) == null) {		//	if group 3 is null, the entity do not contains generics
				this.generics = null;
			} else {
				this.generics = VhdlGeneric.parseFromSource(m.group(4));	//	parse generics
			}
			this.pins = VhdlPin.parseFromSource(m.group(5));				//	parse pins
		} else {
			throw new RuntimeException("entity format is not recognised");	//	the VHDL source is supposed
																			//	to be correct, so no error
																			//	is managed
		}
	}

	/**
	 * @return an object assuring the circuit is described in VHDL
	 */
	@Override
	public Hdl getHDL() {
		return Hdl.VHDL;
	}

	/**
	 * @return String representing the name of the entity described
	 */
	@Override
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @return List of pins the entity contains
	 */
	@Override
	public List<Pin> getPins() {
		return pins;
	}

	/**
	 * @return List of generic parameters the entity contains
	 */
	@Override
	public List<Generic> getGenerics() {
		return generics;
	}

	/**
	 * @return VHDL compliant String representation of the entity
	 */
	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder("entity "+entityName+" is\n");
		if (generics != null) {
			strb.append("\tgeneric (\n\t\t");
			strb.append(generics.stream()
					.map(g->g.toString())
					.collect(Collectors.joining("\n\t\t")));
			strb.append(");\n");
		}
		
		System.out.println(pins.size());
		
		/*for(Pin p : pins)
		{
			System.out.println(p.toString());
		}*/
		strb.append("\tport (\n\t\t");
		Pin tmp = pins.remove(pins.size()-1);
		strb.append(pins.stream()
				.map(p->p.toString() + ";")
				.collect(Collectors.joining("\n\t\t")));
		strb.append("\n\t\t" + tmp.toString() + ");\n");
		strb.append(" end entity "+entityName+";\n");
			
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
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
		result = prime * result + ((generics == null) ? 0 : generics.hashCode());
		result = prime * result + ((pins == null) ? 0 : pins.hashCode());
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
		VhdlInterface other = (VhdlInterface) obj;
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		if (generics == null) {
			if (other.generics != null)
				return false;
		} else if (!generics.equals(other.generics))
			return false;
		if (pins == null) {
			if (other.pins != null)
				return false;
		} else if (!pins.equals(other.pins))
			return false;
		return true;
	}

	/**
	 * Very easy function for testing the class
	 * @param args : arguments parsed from command line
	 */
	public static void main(String[] args) {
		System.out.println("TEST MAIN FOR VhdlInterface");
		
		String testFilePath = "/Users/emanueleparisi/Desktop/acc.vhd";
		
		VhdlInterface i = new VhdlInterface(testFilePath);
		
		System.out.println(i);
	}
}
