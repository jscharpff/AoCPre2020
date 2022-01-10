package aoc2016.day23;

import java.util.List;

import aocutil.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "day23_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day23.class.getResource( "day23_input.txt" ) ).readLines( );
		final List<String> input2 = new FileReader( Day23.class.getResource( "day23_input2.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input, 7 ) );
		System.out.println( "Answer : " + part1( input2, 12 ) );
	}
	
	/**
	 * Runs the specified program in AssemBunny on the safe computer
	 * 
	 * @param input The program
	 * @param R The optional program inputs   
	 * @return  The result stored in register a after all operations
	 */
	private static long part1( final List<String> input, final long... R ) {
		final AssemBunnySafe abs = new AssemBunnySafe( );
		abs.runProgram( input, R );
		return abs.read( "a" );
	}
}
