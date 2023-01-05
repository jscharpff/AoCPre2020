package aoc2019.day25.droids;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCodeMachine;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * Manual Droid implementation that runs the IntCode program in interactive
 * text-based mode so that it can be played via the interface
 * 
 * @author Joris
 */
public class ManualDroid extends IntCodeMachine {
	
	/**
	 * Creates the manual droid
	 * 
	 * @param program The program to run
	 * @throws ICEInvalidState if the program was not set up properly
	 */
	public ManualDroid( final String program ) throws ICEInvalidState {
		super( IntCode.parse( "Droid", program ), false );
		
		getProgram( ).setTerminalMode( this::processInput );
	}

	/**
	 * Processes the input received via the terminal. Offers a WASD shorthand for
	 * navigating through the maze
	 * 
	 * @param input The input as received from the terminal
	 */
	private void processInput( final String input ) {
		
		// replace shorthands by their full commands, if any
		final String command; 
		switch( input ) {
			case "w": command = "north"; break;
			case "s": command = "south"; break;
			case "a": command = "west"; break;
			case "d": command = "east"; break;
			default: command = input;
		}
		
		// send the input to the program
		try {
			feedASCII( command );
			getProgram( ).resume( );
		} catch( ICERuntimeException e ) {
			System.err.println( "Program was not expecting input" );
			e.printStackTrace();
		}
	}
}
