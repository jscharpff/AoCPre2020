package aoc2019.day20;

import java.util.List;

import aocutil.io.FileReader;

public class Day20 {
	
	/**
	 * Day 20 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day20.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> ex3_input = new FileReader( Day20.class.getResource( "example3.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day20.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Example: " + part1( ex2_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex3_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Determine the shortest path through the DonutMaze, using portals!
	 *   
	 * @param input The layout of the DonutMaze 
	 * @return The length of the shortest path 
	 */
	protected static long part1( final List<String> input ) {
		final DonutMaze dm = DonutMaze.fromStringList( input );
		return dm.findShortestPath( );
	}
	
	/**
	 * Determines the shortest path through the DonutMaze, now through recursive
	 * space
	 * 
	 * @param input The layout of the maze 
	 * @return The length of the shortest path through recursive space
	 */
	protected static long part2( final List<String> input ) {
		final DonutMaze dm = DonutMaze.fromStringList( input );
		return dm.findShortestPathInRecursiveSpace( );
	}

}
