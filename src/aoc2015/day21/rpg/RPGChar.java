package aoc2015.day21.rpg;

import java.util.HashSet;
import java.util.Set;

import aoc2015.day21.rpg.RPGItem.RPGItemType;
import aocutil.object.LabeledObject;

/**
 * A character in the RPG game that can deal damage and get hit
 * 
 * @author Joris
 */
public class RPGChar extends LabeledObject {
	/** The hit points */
	private int hp;
	
	/** The current weapon damage */
	private int dmg;
	
	/** The current armour points */
	private int armour;
	
	/** The gold available to the character */
	private int gold;
	
	/** The current set of items the character has equipped */
	private final Set<RPGItem> items;
	
	/**
	 * Creates a new RPG character
	 * 
	 * @param name The character's name
	 * @param hp The initial hit points
	 * @param weapdmg The base weapon damage, without items
	 * @param armour The base armour rating, without items
	 * @param gold The gold the character starts with
	 */
	public RPGChar( final String name, final int hp, final int weapdmg, final int armour, final int gold ) {
		super( name );
		
		this.hp = hp;
		this.dmg = weapdmg;
		this.armour = armour;
		this.gold = gold;
		this.items = new HashSet<>( );
	}
	
	/**
	 * Creates a new RPG Character by copying the specified character. Items are
	 * not copied
	 * 
	 * @param chardata The character data to copy
	 */
	public RPGChar( final RPGChar chardata ) {
		this( chardata.label, chardata.hp, chardata.dmg, chardata.armour, chardata.gold );
	}

	/** @return The current hit points */
	public int getHP( ) { return hp; }
	
	/** @return The current weapon damage, incorporating all items */
	public int getDamage( ) { 
		int damage = dmg;
		for( final RPGItem i : items ) damage += i.dmg;
		return damage;
	}
	
	/** @return The current armour value, incorporating items */
	public int getArmour( ) {
		int arm = armour;
		for( final RPGItem i : items ) arm += i.armour;
		return arm;
	}
	
	/** @return The current gold level of the character */
	public int getGold( ) { return gold; }
	
	/**
	 * Gives the character the amount of gold
	 * 
	 * @param donation The gold to give to the character
	 */
	public void giveGold( final int donation ) {
		this.gold += donation;
	}
	
	/**
	 * Hits the player with the specified number of damage. Armour will be applied
	 * and will decrease the damage taken.
	 * 
	 * @param damage The point of damage that are to be dealt
	 * @return The new hit points of the character, if they are 0 the character
	 *   has died
	 */
	public int hit( final int damage ) {
		// calculate actual damage, with a minimum value of 1, and apply it
		final int dmgdone = Math.max( damage - getArmour( ), 1 );
		hp = Math.max( hp - dmgdone, 0 );
		
		RPG.log( label + " is hit for " + dmgdone + " (" + (damage - dmgdone) + " absorbed) and " + (hp == 0 ? "has died" : "still has " + hp + " hp") );
		
		return hp;
	}
	
	/**
	 * Buys the specified item with the gold still available. Will check
	 * equipment rules before buying
	 * 
	 * @param item The item to buy
	 * @return True iff the character successfully bought the item
	 */
	public boolean buy( final RPGItem item ) {
		// too expensive?
		if( item.cost > getGold( ) ) return false;
		
		// check equipment rules
		final int typecount = items.stream( ).mapToInt( x -> x.ofType( item.type ) ? 1 : 0 ).reduce( Math::addExact ).orElse( 0 );
		if( (item.type == RPGItemType.Weapon || item.type == RPGItemType.Armour) && typecount >= 1 ) return false;
		if( item.type == RPGItemType.Ring && typecount >= 2 ) return false;
		
		// okay, we can buy it!
		items.add( item );
		gold -= item.cost;
		return true;
	}
	
	/**
	 * Reads the character data from an input string
	 * 
	 * @param input The character data
	 * @return The character
	 */
	public static RPGChar fromString( final String input ) {
		final String[] s = input.split( "," );
		return new RPGChar( s[0], Integer.parseInt( s[1] ), Integer.parseInt( s[2] ), Integer.parseInt( s[3] ), Integer.parseInt( s[4] ) );		
	}
}
