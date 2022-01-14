package aoc2017.day13;

import java.util.List;

import aocutil.io.FileReader;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day13.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Traces a packet through the firewall and returns the severity score
	 * 
	 * @param input The list of firewall layers 
	 * @return The severity score of the packet
	 */
	private static long part1( final List<String> input ) {
		final Firewall fw = Firewall.fromStringList( input );
		return fw.trace( );
	}
	
	/**
	 * Determines the first time at which a packet may pass through the firewall
	 * without being detected in any of its layers
	 * 
	 * @param input The firewall layers 
	 * @return The first time at which the packet may safely pass
	 */
	private static long part2( final List<String> input ) {
		final Firewall fw = Firewall.fromStringList( input );
		for( int i = 0; i < 10000000; i++ )
			if( fw.passes( i ) ) return i;		
		
		throw new RuntimeException( "Failed to find solution" );
	}
}
