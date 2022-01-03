package aoc2015.day06;

import java.util.List;

import aocutil.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "day06_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day06.class.getResource( "day06_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + setupGrid( ex_input, false ) );
		System.out.println( "Answer : " + setupGrid( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + setupGrid( ex_input, true ) );
		System.out.println( "Answer : " + setupGrid( input, true ) );
	}
	
	/**
	 * Performs setup of a grid of lights as per Santa's instruction
	 * 
	 * @param input The list of instruction received from Santa
	 * @param bctl True if the light grid has brightness control, false otherwise
	 * @return The number of active lights if it has no bctl, otherwise the sum
	 *   of all brightness levels is returned
	 */
	private static long setupGrid( final List<String> input, final boolean bctl ) {
		final LightGrid grid = new LightGrid( 1000, bctl );
		grid.parseInstructions( input );
		return bctl ? grid.countBrightness( ) : grid.countActive( );
	}
}
