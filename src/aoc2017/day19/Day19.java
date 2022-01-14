package aoc2017.day19;

import java.util.List;

import aocutil.io.FileReader;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day19.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 20 ]---" );
		System.out.print( "Example: " + TubeMaze.fromStringList( ex_input ).getLength( ) + "\n\n" );
		System.out.print( "Answer : " + TubeMaze.fromStringList( input ).getLength( ) );
	}

}