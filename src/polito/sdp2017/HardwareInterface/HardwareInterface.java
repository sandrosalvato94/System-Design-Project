package polito.sdp2017.HardwareInterface;

import java.util.List;

/**
 * An hardware interface is a description of a certain hardware block
 * in a formal Hardware Description Language. This interface provide
 * a way for modeling such objects in an object oriented way.
 */
public interface HardwareInterface {
	/**
	 * @return String representing the Hardware Description Language in
	 * 		   which the circuit is described
	 */
	public Hdl getHDL();
	/**
	 * @return String representing formal name of the circuit described
	 */
	public String getEntityName();
	/**
	 * @return List of the pins of the module, represented through the Pin
	 * 		   interface
	 */
	public List<Pin> getPins();
	/**
	 * @return List of the parameters needed for fully characterize a physical
	 * 		   description of a circuit
	 */
	public List<Generic> getGenerics();
	public String toStringInstantiation();
}
