package aoc2018.day10;

import java.util.List;

import aocutil.io.FileReader;

public class Day10 {
	
	/**
	 * Day 10 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day10.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 10 ]---" );
		System.out.println( "Example:");
		getStarMessage( ex_input, 10 );
		System.out.println(  );
		System.out.println( "Part 1 & 2:" );
		getStarMessage( input, 10 );
	}

	/**
	 * Runs the simulation of star movements until we were able to print the
	 * given number of messages because the stars were in close proximity of one
	 * another
	 * 
	 * @param input The description of the stars and their velocities
	 * @param prints The number of prints we output when stars are in proximity
	 */
	protected static void getStarMessage( final List<String> input, int prints ) {
		final StarMessage s = new StarMessage( input );
		printStars( s );
		
		for( int i = 0; i < 100000 && prints > 0; i++ ) {
			s.move( );
			if( printStars( s ) ) {
				System.out.println( "Round " + (i+1) );
				prints--; 
			}
		}
	}
	
	/**
	 * Prints the message of the stars when the range of coordinates is not
	 * wider than 100 units
	 * 
	 * @param s The star message object holding the star positions
	 * @return True if the message was printed, i.e. the stars were close enough
	 */
	private static boolean printStars( final StarMessage s ) {
		if( s.getWindow( ).size( ).x > 100 || s.getWindow( ).size( ).y > 100 ) {
			return false;
		}
		
		System.out.println( s.toString( ) );
		return true;
	}
}
