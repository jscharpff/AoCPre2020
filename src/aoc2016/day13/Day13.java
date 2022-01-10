package aoc2016.day13;

import aocutil.geometry.Coord2D;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		System.out.println( "---[ Day 13 ]---" );
		System.out.println( "Example: " + part1( 10, new Coord2D( 7, 4 ) ) );
		System.out.println( "Part 1 : " + part1( 1350, new Coord2D( 31, 39 ) ) );
		System.out.println( "Part 2 : " + part2( 1350 ) );
	}
	
	/**
	 * Finds the minimal number of steps required to find the goal position in
	 * the second building of the Easter Bunny's HQ
	 * 
	 * @param input The maze seed
	 * @param goal The goal position to reach   
	 * @return The minimal number of steps to reach the goal 
	 */
	private static long part1( final int input, final Coord2D goal ) {
		final CubicalMaze maze = new CubicalMaze( input );
		return maze.findDistanceTo( new Coord2D( 1, 1 ), goal );
	}
	
	/**
	 * Determines the number of unique locations that can be visited from the
	 * starting location within 50 steps
	 * 
	 * @param input The maze seed   
	 * @return The number of unique (non-wall) locations visited
	 */
	private static long part2( final int input ) {
		final CubicalMaze maze = new CubicalMaze( input );
		return maze.findUniqueLocations( new Coord2D( 1, 1 ), 50 );
	}
}
