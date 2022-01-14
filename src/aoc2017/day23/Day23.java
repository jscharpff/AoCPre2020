package aoc2017.day23;

import java.util.List;

import aocutil.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> input = new FileReader( Day23.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 23 ]---" );
		System.out.println( "Answer : " + part1( input ) );
		System.out.println( "Answer : " + part2(  ) );
	}
	
	/**
	 * Runs the assembly program to determine the number of times the multiply
	 * instruction in ran on the input program
	 * 
	 * @param input The input program 
	 * @return The number of times as "mul" instruction was executed 
	 */
	private static long part1( final List<String> input ) {
		final Coprocessor cp = new Coprocessor( );
		cp.run( input );
		return cp.getMultiplyCount( );
	}

	/**
	 * Through reverse engineering the program was reconstructed and optimised
	 * into the below piece of code 
	 * 
	 * @return The value in the h "register" after running the program 
	 */
	private static long part2(  ) {
		int b = 105700;
		int c = b + 17000;
		int h = 0;
		while( true ) {
		  int f = 1;
		  for( int d = 2; d < b / 2; d++ ) {
		  	if( b % d == 0 ) f = 0;
		  }
		  if( f == 0 ) h++;
		  if( b == c ) break;
		  b += 17;
		}
		return h;
	}
}
