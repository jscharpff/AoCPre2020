package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * Instruction to terminate the program
 * 
 * @author Joris
 */
public class ExitInstruction extends Instruction {	
	/**
	 * Ends the program
	 */
	@Override
	protected void execute( ) throws ICERuntimeException {
		getProgram( ).end( );
	}
}
