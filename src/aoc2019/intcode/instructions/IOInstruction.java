package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICEUnsupported;
import aoc2019.intcode.exceptions.ICEYield;

public class IOInstruction extends Instruction {
	/** The type of IO operation */
	public enum IOOperation {
		Input, Output;
	}
	
	/** The operation to perform */
	protected final IOOperation operation;	
	
	/**
	 * Parses the input instruction
	 * 
	 * @param operation The IOOperation
	 */
	public IOInstruction( final IOOperation operation ) {	
		this.operation = operation;
	}
	
	/** @return The operation of this instruction */
	public IOOperation getOperation( ) { return operation; }

	/**
	 * Requests the user for input or outputs a value, depending on the type of 
	 * operation
	 */
	@Override
	protected void execute( ) throws ICERuntimeException {
		switch( operation ) {
			case Input: input( ); return;
			case Output: output( ); return;
			default:
				throw new ICEUnsupported( getProgram(), "Unknown IO operation " + operation );
		}
	}
	
	/**
	 * Performs the input operation
	 * 
	 * @throws ICERuntimeException
	 */
	protected void input( ) throws ICERuntimeException {
		try {
			writeResult( getProgram( ).input( ) );
		} catch( ICEYield e ) {
			/** yielding execution while waiting for input, do nothing */
		}
	}

	/**
	 * Performs the output operation
	 * 
	 * @throws ICERuntimeException
	 */
	protected void output( ) throws ICERuntimeException {
		getProgram( ).output( getArgumentValue( 0 ) );
	}
}
