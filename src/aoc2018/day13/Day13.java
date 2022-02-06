package aoc2018.day13;

import java.util.List;

import aocutil.io.FileReader;

public class Day13 {
	
	/**
	 * Day 13 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day13.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day13.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Determines the position of the first collision on a set of cart tracks
	 * 
	 * @param input The track layout
	 * @return The position at which the first collision occurs
	 */
	protected static String part1( final List<String> input ) {
		final TrackLayout tracks = TrackLayout.fromStringList( input );
		return tracks.moveUntilCollision( ).toString( );
	}

	/**
	 * Keeps moving the carts until all collided carts have been removed and only
	 * a single one remains
	 * 
	 * @param input The track layout
	 * @return The coordinate of the last remaining cart
	 */
	protected static String part2( final List<String> input ) {
		final TrackLayout tracks = TrackLayout.fromStringList( input );
		return tracks.moveUntilAllCollided( ).toString( );
	}
}
