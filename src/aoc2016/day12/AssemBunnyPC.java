package aoc2016.day12;

import java.util.List;

/**
 * A computer that runs assembunny 
 * 
 * @author Joris
 */
public class AssemBunnyPC {
	/** The current instruction pointer */
	private int IP;
	
	/** The registers it has */
	private long[] R;
	
	/**
	 * Creates a new PC
	 */
	public AssemBunnyPC( ) {
		this.R = new long[ 4 ];
	}
	
	/**
	 * Runs a program on the computer
	 * 
	 * @param program The program as a list of instruction strings
	 * @param r The initial values for its registers
	 */
	public void runProgram( final List<String> program, final long... r ) {
		if( r.length > 4 ) throw new IllegalArgumentException( "Invalid number of registers (max 4): " + r.length );
		
		// initialise IP and registers to their specified values
		IP = 0;
		for( int i = 0; i < R.length; i++ ) R[i] = i < r.length ? r[i] : 0;
		
		// parse commands one by one until the IP is at the end of the program
		while( IP < program.size( ) ) runInstruction( program.get( IP++ ) );

	}

	/**
	 * Runs the specified program line
	 * 
	 * @param line The line of the program
	 */
	private void runInstruction( final String line ) {
		final String instr = line.substring( 0, 3 ).toLowerCase( );
		final String[] args = line.substring( 4 ).split( " " );
		
		// run instruction
		switch( instr ) {
			// manipulations
			case "cpy": write( args[1], read( args[0] ) ); break;
			case "inc": write( args[0], read( args[0] ) + 1 ); break;
			case "dec": write( args[0], read( args[0] ) - 1 ); break;
			
			// jumps
			case "jnz": if( read( args[0] ) != 0 ) jump( Integer.parseInt( args[1] ) ); break;
			
			// unsupported
			default: throw new RuntimeException( "Unsupported instruction: " + instr ); 
		}
	}
	
	/**
	 * Jumps the specified offset
	 * 
	 * @param offset The offset relative to the IP to jump
	 */
	private void jump( final int offset ) {
		// set to index minus 1 as the IP will be increased after executing jump
		IP += offset - 1;
	}
	
	/**
	 * Reads a value from the register
	 * 
	 * @param r The register to read or a literal value
	 * @return The value read or parsed
	 */
	public long read( final String r ) {
		// literal value?
		try {
			return Long.parseLong( r );
		} catch( NumberFormatException nfe ) {}				

		// nope, try registers
		if( r.length( ) == 1 ) {
			final int ridx = r.charAt( 0 ) - 'a';
			if( ridx >= 0 && ridx < R.length ) return R[ridx];
		}

		// failed..
		throw new IllegalArgumentException( "Invalid register or literal: " + r );
	}
	
	/**
	 * Writes a value to the register
	 * 
	 * @param r The register to read
	 * @param v The value to write
	 */
	public void write( final String r, final long v ) {
		if( r.length( ) != 1 ) throw new IllegalArgumentException( "Invalid register: " + r ); 

		final int ridx = r.charAt( 0 ) - 'a';
		if( ridx < 0 || ridx >= R.length ) throw new IllegalArgumentException( "Invalid register: " + r ); 
				
		R[ridx] = v;
	}

}
