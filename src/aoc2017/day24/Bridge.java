package aoc2017.day24;

import java.util.ArrayList;
import java.util.List;

/**
 * A bridge of components
 * 
 * @author Joris
 */
public class Bridge {
	/** The set of components that span this bridge */
	private final List<BridgeComponent> components;
	
	/** The strength of the bridge */
	private int strength;
	
	/** The current pin we can extend from */
	private int out;
	
	/**
	 * Creates a new empty bridge
	 */
	public Bridge( ) {
		this( new ArrayList<>( ) );
	}
	
	/**
	 * Copies an existing bridge
	 * 
	 * @param bridge The bridge to copy
	 */
	public Bridge( final Bridge bridge ) {
		this( bridge.components );
	}
	
	/**
	 * Creates a new bridge
	 * 
	 * @param components The components that make up this bridge 
	 */
	private Bridge( final List<BridgeComponent> components ) {
		this.components = new ArrayList<>( );
		this.strength = 0;
		this.out = 0;
		for( final BridgeComponent c : components ) extend( c );
	}
	
	/**
	 * Extends the bridge by one component
	 * 
	 * @param comp The component to add
	 * @return The new strength of the bridge
	 */
	public int extend( final BridgeComponent comp ) {
		components.add( comp );
		strength += comp.getStrength( );
		out = comp.getOpposite( out );		
		return strength;
	}
	
	/** 
	 * Removes the last component from the bridge
	 *
	 * @return The updated strength 
	 */
	public int pop( ) {
		final BridgeComponent c = components.remove( components.size( ) - 1 );
		strength -= c.getStrength( );
		out = c.getOpposite( out );
		return strength;
	}
	
	/**
	 * @return The pin at the end of the bridge
	 */
	public int outPin( ) {
		return out;
	}
	
	/** @return The strength of the bridge */
	public int getStrength( ) { return strength; }
	
	/** @return The length of the bridge */
	public int length( ) { return components.size( ); }
}
