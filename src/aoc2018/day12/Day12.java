package aoc2018.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {
	
	/**
	 * Day 12 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day12.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 12 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Runs a simulation of plant spreading rules to determine which pots have
	 * plants after 20 generations
	 * 
	 * @param input The initial state and rules for the simulation
	 * @return The sum of pot numbers that contain a plant after 20 generations
	 */
	protected static long part1( final List<String> input ) {
		final BotanicalPots b = BotanicalPots.fromStringList( input );
		b.run( 20 );
		return b.sumPots( );
	}

	/**
	 * Similar to part 1 but now "simulating" 50 billion generations by checking
	 * for convergence and then extending the results
	 * 
	 * @param input The initial state and rules for the simulation
	 * @return The sum of pot numbers that contain a plant after 50b generations
	 */
	protected static long part2( final List<String> input ) {
		final BotanicalPots b = BotanicalPots.fromStringList( input );
		return b.runLongterm( 50000000000l, 100 );
	}
}
