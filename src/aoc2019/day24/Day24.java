package aoc2019.day24;

import java.util.List;

import aocutil.io.FileReader;

public class Day24 {
	
	/**
	 * Day 24 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day24.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 10 ) );
		System.out.println( "Part 2 : " + part2( input, 200 ) );
	}
	
	/**
	 * Simulates the development of bugs on planet Eris until a repeating state
	 * in the simulation is found
	 *   
	 * @param input THe list of strings that describes the initial state of the
	 *   simulation
	 * @return The bio diversity score of the first state that repeats
	 */
	protected static long part1( final List<String> input ) {
		final ErisBugs eb = ErisBugs.fromStringList( input );
		return eb.findRepeating( );
	}
	
	/**
	 * Also simulates the bug development on Eris but now in infinite dimensions
	 * 
	 * @param input THe list of strings that describes the initial state of the
	 *   simulation
	 * @param time The number of time steps to simulate
	 * @return The total number of bugs over all dimensions once the simulation
	 *   is complete
	 */
	protected static long part2( final List<String> input, final int time ) {
		final ErisBugsInfinite eb = ErisBugsInfinite.fromStringList( input );
		return eb.sim( time );
	}

}
