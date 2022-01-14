package aoc2017.day11;

import java.util.List;

import aocutil.geometry.HexCoord;
import aocutil.geometry.HexCoord.HexDir;
import aocutil.io.FileReader;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day11.class.getResource( "example.txt" ) ).readLines( );
		final String input = new FileReader( Day11.class.getResource( "input.txt" ) ).readAll( );
		
		System.out.println( "---[ Day 11 ]---" );
		for( final String ex : ex_input )
			System.out.println( "Example: " + part1( ex ) );
		System.out.println( "Part 1 : " + part1( input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Determines the distance to the position that results after travelling over
	 * a hexagonal coordinate grid 
	 * 
	 * @param input The input of movement instructions 
	 * @return The distance to the resulting end location
	 */
	private static long part1( final String input ) {
		final HexCoord start = new HexCoord( 0, 0 );
		HexCoord c = start;
		for( final String m : input.split( "," ) )
			c = c.move( HexDir.valueOf( m.toUpperCase( ) ) );
		
		return c.dist( start );
	}
	
	/**
	 * Determines the maximum distance away from the starting location that
	 * will be achieved by traversing the hexagonal grid
	 * 
	 * @param input The input of movement instructions 
	 * @return The distance to the location that is the farthest away from the
	 *   starting position, encountered while following the movements
	 */
	private static long part2( final String input ) {
		final HexCoord start = new HexCoord( 0, 0 );
		int maxdist = 0;
		HexCoord c = start;
		for( final String m : input.split( "," ) ) {
			c = c.move( HexDir.valueOf( m.toUpperCase( ) ) );
			final int dist = c.dist( start );
			if( dist > maxdist ) maxdist = dist;
		}
		
		return maxdist;
	}
}
