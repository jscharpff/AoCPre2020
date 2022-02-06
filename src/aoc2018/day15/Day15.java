package aoc2018.day15;

import java.util.List;

import aoc2018.day15.rts.CaveBattle;
import aocutil.io.FileReader;

public class Day15 {
	
	/**
	 * Day 15 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day15.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example 1: " + part1( ex_input ) );
		System.out.println( "Example 2: " + part1( ex2_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example 1: " + part2( ex_input ) );
		System.out.println( "Example 2: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Simulates an epic battle between elves and goblins
	 * 
	 * @param input The initial state of the battle
	 * @return The outcome of the battle after one side has been vanquised
	 */
	protected static long part1( final List<String> input ) {
		final CaveBattle map = CaveBattle.fromStringList( input );
		return map.battle( );
	}

	/**
	 * Try and help the elves a little bit by increasing their attack power until
	 * they win the battle with no losses
	 * 
	 * @param input The initial state of the battle
	 * @return The outcome of the battle with minimal ap increase that makes the
	 *   elves win without losses
	 */
	protected static long part2( final List<String> input ) {
		return CaveBattle.winningBattle( input );
	}
}
