package aoc2015.day11;

import aocutil.io.FileReader;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day11.class.getResource( "day11_example.txt" ) ).readAll( );
		final String input = new FileReader( Day11.class.getResource( "day11_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part1( part1( input ) ) );
	}
	
	/**
	 * Generates the next valid password from the current password
	 * 
	 * @param input The current password 
	 * @return The next valid password, obtained by incrementing characters, that
	 *   meets the security requirements
	 */
	private static String part1( final String input ) {
		return PasswordGenerator.getNextPassword( input );
	}

}
