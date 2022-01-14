package aoc2017.day24;

import aocutil.object.LabeledObject;

/**
 * A single bridge component with two ports that can be connected to
 * 
 * @author Joris
 */
public class BridgeComponent extends LabeledObject {
	/** The number of connector pins on both ends */
	private int p1, p2;
	
	/**
	 * Creates a new connector
	 * 
	 * @param port1 The first port
	 * @param port2 The second port
	 */
	public BridgeComponent( final int port1, final int port2 ) {
		super( port1 + "/" + port2 );
		
		// assign ports to their correct pins
		p1 = Math.min( port1, port2 );
		p2 = Math.max( port1, port2 );
	}
	
	/** @return The strength of this bridge component */
	public int getStrength( ) {
		return p1 + p2;
	}

	/** 
	 * Determines the pin on the other side of p
	 * 
	 * @param p The number of pins of the side we know
	 * @return The number of pins opposite of p
	 */
	public int getOpposite( final int p ) {
		if( p1 == p ) return p2;
		if( p2 == p ) return p1;
		
		throw new IllegalArgumentException( "Pin size " + p + " not part of this component " + toString( ) );
	}
	
	/**
	 * Checks if this bridge component that has a pin that matches the requested
	 * number of pins
	 * 
	 * @param pins The number of pins
	 * @return True iff p1 or p2 equals the number of pins
	 */
	public boolean fits( final int pins ) {
		return p1 == pins || p2 == pins;
	}
	
	/**
	 * Creates a bridge component from a string description
	 * 
	 * @param input The input string
	 * @return The {@link BridgeComponent}
	 */
	public static BridgeComponent fromString( final String input ) {
		final String[] s = input.split( "/" ); 
		return new BridgeComponent( Integer.parseInt( s[0] ), Integer.parseInt( s[1] ) );
	}
}
