package aoc2017.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day12.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the number of programs reachable from program 0
	 * 
	 * @param input The list of programs and their pipe links 
	 * @return The number of programs reachable from 0 via one or more pipes
	 */
	private static long part1( final List<String> input ) {
		final PipeGraph pg = PipeGraph.fromStringList( input );
		return pg.getReachable( 0 );
	}
	
	/**
	 * Determines the number of clusters in the pipe graph
	 * 
	 * @param input The description of programs and pipes 
	 * @return The number of non-connected clusters
	 */
	private static long part2( final List<String> input ) {
		final PipeGraph pg = PipeGraph.fromStringList( input );
		return pg.getNumClusters( );
	}
}
