package aoc2018.day21;

import java.util.List;

import aocutil.io.FileReader;

public class Day21 {
	
	/**
	 * Day 21 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> input = new FileReader( Day21.class.getResource( "input.txt" ) ).readLines( );
		
		// this time only one function, we simply run the program and inspect the
		// values in register 0 to find the instruction counts we need
		System.out.println( "---[ Day 21 ]---" );
		final OpCodeHacker h = new OpCodeHacker( input );
		final long[] halting = h.findHaltingValues( );
		
		System.out.println( "Part 1 : " + halting[0] );
		System.out.println( "Part 2 : " + halting[1] );
		
	}
	
}
