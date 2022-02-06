package aoc2018.day24.iss;

/**
 * The damage types
 * 
 * @author Joris
 */
public enum DamageType {
	Bludgeoning, Cold, Fire, Slashing, Radiation;
	
	/**
	 * Returns the enum that corresponds to the string input, case agnostic
	 * 
	 * @param input The string input
	 * @return The damage type corresponding to the string
	 */
	public static DamageType fromString( final String input ) {
		return DamageType.valueOf( input.substring( 0, 1 ).toUpperCase( ) + input.substring( 1 ).toLowerCase( ) );
	}
}
