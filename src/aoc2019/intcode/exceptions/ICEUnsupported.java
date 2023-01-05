package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;

@SuppressWarnings( "serial" )
public class ICEUnsupported extends ICERuntimeException {
	/**
	 * Creates new Unknown Operand exception
	 * 
	 * @param program The IntCode program
	 * @param message The message
	 */
	public ICEUnsupported( final IntCode program, final String message ) {
		super( program, message );
	}
}
