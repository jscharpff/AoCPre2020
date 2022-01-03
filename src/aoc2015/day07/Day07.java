package aoc2015.day07;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aoc2015.day07.circuit.Circuit;
import aoc2015.day07.circuit.Wire;
import aocutil.io.FileReader;

public class Day07 {

	/**
	 * Day 7 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "day07_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "day07_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + test( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Runs the test on the example input. Parses and runs the circuit logic and
	 * prints the resulting signal values of all signals afterwards
	 * 
	 * @param input The list of gate description
	 * @return The complete wire to value mapping after running the circuit logic
	 */
	private static String test( final List<String> input ) {
		final Circuit c = Circuit.fromStringList( input );
		final Map<Wire, Integer> result = c.run( );
		return result.toString( );
	}

	
	/**
	 * Parses the gate descriptions and runs the circuit logic. Returns the value
	 * of wire "a" after all signals have been processed
	 * 
	 * @param input The list of gate descriptions 
	 * @return The signal value on wire "a" after the circuit has ran 
	 */
	private static int part1( final List<String> input ) {
		final Circuit c = Circuit.fromStringList( input );
		final Map<Wire, Integer> result = c.run( );
		return result.getOrDefault( Wire.fromString( "a" ), -1 );
	}
	
	/**
	 * Parses the gate descriptions and runs the circuit logic twice. The first
	 * run will determine the signal value on wire "a", the second run will use
	 * that value as input for wire "b" and rerun the circuit to once more
	 * determine the value for wire "a".
	 * 
	 * @param input The list of gate descriptions 
	 * @return The signal value on wire "a" after the circuit has ran twice
	 */
	private static int part2( final List<String> input ) {
		// get the answer of part1 and override the signal for wire b with it
		final int signal = part1( input );
		
		// overwrite the description of gate b
		final List<String> newinput = new ArrayList<>( );
		for( final String s : input ) {
			if( s.endsWith( "-> b" ) )
				newinput.add( signal + " -> b" );
			else newinput.add( s );
		}
		
		// and run the updated circuit
		final Circuit c = Circuit.fromStringList( newinput );
		final Map<Wire, Integer> result = c.run( );
		return result.getOrDefault( Wire.fromString( "a" ), -1 );
	}
}
