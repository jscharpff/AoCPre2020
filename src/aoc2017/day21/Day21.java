package aoc2017.day21;

import java.util.List;

import aocutil.io.FileReader;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day21.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day21.class.getResource( "input.txt" ) ).readLines( );

		System.out.println( "---[ Day 21 ]---" );
		System.out.println( "Example: " + part1( ex_input, 2 ) );
		System.out.println( "Part 1 : " + part1( input, 8 ) );
		System.out.println( "Part 2 : " + part2( input, 18 ) );

	}
	
	/**
	 * Determines the number of pixels in the image after several iterations of
	 * the art generation algorithm
	 * 
	 * @param input The fractal ruleset
	 * @param iterations The number of iterations to run the algorithm
	 * @return The number of pixels in the resulting image
	 */
	private static long part1( final List<String> input, final int iterations ) {
		final FractalArt art = new FractalArt( input );
		art.generate( iterations );
		return art.getImage( ).count( true );
	}
	
	/**
	 * Determines the number of pixels in the image after several iterations of
	 * the art generation algorithm, this time without actually generating the
	 * full image.
	 * 
	 * @param input The fractal ruleset
	 * @param iterations The number of iterations to run the algorithm
	 * @return The number of pixels in the resulting image
	 */
	private static long part2( final List<String> input, final int iterations ) {
		final FractalArt art = new FractalArt( input );
		return art.countPixels( iterations );
	}	
}
