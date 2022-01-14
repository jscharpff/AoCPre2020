package aoc2017.day15;

import aocutil.object.LabeledObject;

/**
 * A number-producing generator
 * 
 * @author Joris
 */
public class Generator extends LabeledObject {
	/** The amount of generated numbers */
	private long N;
	
	/** The last generated number */
	private long last;
	
	/** The generation factor */
	private final long factor;
	
	/** The divisor */
	private final static long GEN_DIVIDER = 2147483647;

	/**
	 * Creates a new number generator
	 * 
	 * @param label The label of the generator
	 * @param factor The number generation factor
	 * @param initial The initial value for the generator
	 */
	public Generator( final String label, final long factor, final long initial ) {
		super( label );
		
		this.factor = factor;
		this.last = initial;
		this.N = 0;
	}
	
	/**
	 * Generates the next number
	 * 
	 * @return The generated number
	 */
	public long next( ) {
		N++;
		last = Long.remainderUnsigned( last * factor, GEN_DIVIDER );
		return last;
	}
	
	/** @return The last generated value */
	public long getLast( ) { return last; }
	
	/**
	 * Creates a generator from a string description
	 * 
	 * @param input The input string
	 * @return The generator
	 */
	public static Generator fromString(final String input ) {
		final String[] s = input.split( "," );
		return new Generator( s[0], Long.parseLong( s[2] ), Long.parseLong( s[1] ) );
	}
	
	/** @return The string description of the generator */
	@Override
	public String toString( ) {
		return label + " (" + last + ", " + N + ")";
	}
}
