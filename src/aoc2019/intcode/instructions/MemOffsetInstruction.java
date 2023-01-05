package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * Offsets the memory base by the specified amount
 * 
 * @author Joris
 */
public class MemOffsetInstruction extends Instruction {
	
	@Override
	protected void execute( ) throws ICERuntimeException {
		getProgram( ).offset( (int)getArgumentValue( 0 ) );
	}
}
