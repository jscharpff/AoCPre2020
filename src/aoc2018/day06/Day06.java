package aoc2018.day06;

import java.util.List;

import aocutil.io.FileReader;

public class Day06 {
	
	/**
	 * Day 6 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day06.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 32 ) );
		System.out.println( "Part 2 : " + part2( input, 10000 ) );
	}

	protected static long part1( final List<String> input ) {
		final NodeGrid nodes = new NodeGrid( input );
		return nodes.getLargestFiniteArea( );
	}
	

	protected static long part2( final List<String> input, final int n ) {
		final NodeGrid nodes = new NodeGrid( input );
		return nodes.getSafeSpots( n );
	}
}
