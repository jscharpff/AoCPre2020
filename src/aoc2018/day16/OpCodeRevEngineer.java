package aoc2018.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import aocutil.string.RegexMatcher;

/**
 * Class that tries to reverse engineer the OpCodeMachien by inspecting samples
 * of input and outputs.
 * 
 * @author Joris
 */
public class OpCodeRevEngineer {
	/** The mapping of integer op codes to instructions */
	private final Map<Integer, String> instrmap;
	
	/**
	 * Creates a new reverse engineering program
	 */
	public OpCodeRevEngineer( ) {
		instrmap = new HashMap<>( );
	}
	
	/**
	 * Runs the reverse engineering process on the list of samples and updates
	 * the opcode map as a result of comparing the input and output of the sample
	 * 
	 * @param samples The list of samples, one per line
	 */
	public void reverseEngineer( final List<String> samples ) {
		// get potential mappings using the sample set 
		final Map<Integer, Set<String>> mapping = new HashMap<>( );
		IntStream.range( 0, 16 ).forEach( opcode -> {
			final Set<String> set = new HashSet<>( );
			for( final String op : OpCodeMachine.SUPPORTED_INSTRUCTIONS ) set.add( op );
			mapping.put( opcode, set );				
		} );

		for( final String s : samples ) {
			final int opcode = Integer.parseInt( s.split( ";" )[1].split( " " )[0] );
			mapping.get( opcode ).retainAll( reverseEngineer( s ) );
		}
		
		// and deduce actual instruction set by fixing instructions
		while( mapping.size( ) > 0 ) {
			// fix instructions that are certain and remove those from other sample
			// results
			final List<Integer> singles = new ArrayList<>( mapping.keySet( ).stream( ).filter( k -> mapping.get( k ).size( ) == 1 ).toList( ) );
			
			for( final int opcode : singles ) {
				final String instr = mapping.get( opcode ).iterator( ).next( );
				instrmap.put( opcode, instr );
				mapping.remove( opcode );				
				
				for( final Set<String> maps : mapping.values( ) ) {
					maps.remove( instr );
				}
			}
		}
	}
	
	/**
	 * Reverse engineers one sample and returns all instructions for which the
	 * sample input and output agrees
	 * 
	 * @param sample The sample to inspect
	 * @return The list of instructions that agree with the sample
	 */
	protected List<String> reverseEngineer( final String sample ) {
		// extract elements from the sample
		final String[] s = sample.split( ";" );
		final long[] before = extractMem( s[0] );
		final long[] after = extractMem( s[2] );
		final String args = s[1].substring( s[1].indexOf( " " ) + 1 );
		
		// now run all of the commands and compare its output against the sample
		final List<String> matches = new ArrayList<>( );
		final OpCodeMachine machine = new OpCodeMachine( 4 );
		for( final String inst : OpCodeMachine.SUPPORTED_INSTRUCTIONS ) {
			machine.setMem( before );
			machine.execute( inst + " " + args );
			
			// compare all memory values
			boolean memequals = true;
			for( int i = 0; i < after.length; i++ )
				memequals &= machine.read( i ) == after[i];
			
			if( memequals ) matches.add( inst );
		}

		// return matches for this sample
		return matches;
	}
	
	/**
	 * Extracts the values from the string describing the memory state
	 * 
	 * @param input The string describing the memory state
	 * @return The array of values that correspond to the memory
	 */
	private long[] extractMem( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "\\[#D, #D, #D, #D\\]", input );
		return new long[] { rm.getLong( 1 ), rm.getLong( 2 ), rm.getLong( 3 ), rm.getLong( 4 ) };
	}
	
	/**
	 * Transforms a program of integer opcode to their string equivalents
	 * 
	 * @param intprog The integer code program
	 * @return A new program with string instructions
	 */
	public List<String> transform( final List<String> intprog ) {
		final List<String> program = new ArrayList<>( intprog.size( ) );
		for( final String in : intprog ) {
			final int opcode = Integer.parseInt( in.split( " " )[0] );
			if( !instrmap.containsKey( opcode ) ) throw new RuntimeException( "No value for opcode " + opcode );
			program.add( instrmap.get( opcode ) + in.substring( in.indexOf( " " ) ) );
		}
		return program;
	}
	
	/** @return The current mapping of opcode to instructions */
	@Override
	public String toString( ) {
		return instrmap.toString( );
	}
}
