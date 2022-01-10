package aoc2016.day22;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "day22_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day22.class.getResource( "day22_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, new Coord2D( 2, 0 ) ) );
		System.out.println( "Answer : " + part2( input, new Coord2D( 36, 0 ) ) );
	}
	
	/**
	 * Counts the number of viable pairs in the data storage grid.
	 * 
	 * @param input The nodes in the node grid as listed in the Easter Bunny's
	 *   HQ terminal output
	 * @return The number of viable pairs of nodes
	 */
	private static long part1( final List<String> input ) {
		final NodeGrid grid = NodeGrid.fromStringList( input );
		return grid.countViablePairs( );
	}
	
	/**
	 * Determines the minimum number of data moves we need to perform to get the
	 * top secret data from the given data source node to the node at (0,0), from
	 * which we can read the data.
	 * 
	 * @param input The grid of nodes
	 * @param datasource The grid position of the data we want to read
	 * @return The minimal number of move operations required to move the data to
	 *   the node that we have read access to
	 */	
	private static long part2( final List<String> input, final Coord2D datasource) {
		final NodeGrid grid = NodeGrid.fromStringList( input );
		final long moves = grid.getMinimalDataMoves( datasource );
		return moves;
	}
}
