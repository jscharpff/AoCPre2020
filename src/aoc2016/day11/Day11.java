package aoc2016.day11;

import java.util.List;

import aocutil.io.FileReader;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day11.class.getResource( "day11_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day11.class.getResource( "day11_input.txt" ) ).readLines( );
		final List<String> input2 = new FileReader( Day11.class.getResource( "day11_input2.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + getMinimalAssemblageMoves( ex_input ) );
		System.out.println( "Answer : " + getMinimalAssemblageMoves( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + getMinimalAssemblageMoves( input2 ) );
	}
	
	/**
	 * Determines the minimal number of moves required to get all items to the
	 * assemblage floor
	 * 
	 * @param input The initial factory layout   
	 * @return The minimal number of moves required
	 */
	private static long getMinimalAssemblageMoves( final List<String> input ) {
		final RadioisotopeTestingFacility rtf = new RadioisotopeTestingFacility( input );
		return rtf.getMinimalMoves( );
	}
}
