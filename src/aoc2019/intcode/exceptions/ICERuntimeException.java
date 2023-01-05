package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;

@SuppressWarnings( "serial" )
public class ICERuntimeException extends ICException {
	/** Reference to the program */
	protected final IntCode program;

	/**
	 * Creates a new runtime exception
	 * 
	 * @param program The IntCode program
	 * @param message The message
	 */
	public ICERuntimeException( final IntCode program, final String message ) {
		super( program.getName( ) + ": " + message + " (IP: " + program.getIP( ) + ")" );
		this.program = program;
	}
	
	/**
	 * @return The current program state
	 */
	public IntCode getProgram( ) { return program; }
}
