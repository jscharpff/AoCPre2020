package aoc2015.day22.rpg2.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;
import aoc2015.day22.rpg2.spells.RPGSpellType;

/**
 * AI for casters that uses the same set of spells in cycles and keeps track
 * of the spells cast during the game. Useful as an strategy for infinite-turn
 * games or as a heuristic for minimal mana use.
 * 
 * @author Joris
 */
public class RPGAICastCycle extends RPGCharAI {
	/** The cycle of spells to cast */
	private List<RPGSpellType> spells;
	
	/** The number of spells cast during the game */
	private int spellscast;
	
	/** The current spell index */
	private int nextidx;

	/**
	 * Creates a new cycle of spells that the caster will indefinitely go through
	 * whenever an attack is performed
	 * 
	 * @param character The character that uses this AI 
	 * @param spells The list of spells to cycle over
	 */
	public RPGAICastCycle( final RPGChar character, final RPGSpellType... spells ) {
		super( character );

		this.spells = new ArrayList<>( Arrays.asList( spells ) );
		spellscast = 0;
		nextidx = 0;
	}
	
	/**
	 * Returns the next spell in the list, cycles back to beginning of the list
	 * once all spells have been cast once.
	 * 
	 * @param target The target of the attack
	 * @return The next attack to perform
	 */	@Override
	public void nextAttack( RPGChar target ) throws RPGCharacterDied {
		final RPGSpellType spell = spells.get( nextidx );
		spellscast++;
		nextidx = (nextidx + 1) % spells.size( );

		if( !character.cast( spell, target ) )
			throw new RuntimeException( "Invalid spell to cast in current state: " + spell + " (spells cast " + spellscast + ")" );
		
	}

	/**
	 * Converts the cast cycle into a CastList
	 * 
	 * @return The CastList that includes all spells that were fired by the AI
	 */
	public CastList resultToCastList( ) {
		final CastList cl = new CastList( );
		for( int i = 0; i < spellscast; i++ ) cl.addSpell( spells.get( i % spells.size( ) ) );
		return cl;
	}	
}
