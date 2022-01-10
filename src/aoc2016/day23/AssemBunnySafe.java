package aoc2016.day23;

import java.util.ArrayList;
import java.util.List;

/**
 * A computer that runs assembunny 
 * 
 * @author Joris
 */
public class AssemBunnySafe {
	/** The (modified) program that it is currently running */
	private List<String> program;
	
	/** The current instruction pointer */
	private int IP;
	
	/** The registers it has */
	private long[] R;
	
	/**
	 * Creates a new PC
	 */
	public AssemBunnySafe( ) {
		this.R = new long[ 4 ];
	}
	
	/**
	 * Runs a program on the computer
	 * 
	 * @param prog The program as a list of instruction strings
	 * @param r The initial values for its registers
	 */
	public void runProgram( final List<String> prog, final long... r ) {
		if( r.length > 4 ) throw new IllegalArgumentException( "Invalid number of registers (max 4): " + r.length );
		
		// initialise IP and registers to their specified values
		IP = 0;
		for( int i = 0; i < R.length; i++ ) R[i] = i < r.length ? r[i] : 0;
		
		// parse commands one by one until the IP is at the end of the program
		this.program = new ArrayList<>( prog );
		while( IP < prog.size( ) ) runInstruction( this.program.get( IP++ ) );

	}

	/**
	 * Runs the specified program line
	 * 
	 * @param line The line of the program
	 */
	private void runInstruction( final String line ) {
		final String instr = line.substring( 0, 3 ).toLowerCase( );
		final String[] args = line.length( ) > 3 ? line.substring( 4 ).split( " " ) : new String[0];
		
		// run instruction
		try { 
			switch( instr ) {
				// toggle
				case "tgl": toggleInstruction( (int)read( args[0] ) ); break;
				
				// manipulations
				case "cpy": write( args[1], read( args[0] ) ); break;
				case "inc": write( args[0], read( args[0] ) + 1 ); break;
				case "dec": write( args[0], read( args[0] ) - 1 ); break;
				
				// binary operators
				case "add": write( args[1], read( args[0] ) + read( args[1] ) ); break;
				case "mul": write( args[1], read( args[0] ) * read( args[1] ) ); break;
				case "nop": break;
				
				// jumps
				case "jnz": if( read( args[0] ) != 0 ) jump( (int)read( args[1] ) ); break;
				
				// unsupported
				default: throw new RuntimeException( "Unsupported instruction: " + instr ); 
			}
		} catch( Exception e ) {
			// modifying the program may have lead to an invalid instruction,
			// skip it but still dump it to console
			System.err.println( "Failed execution of " + line + ": " + e.toString( ) );
		}
	}
	
	/**
	 * Toggles the instruction at the given offset from the IP. Manipulates the
	 * program listing itself
	 * 
	 * @param offset The offset of the target instruction
	 */
	private void toggleInstruction( final int offset ) {
		// get index of the target instruction and ignore invalid indices
		final int idx = IP + offset - 1;
		if( idx < 0 || idx >= program.size( ) ) return;
		
		
		// read the instruction at the given line
		final String line = program.remove( idx );
		String instr = line.substring( 0, 3 ).toLowerCase( );
		final String[] args = line.substring( 4 ).split( " " );
		
		// check what to transform the instruction into
		if( args.length == 1 ) {
			instr = instr.equals( "inc" ) ? "dec" : "inc";
		} else if( args.length == 2 ) {
			instr = instr.equals( "jnz" ) ? "cpy" : "jnz";
		}
		
		// now modify the program
		final StringBuilder newline = new StringBuilder( instr );
		for( final String a : args ) newline.append(  " " + a );
		program.add( idx, newline.toString( ) );
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
