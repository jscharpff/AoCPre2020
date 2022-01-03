package aoc2015.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "day08_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day08.class.getResource( "day08_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + test( ex_input, true ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + test( ex_input, false ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Test for part1 that compares the unescaped/escaped string against it
	 * original input 
	 * 
	 * @param input The set of escaped strings
	 * @param unescape True to unescape, false for escape 
	 * @return A string that contains the 
	 */
	private static String test( final List<String> input, final boolean unescape ) {
		// returns all processed strings on a line
		return input.stream( )
				.map( x -> { final String y = unescape ? StringEscaper.unescape( x ) : StringEscaper.escape( x ); return x + ": " + y + " (" + x.length( ) + "/" + y.length( ) + ")"; } )
				.reduce( "", (x,y) -> x + "\n" + y );
	}
	
	/**
	 * Unescapes all input strings and computes the decrease in length after
	 * unescaping. Returns the sum of all length differences 
	 * 
	 * @param input The list of input strings 
	 * @return The total length difference over all original minus unescaped
	 *   string lengths
	 */
	private static long part1( final List<String> input ) {
		return input.stream( ).mapToInt( x -> (x.length( ) - StringEscaper.unescape( x ).length( )) ).sum( );
	}
	
	/**
	 * Escapes all input strings and computes the decrease in length after
	 * escaping. Returns the sum of all length differences 
	 * 
	 * @param input The list of input strings 
	 * @return The total length difference over all escaped minus original 
	 *   string lengths
	 */
	private static long part2( final List<String> input ) {
		return input.stream( ).mapToInt( x -> (StringEscaper.escape( x ).length( ) - x.length( )) ).sum( );
	}
}
