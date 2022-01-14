package aoc2017.day17;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int ex_input = 3;
		final int input = 377;
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Lets the spinlock run rampage and fill its cyclic buffer writing data.
	 * From the resuling data we read the entry right after the one that holds
	 * 2017.
	 * 
	 * @param input The spinlock cycle length
	 * @return The value stored right after 2017
	 */
	private static long part1( final int input ) {
		final Spinlock s = new Spinlock( input );
		s.fillBuffer( 2017 );
		return s.getValue( s.find( 2017 ) + 1 );
	}
	
	/**
	 * Finds the second value in the spinlock memory banks after it has raged for
	 * 50 million cycles.
	 * 
	 * @param input The spinlock cycle length
	 * @return The value at index 1
	 */
	private static long part2( final int input ) {
		final Spinlock s = new Spinlock( input );
		return s.track( 50000000, 1 );
	}
}
