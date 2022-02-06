package aoc2018.day21;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that runs the OpCodeMachine while using a debug routine to inspect
 * register values during execution
 * 
 * @author Joris
 */
public class OpCodeHacker {
	/** The program to run on the opcode machine */
	private final List<String> program;
	
	/**
	 * Creates a new OpCodeHacker for the specified program
	 * 
	 * @param program The program to hack
	 */
	public OpCodeHacker( final List<String> program ) {
		this.program = new ArrayList<>( program );
	}
	
	/**
	 * Finds the lowest, non-negative integer that causes the program to halt
	 * in the fewest number of instructions
	 * 
	 * @param min True for the minimal value, false for maximum value
	 * @param maxinstr The maximum number of instructions to test 
	 * @return The integer value
	 */
	public long[] findHaltingValues( ) {
		
		// run the program with value 0 in register 0 and find out to what values
		// it is compared, stop the program once it repeats a value
		final List<Long> values = new ArrayList<>( );
		final OpCodeJumpMachineDebug op = new OpCodeJumpMachineDebug( 6 );
		op.addDebugRoutine( 28, x -> {
			final long val = x.read( 5 );
			if( values.contains( val ) ) return false;
			values.add( val );
			return true;
		} );
		op.run( program, -1 );
		
		return new long[ ] { values.get( 0 ), values.get( values.size( ) - 1 ) };
	}
}
