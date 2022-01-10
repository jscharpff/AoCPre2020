package aoc2016.day09;

import java.util.List;

import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "day09_example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day09.class.getResource( "day09_example2.txt" ) ).readLines( );
		final String input = new FileReader( Day09.class.getResource( "day09_input.txt" ) ).readAll( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Examples:" );
		for( final String s : ex_input ) 
			System.out.println( "> " + test1( s ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Examples:" );
		for( final String s : ex2_input ) 
			System.out.println( "> " + test2( s ) );
		System.out.println( "Answer : " + part2( input ) );
	}

	/**
	 * Tests the decompression of strings
	 * 
	 * @param input The input string
	 * @return A string that contains the input string, the output string and
	 *   its length
	 */
	private static String test1( final String input ) {
		final String d = Decompressor.decompress( input );
		return input + ": " + d + " (" + d.length( ) + ")";
	}

	/**
	 * Test that only computes the length of decompresses strings
	 * 
	 * @param input The input string
	 * @return The length of the string after decompression
	 */
	private static String test2( final String input ) {
		return input + ": " + Decompressor.getDecompressionLenght( input );
	}

	/**
	 * Determines the length of the given string after decompressing it
	 * 
	 * @param input The input string   
	 * @return The length of the decompressed string
	 */
	private static long part1( final String input ) {
		return Decompressor.decompress( input ).length( );
	}
	
	/**
	 * Determines he length of the given string after decompressing it but now
	 * also processes markers within decompressed data 
	 * 
	 * @param input The input string
	 * @return The length of the decompressed string
	 */
	private static long part2( final String input ) {
		return Decompressor.getDecompressionLenght( input );
	}	
}
