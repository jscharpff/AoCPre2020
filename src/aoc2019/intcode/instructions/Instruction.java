package aoc2019.intcode.instructions;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCode.IntCodeState;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICEInvalidSyntax;
import aoc2019.intcode.exceptions.ICEMemoryInvalid;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICEUnsupported;
import aoc2019.intcode.instructions.BinaryOpInstruction.Operator;
import aoc2019.intcode.instructions.IOInstruction.IOOperation;
import aoc2019.intcode.instructions.JumpInstruction.JumpCondition;
import aocutil.string.StringUtil;

/**
 * A single instruction of the IntCode machine
 * 
 * @author Joris
 */
public abstract class Instruction {
	/** The current program, set when the instruction is executed */
	private IntCode program;
	
	/** The array of its arguments */
	private Argument[] arguments;
	
	
	/**
	 * Determines the instruction to execute
	 * 
	 * @param program Reference to the program
	 * @param instr The instruction definition
	 * @param args The instruction arguments
	 * @return The parsed instruction
	 * @throws ICEInvalidSyntax 
	 */
	public static Instruction getInstruction( final InstructionSet instr, final Argument[] args ) throws ICEInvalidSyntax {
		// build the instruction to execute
		final Instruction I;
		switch( instr ) {
			// binary (mathematical) operators
			case ADD: I = new BinaryOpInstruction( Operator.Add ); break;
			case MULTIPLY: I = new BinaryOpInstruction( Operator.Multiply );	break;
			case COMP_EQ: I = new BinaryOpInstruction( Operator.Equals ); break;
			case COMP_LT: I = new BinaryOpInstruction( Operator.LessThan );	break;
				
			// IO operations
			case INPUT: I = new IOInstruction( IOOperation.Input );	break;
			case OUTPUT: I = new IOInstruction( IOOperation.Output ); break;
				
			// jump instructions
			case JUMP_TRUE: I = new JumpInstruction( JumpCondition.JumpIfTrue ); break;
			case JUMP_FALSE: I = new JumpInstruction( JumpCondition.JumpIfFalse ); break;
			
			// memory modification
			case MEM_OFFSET: I = new MemOffsetInstruction( ); break;
				
			// terminate the program
			case EXIT: I = new ExitInstruction( ); break;
				
			// unsupported operation
			default:
				throw new ICEInvalidSyntax( "Unsupported instruction in program: " + instr );
		}
		
		// add arguments to it and return the instruction
		I.arguments = args;
		return I;
	};
	
	/**
	 * Executes the instruction
	 * 
	 * @param prog The program that is executing the instruction
	 * @throws ICERuntimeException if any exception occurred during execution
	 */
	public void execute( final IntCode prog ) throws ICERuntimeException {
		// set context information for the instruction and execute it
		this.program = prog;
		this.execute( );
	}
	
	/**
	 * @return The current reference to the running program
	 * 
	 * @throws ICEInvalidState if the program is not active
	 */
	protected IntCode getProgram( ) throws ICEInvalidState {
		if( !program.isRunning( ) )
			throw new ICEInvalidState( program, IntCodeState.Running, "Program reference not set or referencing non-running program" );
		
		return program;
	}
	
	/**
	 * Determines the value of the specified argument
	 * 
	 * @param index The argument index
	 * @return The value or address referred to by the specified argument
	 * @throws ICEMemoryInvalid if the referenced memory is invalid
	 * @throws ICEUnsupported if the parameter addressing mode is invalid
	 * @throws ICEInvalidState if the program is not running
	 */
	protected long getArgumentValue( final int index ) throws ICEUnsupported, ICEMemoryInvalid, ICEInvalidState {
		if( arguments == null || index < 0 || index >= arguments.length ) 
			throw new IndexOutOfBoundsException( "Argument index " + index + " not valid or arguments not set (arguments " + StringUtil.fromArray( arguments ) + ")" );
		
		return getProgram( ).load( arguments[index].value, arguments[index].mode );
	}

	/**
	 * Writes the result of an operation into the address described by the last
	 * argument of the instruction (regardless of parameter mode)
	 * 
	 * @param value The result to write
	 * @throws ICEMemoryInvalid if the target address of the write location is invalid 
	 * @throws ICEUnsupported if the immediate mode is used
	 */
	protected void writeResult( final long value ) throws ICEMemoryInvalid, ICEUnsupported, ICEInvalidState {
		final int index = arguments.length - 1;
		getProgram( ).store( arguments[index].value, arguments[index].mode, value );
	}
	
	/**
	 * Executes the instruction
	 * 
	 * @throws ICERuntimeException on runtime errors
	 */
	protected abstract void execute( ) throws ICERuntimeException;
}
