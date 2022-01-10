package aoc2015.day22.rpg2;

import java.util.HashSet;
import java.util.Set;

import aoc2015.day22.rpg2.ai.RPGAIBasic;
import aoc2015.day22.rpg2.ai.RPGCharAI;
import aoc2015.day22.rpg2.spells.RPGSpell;
import aoc2015.day22.rpg2.spells.RPGSpellType;
import aocutil.object.LabeledObject;

/**
 * A character in the RPG game that can deal damage and get hit
 * 
 * @author Joris
 */
public class RPGChar extends LabeledObject {
	/** The class of the character */
	private final RPGCharClass charclass;
	
	/** The hit points */
	private int hp;
	
	/** The current weapon damage */
	private int dmg;
	
	/** The current armour points */
	private int armour;
	
	/** The current armour bonus */
	private int armourbonus;
	
	/** The gold available to the character */
	private int mana;

	/** The list of spell effects currently active on the character */
	private Set<RPGSpell> buffs;
	
	/** The AI guiding the actions of the character */
	private RPGCharAI ai;
	
	/**
	 * Creates a new RPG character
	 * 
	 * @param name The character's name
	 * @param charclass The class of the character
	 * @param hp The initial hit points
	 * @param weapdmg The base weapon damage, without items
	 * @param armour The base armour rating, without items
	 * @param mana The mana the character starts with
	 */
	public RPGChar( final String name, final RPGCharClass charclass, final int hp, final int weapdmg, final int armour, final int mana ) {
		super( name );
		
		this.charclass = charclass;
		setAI(new RPGAIBasic( this ) );
		this.hp = hp;
		this.dmg = weapdmg;
		this.armour = armour;
		this.armourbonus = 0;
		this.mana = mana;
		this.buffs = new HashSet<>( );
	}
	
	/**
	 * Creates a new RPG Character by copying the specified character. Items are
	 * not copied
	 * 
	 * @param chardata The character data to copy
	 */
	public RPGChar( final RPGChar chardata ) {
		this( chardata.label, chardata.charclass, chardata.hp, chardata.dmg, chardata.armour, chardata.mana );
	}
	
	/**
	 * Sets the AI that controlls the character
	 * 
	 * @param ai The AI that controls the actions of the character
	 */
	public void setAI( final RPGCharAI ai ) {
		this.ai = ai;
	}
	
	/** @return The character class */
	public RPGCharClass getCharClass( ) {
		return charclass;
	}

	/** @return The current hit points */
	public int getHP( ) { return hp; }
	
	/** @return The current weapon damage, incorporating all spells */
	public int getDamage( ) { 
		return dmg;
	}
	
	/** @return The current armour value, incorporating spells */
	public int getArmour( ) {
		return armour + armourbonus;
	}
	
	/**
	 * Adds the armour bonus
	 * 
	 * @param bonus The bonus value to add
	 */
	public void addArmourBonus( final int bonus ) {
		this.armourbonus += bonus;
	}
	
	/**
	 * Gives mana to the character
	 * 
	 * @param manaadd The mana to add
	 */
	public void addMana( final int manaadd ) {
		this.mana += manaadd;
		RPG2.log( this, "receives " + manaadd + " mana and now has " +  mana + " mana" );
	}
	
	/**
	 * Casts the specified spell
	 * 
	 * @param spell The spell to cast
	 * @param target The target of the spell
	 * @return True if the cast succeeded, false otherwise
	 * @throws RPGCharacterDied 
	 */
	public boolean cast( final RPGSpellType spell, final RPGChar target ) throws RPGCharacterDied {
		if( spell.getManaCost( ) > mana ) {
			return false;
		}
		mana -= spell.getManaCost( );
		
		RPG2.log( this, "casts " + spell + " for " + spell.getManaCost( ) + " mana, remaining mana " + mana );

		return RPGSpell.cast( spell, this, target );
	}
	
