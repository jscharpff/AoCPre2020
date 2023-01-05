package aoc2019.intcode.exceptions;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCode.IntCodeState;
import aocutil.string.StringUtil;

@SuppressWarnings( "serial" )
public class ICEInvalidState extends ICERuntimeException {
	/**
	 * Creates a new Invalid State Exception
	 * 
	 * @param program The current active program
	 * @param expected The state it was expected to be in
	 * @param message The error message
	 */
	public ICEInvalidState( final IntCode program, final IntCodeState expected, final String message ) {
		super( program, message + " [current state " + program.getState( ) + ", expected state " + expected + "]" );
	}
	
	/**
	 * Creates a new Invalid State Exception where multiple states were valid
	 * 
	 * @param program The current active program
	 * @param message The error message
	 * @param expected List of states it was expected to be in
	 */
	public ICEInvalidState( final IntCode program, final String message, final IntCodeState... expected ) {
		super( program, message + " [current state " + program.getState( ) + ", expected state " + StringUtil.fromArray( expected ) + "]" );
	}
	
	/***
	 * Shorthand function to perform a test whether the program is in the required
	 * state
	 * 
	 * @param program The program to test
	 * @param state The state the program is expected to be in
	 * @throws ICEInvalidState iff the program is not in the state
	 */
	public static void throwIfNotValid( final IntCode program, final IntCodeState state ) throws ICEInvalidState {		
		throwIfNotValid( program, state, "Program is in an invalid state"	);
	}
	
	/***
	 * Shorthand function to perform a test whether the program is in the required
	 * state
	 * 
	 * @param program The program to test
	 * @param state The state the program is expected to be in
	 * @param message The informational message to throw
	 * @throws ICEInvalidState iff the program is not in the state
	 */
	public static void throwIfNotValid( final IntCode program, final IntCodeState state, final String message ) throws ICEInvalidState {		
		if( program.getState( ) != state )
			throw new ICEInvalidState( program, state, message	);
	}
	
	/***
	 * Shorthand function to perform a test whether the program is in one of 
	 * the required states
	 * 
	 * @param program The program to test
	 * @param states The list of states the program is may be in
	 * @param message The informational message to throw
	 * @throws ICEInvalidState iff the program is not in the state
	 */
	public static void throwIfNotValid( final IntCode program, final String message, final IntCodeState... states ) throws ICEInvalidState {
		for( final IntCodeState s : states )
			if( program.getState( ) == s ) return;
		
		throw new ICEInvalidState( program, message, states	);
	}
}
