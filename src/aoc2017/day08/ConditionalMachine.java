package aoc2017.day08;

import aocutil.assembly.RegisterAssemblyMachine;

/**
 * Register-based machine that can perform instructions under conditional
 * clauses
 * 
 * @author Joris
 */
public class ConditionalMachine extends RegisterAssemblyMachine {
	/** Keep track of the highest memory register value through a special register */
	private static final String R_HIGHEST = "highest"; 
	
	/**
	 * Creates a new ConditionalMachine
	 */
	public ConditionalMachine( ) {
		super( "ConditionalMachine" );
		write( R_HIGHEST, 0 );
	}
	
	/**
	 * Executes a single command
	 * 
	 * @param cmd The command to execute
	 */
	@Override
	protected void execute( final String cmd ) {
		// split instruction and condition
		final String[] s = cmd.split( " if " );
		
		// first evaluate condition
		if( !condition( s[1] ) ) return;
		
		// condition is met, evaluate instruction
		final String[] i = s[0].split( " " );
		final String r = i[0];
		final int v = Integer.parseInt( i[2] );
		switch( i[1] ) {
			case "inc": write( r, read( r ) + v ); break;
			case "dec": write( r, read( r ) - v ); break;
			
			default: throw new RuntimeException( "Unsupported instruction: " + i[1] );			
		}
	}

	/**
	 * Evaluates the condition string
	 * 
	 * @param cond The condition
	 * @return True if the condition evaluates to true
	 */
	private boolean condition( final String cond ) {
		final String[] c = cond.split( " " );
		final long v1 = read( c[0] );
		final long v2 = Long.parseLong( c[2] );
		
		switch( c[1] ) {
			case ">": return v1 > v2;
			case "<": return v1 < v2;
			case ">=": return v1 >= v2;
			case "<=": return v1 <= v2;
			case "==": return v1 == v2;
			case "!=": return v1 != v2;
			default: throw new RuntimeException( "Invalid conditional operand: " + c[1] );
		}
	}
	
	/**
	 * Writes the value to the memory register
	 * 
	 * @param r The register
	 * @param value The value to write
	 */
	@Override
	public void write( final String r, final long value ) {
		if( value > read( R_HIGHEST ) ) super.write( R_HIGHEST, value );
		super.write( r, value );
	}
	
	/**
	 * Retrieves the highest register value, either currently or all-time highest
	 * 
	 * @param current True to get the currently highest value, false for highest
	 *   value written to memory so far 
	 * @return The highest value ever recorded in one of the registers
	 */
	public long getMaxRegisterValue( final boolean current ) { 
		if( current )
			return getRegisters( ).keySet( ).stream( ).filter( x -> !x.equals( R_HIGHEST ) ).mapToLong( x -> read( x ) ).max( ).getAsLong( );
		else
			return read( R_HIGHEST );
	}	
}
