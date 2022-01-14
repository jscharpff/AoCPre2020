package aoc2017.day03;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int input = 361527;
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + SpiralMemory.getDistance( 1024 ) );
		System.out.println( "Answer : " + SpiralMemory.getDistance( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + SpiralMemory.getFirstLarger( 120 ) );
		System.out.println( "Answer : " + SpiralMemory.getFirstLarger( input ) );
	}
}
