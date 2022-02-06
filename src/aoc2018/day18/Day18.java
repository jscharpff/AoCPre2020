package aoc2018.day18;

import java.util.List;

import aocutil.io.FileReader;

public class Day18 {
	
	/**
	 * Day 18 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day18.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + simulate( ex_input, 10 ) );
		System.out.println( "Part 1 : " + simulate( input, 10 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Part 2 : " + simulate( input, 1000000000 ) );
	}

	/**
	 * Simulates the lumber collection process
	 * 
	 * @param input The initial state of the area as a list of string
	 * @param steps The number of steps to simulate
	 * @return The resource value of the state resulting after the simulation
	 */
	protected static long simulate( final List<String> input, final int steps ) {
		final LumberCollection lc = LumberCollection.fromStringList( input );
		return lc.run( steps );
	}
}
