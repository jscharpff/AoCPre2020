package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;

/**
 * Spell that periodically recharges the casters mana
 * 
 * @author Joris
 */
public class RPGSRecharge extends RPGSpell {
	
	/**
	 * Creates a new recharge spell
	 * 
	 * @param caster The character that cast the spell
	 */
	public RPGSRecharge( final RPGChar caster ) {
		super( RPGSpellType.Recharge, caster );
	}

	/**
	 * Refreshes a bit of mana of the target
	 * 
	 * @param target The target of the spell, should be the caster
	 */
	@Override
	protected void applyTick( final RPGChar target ) throws RPGCharacterDied {
		target.addMana( 101 );
	}
}
