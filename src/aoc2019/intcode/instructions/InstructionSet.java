package aoc2019.intcode.instructions;

/**
 * The set of available instructions
 * 
 * @author Joris
 */
public enum InstructionSet {
	// binary operations
	ADD(1, 3),
	MULTIPLY(2, 3),
	
	// IO
	INPUT(3,1),
	OUTPUT(4,1),
	
	// jumps
	JUMP_TRUE(5, 2),
	JUMP_FALSE(6, 2),
	
	// binary comparators
	COMP_LT(7, 3),
	COMP_EQ(8,3),
	
	// memory manipulation
	MEM_OFFSET( 9, 1 ),
	
	// terminates the program
	EXIT(99, 0);
	
	/** The integer code of the instruction */
	private final int intcode;
	
	/** The number of required arguments */
	private final int arguments;
	
	/**
	 * Creates a new Instruction definition
	 * 
	 * @param intcode The integer code of the instruction
	 * @param arguments The number of arguments it requires
	 */
	private InstructionSet( final int intcode, final int arguments ) {
		this.intcode = intcode;
		this.arguments = arguments;
	}
	
	/**
	 * Returns the InstructionSet definition that corresponds to the given intcode
	 * 
	 * @param code The intcode of the instruction
	 * @return The instruction, null if no such instruction is defined
	 */
	public static InstructionSet fromIntCode( final int code ) {
		for( InstructionSet i : InstructionSet.values( ) ) if ( i.intcode == code ) return i;
		return null;
	}

	/** @return The integer code of the instruction */
	public int getIntCode( ) { return intcode; }
	
	/** @return The number of required arguments of this instruction */
	public int getArgumentCount( ) { return arguments; }
}
