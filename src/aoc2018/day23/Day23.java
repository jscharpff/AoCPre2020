package aoc2018.day23;

import java.util.List;

import aocutil.geometry.Coord3D;
import aocutil.io.FileReader;

public class Day23 {
	
	/**
	 * Day 23 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day23.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day23.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Finds the nanobot with the strongest signal radius and counts the numbre
	 * of nanobots in range of this one
	 * 
	 * @param input The list of nanobots
	 * @return The number of nanobots in range of the strongest scanner
	 */
	protected static long part1( final List<String> input ) {
		final EmergencyTeleport device = EmergencyTeleport.fromStringList( input );
		return device.countInRangeOfStrongest( );
	}
	
	/**
	 * Finds the shortest Manhattan distance to a position that is covered by the
	 * scanners of the most nanobots
	 * 
	 * @param input The list of nanobots
	 * @return The Manhattan distance to the closest coordinate with the highest
	 *   nanobot coverage
	 */
	protected static long part2( final List<String> input ) {
		final EmergencyTeleport device = EmergencyTeleport.fromStringList( input );
		return device.getBestCoverageDistance( new Coord3D( 0, 0, 0 ) );
	}	
}
