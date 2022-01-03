package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Poison that periodically damages the target
 * 
 * @author Joris
 */
public class RPGSPoison extends RPGSpell {
	
	/**
	 * Creates a new poison spell
	 * 
	 * @param caster The character that cast the spell
	 */
	public RPGSPoison( final RPGChar caster ) {
		super( RPGSpellType.Poison, caster );
	}

	/**
	 * Damages the target every tick
	 * 
	 * @param target The target that suffers damage
	 */	
	@Override
	protected void applyTick( final RPGChar target ) throws RPGCharacterDied {
		target.takeHit( 3, true );
	}
}
