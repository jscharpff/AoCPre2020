package aoc2017.day14;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = "flqrgnkx";
		final String input = "vbqugkhl";
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the number of bits on the disk, generating the disk image from
	 * the knot hash key in the input
	 * 
	 * @param input The hash key to use in the knot hash algorithm  
	 * @return The number of set bits on the disk
	 */
	private static long part1( final String input ) {
		final Defragmenter d = new Defragmenter( input );
		return d.getDiskImage( ).count( true );
	}
	
	/**
	 * Determines the number of unique regions of adjacent true bits in the disk
	 * image 
	 * 
	 * @param input The hash key to produce the image of the disk
	 * @return The number of unique regions with true bits 
	 */
	private static long part2( final String input ) {
		final Defragmenter d = new Defragmenter( input );
		return d.getDiskRegions( d.getDiskImage( ) );
	}
}
