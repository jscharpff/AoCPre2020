package aoc2019.intcode.exceptions;

@SuppressWarnings( "serial" )
public class ICEInvalidSyntax extends ICException {
	/**
	 * Creates new Invalid Syntax exception
	 * 
	 * @param message The message
	 */
	public ICEInvalidSyntax( final String message ) {
		super( message );
	}
}
