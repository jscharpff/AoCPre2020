package aoc2018.day25;

import java.util.List;

import aocutil.geometry.CoordND;
import aocutil.io.FileReader;

public class Day25 {
	
	/**
	 * Day 25 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day25.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day25.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 25 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );
	}

	/**
	 * Counts the number of constellations that can be formed from the points
	 * in the input
	 * 
	 * @param input The list of points
	 * @return The number of non-overlapping constellations
	 */
	protected static long part1( final List<String> input ) {
		final List<CoordND> points = input.stream( ).map( CoordND::fromString ).toList( );
		return Constellation.fromPoints( points ).size( );
	}
}
