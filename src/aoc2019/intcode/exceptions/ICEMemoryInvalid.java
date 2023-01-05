package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;

@SuppressWarnings( "serial" )
public class ICEMemoryInvalid extends ICERuntimeException {
	/**
	 * Creates a new memory addressing exception
	 * 
	 * @param program The IntCode program
	 * @param address The address that we tried to get
	 */
	public ICEMemoryInvalid( final IntCode program, final long address ) {
		super( program, "Memory index out of bounds " + address );
	}
}
