package aoc2019.intcode.exceptions;

@SuppressWarnings( "serial" )
public abstract class ICException extends Exception {	
	/**
	 * Creates a new IntCode Exception
	 * 
	 * @param message The error message
	 */
	public ICException( final String message ) {
		super( message );
	}

}
