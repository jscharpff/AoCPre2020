package aoc2016.day05;

import aocutil.Util;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( "abc", "00000" ) );
		System.out.println( "Answer : " + part1( "cxdnnyjw", "00000" ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( "abc", "00000" ) );
		System.out.println( "Answer : " + part2( "cxdnnyjw", "00000" ) );
	}
	
	/**
	 * Finds the door code by finding MD5 hashes that start with the given prefix 
	 * Finds the smallest hash number that, together with the key, generates a 
	 * hash that starts with the given prefix and then extracts the next number
	 * from that hash as next digit for the code. This is repeated 8 times
	 * 
	 * @param key The private key
	 * @return The security code for the door
	 */
	private static String part1( final String key, final String prefix ) {
		final StringBuilder code = new StringBuilder( );
		for( long i = 0; i < 1000000000l; i++ ) {
			final String hash = Util.MD5( key + i );
			if( hash.startsWith( prefix ) ) {
				code.append( hash.charAt( 5 ) );
			}
			
			if( code.length( ) == 8 ) return code.toString( );
		}
		
		throw new RuntimeException( "Failed to produce valid hash" );
	}
	
	/**
	 * Same as part1 but now the hash also specifies the index at which the digit
	 * is to be set in the code 
	 * 
	 * @param key The private key
	 * @return The security code for the door
	 */
	private static String part2( final String key, final String prefix ) {
		final char[] code = new char[ 8 ];
		for( int i = 0; i < code.length; i++ ) code[i] = '_';
		
		// simply keep generating hashses until we find the prefix
		int remaining = code.length;
		for( long i = 0; i < 1000000000l; i++ ) {
			final String hash = Util.MD5( key + i );
			if( hash.startsWith( prefix ) ) {
				// parse as radix 16 to allow hex, will be ignored as they are not
				// valid index positions anyway
				final int idx = Integer.parseInt( "" + hash.charAt( 5 ), 16 );
				if( idx < 0 || idx >= code.length ) continue;
				
				// only write code if the index had no value yet
				if( code[ idx ] == '_' ) {
					remaining--;
					code[ idx ] = hash.charAt( 6 );
				}
			}
			
			// have we set all positions now?
			if( remaining == 0 ) return String.valueOf( code );
		}
		
		throw new RuntimeException( "Failed to produce valid hash" );
	}
}
