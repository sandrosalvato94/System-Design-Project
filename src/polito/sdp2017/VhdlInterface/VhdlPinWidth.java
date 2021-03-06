package polito.sdp2017.VhdlInterface;

/**
 * VhdlPinWidth is a class representing the parallelism of a pin within a
 * module, using the VHDL syntax. The PinWidth is characterized by a left bound,
 * a right bound and a direction, representing the way in which the pins are numbered.
 * Both the left and right attributes are Strings, for allowing the definition of
 * parametric bounds.
 */
public class VhdlPinWidth {
	private String left;		//	left bound of the bus
	private String right;		//	right bound of the bus
	private String direction;	// 	direction, it is supposed to be "to" or "downto"
	
	/**
	 * Constructor for VhdlPinWidth, which simply copies the parameter passed to it, without
	 * performing any check on them.
	 * @param left      : left bound
	 * @param direction : direction, it is supposed to be "to" or "downto"
	 * @param right     : right bound
	 */
	public VhdlPinWidth(String left, String direction, String right) {
		this.left = left;
		this.right = right;
		this.direction = direction;
	}

	/**
	 * @return the left bound of the bus
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * Set the left bound of the bus
	 * @param left : left side of the pin wifth
	 */
	public void setLeft(String left) {
		this.left = left;
	}

	/**
	 * @return the right bound of the bus
	 */
	public String getRight() {
		return right;
	}

	/**
	 * Set the right bound of the bus
	 * @param right : right side of the pin width
	 */
	public void setRight(String right) {
		this.right = right;
	}

	/**
	 * @return the direction for the numbering of the pins on the bus
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @return a String representing the pin parallelism using the VHDL syntax
	 */
	public String toString() {
		return left+" "+direction+" "+right;
	}

	/**
	 * auto-generated by Eclipse
	 * @return hash code of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		VhdlPinWidth other = (VhdlPinWidth) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
}
