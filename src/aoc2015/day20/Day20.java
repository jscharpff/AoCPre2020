package aoc2015.day20;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Answer : " + part1( 36000000 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part2( 36000000 ) );
	}
	
	/**
	 * Determines the first house to receive at least the input number of
	 * presents, where elves visit an infinite number of houses
	 * 
	 * @param input The number of presents that we are looking for
	 * @return The number of the first house that received the number of presents
	 */
	private static long part1( final long input ) {
		return PresentDelivery.getFirstHouseToGet( input, -1 );
	}
	
	/**
	 * Same as part 1 but now the elves visit exactly 50 houses instead of an
	 * infinite number
	 * 
	 * @param input The number of presents we are looking for
	 * @return The number of the first house that received the number of presents,
	 *   where now elves only visit 50 houses
	 */
	private static long part2( final long input ) {
		return PresentDelivery.getFirstHouseToGet( input, 50 );
	}
}
