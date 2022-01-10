package aoc2016.day16;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = "10000";
		final String input = "10111011111001111";
		
		System.out.println( "---[ Day 16 ]---" );
		System.out.println( "Example: " + part1( ex_input, 20 ) );
		System.out.println( "Part 1 : " + part1( input, 272 ) );
		System.out.println( "Part 2 : " + part1( input, 35651584 ) );
	}
	
	/**
	 * Generates enough data to fill the required length and then computes the
	 * checksum of that data
	 * 
	 * @param input The input string to generate data from
	 * @param length The required length of the data   
	 * @return The checksum of the data
	 */
	private static String part1( final String input, final int length ) {
		return DragonChecksum.checkSum( DragonChecksum.generateData( input, length ) );
	}
}
