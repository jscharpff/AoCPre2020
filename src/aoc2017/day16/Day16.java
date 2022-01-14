package aoc2017.day16;

import java.util.Arrays;

import aocutil.io.FileReader;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day16.class.getResource( "example.txt" ) ).readAll( );
		final String input = new FileReader( Day16.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( 5, ex_input, 1 ) );
		System.out.println( "Answer : " + part1( 16, input, 1 ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part1( 5, ex_input, 1000000000 ) );
		System.out.println( "Answer : " + part1( 16, input, 1000000000 ) );
	}
	
	/**
	 * Performs the program dance for several times
	 * 
	 * @param programs The number of programs dancing
	 * @param input The dance moves as a comma separated list
	 * @param rounds The number of rounds to dance
	 * @return The resulting listing of programs when the dancing is finished
	 */
	private static String part1( final int programs, final String input, final long rounds ) {
		final ProgramDance pd = new ProgramDance( programs );
		return pd.dance( Arrays.asList( input.split( "," ) ), rounds );
	}
}
