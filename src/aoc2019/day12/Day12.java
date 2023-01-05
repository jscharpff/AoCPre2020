package aoc2019.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {
	
	/**
	 * Day 12 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day12.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day12.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 10 ) );
		System.out.println( "Example: " + part1( ex2_input, 100 ) );
		System.out.println( "Part 1 : " + part1( input, 1000 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Simulates the orbiting of moons for the given number of steps
	 *   
	 * @param input The description of the initial positions of the moons
	 * @param steps The number of steps to simulate 
	 * @return The total energy of the system once the simulation terminates
	 */
	protected static long part1( final List<String> input, final int steps ) {
		final OrbitSimulator os = OrbitSimulator.fromStringList( input );
		return os.simulate( steps );
	}
	
	/**
	 * Finds the first step in the simulation at which all the moons are in
	 * exactly the same state at they begun
	 * 
	 * @param input The description of the moon's 
	 * @return The number of steps until a repetition occurs in the simulation
	 */
	protected static long part2( final List<String> input ) {
		final OrbitSimulator os = OrbitSimulator.fromStringList( input );
		return os.findRepetition( );
	}

}
