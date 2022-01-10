package aoc2016.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "day12_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day12.class.getResource( "day12_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 12 ]---" );
		System.out.println( "Example: " + runProgram( ex_input ) );
		System.out.println( "Part 1 : " + runProgram( input ) );
		System.out.println( "Part 2 : " + runProgram( input, 0, 0, 1 ) );
	}
	
	/**
	 * Runs the specified program on an AssemBunny PC
	 * 
	 * @param input The program instructions
	 * @param R The initial register values for the program   
	 * @return The contents of register a at the end of the program
	 */
	private static long runProgram( final List<String> input, final long... R ) {
		final AssemBunnyPC pc = new AssemBunnyPC( );
		pc.runProgram( input, R );
		return pc.read( "a" );
	}
}
