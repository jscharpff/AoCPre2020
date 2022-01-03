package aoc2015.day10;

import aocutil.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int ex_input = Integer.parseInt( new FileReader( Day10.class.getResource( "day10_example.txt" ) ).readAll( ) );
		final int input = Integer.parseInt( new FileReader( Day10.class.getResource( "day10_input.txt" ) ).readAll( ) );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 40 ) );
		System.out.println( "Answer : " + part1( input, 40 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part1( input, 50 ) );
	}
	
	/**
	 * Plays the game of LookAndSay for the specified number of rounds.
	 * 
	 * @param input The starting input for the game
	 * @param rounds The number of rounds to play
	 * @return The length of the string that results after playing the rounds
	 */
	private static int part1( final int input, final int rounds ) {
		final LookAndSay ls = new LookAndSay( );
		return ls.play( rounds, "" + input ).length( );
	}
}
