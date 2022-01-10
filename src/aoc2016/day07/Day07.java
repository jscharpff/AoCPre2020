package aoc2016.day07;

import java.util.List;

import aocutil.io.FileReader;

public class Day07 {

	/**
	 * Day 7 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "day07_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "day07_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Count the number of IPv7 addresses that support Transport-Layer Snooping
	 * 
	 * @param input The list of IPv7 addresses
	 * @return The number of addresses that support TLS
	 */
	private static long part1( final List<String> input ) {
		return input.stream( ).map( IPv7::new ).filter( IPv7::supportsTLS ).count( );
	}

	/**
	 * Count the number of IPv7 addresses that support Super-Secret Listening
	 * 
	 * @param input The list of IPv7 addresses
	 * @return The number of addresses that support SSL
	 */
	private static long part2( final List<String> input ) {
		return input.stream( ).map( IPv7::new ).filter( IPv7::supportsSSL ).count( );
	}
}
