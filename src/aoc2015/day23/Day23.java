package aoc2015.day23;

import java.util.List;

import aocutil.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "day23_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day23.class.getResource( "day23_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + runComputer( ex_input, 0 ) );
		System.out.println( "Part 1 : " + runComputer( input, 0 ) );
		System.out.println( "Part 2 : " + runComputer( input, 1 ) );
	}
	
	/**
	 * Runs the computer with the specified program listing as its input
	 * 
	 * @param input The program
	 * @param ra The initial value for the a register 
	 * @return The value of register b after the program has terminated
	 */
	private static long runComputer( final List<String> input, final long ra ) {
		final Computer c = new Computer( );
		c.runProgram( input, ra, 0 );
		return c.read( "b" );
	}
}
