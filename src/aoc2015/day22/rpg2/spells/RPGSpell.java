package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPG2;
import aoc2015.day22.rpg2.RPGChar;
import aoc2015.day22.rpg2.RPGCharacterDied;
import aocutil.object.LabeledObject;

public abstract class RPGSpell extends LabeledObject {
	/** The caster of the spell */
	protected final RPGChar caster;
	
	/** The remaining ticks */
	private int ticks;
	
	/**
	 * Creates a new spell with the given name and mana consumption
	 * 
	 * @param spell The spell type
	 */
	public RPGSpell( final RPGSpellType spell, final RPGChar caster ) {
		super( spell.toString( ) );
		
		this.caster = caster;
		this.ticks = spell.ticks;
	}
	
	/**
	 * Applies the spell
	 * 
	 * @param source The character that casts the spell
	 * @param target The target of the spell
	 * @return True if the cast was successful
	 * @throws RPGCharacterDied 
	 */
	public static boolean cast( final RPGSpellType spell, final RPGChar source, final RPGChar target ) throws RPGCharacterDied {
		// create the spell and determine the actual target
		final RPGSpell spellcast = spell.newInstance( source );
		final RPGChar dest = (spell.offensive ? target : source);

		// try and cast the spell
		final boolean castsucces = spellcast.castDirect( dest );
		
		// if successful, afflict the target with the periodic effect
		if( castsucces && spell.isPeriodic( ) ) {
			// apply periodic effect is necessary
			if( !dest.addSpellEffect( spellcast ) ) return false;
		}		

		return castsucces;
	}
	
	/**
	 * The spell implementation of cast
	 * 
	 * @param target The target of the spell
	 * @throws RPGCharacterDied if the target died as an effect of the spell
	 */
	protected boolean castDirect( final RPGChar target ) throws RPGCharacterDied {
		// can be overridden in sub classes to implement a direct effect
		return true;
	}
	
	/**
	 * Handles a single tick of the spell
	 * 
	 * @param target The target of the spell
	 * @return True while there is another tick left
	 * @throws RPGCharacterDied 
	 */
	public boolean tick( final RPGChar target ) throws RPGCharacterDied {
		ticks--;
		RPG2.log( target, "is affected by " + label + " (" + ticks + " ticks remain)" );
		applyTick( target );
		if( ticks == 0 ) {
			RPG2.log( target, label + " weaars off" );
			endTick( target );
		}
		return ticks > 0;
	}
	
	/**
	 * The spell implementation of the tick
	 * 
	 * @param target The target that is afflicted by the spell
	 * @throws RPGCharacterDied 
	 */
	protected void applyTick( final RPGChar target ) throws RPGCharacterDied {
		// do nothing by default, may be overridden to apply spell effects each tick
	}
	
	/**
	 * Called when the spell has expired
	 * 
	 * @param target The target of the spell
	 */
	protected void endTick( final RPGChar target ) {
		// do nothing by default, may be overridden to remove spell effects
	}
}
