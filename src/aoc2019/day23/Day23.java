package aoc2019.day23;

import aoc2019.day23.network.ICNetwork;
import aocutil.io.FileReader;

public class Day23 {
	
	/**
	 * Day 23 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String input = new FileReader( Day23.class.getResource( "input.txt" ) ).readAll( );
	
		System.out.println( "---[ Day 23 ]---" );
		System.out.println( "Part 1: " + part1( input, true ) );
		System.out.println( "Part 2: " + part1( input, false ) );
	}
	
	/**
	 * Sets up a network of 50 programs that communicate using packets. The
	 * network will continue transmission until either the first broadcast packet
	 * is received or until the NAT that receives such broadcasts sends the same
	 * Y value twice.
	 *   
	 * @param input The program to run by the network peers
	 * @param stop True to stop on receiving the first broadcast message, false
	 *   to stop when the NAT sends the same Y value twice
	 * @return Either the Y value of the first broadcast message (if stop is
	 *   true), or the first Y value that is sent twice by the NAT
	 */
	protected static long part1( final String input, final boolean stop ) throws Exception {
		final ICNetwork n = new ICNetwork( 50, input );
		return n.run( stop );
	}

}
