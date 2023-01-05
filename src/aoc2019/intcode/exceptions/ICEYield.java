package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;

@SuppressWarnings( "serial" )
public class ICEYield extends ICERuntimeException {

	/**
	 * Creates a new IntCode Yield exception that is thrown to interrupt the
	 * input instruction when there is no input available
	 * 
	 * @param program The IntCode program
	 */
	public ICEYield( IntCode program ) {
		super( program, "Yielding execution while waiting for input" );
	}

}
