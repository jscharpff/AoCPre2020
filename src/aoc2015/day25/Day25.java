package aoc2015.day25;

public class Day25 {

	/**
	 * Day 25 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Answer: " + part1( 2049, 3029 ) );
	}
	
	/**
	 * The codex to Santa's trusty old weather machine that translates a given row
	 * and column number into a code to start the machine!
	 * 
	 * @param row The row number
	 * @param column The column number 
	 * @return The code to start Santa's weather machine
	 */
	private static long part1( final int row, final int column ) {
		// determine all numbers before me in the pyramid below the diagonal on
		// which the row,col combination reside using the sum of natural numbers
		final long n = row + column - 2;
		final long euler = n * (n + 1) / 2;
		
		// combine that with the elements before the row,col on the diagonal itself
		// to find the number of generated codes
		long codeindex = euler + column;
		
		// okay, now we know the number of iterations. Find the code
		long code = 20151125;
		while( --codeindex > 0 ) {
			code = code * 252533 % 33554393;
		}
		
		return code;
	}

}
