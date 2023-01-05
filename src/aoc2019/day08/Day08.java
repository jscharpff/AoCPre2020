package aoc2019.day08;

import aocutil.grid.CoordGrid;
import aocutil.io.FileReader;

public class Day08 {
	
	/**
	 * Day 8 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String input = new FileReader( Day08.class.getResource( "input.txt" ) ).readAll( );
	
		System.out.println( "---[ Day 08 ]---" );
		System.out.println( "Part 1: " + part1( input ) );
		System.out.println( "Part 2: " );
		part2( input );
	}
	
	/**
	 * Finds the layer with the fewest zeroes and returns the product of number
	 * of ones and twos of that layer
	 *  
	 * @param input The image input data
	 * @return The product of numbers of ones and twos of the image layer that
	 *   has the fewest zeroes
	 */
	protected static long part1( final String input ) {
		final SpaceImage img = SpaceImage.fromString( input, 25, 6 );
		final CoordGrid<Integer> l = img.getLayerWithFewest( 0 );
		return l.count( 1 ) * l.count( 2 );
	}
	
	/**
	 * Flattens the image by merging layers, prints the result
	 * 
	 * @param input The image data 
	 */
	protected static void part2( final String input ) {
		final SpaceImage img = SpaceImage.fromString( input, 25, 6 );
		System.out.println( img.flatten( ).toString( x -> x == 1 ? "#" : "." ) );
	}

}
