package aoc2015.day22.rpg2.ai;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;
import aoc2015.day22.rpg2.spells.RPGSpellType;

/**
 * Basic AI that simply performs a melee attack (Fighter) or fires a magic
 * missile (Wizard)
 * 
 * @author Joris
 */
public class RPGAIBasic extends RPGCharAI {

	/**
	 * Initialises the AI for the given character
	 * 
	 * @param character
	 */
	public RPGAIBasic( RPGChar character ) {
		super( character );
	}

	/**
	 * Always returns the same attack to perform, depending on the class of the
	 * character
	 * 
	 * @param target The target of the attack
	 * @return The next attack to perform
	 */
	@Override
	public void nextAttack( RPGChar target ) throws RPGCharacterDied {
		switch( character.getCharClass( ) ) {
			case Fighter:
				character.melee( target );
				return;

			case Wizard:
				if( !character.cast( RPGSpellType.MagicMissile, target ) )
					throw new RuntimeException( "Illegal spell cast in current turn" );
				return;
				
			default:
				throw new RuntimeException( "Unknown character class: " + character.getCharClass( ) );
		}
	}
}
