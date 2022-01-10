package aoc2016.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "day08_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day08.class.getResource( "day08_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 & 2 ]---" );
		System.out.println( "Example: " + reconstructScreen( ex_input, 7, 3 ) + "\n" );
		System.out.println( "Answer : " + reconstructScreen( input, 50, 6 ) );
	}
	
	/**
	 * Reconstructs the output to screen of pixels by processing the commands
	 * in the input
	 * 
	 * @param input The list of commands to produce output
	 * @param width The width of the screen
	 * @param height The height of the screen    
	 * @return The number of active pixels after processing all commands
	 */
	private static long reconstructScreen( final List<String> input, final int width, final int height ) {
		final DoorScreen screen = new DoorScreen( width, height );
		screen.process( input );
		System.out.println( screen );
		return screen.count( );
	}
}
