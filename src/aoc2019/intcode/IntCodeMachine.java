package aoc2019.intcode;

import aoc2019.intcode.IntCode.IntCodeState;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * Machine that uses an intcode program as its software
 * 
 * @author Joris
 */
public abstract class IntCodeMachine {
	/** The IntCode program that serves as the software of this machine */
	protected final IntCode program;
	
	/** True to run in threaded mode */
	protected final boolean threaded; 

	/**
	 * Creates a new IntCode Machine
	 * 
	 * @param program The program to run
	 * @param threaded True to run the program in threaded mode
	 */
	public IntCodeMachine( final IntCode program, final boolean threaded ) {
		this.program = program;
		this.threaded = threaded;
	}
	
	
	/**
	 * Activates or resumes the machine
	 * 
	 * @param threaded True to run the program in threaded mode
	 * @throws ICERuntimeException 
	 */
	public void activate( ) throws ICERuntimeException {
		program.setLogtypes( );
		if( threaded ) program.runThreaded( );
		else program.resume( );
	}
	
	
	/**
	 * Deactivates the machine
	 */
	public void deactivate( ) {
		program.halt( true );
	}
	
	
	/** @return True iff the IntCode program is still running */
	public boolean isActive( ) { return program.getState( ) == IntCodeState.Running || program.getState( ) == IntCodeState.AwaitingInput; }

	
	/** @return The state of the intcode program */
	public IntCodeState getState( ) { return program.getState( ); }
	
	/** @return The program it is running */
	public IntCode getProgram( ) { return program; }
	
	/**
	 * Sends a single signal to the progam
	 * 
	 * @param signal The signal to send
	 * @throws ICERuntimeException if the machine was not expecting an input
	 */
	public void feed( final long signal ) throws ICERuntimeException {
		program.feed( signal );
	}
	
	/**
	 * Feeds an ASCII encoded string to the program
	 * 
	 * @param string The string to send to the program
	 * @throws ICERuntimeException if the machine was not expecting input
	 */
	public void feedASCII( final String... string ) throws ICERuntimeException {
		// feed the ints corresponding to the ASCII codes in the string 
		for( final String s : string ) {
			for( final char c : s.toCharArray( ) ) program.feed( (long)c );
			// end string with 'newline' character
			program.feed( 10 );
		}
	}
	
	/**
	 * Consumes a single output value
	 * 
	 * @return The output value
	 * @throws ICEInputOutput if no output is available
	 */
	public long consume( ) throws ICEInputOutput {
		return program.consume( );
	}
}
