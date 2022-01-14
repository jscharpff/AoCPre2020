package aoc2017.day10;

import java.util.List;

import aocutil.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day10.class.getResource( "example.txt" ) ).readAll( );
		final List<String> ex2_input = new FileReader( Day10.class.getResource( "example2.txt" ) ).readLines( );
		final String input = new FileReader( Day10.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( 5, ex_input ) );
		System.out.println( "Answer : " + part1( 256, input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex2_input )
			System.out.println( "Example: " + part2( s ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Performs a single round of KnotHashing 
	 * 
	 * @param N The ring size
	 * @param input The length sequence to use in the hashing process
	 * @return The product of the first two hash integers
	 */
	private static long part1( final int N, final String input ) {
		final HashKnot h = new HashKnot( input, N, false );
		final int[] hash = h.hash( );
		return hash[0] * hash[1];
	}
	
	/**
	 * Determines the hash for the input string
	 * 
	 * @param input The input string to feed to the hashing algorithm 
	 * @return The hash of the input string
	 */
	private static String part2( final String input ) {
		final HashKnot h = new HashKnot( input, 256, true );
		return h.hash( 64 );
	}
}
