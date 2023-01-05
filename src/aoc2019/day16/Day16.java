package aoc2019.day16;

import aocutil.io.FileReader;

public class Day16 {
	
	/**
	 * Day 16 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day16.class.getResource( "example.txt" ) ).readAll( ); 
		final String ex2_input = new FileReader( Day16.class.getResource( "example2.txt" ) ).readAll( ); 
		final String input = new FileReader( Day16.class.getResource( "input.txt" ) ).readAll( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 4 ) );
		System.out.println( "Example: " + part1( ex2_input, 100 ) );
		System.out.println( "Part 1 : " + part1( input, 100 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Computes the signal after applying the number of phases of the FTT
	 * algorithm to it
	 *   
	 * @param input The input signal
	 * @param phases The number of FTT phases to run 
	 * @return The first 8 digits of the transformed signal 
	 */
	protected static String part1( final String input, final int phases ) {
		return FFT.transform( input, phases ).substring( 0, 8 );
	}
	
	/**
	 * Retrieves the hidden message that is encoded in the message after
	 * repeating it a 1000 times and applying 100 phases
	 * 
	 * @param input The input signal 
	 * @return The hidden message in the transformed result
	 */
	protected static String part2( final String input ) {
		return FFT.decode( input, 10000, 100 );
	}

}
