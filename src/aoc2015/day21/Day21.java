package aoc2015.day21;

import java.io.IOException;
import java.net.URL;

import aoc2015.day21.rpg.RPGGenie;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {		
		final URL gamefile = Day21.class.getResource( "day21_input.txt" );
		
		System.out.println( "---[ Day 21 ]---" );
		System.out.println( "Part 1 : " + part1( gamefile ) );
		System.out.println( "Part 2 : " + part2( gamefile ) );
	}
	
	/**
	 * Determines the least amount of gold to spend on equipment such that the
	 * player in the game is able to beat the boss
	 * 
	 * @param gamefile The file that contains the game data
	 * @return The least amount of gold to spend that leads to winning the game
	 * @throws IOException 
	 */
	private static long part1( final URL gamefile ) throws IOException {
		final RPGGenie rpg = new RPGGenie( gamefile );
		return rpg.getMinimumGoldToWin( );
	}
	
	/**
	 * Determines the maximum amount of gold to spend on equipment while still
	 * losing the game
	 * 
	 * @param gamefile The file that contains the game data
	 * @return The maximum amount of gold the player can spend on equipment but
	 *   still lose the game
	 * @throws IOException 
	 */
	private static long part2( final URL gamefile ) throws IOException {
		final RPGGenie rpg = new RPGGenie( gamefile );
		return rpg.getMaximumGoldToLose( );
	}
}
