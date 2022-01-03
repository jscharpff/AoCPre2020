package aoc2015.day12;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day12.class.getResource( "day12_example.txt" ) ).readAll( );
		final String input = new FileReader( Day12.class.getResource( "day12_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums all the numbers in the input and return their sum
	 * 
	 * @param input The input document 
	 * @return The sum of all numbers present in the input
	 */
	private static long part1( final String input ) {
		return Accounting.sumNumbers( input );
	}
	
	/**
	 * Sums all the numbers in the numbers but this time only consider numbers
	 * that are not contained within JSON objects that have a property value
	 * "red" for any of its properties.
	 * 
	 * @param input The input JSON document 
	 * @return The sum of all numbers that do not belong to an object with the
	 *   property value "red" somewhere
	 */
	private static long part2( final String input ) {
		return Accounting.sumNumbersFiltered( input, "red" );
	}
}
