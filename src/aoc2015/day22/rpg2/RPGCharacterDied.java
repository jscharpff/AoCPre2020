package aoc2015.day22.rpg2;

@SuppressWarnings( "serial" )
public class RPGCharacterDied extends Exception {
	/** The character that has died */
	public final RPGChar character;
	
	/**
	 * Creates a new exception to stop the game because one of the characters
	 * died
	 * 
	 * @param character The character that died
	 */
	public RPGCharacterDied( final RPGChar character ) {
		super( character + " has died" );
		
		this.character = character;
	}

}
