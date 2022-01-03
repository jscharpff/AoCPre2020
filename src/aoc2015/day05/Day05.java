package aoc2015.day05;

import java.util.List;

import aocutil.io.FileReader;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day05.class.getResource( "day05_example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day05.class.getResource( "day05_example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day05.class.getResource( "day05_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + example( s, false ) );
		System.out.println( "Answer : " + part1( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex2_input )
			System.out.println( "Example: " + example( s, true ) );
		System.out.println( "Answer : " + part1( input, true ) );
	}
	
	private static String example( final String input, final boolean newcheck ) {
		final SantasStringChecker ssc = new SantasStringChecker( );
		final boolean nice = newcheck ? ssc.checkNew( input ) : ssc.check( input );
		return input + " = " + (nice ? "nice" : "naughty");		
	}
	
	/**
	 * 
	 * @param input 
	 * @return 
	 */
	private static long part1( final List<String> input, final boolean newcheck ) {
		final SantasStringChecker ssc = new SantasStringChecker( );
		long nicecount = 0;
		for( final String s : input )
			nicecount += (newcheck ? ssc.checkNew( s ) : ssc.check( s )) ? 1 : 0;
		return nicecount;
	}
}
