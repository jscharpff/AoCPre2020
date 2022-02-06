package aoc2018.day19;

import java.util.ArrayList;
import java.util.List;

import aocutil.string.StringUtil;

/**
 * Slightly less simple assembly-like machine with 6 registers and support for
 * jumps via an instruction pointer register binding 
 * 
 * @author Joris
 */
public class OpCodeJumpMachine {
	/** Its registers */
	private final long[] mem;
	
	/** The current instruction pointer value */
	private int IP;
	
	/** The register that holds the current instruction pointer value */
	private int ip_register;
	
	/**
	 * Creates a new opcode machine
	 * 
	 * @param registers The number of memory registers
	 */
	public OpCodeJumpMachine( final int registers ) {
		mem = new long[ registers ];
		reset( );
	}
	
	/** Resets the machine to its initial state */
	public void reset( ) {
		ip_register = -1;
		IP = -1;
		for( int i = 0; i < mem.length; i++ ) mem[i] = 0;
	}
	
	/**
	 * Runs a program
	 * 
	 * @param input The list of instructions
	 */
	public void run( final List<String> input ) {
		final List<String> program = new ArrayList<>( );
		
		// first process declarations in the program
		for( final String s : input ) {
			if( s.startsWith( "#" ) ) declare( s );
			else program.add( s );
		}
		
		// setup instruction pointer
		IP = 0;
		while( IP >= 0 && IP < program.size( ) ) {
			if( ip_register != -1 ) write( ip_register, IP );
			execute( program.get( IP ) );
			if( ip_register != -1 ) IP = (int)read( ip_register );
			IP++;
		}
	}
	
	/**
	 * Processes a declaration command
	 * 
	 * @param decl The declaration
	 */
	private void declare( final String decl ) {
		final String[] d = decl.substring( 1 ).split( " " );
		
		switch( d[0] ) {
			case "ip":
				ip_register = Integer.parseInt( d[1] );
				break;
				
				default:
					throw new IllegalArgumentException( "Unsupported declaration: " + d[0] );
		}
	}
	
	/**
	 * Runs a single instruction from string
	 * 
	 * @param instr The instruction to run
	 */
	private void execute( final String instr ) {
		final String[] i = instr.split( " " );
		if( i.length != 4 ) throw new IllegalArgumentException( "Invalid instruction: " + instr );
		
		// get instruction 
		final String in = i[0].substring( 0, 4 );
		
		// parse arguments and target register
		final int dest = Integer.parseInt( i[3] );
		final long[] args = new long[] { Long.parseLong( i[1] ), Long.parseLong( i[2] ) };
		
		// now execute the instruction
		final long result;		
		switch( in ) {
			case "addi": case "addr": result = get( args[0], false ) + get( args[1], in.charAt( 3 ) == 'i' ); break;
			case "muli": case "mulr": result = get( args[0], false ) * get( args[1], in.charAt( 3 ) == 'i' ); break;
			case "bani": case "banr": result = get( args[0], false ) & get( args[1], in.charAt( 3 ) == 'i' ); break;
			case "bori": case "borr": result = get( args[0], false ) | get( args[1], in.charAt( 3 ) == 'i' ); break;
			case "seti": case "setr": result = get( args[0], in.charAt( 3 ) == 'i' ); break;
			
			case "gtir": case "gtri": case "gtrr": 
				result = get( args[0], in.charAt( 2 ) == 'i' ) > get( args[1], in.charAt( 3 ) == 'i' ) ? 1 : 0; break;
			
			case "eqir": case "eqri": case "eqrr":
				result = get( args[0], in.charAt( 2 ) == 'i' ) == get( args[1], in.charAt( 3 ) == 'i' ) ? 1 : 0; break;
			
			default: throw new IllegalArgumentException( "Unsupported instruction: " + in );
		}
		
		// and set the target value in the destination register
		write( dest, result );
	}
	
	/**
	 * Gets the value of an argument, either directly or from a register
	 * 
	 * @param value The value to parse
	 * @param immediate True for immediate mode, false for register
	 * @return The value or the value in the register indexed by the value if
	 *   immediate mode is true
	 */
	private long get( final long value, final boolean immediate ) {
		return immediate ? value : read( (int) value );
	}

	/**
	 * Reads a value from its registers
	 * 
	 * @param r The register index
	 * @return The value in the register
	 */
	public long read( final int r ) {
		if( r < 0 || r >= mem.length ) throw new IllegalArgumentException( "Invalid register index: " + r ); 
		return mem[ r ];
	}
	
	/**
	 * Writes a value to memory
	 * 
	 * @param r The register to write to
	 * @param value The value to write
	 */
	public void write( final int r, final long value ) {
		if( r < 0 || r >= mem.length ) throw new IllegalArgumentException( "Invalid register index: " + r ); 
		mem[ r ] = value;
	}
	
	/**
	 * Sets the values of all registers given the array of values
	 * 
	 * @param values The values to set, indexed in alignment with the memory
	 *   registers
	 */
	public void setMem( final long[] values ) {
		if( values.length != mem.length ) throw new IllegalArgumentException( "Invalid values array, length must equal the number of registers (" + mem.length + ")" );
		
		for( int i = 0; i < values.length; i++ )
			write( i, values[i] );
	}
	
	/** @return The contents of the memory as a string */
	@Override
	public String toString( ) {
		return "ip=" + IP + " [" + StringUtil.fromArray( mem ) + "]";
	}
	
	/** The list of all supported commands */
	public static final String[] SUPPORTED_INSTRUCTIONS = {
			"addi", "addr", "muli", "mulr", 
			"bani", "banr", "bori", "borr",
			"seti", "setr",
			"gtri", "gtir", "gtrr",
			"eqri", "eqir", "eqrr"
	};
}
