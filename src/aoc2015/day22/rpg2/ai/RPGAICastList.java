package aoc2015.day22.rpg2.ai;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGChar.RPGCharClass;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * AI for casters that uses a predefined, static list of spells to cast
 * 
 * @author Joris
 */
public class RPGAICastList extends RPGCharAI {
	/** Uses a predefined set of actions */
	private final CastList spells;

	/**
	 * Creates a new AI to cast the spells in their order as captured by the
	 * CastList
	 * 
	 * @param character The character that this AI belongs to
	 * @param spells The list spells to perform
	 */
	public RPGAICastList( final RPGChar character, final CastList spells ) {
		super( character );
		if( character.getCharClass( ) != RPGCharClass.Wizard )
			throw new RuntimeException( "Invalid AI for character class: " + character.getCharClass( ) );
		
		this.spells = spells;
	}

	/**
	 * Casts the next spell in the cast list
	 * 
	 * @param target The target of the spell
	 * @throws RuntimeException if the spell list is exhausted before the end of
	 *   the game or the spell cannot be cast in the current state
	 */
	@Override
	public void nextAttack( RPGChar target ) throws RPGCharacterDied {
		if( !character.cast( spells.next( ), target ) )
			throw new RuntimeException( "Illegal spell cast in current turn" );
	}
}
