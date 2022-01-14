package aoc2017.day06;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import aocutil.string.StringUtil;

/**
 * Class that models a debugger that checks a cyclic structure of memory banks
 * and reallocates memory in each cycle
 * 
 * @author Joris
 */
public class MemDebugger {
	/** The number of blocks in each of the memory banks */
	private final int[] mem;
	
	/**
	 * Creates a new debugger from the array describing the current memory use
	 * 
	 * @param input The memory state
	 */
	public MemDebugger( final int[] input ) {
		mem = Arrays.copyOf(  input, input.length );		
	}
	
	/**
	 * Determines the number of steps until we detect a cycle in the memory
	 * redistribution process
	 * 
	 * @return The number of steps until a cycle occurs
	 */
	public long detectCycle( ) {
		long steps = 0;
		final Set<String> seen = new HashSet<>( );
		
		while( steps < 100000 ) {
			if( seen.contains( toString( ) ) ) return steps;
			seen.add( toString( ) );
			
			redistribute( );
			steps++;
		}
		
		throw new RuntimeException( "Failed to detect a cycle" );
	}
	
	/**
	 * Determines the the length of a cycle in the memory redistribution process
	 * 
	 * @return The number of steps between two similar states
	 */
	public long detectCycleLength( ) {
		long steps = 0;
		final Map<String, Long> seen = new HashMap<>( );
		
		while( steps < 100000 ) {
			final String state = toString( );
			if( seen.containsKey( state ) ) return steps - seen.get( state );
			seen.put( state, steps );
			
			redistribute( );
			steps++;
		}
		
		throw new RuntimeException( "Failed to detect a cycle" );
	}
	
	/**
	 * Redistributes the blocks of memory of the bank that has the most blocks
	 * over all other banks
	 */
	private void redistribute( ) {
		// get index of memory with most blocks
		final int max = IntStream.of( mem ).max( ).getAsInt( );
		int idx = -1;
		while( mem[ ++idx ] != max );
		mem[idx] = 0;
		
		// and redistribute over all banks
		final int N = mem.length;
		for( int i = 1; i <= N; i++ ) {
			mem[ (idx + i) % N ] += max / N + (i <= (max % N) ? 1 : 0);
		}
	}
	
	/**
	 * @return The current values in memory
	 */
	@Override
	public String toString( ) {
		return StringUtil.fromArray( mem );
	}

}
