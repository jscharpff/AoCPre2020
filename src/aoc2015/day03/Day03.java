package aoc2015.day03;

import java.util.List;

import aocutil.io.FileReader;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day03.class.getResource( "day03_example.txt" ) ).readLines( );
		final String input = new FileReader( Day03.class.getResource( "day03_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + deliver( s, false ) );
		System.out.println( "Answer : " + deliver( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + deliver( s, true ) );
		System.out.println( "Answer : " + deliver( input, true ) );
	}
	
	/**
	 * Instructs Santa to deliver presents according to a route that is given by
	 * the elves
	 * 
	 * @param input The route described by a string of <, >, ^ and v symbols
	 * @param userobot True to help Santa out with a delivery Robot  
	 * @return The number of houses visited by Santa (and its Robot helper)
	 */
	private static long deliver( final String input, final boolean userobot ) {
		final DeliveryTracker d = new DeliveryTracker( );
		d.deliver( input, userobot );
		return d.countHousesDelivered( );
	}

}
