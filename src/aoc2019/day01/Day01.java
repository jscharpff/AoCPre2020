package aoc2019.day01;

import java.util.List;

import aocutil.io.FileReader;

public class Day01 {
	
	/**
	 * Day 1 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day01.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day01.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Determines the fuel cost for all of the modules in the space ship
	 *  
	 * @param input The list of module weights
	 * @return The total fuel required  	
	 */
	protected static long part1( final List<String> input ) {
		return input.stream( ).mapToLong( Long::parseLong ).map( m -> m / 3 - 2 ).sum( );
	}
	
	/**
	 * Determines the fuel cost for all of the modules in the space ship, now
	 * also accounting for the additional weight of the fuel itself
	 *  
	 * @param input The list of module weights
	 * @return The total fuel required  	
	 */
	protected static long part2( final List<String> input ) {
		return input.stream( ).mapToLong( Long::parseLong ).map( Day01::fuelcost ).sum( );
	}

	/**
	 * Computes the fuel cost of the given mass
	 * 
	 * @param mass The weight of the fuel module
	 * @return Its fuel cost
	 */
	private static long fuelcost( final long mass ) {
		long fuel = (mass / 3) - 2;
		if( fuel <= 0 ) return 0;
		else return fuelcost( fuel ) + fuel;
	}
}
