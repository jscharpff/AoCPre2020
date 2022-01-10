package aoc2016.day06;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "day06_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day06.class.getResource( "day06_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + decodeMessage( ex_input, true ) );
		System.out.println( "Answer : " + decodeMessage( input, true ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + decodeMessage( ex_input, false ) );
		System.out.println( "Answer : " + decodeMessage( input, false ) );
	}
	
	/**
	 * Decodes the intercepter Easter Bunny communication by performing error
	 * correction
	 * 
	 * @param input The intercepted message as a list of strings
	 * @param mostcommon True to use the most common character at each position
	 *   to correct errors, false for least common   
	 * @return The decoded, error-corrected message
	 */
	private static String decodeMessage( final List<String> input, final boolean mostcommon ) {
		final int N = input.get( 0 ).length( );
		
		// count the occurrence of letter per string position 
		final Map<Integer, Map<Character, Integer>> occ = new HashMap<>( N );
		for( int i = 0; i < N; i ++ ) occ.put( i, new HashMap<>( N ) );
		
		// go over all strings and find occurrence of letters
		for( final String s : input ) {
			for( int i = 0; i < s.length( ); i++ ) {
				final Map<Character, Integer> m = occ.get( i );
				m.put( s.charAt( i ), m.getOrDefault( s.charAt( i ), 0 ) + 1 );
			}
		}
				
		// now keep only the most/least occurring character per position
		final StringBuilder decoded = new StringBuilder( N );
		for( int i = 0; i < N; i++ ) {
			final Map<Character, Integer> m = occ.get( i );
			final List<Character> chars = new ArrayList<>( m.keySet( ) );
			chars.sort( (x,y) -> { final int d = m.get( y ) - m.get( x ); return d == 0 ? x - y : d; } );
			decoded.append( chars.get( mostcommon ? 0 : chars.size( ) - 1 ) );
		}
		
		return decoded.toString( );
	}
}
