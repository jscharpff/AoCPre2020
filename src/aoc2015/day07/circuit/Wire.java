package aoc2015.day07.circuit;

import aocutil.object.LabeledObject;

/**
 * Represents a single wire or literal
 * 
 * @author Joris
 */
public class Wire extends LabeledObject {
	/** The literal value */
	protected final int value;
	
	/**
	 * Creates a new wire
	 * 
	 * @param label The wire label
	 */
	private Wire( final String label ) {
		super( label );
		this.value = -1;
	}
	
	/**
	 * Creates a new literal value wire
	 * 
	 * @param value The value
	 */
	private Wire( final int value ) {
		super( "LIT:" + value );
		this.value = value;
	}
	
	/** @return True iff this Wire is a literal value */
	public boolean isLiteral( ) { return this.value != -1; }
	
	/** @return The string description of the label */
	@Override
	public String toString( ) {
		return isLiteral( ) ? "" + this.value : label;
	}
	
	/**
	 * Reconstructs a wire from a string value
	 * 
	 * @param input The string value
	 * @return The wire
	 */
	public static Wire fromString( final String input ) {
		try {
			return new Wire( Integer.parseInt( input ) );
		} catch( NumberFormatException nfe ) {
			return new Wire( input );
		}
	}
}
