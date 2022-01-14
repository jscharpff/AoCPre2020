package aoc2017.day01;

import java.util.List;

import aocutil.io.FileReader;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day01.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day01.class.getResource( "example2.txt" ) ).readLines( );
		final String input = new FileReader( Day01.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example " + s + ": " + part1( s ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex2_input )
			System.out.println( "Example " + s + ": " + part2( s ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums all digits that match the subsequent digit in the input, including a
	 * wrap around
	 * 
	 * @param input The string of digits 
	 * @return The sum of all digits that match the next one in the string
	 */
	private static long part1( final String input ) {
		final int N = input.length( );
		int match = 0;
		for( int i = 0; i < N; i++ )
			match += input.charAt( i % N ) == input.charAt( (i + 1) % N ) ? (int)(input.charAt( i % N ) - '0') : 0;
		return match;
	}
	
	/**
	 * Sums all digits that match the digit that is indexed half the length of
	 * the input string away, including wrap around
	 * 
	 * @param input The string of digits 
	 * @return The sum of all digits that match the digits halfway farther
	 */	private static long part2( final String input ) {
		final int N = input.length( );
		final int half = N / 2;
		int match = 0;
		for( int i = 0; i < N; i++ )
			match += input.charAt( i % N ) == input.charAt( (i + half) % N ) ? (int)(input.charAt( i % N ) - '0') : 0;
		return match;	}
}
