package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Hurls a magic missile at the target
 * 
 * @author Joris
 */
public class RPGSMagicMissile extends RPGSpell {
	
	/**
	 * Creates a new magic missile spell
	 * 
	 * @param caster The character that cast the spell
	 */
	public RPGSMagicMissile( final RPGChar caster ) {
		super( RPGSpellType.MagicMissile, caster );
	}
	
	/**
	 * Directly damages the target
	 * @param target The target of the spell
	 * 
	 */
	@Override
	public boolean castDirect( final RPGChar target ) throws RPGCharacterDied {
		// immediately deals damage to the target
		target.takeHit( 4, true );
		return true;
	}
}
