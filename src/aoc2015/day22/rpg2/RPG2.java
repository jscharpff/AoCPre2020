package aoc2015.day22.rpg2;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2015.day22.rpg2.ai.RPGCharAI;
import aocutil.io.FileReader;

/**
 * A slightly less simple RPG game in which a player has to take on a boss, but
 * now as a Wizard casting spell.
 * 
 * @author Joris
 */
public class RPG2 {
	/** The initial game data */
	private final RPGChar[] chardata;
	
	/** The player character */
	private RPGChar player;
	
	/** The boss character */
	private RPGChar boss;
	
	/** Easy of hard mode */
	private boolean hardmode;
	
	/** True to enable logging, false otherwise */
	private static boolean logging;
	
	/**
	 * Creates a new RPG game where the player is to slay the evil boss
	 * 
	 * @param chars The characters in the game
	 */
	public RPG2( final RPGChar[] chars ) {
		logging = false;
		this.chardata = chars;
		this.hardmode = false;

		reset( );
	}
	
	/** Enables/disables logging
	 * @param log True to enable, false to disable */
	public void setLogging( final boolean log ) {
		logging = log;
	}
	
	/** Resets the game */
	public void reset( ) {
		player = new RPGChar( chardata[0] );
		boss = new RPGChar( chardata[1] );
	}
	
	/** @return The player character */
	public RPGChar getPlayer( ) { return player; }
	
	/**
	 * Sets the AI to guide the player
	 * 
	 * @param ai The CharAI to use
	 */
	public void setPlayerAI( final RPGCharAI ai ) {
		player.setAI( ai );
	}
	
	/**
	 * Enables/disables the hard mode for the game
	 * 
	 * @param hard True to activate hard mode
	 */
	public void setHardMode( final boolean hard ) {
		this.hardmode = hard;
	}
	
	/** @return True if we are running in hard mode */
	public boolean isHardMode( ) { return hardmode; }

	/**
	 * Enacts the final boss battle with the current characters and their stats.
	 * 
	 * @return The character that survived the encounter
	 */
	public RPGChar battle( ) {
		RPG2.log( null, "--[ BATTLE STARTING ]--" );
		
		try {
			while( true ) {
				// first apply magic effects, then perform an attack
				log( player, "starts his turn" );
				if( hardmode ) player.takeHit( 1, true );
				player.applyEffects( );
				boss.applyEffects( );
				player.attack( boss );

				log( null, "" );

				// then the boss, again first apply magic effects and then perform a swing!
				log( boss, "starts his turn" );
				player.applyEffects( );
				boss.applyEffects( );
				boss.attack( player );
				
				log( null, "" );
			}
		} catch( RPGCharacterDied death ) {
			RPG2.log( death.character, "has died" );
			return death.character.equals( player ) ? boss : player;
		}
	}
	
	/**
	 * Creates a new RPG from a game file
	 * 
	 * @param gamefile The file that contains the game data
	 * @return The RPG game
	 * @throws IOException if the file read failed
	 */
	public static RPG2 fromFile( final URL gamefile ) throws IOException {
		final List<String> input = new FileReader( gamefile ).readLines( );

		final Set<RPGChar> chars = new HashSet<>( input.size( ) );
		for( final String s : input ) chars.add( RPGChar.fromString( s ) );

		return new RPG2( chars.toArray( new RPGChar[2] ) );
	}
	
	/**
	 * Logs a message for the character (if logging is enabled)
	 * 
	 * @param character The character to which a log applies
	 * @param message The message to log
	 */
	public static void log( final RPGChar character, final String message ) {
		if( !logging ) return;
		
		System.out.println( (character != null ? "(" + character + ") " : "") + message );
	}
}
