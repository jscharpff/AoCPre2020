package aoc2015.day21.rpg;

import aocutil.object.LabeledObject;

/**
 * An item that modifies the stats of a character
 * 
 * @author Joris
 */
public class RPGItem extends LabeledObject {
	/** The item type */
	protected final RPGItemType type;
	
	/** The cost of the item */
	protected final int cost;
	
	/** The attack power it grants as additional damage points */ 
	protected final int dmg;
	
	/** The armour value as additional defense points */
	protected final int armour;
	
	/**
	 * Creates a new item
	 * 
	 * @param name The name of the item
	 * @param type The item type
	 * @param cost The cost of the item
	 * @param dmg The additional damage this item gives the character
	 * @param armour The additional armour this item gives the character
	 */
	public RPGItem( final String name, final RPGItemType type, final int cost, final int dmg, final int armour ) {
		super( name );
		
		this.type = type;
		this.cost = cost;
		this.dmg = dmg;
		this.armour = armour;
	}

	/**
	 * Checks if the item is of a certain type
	 * 
	 * @param t The type to check
	 * @return True if the type equals that of the item
	 */
	public boolean ofType( final RPGItemType t ) {
		return type.equals( t );
	}
	
	/**
	 * Creates a new item from a string description
	 * 
	 * @param input The string description of the item
	 * @return The RPGItem
	 */
	public static RPGItem fromString( final String input ) {
		final String s[] = input.split( "," );
		return new RPGItem( s[1], RPGItemType.valueOf( s[0] ), Integer.parseInt( s[2] ), Integer.parseInt( s[3] ), Integer.parseInt( s[4] ) );
	}

	/**
	 * Available item types
	 */
	public enum RPGItemType {
		Weapon,
		Armour,
		Ring;
	}
}
