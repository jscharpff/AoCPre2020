package aoc2016.day18;

import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day18.class.getResource( "day18_example.txt" ) ).readAll( );
		final String input = new FileReader( Day18.class.getResource( "day18_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Day 18 ]---" );
		System.out.println( "Example: " + countSafeTiles( ex_input, 10 ) );
		System.out.println( "Part 1 : " + countSafeTiles( input, 40 ) );
		System.out.println( "Part 2 : " + countSafeTiles( input, 400000 ) );
	}
	
	/**
	 * Counts the number of safe tiles in a maze that is generated with the given
	 * first row of tiles and the number of rows it spans
	 * 
	 * @param input The first row of the maze
	 * @param rows The number of rows the maze has   
	 * @return The number of safe tiles, summed over all rows of the maze
	 */
	private static long countSafeTiles( final String input, final int rows ) {
		return TrapMaze.countSafe( input, rows );
	}
}
