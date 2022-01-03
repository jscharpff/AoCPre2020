package aoc2015.day17;

import java.util.List;

import aocutil.io.FileReader;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day17.class.getResource( "day17_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day17.class.getResource( "day17_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 25 ) );
		System.out.println( "Answer : " + part1( input, 150 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 25 ) );
		System.out.println( "Answer : " + part2( input, 150 ) );
	}
	
	/**
	 * Determines the unique number of combinations of cups that together exactly
	 * have the required volume of Eggnogg to store
	 * 
	 * @param input The set of cups to our disposal
	 * @param storevolume The volume that the cups need to hold 
	 * @return The number of unique configurations of cups that hold the volume
	 */
	private static long part1( final List<String> input, final int storevolume ) {
		final EggnogDistributor ED = new EggnogDistributor( input );
		return ED.countCombinations( storevolume );
	}
	
	/**
	 * Determines the unique number of combinations of cups that together exactly
	 * have the required volume of Eggnogg to store, but only with the smallest
	 * psossible number of cups
	 * 
	 * @param input The set of cups to our disposal
	 * @param storevolume The volume that the cups need to hold 
	 * @return The number of unique configurations of cups that hold the volume
	 */
	private static long part2( final List<String> input, final int storevolume ) {
		final EggnogDistributor ED = new EggnogDistributor( input );
		return ED.countSmallestCombinations( storevolume );
	}
}
