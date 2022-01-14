package aoc2017.day09;

import java.util.List;

import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day09.class.getResource( "example2.txt" ) ).readLines( );
		final String input = new FileReader( Day09.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + part1( s ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex2_input )
			System.out.println( "Example: " + part2( s ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Computes the garbage collector score of the data string
	 * 
	 * @param input The input data string 
	 * @return The score according to the GC
	 */
	private static long part1( final String input ) {
		final GarbageCollector gc = new GarbageCollector( input );
		return gc.getScore( );
	}
	
	/**
	 * Computes the number of characters removed due to being garbage data that
	 * is not explicitly marked as such
	 * 
	 * @param input The input data string
	 * @return The number of characters of garbage data not marked as such, i.e.
	 *   not prefixed by a ! character
	 */
	private static long part2( final String input ) {
		final GarbageCollector gc = new GarbageCollector( input );
		return gc.getGarbageCollected( );
	}
}
