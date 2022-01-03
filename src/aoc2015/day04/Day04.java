package aoc2015.day04;

import java.util.List;

import aocutil.io.FileReader;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "day04_example.txt" ) ).readLines( );
		final String input = new FileReader( Day04.class.getResource( "day04_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + generateHash( s, "00000" ) );
		System.out.println( "Answer : " + generateHash( input, "00000" ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + generateHash( input, "000000" ) );
	}
	
	/**
	 * Generates the smallest Advent Coin Hash that matches the specified prefix
	 * 
	 * @param input The secret key to use to generate a hash
	 * @param prefix The prefix to match
	 * @return The first number that generates a valid Advent Coin Hash
	 */
	private static long generateHash( final String input, final String prefix ) {
		final AdventCoins coins = new AdventCoins( );		
		return coins.getSmallestHashNumber( input, prefix );
	}
}
