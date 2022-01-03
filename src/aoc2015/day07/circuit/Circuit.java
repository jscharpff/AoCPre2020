package aoc2015.day07.circuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Emulates Bobby Tables' circuit board of wires and logic gates
 * 
 * @author Joris
 */
public class Circuit {
	/** The list of logical gates within the circuit */
	private List<LogicGate> gates;
	
	/**
	 * Creates a new Circuit
	 * 
	 * @param gates The logical gates of the circuit board
	 */
	private Circuit( final List<LogicGate> gates ) {
		this.gates = gates;
	}
	
	/**
	 * Run the circuit board!
	 * 
	 * @return The signals that are on the wires
	 */
	public Map<Wire, Integer> run( ) {
		// create map of wires to hold current signal values
		final Map<Wire, Integer> wires = new HashMap<>( );
		
		// process all instructions
		final List<LogicGate> remaining = new ArrayList<>( gates );
		while( remaining.size( ) > 0 ) {
			final LogicGate gate = remaining.remove( 0 );
			if( !gate.process( wires ) ) remaining.add( gate );
		}
		
		return wires;
	}

	/** 
	 * Parses a circuit board from a list of strings
	 * 
	 * @param input The circuit board description
	 * @return The circuit board
	 */
	public static Circuit fromStringList( final List<String> input ) {
		return new Circuit( input.stream( ).map( LogicGate::fromString ).toList( ) );			
	}
	
	/** @return The circuit's gates */
	@Override
	public String toString( ) {
		return gates.toString( );
	}
}
