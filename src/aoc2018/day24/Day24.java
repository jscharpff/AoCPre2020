package aoc2018.day24;

import java.util.List;

import aoc2018.day24.iss.ImmuneSystemSimulator;
import aocutil.io.FileReader;

public class Day24 {
	
	/**
	 * Day 24 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "example.txt" ) ).readLineGroups( "\n" );
		final List<String> input = new FileReader( Day24.class.getResource( "input.txt" ) ).readLineGroups( "\n" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) + "\n" );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Simulates the epic battle between the immune system of Santa's reindeer
	 * and infectious attackers
	 * 
	 * @param input The input that describes the simulation
	 * @return The number of units remaining on the winning team after the battle
	 */
	protected static long part1( final List<String> input ) {
		final ImmuneSystemSimulator iss = ImmuneSystemSimulator.fromStringList( input );
		return iss.battle( );
	}
	
	/**
	 * Helps Santa's reindeer by finding the minimal boost in attack power such
	 * that the immune system wins.
	 * 
	 * @param input The input that describes the simulation
	 * @return The remaining units of the immune systems after winning the battle
	 *   with minimal attack power boost, i.e., the first value for which the
	 *   immune system is able to defeat the attackers
	 */
	protected static long part2( final List<String> input ) {
		return ImmuneSystemSimulator.findAPBoostToWin( input );
	}
}
