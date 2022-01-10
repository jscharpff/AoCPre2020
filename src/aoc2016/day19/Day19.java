package aoc2016.day19;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + WhiteElephantParty.playSimple( 5 ) );
		System.out.println( "Answer : " + WhiteElephantParty.playSimple( 3017957 ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + WhiteElephantParty.playComplex( 5 ) );
		System.out.println( "Answer : " + WhiteElephantParty.playComplex( 3017957 ) );
	}
}
