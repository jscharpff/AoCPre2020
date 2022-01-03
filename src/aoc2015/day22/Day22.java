package aoc2015.day22;

import java.io.IOException;
import java.net.URL;

import aoc2015.day22.rpg2.RPG2;
import aoc2015.day22.rpg2.RPG2Genie;
import aoc2015.day22.rpg2.ai.CastList;
import aoc2015.day22.rpg2.ai.RPGAICastList;
import aoc2015.day22.rpg2.spells.RPGSpellType;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final URL ex_gamefile = Day22.class.getResource( "day22_example.txt" );
		final URL ex_gamefile2 = Day22.class.getResource( "day22_example2.txt" );
		final URL gamefile = Day22.class.getResource( "day22_input.txt" );
		
		System.out.println( "---[ Day 21 ]---" );
		System.out.println( "---[ Tests ]---" );
		test( ex_gamefile, RPGSpellType.Poison, RPGSpellType.MagicMissile );
		test( ex_gamefile2, RPGSpellType.Recharge, RPGSpellType.Shield, RPGSpellType.Drain, RPGSpellType.Poison, RPGSpellType.MagicMissile );
		System.out.println( "\n---[ Part 1 ]---" );
		System.out.println( "\n>> Answer: " + playMinimalMana( gamefile, false ) );
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "\n>> Answer: " + playMinimalMana( gamefile, true ) );
	}
	
	/**
	 * Runs test games and logs the battle to the console
	 * 
	 * @param gamefile The gamefile containing the test game input
	 * @param spells The spells to use in the test run
	 * @throws IOException if the file reading failed
	 */
	private static void test( final URL gamefile, final RPGSpellType... spells ) throws IOException {
		final RPG2 game = RPG2.fromFile( gamefile );
		
		game.setPlayerAI( new RPGAICastList( game.getPlayer( ), new CastList( spells ) ) );
		game.setLogging( true );
		
		game.battle( );
		System.out.println(  );
	}
	
	/**
	 * Determines the set of spells that defeat the boss while using minimal mana
	 * 
	 * @param gamefile The file containing the game data
	 * @param hardmode True for hard mode in which the player loses one point of
	 *   health every round
	 * @return The minimal mana required to defeat the boss
	 * @throws IOException 
	 */
	private static long playMinimalMana( final URL gamefile, final boolean hardmode ) throws IOException {
		final RPG2Genie genie = new RPG2Genie( gamefile );
		return genie.getMinimumManaToWin( hardmode );
	}
}
