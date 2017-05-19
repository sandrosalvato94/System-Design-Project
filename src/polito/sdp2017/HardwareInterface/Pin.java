package polito.sdp2017.HardwareInterface;

/**
 * The Pin interface represent the communication ports with which
 * an hardware block communicates with the external world; a Pin is 
 * normally characterized by a name which identifies it, a type, a
 * direction and a dimension, which is the parallelism of the port.
 */
public interface Pin {
	/**
	 * @return String representing the name of the pin
	 */
	public String getName();
	/**
	 * @return String representing the type of the pin
	 */
	public String getType();
	/**
	 * @return String representing if the pin can be read, written or both
	 */
	public String getDirection();
	/**
	 * @return String representing the parallelism of the pin. If the pin is
	 * 		   a single wire this should return an empty string
	 */
	public String getDimension();
}
