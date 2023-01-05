package aoc2019.day23.network;

/**
 * Single packet that can be transmitted over the network
 * 
 * @author Joris
 */
public class ICPacket {
	/** The source address */
	protected final int source;
	
	/** The destination address */
	protected final int dest;
	
	/** The X value */
	protected final long X;
	
	/** the Y value */
	protected final long Y;
	
	/**
	 * Creates a new packet
	 * 
	 * @param source The source interface address
	 * @param dest The destination interface address
	 * @param X
	 * @param Y
	 */
	public ICPacket( final int source, final int dest, final long X, final long Y ) {
		this.source = source;
		this.dest = dest;
		this.X = X;
		this.Y = Y;
	}
	
	/**
	 * Creates a new packet that has the same origin and content but with a new
	 * destination
	 * 
	 * @param newdest The new destination address
	 * @return The new packet with updated destination
	 */
	public ICPacket reroute( final int newdest ) {
		return new ICPacket( source, newdest, X, Y );
	}
	
	/** @return The string describing the packet */
	@Override
	public String toString( ) {
		return "[src: " + source + ", dst: " + dest + ", X: " + X + ", Y: " + Y + "]";
	}
}
