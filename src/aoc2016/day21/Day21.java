package aoc2016.day21;

import java.util.List;

import aocutil.io.FileReader;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day21.class.getResource( "day21_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day21.class.getResource( "day21_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, "abcde" ) );
		System.out.println( "Answer : " + part1( input, "abcdefgh" ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part2( input, "fbgdceah" ) );
	}
	
	/**
	 * Scrambles the key given the scrambling instructions
	 * 
	 * @param input The scrambling instructions
	 * @param key The key to scramble    
	 * @return The scrambled key
	 */
	private static String part1( final List<String> input, final String key ) {
		final PasswordScrambler pws = new PasswordScrambler( input );
		return pws.scramble( key );
	}
	
	/**
	 * Unscrambles the given password that was generated using the given
	 * instructions
	 * 
	 * @param input The set of scrambling instructions
	 * @param password The password to unscramble
	 * @return The key that was used to generate the password
	 */
	private static String part2( final List<String> input, final String password ) {
		final PasswordScrambler pws = new PasswordScrambler( input );
		return pws.unscramble( password );
	}
}
