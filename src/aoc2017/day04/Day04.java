package aoc2017.day04;

import java.util.List;
import java.util.stream.Stream;

import aocutil.io.FileReader;
import aocutil.string.StringUtil;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day04.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day04.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Counts the number of valid pass phrases in which no word occurs twice
	 * 
	 * @param input The list of pass phrases
	 * @return The number of valid ones
	 */
	private static long part1( final List<String> input ) {
		long count = 0;
		for( final String in : input ) {
			final String[] s = in.split( " " );
			count += Stream.of( s ).distinct( ).count( ) < s.length ? 0 : 1;
		}
		return count;
	}
	
	/**
	 * Counts the number of valid pass phrases in which no two words are anagrams
	 * 
	 * @param input The list of pass phrases
	 * @return The number of valid ones
	 */
	private static long part2( final List<String> input ) {
		long count = 0;
		for( final String in : input ) {
			final String[] s = in.split( " " );
			boolean invalid = false;
			for( int i = 0; i < s.length - 1; i++ )
				for( int j = i + 1; j < s.length; j++ )
					invalid |= StringUtil.isAnagram( s[i], s[j] );
			
			if( !invalid ) count++;
		}
		return count;
	}
}
