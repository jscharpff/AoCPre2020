package aoc2016.day15;

import java.util.List;

import aocutil.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "day15_example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day15.class.getResource( "day15_example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "day15_input.txt" ) ).readLines( );
		final List<String> input2 = new FileReader( Day15.class.getResource( "day15_input2.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 15 ]---" );
		System.out.println( "Example 1: " + getCapsuleDropTime( ex_input ) );
		System.out.println( "Example 2: " + getCapsuleDropTime( ex2_input ) );
		System.out.println( "Part 1   : " + getCapsuleDropTime( input ) );
		System.out.println( "Part 2   : " + getCapsuleDropTime( input2 ) );
	}
	
	/**
	 * Determines the first time step at which the capsule can be dropped so that
	 * it falls through all the kinetic discs
	 * 
	 * @param input The description of the kinetic discs, one per line   
	 * @return The first time slot all their openings align properly
	 */
	private static long getCapsuleDropTime( final List<String> input ) {
		final Sculpture sculp = Sculpture.fromStringList( input );
		return sculp.getTimeToPress( );
	}
}
