package aoc2015.day02;

import java.util.Arrays;
import java.util.List;

import aocutil.io.FileReader;
import aocutil.string.RegexMatcher;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "day02_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "day02_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Computes the area of wrapping paper required to wrap all the gifts in the
	 * input list
	 * 
	 * @param input List of present sizes formatted as HxWxD 
	 * @return The total wrapping paper required
	 */
	private static long part1( final List<String> input ) {
		long wrapping = 0;
		
		// parse each present's dimensions
		for( final String s : input ) {
			// try and match the string
			final RegexMatcher rm = RegexMatcher.match( "(\\d+)x(\\d+)x(\\d+)", s );
			
			// read all dimensions and sort by size
			final int[] dim = rm.getInts( );
			Arrays.sort( dim );
			
			// add the area of the box to the required wrapping paper
			wrapping += 2 * (dim[0] * dim[1] + dim[0] * dim[2] + dim[1] * dim[2]);
			
			// and add some slack
			wrapping += dim[0] * dim[1]; 
		}
		
		return wrapping;
	}
	
	/**
	 * Computes the length of ribbon paper required to wrap all the gifts in the
	 * input list
	 * 
	 * @param input List of present sizes formatted as HxWxD 
	 * @return The total wrapping paper required
	 */
	private static long part2( final List<String> input ) {
		long ribbon = 0;
		
		// parse each present's dimensions
		for( final String s : input ) {
			// try and match the string
			final RegexMatcher rm = RegexMatcher.match( "(\\d+)x(\\d+)x(\\d+)", s );
			
			// read all dimensions and sort by size
			final int[] dim = rm.getInts( );
			Arrays.sort( dim );
			
			// add the smallest length of wrapping paper to add
			ribbon += Math.min( 2 * (dim[0] + dim[1]), Math.min( 2 * (dim[0] + dim[2]), 2 * (dim[1] + dim[2] )) );
			
			// and add some slack
			ribbon += dim[0] * dim[1] * dim[2]; 
		}
		
		return ribbon;
	}	
}
