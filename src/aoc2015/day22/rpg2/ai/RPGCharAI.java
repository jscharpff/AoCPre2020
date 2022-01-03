package aoc2015.day22.rpg2.ai;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Abstract class that captures Attack AI implementations
 * 
 * @author Joris
 *
 */
public abstract class RPGCharAI {
	/** The owner of this AI */
	protected RPGChar character;
	
	/**
	 * Creates a new AI for the character
	 * 
	 * @param character The owning character for the AI
	 */
	public RPGCharAI( final RPGChar character ) {
		this.character = character;
	}
	
	/**
	 * Determines the next attack of the character
	 * 
	 * @param target The target of the attack
	 * @throws RPGCharacterDied if the character died as an effect of the attack
	 */	
	public abstract void nextAttack( final RPGChar target ) throws RPGCharacterDied;

}
