package aoc2019.intcode.instructions;

import aoc2019.intcode.exceptions.ICERuntimeException;

public class JumpInstruction extends Instruction {
	/** The condition value that needs to be matched */
	protected final JumpCondition condition;
	
	/** Available jump conditions */
	public enum JumpCondition {
		JumpIfTrue, JumpIfFalse;
	}
	
	/**
	 * Creates a new jump instruction
	 * 
	 * @param condition The condition required to match for a jump
	 */
	public JumpInstruction( final JumpCondition condition ) {
		
		this.condition = condition;
	}

	/**
	 * Performs a jump (move of the instruction pointer) if the condition is met
	 */
	@Override
	protected void execute( ) throws ICERuntimeException {
		final long input = getArgumentValue( 0 );
		
		// evaluate jump conditions (negative evaluation here!)
		if( condition == JumpCondition.JumpIfTrue  && input == 0 ) return;
		if( condition == JumpCondition.JumpIfFalse && input != 0 ) return;
		
		// condition satisfied, jumpp!
		getProgram( ).jump( (int)getArgumentValue( 1 ) );
	}

}
