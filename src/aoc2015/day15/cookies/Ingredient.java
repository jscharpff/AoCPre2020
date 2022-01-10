package aoc2015.day15.cookies;

import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

/**
 * Class that represents a single ingredient and its cookie-enhancing
 * properties
 * 
 * @author Joris
 */
public class Ingredient extends LabeledObject {
	/** The property column numbers */
	protected static final int Capacity = 0, Durability = 1, Flavour = 2, Texture = 3, Calories = 4;

	/** The property values */
	protected final int[] properties;

	/**
	 * Creates a new cookie ingredient
	 * 
	 * @param name The name of the ingredient
	 * @param properties The array containing a value for each of its properties
	 */
	private Ingredient( final String name, final int[] properties ) {
		super( name );
		if( properties.length != 5 ) throw new IllegalArgumentException( "Invalid number of properties in array: " + properties.length );
		
		this.properties = properties.clone( );
	}

	/** @return The string describing the cookie */
	@Override
	public String toString( ) {
		return label + ": capacity " + properties[Capacity] + ", durability " + properties[Durability] 
				+ ", flavor " + properties[Flavour] + ", tecture " + properties[Texture] + ", calories " + properties[Calories];
	}

	/**
	 * Creates a new ingredient from a string description
	 * 
	 * @param input The string describing the ingredient
	 * @return The ingredient object
	 */
	public static Ingredient fromString( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)", input );
		
		return new Ingredient( rm.get( 1 ), rm.getInts( new int[] { 2, 3, 4, 5, 6 } ) );
	}
}
