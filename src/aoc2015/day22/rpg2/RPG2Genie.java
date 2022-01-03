package aoc2015.day22.rpg2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import aoc2015.day22.rpg2.ai.CastList;
import aoc2015.day22.rpg2.ai.RPGAICastList;
import aoc2015.day22.rpg2.spells.RPGSpellType;

/**
 * Game Genie for the RPG game!
 * 
 * @author Joris
 */
public class RPG2Genie {
	/** The fle that contains the game data */
	private URL gamefile;
	
	/**
	 * Creates a new Game Genie
	 * 
	 * @param gamefile The file that contains the game data
	 */
	public RPG2Genie( final URL gamefile ) {
		this.gamefile = gamefile;
	}

	/**
	 * Determines the minimum amount of mana needed to kill the boss and win the
	 * game
	 * 
	 * @param hardmode True if hard mode is to be enabled
	 * @return The minimal amount of mana
	 * @throws IOException if reading the game file failed
	 */
	public int getMinimumManaToWin( final boolean hardmode ) throws IOException {
		final RPG2 game = RPG2.fromFile( gamefile );
		game.setLogging( false );
		game.setHardMode( hardmode );
		
		// go over all spell sets, but with an increasing number of spells
		CastList best = null;
		int minspells = 1;
		for( int maxspells = 5; maxspells <= 50; maxspells += 3 ) {
			final GenerateSettings settings = new GenerateSettings( minspells, maxspells, 500, best == null ? Integer.MAX_VALUE : best.getManaCost( ) );
			System.out.println( "Generating spell sets of sizes " + minspells + " to " + maxspells + " with maximum mana cost " + settings.maxmana );
			final List<CastList> spelllists = generateSpellLists( EnumSet.allOf( RPGSpellType.class ), settings );
			if( spelllists.size( ) == 0 ) {
				System.out.println( "No more spell sets can be found!" );
				break;
			}
			System.out.println( "Done, generated " + spelllists.size( ) + " sets" );
			System.out.println( "Starting simulation of battles" );
			for( final CastList spells : spelllists ) {			
				// only consider spell lists that have lower mana cost than the current best
				if( best != null && spells.getManaCost( ) >= best.getManaCost( ) ) continue;
				
				game.reset( );
				game.setPlayerAI( new RPGAICastList( game.getPlayer( ), spells ) );
			
				// check the winner of the battle, discard if boss wins
				try {
					if( !game.battle( ).equals( game.getPlayer( ) ) ) continue;
					System.err.println( "Found new winning spell set: " + spells );
					best = spells;			
				} catch( RuntimeException e ) {
					// failed to complete the game, discard this spell set
				}
			}
			
			System.out.println( "Simulation completed, trying next set size" );
			minspells = maxspells + 1;
		}
		
		return best.getManaCost( );
	}
	
	/**
	 * Generates spell lists for the given settings
	 * 
	 * @param spells The spells to chose from
	 * @param settings The generation settings
	 * @return The list of all possible combinations of spells that meat the
	 *   generation criteria
	 */
	private List<CastList> generateSpellLists( final EnumSet<RPGSpellType> spells, final GenerateSettings settings ) {
		final List<CastList> castlists = new ArrayList<>( );
		generateSpellLists( castlists, spells, new CastList( ), settings, settings.startmana );
		return castlists;
	}
	
	/**
	 * Actual generation in a DFS way
	 * 
	 * @param castlists The current set of spell lists successfully generated
	 * @param spells The set of available spells to chose from
	 * @param current The current spell list we are building
	 * @param settings The generation settings
	 * @param manaleft The mana left for a successive spell
	 */
	private void generateSpellLists( final List<CastList> castlists, final EnumSet<RPGSpellType> spells, final CastList current, final GenerateSettings settings, final int manaleft ) {		
		// is the spell set still interesting?
		if( current.getManaCost( ) >= settings.maxmana ) return;
		if( current.size( ) > settings.maxspells ) return;
		
		// add current cast list to sets if it contains enough spells
		if( current.size( ) >= settings.minspells ) castlists.add( current.copy( ) );
		
		// add another spell!
		for( final RPGSpellType spell : spells ) {	
			// try to add it
			if( spell.getManaCost( ) > manaleft ) continue;
			if( !current.addSpell( spell ) ) continue;
			
			// spell is added, try the next
			final int newmana = manaleft - spell.getManaCost( ) + (spell == RPGSpellType.Recharge ? (101 * spell.getPeriodicTimer( )) : 0);
			generateSpellLists( castlists, spells, current, settings, newmana );
			
			// remove to try the next
			current.popSpell( );
		}
	}
	
	/**
	 * Container for generation data parameters
	 */
	private class GenerateSettings {
		/** Minimum number of spells in the set */
		private final int minspells;
		
		/** Maximum number of spells in the set */
		private final int maxspells;
		
		/** The starting value for the mana available */
		private final int startmana;
		
		/** The maximum mana that may be spent, i.e. an upper bound */
		private final int maxmana;
		
		/**
		 * Creates a new generation settings container with the specified
		 * parameter limits
		 * 
		 * @param minspells
		 * @param maxspells
		 * @param startmana
		 * @param maxmana
		 */
		private GenerateSettings( final int minspells, final int maxspells, final int startmana, final int maxmana ) {
			this.minspells = minspells;
			this.maxspells = maxspells;
			this.startmana = startmana;
			this.maxmana   = maxmana;
		}
	}
}