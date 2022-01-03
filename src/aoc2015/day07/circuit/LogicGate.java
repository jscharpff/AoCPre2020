package aoc2015.day07.circuit;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single logical gate
 * 
 * @author Joris
 */
public class LogicGate {
	/** The output wire of this gate */
	private final Wire output;
	
	/** The input wire(s) */
	private final Wire[] inputs;
	
	/** The operation that is to be performed */
	private final Op op;

	/** The possible operations of this gate */
	private enum Op {
		Set,
		And,
		Or,
		Not,
		LShift,
		RShift;
		
		protected static Op fromString( final String input ) {
			for( final Op o : values( ) )
				if( o.toString( ).toUpperCase( ).equals( input ) ) return o;
			
			throw new RuntimeException( "Unknown operand: " + input );
		}
	}
	
	/**
	 * Creates a new logical gate
	 * 
	 * @param inputs The input wires
	 * @param output The output wire
	 * @param op The logical operation to perform
	 * @param value The value to use in 
	 */
	protected LogicGate( final Wire[] inputs, final Wire output, final Op op ) {
		this.inputs = inputs != null ? inputs.clone( ) : new Wire[ 0 ];
		this.output = output;
		this.op = op;
	}
	
	/**
	 * Performs the operation of the logical gate, given the current wire
	 * values. Will add/update the resulting value for the output wire in the map 
	 * 
	 * @param wires The current value of the wires
	 * @return True iff the gate was successfully processed, i.e. its input wires
	 *   all had a valid value. False otherwise
	 */
	public boolean process( final Map<Wire, Integer> wires ) {
		// prepare input values
		final int[] in = new int[ inputs.length ];
		for( int i = 0; i < in.length; i++ ) {
			if( inputs[i].isLiteral( ) ) { 
				in[i] = inputs[i].value;
				continue;
			}
			
			if( !wires.containsKey( inputs[i] ) ) return false;
			in[i] = wires.get( inputs[i] );		
		}
		
		// process the gate logic
		final int newvalue;
		switch( op ) {
			case Set: newvalue = in[0]; break;
			case And: newvalue = in[0] & in[1]; break;
			case Or:  newvalue = in[0] | in[1]; break;
			case Not:	newvalue = 65535 - in[0]; break;
			case LShift: newvalue = in[0] << in[1]; break;
			case RShift: newvalue = in[0] >> in[1]; break;
			
			default: throw new RuntimeException( "Gate logic not implemented: " + op );
		}
		
		// update the wire map as a result of the operation
		wires.put( output, newvalue );
		return true;
	}
	
	/**
	 * Constructs the gate from a string description
	 * 
	 * @param gate The gate description
	 * @return The LogicGate that models the logic
	 */
	public static LogicGate fromString( final String gate ) {
		// splits the string at the result
		final String[] s = gate.split( " -> " );
		final Wire output = Wire.fromString( s[1] );
		
		// parse a binary operation
		Matcher m = Pattern.compile( "^(\\d+|\\w+) (AND|OR|LSHIFT|RSHIFT) (\\d+|\\w+)$" ).matcher( s[0] );
		if( m.find( ) ) {
			final Op op = Op.fromString( m.group( 2 ) );
			return new LogicGate( new Wire[] { Wire.fromString( m.group( 1 ) ), Wire.fromString( m.group( 3 ) ) }, output, op );
		}
		
		// not operation
		m = Pattern.compile( "^NOT (\\d+|\\w+)$" ).matcher( s[0] );
		if( m.find( ) ) {
			return new LogicGate( new Wire[] { Wire.fromString( m.group( 1 ) ) }, output, Op.Not );
		}
		
		// parse a simple set operation
		m = Pattern.compile( "^(\\d+|\\w+)$" ).matcher( s[0] );
		if( m.find( ) ) {
			return new LogicGate( new Wire[] { Wire.fromString( m.group( 1 ) ) }, output, Op.Set );
		}
	
		// unknown gate
		throw new RuntimeException( "Invalid gate: " + gate );
	}
	
	/** @return The string representation of the gate */
	@Override
	public String toString( ) {
		switch( op ) {
			case Set: return inputs[0] + " -> " + output;
			case Not: return "NOT " + inputs[0] + " -> " + output;

			case And:  
			case Or: 
			case LShift:
			case RShift:
				return inputs[0] + " " + op.toString( ).toUpperCase( ) + " " + inputs[1] + " -> " + output;
		}
		
		throw new RuntimeException( "Unknown logical operation: " + op );
	}
}
