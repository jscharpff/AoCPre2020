package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICEUnsupported;

public class BinaryOpInstruction extends Instruction {
	/** Set of available operators */
	protected enum Operator {
		// mathematical operators
		Add, Multiply,
		
		// comparators
		LessThan, Equals;
	}
	
	/** The binary operator to use */
	protected final Operator op;
	
	/**
	 * Creates a new binary operation
	 * 
	 * @param operator The operation to perform
	 * @param args The instruction arguments
	 */
	public BinaryOpInstruction( final Operator operator ) {
		this.op = operator;
	}
	
	/**
	 * Executes the instruction by performing the binary operation on the first
	 * two arguments. Stores the result in the program at the index specified by
	 * the third argument  
	 * 
	 * @param program Reference to the running IntCode program
	 * @return The result of the operation
	 * @throws ICEUnsupported if the operation is unsupported
	 */
	@Override
	protected void execute( ) throws ICERuntimeException {
		long result = 0;
		
		final long a1 = getArgumentValue( 0 );
		final long a2 = getArgumentValue( 1 );

		// perform computation
		switch( op ) {
			case Add: result = a1 + a2;	break;
			case Multiply: result = a1 * a2; break;
			
			case LessThan: result = a1 < a2 ? 1 : 0; break; 
			case Equals: result = a1 == a2 ? 1 : 0; break; 
		
			default:
				throw new ICEUnsupported( getProgram( ), "Unsupported operator " + op + " in binary operation" );
		}
		
		// store the result
		writeResult( result );
	}
}
