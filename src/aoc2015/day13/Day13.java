package aoc2015.day13;

import java.util.List;

import aocutil.io.FileReader;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "day13_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day13.class.getResource( "day13_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the optimal seating arrangement based upon the liking ratings
	 * described within the input
	 * 
	 * @param input The strings describing the inter-people relationships through
	 *   happiness modifiers
	 * @return The maximum happiness that can be obtained by carefull placement
	 *   of people according to their preferences
	 */
	private static long part1( final List<String> input ) {
		final SeatingArranger SA = SeatingArranger.fromStringList( input );
		final String bestseating = SA.getBestSeating( );
		return Long.parseLong( bestseating.split( ": " )[1] );
	}
	
	/**
	 * Determines the optimal seating arrangement based upon the liking ratings
	 * described within the input, but now also includes an additional "neutral"
	 * guest that has happiness 0 modifier towards all other people.
	 * 
	 * @param input The strings describing the inter-people relationships through
	 *   happiness modifiers
	 * @return The maximum happiness that can be obtained by carefull placement
	 *   of people according to their preferences
	 */
	private static long part2( final List<String> input ) {
		final SeatingArranger SA = SeatingArranger.fromStringList( input ).addNeutral( );
		final String bestseating = SA.getBestSeating( );
		return Long.parseLong( bestseating.split( ": " )[1] );
	}
}
