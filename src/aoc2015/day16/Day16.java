package aoc2015.day16;

import java.util.List;

import aocutil.io.FileReader;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> input = new FileReader( Day16.class.getResource( "day16_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Answer : " + findAuntSue( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + findAuntSue( input, false ) );
	}
	
	/**
	 * Finds the Aunt sue that sent a gift based upon the properties that are
	 * known about her 
	 * 
	 * @param input The list of all known aunts Sue
	 * @param old True to use the older version of the machine, false for new  
	 * @return The aunt sue that meets the given criteria
	 */
	private static String findAuntSue( final List<String> input, final boolean old ) {
		final MFCSAM kit = new MFCSAM( );
		kit.addAll( input );
		
		kit.filter( "children: 3, cats: 7, samoyeds: 2, pomeranians: 3, akitas: 0, vizslas: 0, goldfish: 5, trees: 3, cars: 2, perfumes: 1", old );
		return kit.toString( );
	}

}
