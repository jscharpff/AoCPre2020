package aoc2015.day09;

import java.util.List;

import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "day09_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day09.class.getResource( "day09_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, true ) );
		System.out.println( "Answer : " + part1( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part1( ex_input, false ) );
		System.out.println( "Answer : " + part1( input, false ) );
	}
	
	/**
	 * Computes the shortest/longest route length for Santa to deliver his
	 * presents to all the cities mentioned in the distances list
	 * 
	 * @param input The list of all inter-city distances
	 * @param shortest True to return the shortest distance, false for longest 
	 * @return The shortest/longest distance Santa needs to travel to visit all
	 *   cities exactly once.
	 */
	private static long part1( final List<String> input, final boolean shortest ) {
		final DeliveryRouter DR = new DeliveryRouter( input );
		final String bestroute = shortest ? DR.getShortestRoute( ) : DR.getLongestRoute( );
		return Long.parseLong( bestroute.split( ": " )[1] );
	}
}
