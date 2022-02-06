package aoc2018.day03;

import java.util.List;

import aocutil.io.FileReader;

public class Day03 {
	/**
	 * Day 3 of AoC2018
	 * 
	 * https://adventofcode.com/2018/day/3
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
	 * Part 1: count the number of squares that are overlapped by multiple claims
	 * 
	 * @param input The claims in a string list
	 * @return The number of overlapped squares
	 */
	protected static int part1( final List<String> input ) {
		final Canvas canvas = new Canvas( 1024, 1024 ); 
		
		for( String i : input )
			canvas.addClaim( Claim.fromString( i ) );
		
		return canvas.countMultipleClaims( );
	}

	/**
	 * Part 2: find the ID of the claim that does not overlap any other
	 * 
	 * @param input The claims in a string list
	 * @return The number of overlapped squares
	 */
	protected static int part2( final List<String> input ) {
		final Canvas canvas = new Canvas( 1024, 1024 ); 
		
		for( String i : input )
			canvas.addClaim( Claim.fromString( i ) );
		
		// go over claims again and return the ID of the one that has no double claims
		for( Claim c : canvas.getClaims( ) ) {
			if( canvas.isValid( c ) ) return c.getID( );
		}
		
		throw new RuntimeException( "Failed to find non-overlapping claim" );
	}
}
