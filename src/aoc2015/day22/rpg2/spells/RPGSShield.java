package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Spell that temporarily increases the armour of the player
 * 
 * @author Joris
 */
public class RPGSShield extends RPGSpell {
	/** The armour bonus to apply */
	private final int armourbonus = 7;
	
	/**
	 * Creates a new shield spell
	 * 
	 * @param caster The character that cast the spell
	 */
	public RPGSShield( final RPGChar caster ) {
		super( RPGSpellType.Shield, caster );
	}
	
	/**
	 * Apply the armour bonus directly
	 * 
	 * @param target The target of the spell, should be the caster
	 */
	@Override
	public boolean castDirect( final RPGChar target ) throws RPGCharacterDied {
		// immediately deals damage to the target but also heal the player
		target.addArmourBonus( armourbonus );
		return true;
	}
	
	/**
	 * Removes the armour bonus
	 * 
	 * @param target The target of the spell, should be the caster
	 */
	@Override
	protected void endTick( RPGChar target ) {
		target.addArmourBonus( -armourbonus );
	}
}
