package aoc2017.day05;

import java.util.List;

import aocutil.io.FileReader;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day05.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day05.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + escapeJumpMaze( ex_input, false ) );
		System.out.println( "Answer : " + escapeJumpMaze( input, false ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + escapeJumpMaze( ex_input, true ) );
		System.out.println( "Answer : " + escapeJumpMaze( input, true ) );
	}
	
	/**
	 * Tries to escape a maze of infinite loops, constructed by jumps with
	 * relative offsets. Modifies offsets until we escape the maze!
	 * 
	 * @param input The list of jump offsets, one per line
	 * @param even_stranger True if the jumps are behaving even stranger...
	 * @return The number of jumps performed until we escaped the maze
	 */
	private static long escapeJumpMaze( final List<String> input, final boolean even_stranger ) {
		// create array of jump offsets
		final int[] jumps = input.stream( ).mapToInt( Integer::parseInt ).toArray( );
		int IP = 0;
		
		int steps = 0;
		while( 0 <= IP && IP < jumps.length ) {
			// get the offset to jump
			final int offset = jumps[ IP ];
			jumps[ IP ] += (even_stranger && jumps[ IP ] >= 3) ? -1 : 1;
			IP += offset;
			steps ++;
		}
		
		return steps;
	}
}
