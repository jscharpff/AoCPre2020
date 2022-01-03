package aoc2015.day01;

import aocutil.io.FileReader;
import aocutil.string.StringUtil;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day01.class.getResource( "day01_example.txt" ) ).readAll( );
		final String input = new FileReader( Day01.class.getResource( "day01_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the floor Santa will end up in based upon the floor movement
	 * instructions. The '(' character will move Santa a floor up while ')' moves
	 * him down.
	 * 
	 * @param input The instructions
	 * @return The floor Santa ends up on after following the instructions
	 */
	private static long part1( final String input ) {
		return StringUtil.count( input, '(' ) - StringUtil.count( input, ')' );
	}
	
	
	/**
	 * Determines the number of instruction Santa needs to follow until he
	 * reaches the basement at level -1
	 * 
	 * @param input The instructions
	 * @return The number of moves until the basement is reached
	 */
	private static long part2( final String input ) {
		int floor = 0;
		for( int moves = 0; moves < input.length( ); moves++ ) {
			floor += input.charAt( moves ) == '(' ? 1 : -1;
			if( floor == -1 ) return moves + 1;
		}
		
		throw new RuntimeException( "Santa didn't reach the basement..." );
	}
		
}
