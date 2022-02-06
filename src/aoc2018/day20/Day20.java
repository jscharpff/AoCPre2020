package aoc2018.day20;

import aocutil.io.FileReader;

public class Day20 {
	
	/**
	 * Day 20 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day20.class.getResource( "example.txt" ) ).readAll( );
		final String input = new FileReader( Day20.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Finds the shortest path to a room on the construction site that crosses
	 * the most doors
	 * 
	 * @param input The regex describing the construction site paths
	 * @return The number of doors crossed
	 */
	protected static long part1( final String input ) {
		final ConstructionSite site = new ConstructionSite(	input );
		return site.findPathWithMostDoors( );
	}
	
	/**
	 * Finds the number of rooms that need at least 1000 doors crossed to get
	 * there
	 * 
	 * @param input The regex fescribing the construction site
	 * @return The number of rooms at least 1000 doors away
	 */
	protected static long part2( final String input ) {
		final ConstructionSite site = new ConstructionSite(	input );
		return site.countRoomsWithNumberOfDoors( 1000 );
	}	
}
