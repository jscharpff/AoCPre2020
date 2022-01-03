package aoc2015.day18;

import java.util.List;

import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "day18_example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day18.class.getResource( "day18_example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day18.class.getResource( "day18_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + animateGrid( ex_input, 4, false ) );
		System.out.println( "Answer : " + animateGrid( input, 100, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + animateGrid( ex2_input, 5, true ) );
		System.out.println( "Answer : " + animateGrid( input, 100, true ) );
	}
	
	/**
	 * Animates the given grid of lights and returns the number of lights that
	 * are turned on once the animation terminates
	 * 
	 * @param input The list of strings that describe the initial grid
	 *   configuration
	 * @param steps The number of steps to animate
	 * @param cornermode True to enable the "always on" mode for the lights in
	 *   the corners of the grid. 
	 * @return The number of lights that are on when the animation ends
	 */
	private static long animateGrid( final List<String> input, final int steps, final boolean cornermode ) {
		final AnimatedGrid grid = AnimatedGrid.fromStringList( input );
		grid.setCornerMode( cornermode );
		grid.simulate( steps );
		return grid.getLightCount( );
	}
}
