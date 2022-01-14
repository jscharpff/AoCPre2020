package aoc2017.day22;

import java.util.List;

import aocutil.io.FileReader;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day22.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + getInfections( ex_input, 10000, false ) );
		System.out.println( "Answer : " + getInfections( input, 10000, false ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + getInfections( ex_input, 10000000, true) );
		System.out.println( "Answer : " + getInfections( input, 10000000, true ) );
	}
	
	/**
	 * Runs the virus spreading simulation and count the number of infections
	 * happening
	 * 
	 * @param input The initial grid
	 * @param rounds The number of rounds to simulate
	 * @param boolean evolved True if the virus has evolved!
	 * @return The number of infections that happed during the simulation
	 */
	private static long getInfections( final List<String> input, final int rounds, final boolean evolved ) {
		final VirusGrid grid = new VirusGrid( input );
		return grid.run( rounds, evolved );
	}
}
