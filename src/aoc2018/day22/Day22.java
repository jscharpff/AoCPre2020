package aoc2018.day22;

import java.util.List;

import aocutil.io.FileReader;

public class Day22 {
	
	/**
	 * Day 22 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day22.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day22.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example (45)  : " + part2( ex_input ) );
		System.out.println( "Example (1087): " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Determines the total risk level in the region of the cave spanning from
	 * entrance to target
	 * 
	 * @param input The strings that describe the maze
	 * @return The risk level
	 */
	protected static long part1( final List<String> input ) {
		final CaveMaze maze = CaveMaze.fromStringList( input );
		return maze.getRiskLevel( ); 
	}
	
	/**
	 * Finds the fastest way from entrance to target through the maze
	 * 
	 * @param input The strings that describe the maze
	 * @return The minimal time required to get to the target through the maze
	 */
	protected static long part2( final List<String> input ) {
		final CaveMaze maze = CaveMaze.fromStringList( input );
		return maze.getRescueTime( );
	}
}
