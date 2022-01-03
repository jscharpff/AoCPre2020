package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Drains the target of 2 HP and heals the caster for the drained amount
 * 
 * @author Joris
 */
public class RPGSDrain extends RPGSpell {
	
	/**
	 * Creates a new drain spell
	 * 
	 * @param caster The character that cast the spell
	 */
	public RPGSDrain( final RPGChar caster ) {
		super( RPGSpellType.Drain, caster );
	}
	
	/**
	 * Directly drains the target of 2 HP and heals the caster
	 * 
	 * @param target The target of the spell
	 */
	@Override
	public boolean castDirect( final RPGChar target ) throws RPGCharacterDied {
		target.takeHit( 2, true );
		caster.heal( 2 );
		return true;
	}

}
