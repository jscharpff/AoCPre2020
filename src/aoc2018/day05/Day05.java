package aoc2018.day05;

import aocutil.io.FileReader;

public class Day05 {
	
	/**
	 * Day 5 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day05.class.getResource( "example.txt" ) ).readLines( ).get( 0 );
		final String input = new FileReader( Day05.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	protected static long part1( final String input ) {
		String in = "" + input;
		String prev;
		do {
			prev = in;
			in = reduce( in );
		} while( !in.equals( prev ) );
		
		return in.length( );
	}
	
	
	protected static long part2( final String input ) {
		long shortest = Long.MAX_VALUE;
		
		for( int c = (int)'a'; c <= (int)'z'; c++ ) {
			if( input.toLowerCase( ).indexOf( c ) == -1 ) continue;
			
			// remove all occurrences of the type
			final String in = input.replaceAll( "" + (char)c, "" ).replaceAll( "" + (char)(c-32), "" );
			
			// and reduce it
			final long len = part1( in );
			if( len < shortest ) shortest = len;
		}
		
		return shortest;
	}
	

	protected static String reduce( final String in ) {
		final StringBuffer sb = new StringBuffer( );
		for( int i = 0; i < in.length( ); i++ ) {
			if( i == in.length( ) - 1 ) {
				sb.append( in.charAt( i ) );
				break;
			}

			// do a single char lookahead, remove if it is the same character but capitalised (or vice versa)
			final char c = in.charAt( i );
			if( Math.abs( c - in.charAt( i + 1 ) ) == 32 ) {
				i++; continue;
			}
			sb.append( in.charAt( i ) );
		}
		return sb.toString( );
	}
}
