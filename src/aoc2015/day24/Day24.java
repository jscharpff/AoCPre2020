package aoc2015.day24;

import java.util.List;

import aocutil.io.FileReader;

public class Day24 {

	/**
	 * Day 24 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "day24_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day24.class.getResource( "day24_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + packPresents( ex_input, 3 ) );
		System.out.println( "Answer : " + packPresents( input, 3 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + packPresents( ex_input, 4 ) );
		System.out.println( "Answer : " + packPresents( input, 4 ) );
	}
	
	/**
	 * Optimised the packing of presents such that Santa sits nicely and the risk
	 * of "complications" is minimised. 
	 * 
	 * @param input The list of package weights to pack
	 * @param groups The number of groups to pack them into 
	 * @return The minimal quantum entanglement value as to minimise
	 *   the aforementioned complications 
	 */
	private static long packPresents( final List<String> input, final int groups ) {
		final SleighBalancer balancer = new SleighBalancer( input, groups );
		return balancer.optimiseQuantumEntanglement( );
	}
}
