package aoc2016.day17;

import aocutil.geometry.Coord2D;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex1_input = "ihgpwlah";
		final String ex2_input = "kglvqrro";
		final String ex3_input = "ulqzkmiv";
		final String input = "vwbaicqe";
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example 1: " + part1( ex1_input ) );
		System.out.println( "Example 2: " + part1( ex2_input ) );
		System.out.println( "Example 3: " + part1( ex3_input ) );
		System.out.println( "Part 1   : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example 1: " + part2( ex1_input ) );
		System.out.println( "Example 2: " + part2( ex2_input ) );
		System.out.println( "Part 2   : " + part2( input ) );
	}
	
	/**
	 * Finds the shortest path to the vault, which is located in the bottom
	 * right of the maze. 
	 * 
	 * @param input The pass key that is used to determine what doors are open   
	 * @return The shortest path to the vault
	 */
	private static String part1( final String input ) {
		final HashMaze maze = new HashMaze( 4, 4, input );
		return maze.findShortestRoute( new Coord2D( 3, 3 ) ).toString( );
	}
	
	/**
	 * Finds the length of the longest possible route to the vault. Here, length
	 * is defined as the number of moves taken to get there.
	 * 
	 * @param input The pass key to use on the maze doors   
	 * @return The length of the longest route
	 */
	private static long part2( final String input ) {
		final HashMaze maze = new HashMaze( 4, 4, input );
		return maze.findLongestRouteLength( new Coord2D( 3, 3 ) );
	}	
}
