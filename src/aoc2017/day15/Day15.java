package aoc2017.day15;

import java.util.List;

import aocutil.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Compares 40 million samples and returns the number of times both 
	 * generators produce a number that agrees in the lower 16 bits
	 * 
	 * @param input The generator descriptions 
	 * @return The number of times the lower 16 bits agree
	 */
	private static long part1( final List<String> input ) {
		final Judge j = Judge.fromStringList( input );
		return j.compare( 40000000 );
	}
	
	/**
	 * The same as part 1 but now only compare a sample of each generator when it
	 * is a multitude of certain numbers
	 * 
	 * @param input The generator description 
	 * @return The count of compared samples that agree in the lower 16 bits  
	 */
	private static long part2( final List<String> input ) {
		final Judge j = Judge.fromStringList( input );
		return j.compareAsync( 5000000, 4, 8 );
	}
}
