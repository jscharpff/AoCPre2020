package aoc2017.day25;

import java.util.List;

import aocutil.io.FileReader;

public class Day25 {

	/**
	 * Day 25 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day25.class.getResource( "example.txt" ) ).readLineGroups( ";");
		final List<String> input = new FileReader( Day25.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	}
	
	/**
	 * Runs a turing machine to determine the checksum of the printer 
	 * 
	 * @param input The description of the turing machine 
	 * @return The checksum hat results after running the machine
	 */
	private static long part1( final List<String> input ) {
		final TuringMachine tm = new TuringMachine( input );
		tm.run( );
		return tm.checksum( );
	}
}
