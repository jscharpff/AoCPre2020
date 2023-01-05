package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICEInvalidSyntax;

/**
 * Argument for instructions
 * 
 * @author Joris
 */
public class Argument {
	/** The integer value of the argument */
	protected final long value;
	
	/** The parameter mode */
	protected final AddressingMode mode;
	
	/** Addressing modes */
	public enum AddressingMode {
		Immediate,
		Position,
		Relative;
	}
	
	/**
	 * Creates a new argument
	 * 
	 * @param value The argument value
	 * @param mode The parameter addressing mode
	 * @throws ICEInvalidSyntax if the addressing mode is invalid
	 */
	public Argument( final long value, final char mode ) throws ICEInvalidSyntax {
		this.value = value;
		
		switch( mode ) {
			case '0': this.mode = AddressingMode.Position; break;
			case '1': this.mode = AddressingMode.Immediate; break;
			case '2': this.mode = AddressingMode.Relative; break;
			default: throw new ICEInvalidSyntax( "Invalid parammeter mode: " + mode ); 
		}
	}
	
	@Override
	public String toString( ) {
		switch( mode ) {
			case Immediate: return "" + value;
			case Position: return "[" + value + "]";
			case Relative: return "[+" + value + "]";
			default: throw new RuntimeException( "Parameter addressing mode not implemented in toString()" );
		}
	}
}