	/**
	 * Adds a new active spell effect
	 * 
	 * @param spell The spell to apply
	 * @return True if the effect was added
	 */
	public boolean addSpellEffect( final RPGSpell spell ) {
		if( buffs.contains( spell ) ) return false;
		
		RPG2.log( this, "is afflicted by " + spell );
		buffs.add( spell );
		return true;
	}
	
	/**
	 * Removes an active spell effect
	 * 
	 * @param spell The spell effect to remove
	 */
	public void removeSpellEffect( final RPGSpell spell ) {
		buffs.remove( spell );
	}
	
	/**
	 * Applies active spell effects to the player
	 * @throws RPGCharacterDied 
	 */
	public void applyEffects( ) throws RPGCharacterDied {
		// apply current buffs and keep those of which the timer has not expired
		final Set<RPGSpell> newbuffs = new HashSet<>( buffs.size( ) );
		for( final RPGSpell s : buffs ) {
			if( s.tick( this ) ) newbuffs.add( s );
		}
		buffs = newbuffs;
		
		// check if this caused the character to die
		checkDeath( );
	}
	
	/**
	 * Performs an attack! Uses the AI to determine which attack to perform
	 * 
	 * @param target The target of the attack
	 * @throws RPGCharacterDied if the target has died as a result of the attack
	 */
	public void attack( final RPGChar target ) throws RPGCharacterDied {
		ai.nextAttack( target );
	}
	
	/**
	 * Performs a melee attack
	 * 
	 * @param target The target of the attack
	 * @throws RPGCharacterDied if the target died as a result of the melee
	 *   attack 
	 */
	public void melee( final RPGChar target ) throws RPGCharacterDied {
		if( charclass != RPGCharClass.Fighter ) throw new RuntimeException( "Invalid action for character class " + charclass );
		
		RPG2.log( this, "performs a meelee attack for " + this.getDamage( ) + " damage" );
		target.takeHit( this.getDamage( ), false );
	}
		
	/**
	 * Hits the character with the specified number of damage. Armour will be 
	 * applied and will decrease the damage taken.
	 * 
	 * @param damage The point of damage that are to be dealt
	 * @param magic True for magic damage
	 * @return The new hit points of the character, if they are 0 the character
	 *   has died
	 * @throws RPGCharacterDied 
	 */
	public void takeHit( final int damage, final boolean magic ) throws RPGCharacterDied {
		// calculate actual damage, with a minimum value of 1, and apply it
		final int dmgdone = Math.max( damage - (!magic ? getArmour( ) : 0), 1 );
		hp = Math.max( hp - dmgdone, 0 );
		
		RPG2.log( this, "is hit for " + dmgdone + " (" + (damage - dmgdone) + " absorbed) and still has " + hp + " hp" );

		// check if this caused the character to die
		checkDeath( );
	}
	
	/**
	 * Checks if the player has died, i.e. his hp <= 0
	 * 
	 * @throws RPGCharacterDied
	 */
	private void checkDeath( ) throws RPGCharacterDied {
		if( hp <= 0 ) {
			hp = 0;
			throw new RPGCharacterDied( this );
		}
	}
	
	/**
	 * Heals the character for the specified amount of hp
	 * 
	 * @param points The hit points to heal
	 * @throws RPGCharacterDied if the character was already dead, to prevent 
	 *   reviving characters
	 */
	public void heal( final int points ) throws RPGCharacterDied {
		// first check if the player is not dead
		checkDeath( );
		
		hp += points;
		RPG2.log( this, "is healed for " + points + " hp and now has " + hp + " hp" );
	}
	
	/**
	 * Reads the character data from an input string
	 * 
	 * @param input The character data
	 * @return The character
	 */
	public static RPGChar fromString( final String input ) {
		final String[] s = input.split( "," );
		return new RPGChar( s[0], RPGCharClass.valueOf( s[1] ), Integer.parseInt( s[2] ), Integer.parseInt( s[3] ), Integer.parseInt( s[4] ), Integer.parseInt( s[5] ) );		
	}
	
	/**
	 * The character type
	 */
	public enum RPGCharClass {
		Fighter,
		Wizard;
	}
}
