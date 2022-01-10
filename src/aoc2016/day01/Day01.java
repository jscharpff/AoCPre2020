package aoc2016.day01;

import java.util.HashSet;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String input = new FileReader( Day01.class.getResource( "day01_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( "R5, L5, R5, R3" ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( "R8, R4, R4, R8" ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Compute the destination for Santa to go given a list of movement
	 * instruction 
	 * 
	 * @param input The comma separated list of Lx / Rx movement instructions 
	 * @return The distance of the position Santa ends up in
	 */
	private static long part1( final String input ) {
		final String[] s = input.split( ", ");
		Coord2D c = new Coord2D( 0, 0 );
		int dir = 0;
		for( final String i : s ) {
			dir = (dir + (i.charAt( 0 ) == 'L' ? 270 : 90)) % 360;
			c = c.moveDir( dir, Integer.parseInt( i.substring( 1 ) ) );
		}
		return c.getManhattanDistance( new Coord2D( 0, 0 ) );
	}
	
	/**
	 * Checks for the first coordinate that Santa will visit twice if he follows
	 * the movement instructions
	 * 
	 * @param input The comma separated list of Lx / Rx movement instructions 
	 * @return The distance of the first position Santa visits twice
		 */
	private static long part2( final String input ) {
		final Set<Coord2D> visited = new HashSet<>( );
		final String[] s = input.split( ", ");
		Coord2D c = new Coord2D( 0, 0 );
		int dir = 0;
		for( final String i : s ) {
			dir = (dir + (i.charAt( 0 ) == 'L' ? 270 : 90)) % 360;
			
			// move in single steps to detect coordinates we've already visited
			for( int d = 0; d < Integer.parseInt( i.substring( 1 ) ); d++ ) {
				c = c.moveDir( dir, 1 );
			
				if( visited.contains( c ) ) return c.getManhattanDistance( new Coord2D( 0, 0 ) );
				visited.add( c );
			}
		}

		throw new RuntimeException( "Failed to find duplicate coordinate" );
	}

}
