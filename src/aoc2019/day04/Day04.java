package aoc2019.day04;

import aoc2019.day04.PasswordValidator.Version;

public class Day04 {
	
	/**
	 * Day 4 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] input = new int[] { 264360, 746325 };
	
		System.out.println( "---[ Day 4 ]---" );
		System.out.println( "Part 1 : " + countValid( input[0], input[1], Version.v1 ) );
		System.out.println( "Part 2 : " + countValid( input[0], input[1], Version.v2 ) );
	}
	
	/**
	 * Counts the number of valid passwords in the given range
	 *  
	 * @param min The minimal password value
	 * @param max The maximal password value
	 * @param version The version of the validator to use
	 * @return The number of valid passwords within the given range
	 */
	protected static long countValid( final int min, final int max, final Version version ) {
		final PasswordValidator pwd = new PasswordValidator( min, max, version );
		return pwd.countValid( );
	}
}
