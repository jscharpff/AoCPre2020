package aoc2017.day02;

import java.util.List;

import aocutil.io.FileReader;
import aocutil.string.RegexMatcher;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day02.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the checksum of the spreadsheet by summing over the difference
	 * between max and min values per row
	 * 
	 * @param input The spreadsheet as string of columns per row 
	 * @return The checksum
	 */
	private static long part1( final List<String> input ) {
		int checksum = 0;
		for( final String s : input ) {
			final RegexMatcher r = new RegexMatcher( "(-?\\d+)" );
			final List<Integer> matches = r.matchAll( s, x -> Integer.parseInt( x.group( 1 ) ) );
			matches.sort( Integer::compareTo );
			
			checksum += matches.size() >= 2 ? matches.get( matches.size( ) - 1 ) - matches.get( 0 ) : 0; 
		}
		return checksum;
	}
	
	/**
	 * Determines the checksum of the spreadsheet by for every row finding the
	 * two values that are integer divisible and adding the result of that
	 * division to the current checksum 
	 *  
	 * @param input The spreadsheet as string of columns per row 
	 * @return The checksum
	 */
	private static long part2( final List<String> input ) {
		int checksum = 0;
		for( final String s : input ) {
			final RegexMatcher r = new RegexMatcher( "(-?\\d+)" );
			final List<Integer> matches = r.matchAll( s, x -> Integer.parseInt( x.group( 1 ) ) );
			final Integer[] M = matches.toArray( new Integer[ 0 ] );
			
			// only sum the two integers that allow an integer division
			for( int i = 0; i < matches.size( ) - 1; i++ )
				for( int j = i + 1; j < matches.size( ); j++ )					
					if( M[i] % M[j] == 0 ) checksum += M[i] / M[j];
					else if( M[j] % M[i] == 0 ) checksum += M[j] / M[i];
			
		}
		return checksum;
	}
}
