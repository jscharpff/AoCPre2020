package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;

@SuppressWarnings( "serial" )
public class ICEInputOutput extends ICERuntimeException {
	/**
	 * Creates new input exeption exception
	 * 
	 * @param program The IntCode program
	 * @param message The message
	 */
	public ICEInputOutput( final IntCode program, final String message ) {
		super( program, message );
	}
}
