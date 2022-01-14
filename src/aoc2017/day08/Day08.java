package aoc2017.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day08.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + runProgram( ex_input, true ) );
		System.out.println( "Answer : " + runProgram( input, true ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + runProgram( ex_input, false ) );
		System.out.println( "Answer : " + runProgram( input, false ) );
	}
	
	/**
	 * Runs the conditional code on the machine and returns the highest resulting
	 * register value or all-time highest register value
	 * 
	 * @param input The conditional program
	 * @param highestout True to return highest value in outcome, false for all-
	 *   time highest value in memory 
	 * @return The highest register value in the outcome or all-time
	 */
	private static long runProgram( final List<String> input, final boolean highestout ) {
		final ConditionalMachine cm = new ConditionalMachine( );
		cm.run( input );
		return cm.getMaxRegisterValue( highestout );
	}
}
