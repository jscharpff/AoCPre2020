package aoc2018.day19;

import java.util.List;

import aocutil.io.FileReader;

public class Day19 {
	
	/**
	 * Day 19 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day19.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "TEST   : " + part2( 0 ) );
		System.out.println( "Part 2 : " + part2( 1 ) );
	}

	/**
	 * Runs the opcode machine on the specified input program that contains
	 * instructions that modify the instruction pointer
	 * 
	 * @param input The program to run
	 * @return The value of register 0 after completion
	 */
	protected static long part1( final List<String> input ) {
		final OpCodeJumpMachine oc = new OpCodeJumpMachine( 6 );
		oc.run( input );
		return oc.read( 0 );
	}
	
	/**
	 * Runs the same program as the input, but reverse engineered and
	 * reconstructed in more optimised Java code 
	 * 
	 * @param a The value of register 0 at start
	 * @return The value of register 0 at the end of the program
	 */
	protected static long part2( int a ) {
		long c = a == 1 ? 10551424 : 1024;
		a = 0;

		for( int b = 1; b <= c; b++ ) {
			for( int f = 1; f * b <= c; f++ ) {
				if( b * f == c ) a += b;
			}
		}
		
		return a;
	}

}
