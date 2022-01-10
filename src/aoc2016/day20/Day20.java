package aoc2016.day20;

import java.util.List;

import aocutil.io.FileReader;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "day20_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day20.class.getResource( "day20_input.txt" ) ).readLines( );
		
		final long MAX_IP = 4294967295l;
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 9 ) );
		System.out.println( "Answer : " + part1( input, MAX_IP ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 9 ) );
		System.out.println( "Answer : " + part2( input, MAX_IP ) );
	}
	
	/**
	 * Finds the first IP that is not blocked by any fire wall rule
	 *  
	 * @param input The ruleset of the firewall
	 * @param maxIP The maximal IP address that is considered   
	 * @return The first available IP address
	 */
	private static long part1( final List<String> input, final long maxIP ) {
		final Firewall FW = new Firewall( input, maxIP );
		return FW.getFirstAllowedIP( );
	}
	
	/**
	 * Finds amount of IPs not blocked by the firewall
	 *  
	 * @param input The ruleset of the firewall
	 * @param maxIP The maximal IP address that is considered   
	 * @return The number of available IP Addresses
	 */
	private static long part2( final List<String> input, final long maxIP ) {
		final Firewall FW = new Firewall( input, maxIP );
		return FW.getAvailableIPs( );
	}
}
