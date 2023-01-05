package aoc2019.day03;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day03 {
	
	/**
	 * Day 3 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day03.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day03.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Finds the intersection of two wires that is closest to the origin
	 *  
	 * @param input The list containing the description of the two wires
	 * @return The Manhattan distance to the closest intersection of both lines  	
	 */
	protected static long part1( final List<String> input ) {
		final Coord2D origin = new Coord2D( 0, 0 );
		final Wire w1 = Wire.fromString( input.get( 0 ) );
		final Wire w2 = Wire.fromString( input.get( 1 ) );
		
		// find all intersections and return closest
		final List<Coord2D> ints = w1.getIntersections( w2 );
		ints.sort( (c1,c2) -> c1.getManhattanDistance( origin ) - c2.getManhattanDistance( origin ) );
		
		// return second entry, first one must be the origin itself
		return ints.get( 1 ).getManhattanDistance( origin );
	}
	
	/**
	 * Finds the intersection of both wires that takes the lowest total number of
	 * steps to reach over both wires
	 * 
	 * @param input The list containing the description of the two wires
	 * @return The total number of steps to travel to the intersection that
	 *   minimises the total distance
	 */
	protected static long part2( final List<String> input ) {
		final Coord2D origin = new Coord2D( 0, 0 );
		final Wire w1 = Wire.fromString( input.get( 0 ) );
		final Wire w2 = Wire.fromString( input.get( 1 ) );
		
		// find all intersections and remove the origin 
		final List<Coord2D> ints = w1.getIntersections( w2 );
		ints.remove( origin );
		
		// then determine combined distance to each point and return lowest
		return ints.stream( ).mapToLong( c -> w1.trace( c ) + w2.trace( c ) ).min( ).getAsLong( );
	}

}
