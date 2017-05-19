package polito.sdp2017.HardwareInterface;

/**
 * The Generic interface represent a generic parameter in the
 * HDL description of any hardware component. It refers to the
 * "generic" keyword in VHDL or the "parametric" keyword in Verilog.
 */
public interface Generic {
	/**
	 * @return String representing the name of the generic parameter
	 */
	public String getName();
	/**
	 * @return String representing the type of the generic parameter
	 */
	public String getType();
	/**
	 * @return String representing the default value of the generic parameter.
	 * 		   If the parameter has no default value, null is supposed to be
	 * 		   returned
	 */
	public String getDefaultValue();
}
