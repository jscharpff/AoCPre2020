package aoc2019.day06;

import java.util.List;

import aocutil.io.FileReader;

public class Day06 {
	
	/**
	 * Day 6 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day06.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Performs a checksum that counts the total number of direct and indirect
	 * orbiting relations
	 *  
	 * @param input The list of orbital relations
	 * @return The orbit map cheksum
	 */
	protected static long part1( final List<String> input ) {
		final OrbitMap om = OrbitMap.fromStringList( input );
		return om.checksum( );
	}
	
	/**
	 * Determines the number of orbital transfers required to get from the orbit
	 * I am at to the orbit of Santa
	 * 
	 * @param input The list of orbital relations 
	 * @return The number of orbital transfers required
	 */
	protected static long part2( final List<String> input ) {
		// return transfers but exclude the start and goal orbits themselves
		// (hence the -2)
		final OrbitMap om = OrbitMap.fromStringList( input );
		return om.countTransfers( "YOU", "SAN" ) - 2 ;
	}

}
