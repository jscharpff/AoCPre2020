package aoc2016.day03;

import java.util.List;

import aocutil.io.FileReader;
import aocutil.string.RegexMatcher;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day03.class.getResource( "day03_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day03.class.getResource( "day03_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Counts the number of valid triangles in the input
	 * 
	 * @param input The list of triangle sides, three per string
	 * @return The count of valid triangles
	 */
	private static long part1( final List<String> input ) {
		int count = 0;
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "^\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*$", s );
			int[] t = rm.getInts( );
			count += (t[0] + t[1] > t[2] && t[0] + t[2] > t[1] && t[1]+ t[2] > t[0] ) ? 1 : 0;
		}
		return count;
	}
	
	/**
	 * Again counts the number of valid triangles in the input, but now the
	 * triangles are specified column wise. That is, every three subsequent rows
	 * in the same column correspond to one triangle.
	 * 
	 * @param input The list of triangle sides, now column wise
	 * @return The count of valid triangles
	 */
	private static long part2( final List<String> input ) {
		// we need to transform the data into an array of triangle sides that
		// can be processed linearly
		final int N = input.size( );
		final int cols = 3;
		final int[] triangledata = new int[ N * cols ];

		// parse data into triangle data array 
		int row = 0;
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "^\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*$", s );

			int[] t = rm.getInts( );
			for( int i = 0; i < cols; i++ )
				triangledata[N * i + row] = t[i];
			row++;
		}
		
		// then validate triangles in sets of three subsequent numbers
		int count = 0;
		for( int i = 0; i < triangledata.length; i += 3 )
			count += (triangledata[i] + triangledata[i+1] > triangledata[i+2] && 
					triangledata[i] + triangledata[i+2] > triangledata[i+1] &&
					triangledata[i+1]+ triangledata[i+2] > triangledata[i] ) ? 1 : 0;
		return count;
	}
}
