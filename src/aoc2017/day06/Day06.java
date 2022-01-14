package aoc2017.day06;

import aocutil.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new FileReader( Day06.class.getResource( "example.txt" ) ).readIntArray( );
		final int[] input = new FileReader( Day06.class.getResource( "input.txt" ) ).readIntArray( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the first step in which the memory redistribution process leads
	 * to a state that was seen before, i.e. it has produced a cycle
	 * 
	 * @param input The initial values of the memory banks
	 * @return The steps until a cycle is encountered
	 */
	private static long part1( final int[] input ) {
		final MemDebugger md = new MemDebugger( input );
		return md.detectCycle( );
	}
	
	/**
	 * Determines the length of the cycle
	 * 
	 * @param input The initial values in the memory banks
	 * @return The cycle length in number of redistribution steps
	 */
	private static long part2( final int[] input ) {
		final MemDebugger md = new MemDebugger( input );
		return md.detectCycleLength( );
	}
}
