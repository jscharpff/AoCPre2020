package aoc2015.day23;

import java.util.List;

/**
 * A simple computer with 2 registers that can perform some simple arithmetics
 * and jumps 
 * 
 * @author Joris
 */
public class Computer {
	/** The current instruction pointer */
	private int IP;
	
	/** The two registers it has */
	private long a, b;
	
	/**
	 * Runs a program on the computer
	 * 
	 * @param program The program as a list of instruction strings
	 * @param ra The initial value for the a register
	 * @param rb The initial value for the b register 
	 */
	public void runProgram( final List<String> program, final long ra, final long rb ) {
		// initialise IP and registers to their specified values
		IP = 0;
		a = ra;
		b = rb;
		
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
		final String[] args = line.substring( 4 ).split( ", " );
		
		// run instruction
		switch( instr ) {
			// manipulations
			case "hlf": write( args[0], read( args[0] ) / 2 ); break;
			case "tpl": write( args[0], read( args[0] ) * 3 ); break;
			case "inc": write( args[0], read( args[0] ) + 1 ); break;

			// jumps
			case "jmp": jump( Integer.parseInt( args[0] ) ); break;
			case "jie": if( read( args[0] ) % 2 == 0 ) jump( Integer.parseInt( args[1] ) ); break;
			case "jio": if( read( args[0] ) == 1 ) jump( Integer.parseInt( args[1] ) ); break;
			
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
	 * @param r The register to read
	 * @return The value read from the register
	 */
	public long read( final String r ) {
		switch( r ) {
			case "a": return a;
			case "b": return b;
			default: throw new IllegalArgumentException( "Invalid register: " + r );
		}
	}
	
	/**
	 * Writes a value to the register
	 * 
	 * @param r The register to read
	 * @param v The value to write
	 */
	public void write( final String r, final long v ) {
		switch( r ) {
			case "a": a = v; break;
			case "b": b = v; break;
			default: throw new IllegalArgumentException( "Invalid register: " + r );
		}
	}

}
