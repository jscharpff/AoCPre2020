package aoc2018.day16;

import java.util.List;

import aocutil.io.FileReader;

public class Day16 {
	
	/**
	 * Day 16 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day16.class.getResource( "example.txt" ) ).readLineGroups( ";" );
		final List<String> input = new FileReader( Day16.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		final List<String> input2 = new FileReader( Day16.class.getResource( "input2.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 16 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Part 2 : " + part2( input, input2 ) );
	}

	/**
	 * Runs the samples through the reverse engineering process and counts the
	 * number of samples that correspond to 3 or more instructions
	 * 
	 * @param input The set of samples
	 * @return The number of samples that agree with 3 or more instructions
	 */
	protected static long part1( final List<String> input ) {
		final OpCodeRevEngineer re = new OpCodeRevEngineer( );
		int count = 0;
		for( final String s : input ) {
			count += re.reverseEngineer( s ).size( ) >= 3 ? 1 : 0;
		}
		return count;
	}

	/**
	 * Reverse engineers the mapping of integer opcode to the string equivalent
	 * using the sample set, then transforms the integer-based program into its
	 * string-based equivalent and runs it.
	 * 
	 * @param samples The sample set to use in reverse engineering process
	 * @param intprog The integer-based program to run
	 * @return The contents in register 0 after execution of the program
	 */
	protected static long part2( final List<String> samples, final List<String> intprog ) {
		// first reverse engineer the opcodes
		final OpCodeRevEngineer re = new OpCodeRevEngineer( );
		re.reverseEngineer( samples );
		
		// then run the transformed program and get the result
		final OpCodeMachine machine = new OpCodeMachine( 4 );
		machine.run( re.transform( intprog ) );
		return machine.read( 0 );
	}
}
