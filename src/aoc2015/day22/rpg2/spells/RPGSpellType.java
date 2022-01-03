package aoc2015.day22.rpg2.spells;

import aoc2015.day22.rpg2.RPGChar;

/**
 * The set of available spells
 */
public enum RPGSpellType {
	MagicMissile(53, RPGSMagicMissile.class, 0, true),
	Drain(73, RPGSDrain.class, 0, true),
	Shield(113, RPGSShield.class, 6, false),
	Poison(173, RPGSPoison.class, 6, true),
	Recharge(229, RPGSRecharge.class, 5, false);
	
	/** The mana cost of the spell */
	private int manacost;
	
	/** The spell that is cast */
	private Class<? extends RPGSpell> spellclass;
	
	/** The number of ticks the spell lasts */
	protected final int ticks;
	
	/** Offensive or defensive spell */
	protected final boolean offensive;
	
	/**
	 * Creates a new spell with the specified mana cost
	 * 
	 * @param manacost The cost of the spell
	 * @param spellclass The class that implements the spell effects
	 * @param ticks The number of periodic ticks, 0 for immediate spells
	 * @param offensive True if the target is the enemy, false for the caster
	 */
	private RPGSpellType( final int manacost, final Class<? extends RPGSpell> spellclass, final int ticks, final boolean offensive ) {
		this.manacost = manacost;
		this.spellclass = spellclass;
		this.ticks = ticks;
		this.offensive = offensive;
	}
	
	/** @return True if the spell has a periodic tick */
	public boolean isPeriodic( ) { return ticks > 0; }
	
	/** @return The number of ticks the periodic effect lasts */
	public int getPeriodicTimer( ) { return ticks; }
	
	/** @return The spell mana cost */
	public int getManaCost( ) { return manacost; }
	
	/**
	 * Initialises the spell
	 * 
	 * @param caster The character that cast the spell
	 * @return The new instance of the spell
	 */
	public RPGSpell newInstance( final RPGChar caster ) {
		try {
			return (RPGSpell) spellclass.getConstructor( RPGChar.class ).newInstance( caster );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}	
}