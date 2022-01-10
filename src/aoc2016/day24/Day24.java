package aoc2016.day24;

import java.util.List;

import aocutil.io.FileReader;

public class Day24 {

	/**
	 * Day 24 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "day24_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day24.class.getResource( "day24_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + getTravelDistance( ex_input, false ) );
		System.out.println( "Answer : " + getTravelDistance( input, false ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + getTravelDistance( ex_input, true ) );
		System.out.println( "Answer : " + getTravelDistance( input, true ) );
	}
	
	/**
	 * Determines the minimal travel distance for the robot in the air ducts to
	 * travel to all the exposed wires
	 * 
	 * @param input The air duct grid as textual representation
	 * @param roundtrip True if the robot needs to return to its starting
	 *   position   
	 * @return The minimal travel distance
	 */
	private static long getTravelDistance( final List<String> input, final boolean roundtrip ) {
		final AirDuctSystem ads = AirDuctSystem.fromStringList( input );
		return ads.getTravelDistance( roundtrip );
	}
}
