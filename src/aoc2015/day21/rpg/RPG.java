package aoc2015.day21.rpg;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.io.FileReader;

/**
 * A simple RPG game in which a player has to take on a boss, optionally
 * suppoerted by stats-enhancing items
 * 
 * @author Joris
 */

public class RPG {
	/** The initial game data */
	private final RPGChar[] chardata;
	
	/** The player character */
	private RPGChar player;
	
	/** The boss character */
	private RPGChar boss;
	
	/** The shop */
	protected final RPGShop shop;
	
	/** True to enable logging, false otherwise */
	private static boolean logging;
	
	/**
	 * Creates a new RPG game where the player is to slay the evil boss
	 * 
	 * @param chars The characters in the game
	 * @param shop The shop with helpful items
	 */
	public RPG( final RPGChar[] chars, final RPGShop shop ) {
		logging = false;
		this.chardata = chars;
		this.shop = shop;

		reset( );
	}
	
	/** Resets the game */
	public void reset( ) {
		player = new RPGChar( chardata[0] );
		boss = new RPGChar( chardata[1] );
		shop.reset( );
	}
	
	/** @return The player character */
	public RPGChar getPlayer( ) { return player; }
	
	/**
	 * Let's the specified character buy the item, if it has sufficient gold.
	 * 
	 * @param character The character that wants to buy the item
	 * @param item The item to buy
	 * @return True iff the character was successful in the purchase
	 */
	public boolean buyItem( final RPGChar character, final RPGItem item ) {
		// already sold?
		if( !shop.isAvailable( item ) ) return false;
		
		// try to buy it
		if( !character.buy( item ) ) return false;
		
		// sold!
		shop.sold( item );
		return true;
	}

	/**
	 * Enacts the final boss battle with the current characters and their stats.
	 * 
	 * @return The character that survived the encounter
	 */
	public RPGChar battle( ) {
		RPG.log( "--[ BATTLE STARTING ]--" );
		
		while( true ) {
			// let player hit first, return the player if the boss is now dead
			if( boss.hit( player.getDamage( ) ) == 0 ) return player;			
			
			// then the boss, and return it if it killed the player
			if( player.hit( boss.getDamage( ) ) == 0 ) return boss;
		}
	}
	
	/**
	 * Creates a new RPG from a game file
	 * 
	 * @param gamefile The file that contains the game data
	 * @return The RPG game
	 * @throws IOException if the file read failed
	 */
	public static RPG fromFile( final URL gamefile ) throws IOException {
		final List<String> input = new FileReader( gamefile ).readLines( );

		final Set<RPGChar> chars = new HashSet<>( input.size( ) );
		for( final String s : input ) chars.add( RPGChar.fromString( s ) );
		
		// and get the shop data
		final RPGShop shop = RPGShop.fromFile( RPG.class.getResource( "items.txt" ) );
		return new RPG( chars.toArray( new RPGChar[2] ), shop );
	}
	
	/**
	 * Logs a message (if logging is enabled)
	 * 
	 * @param msg The message to log
	 */
	protected static void log( final String message ) {
		if( logging ) System.out.println( message );
	}
}
