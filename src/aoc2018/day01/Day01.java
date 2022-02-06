package aoc2018.day01;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import aocutil.io.FileReader;

public class Day01 {
	/**
	 * Day 1 of the AoC 2018 event
	 * 
	 * https://adventofcode.com/2018/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception 
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new int[ ] { +1, -2, +3, +1 };
		final int[] input = new FileReader( Day01.class.getResource( "input.txt" ) ).readIntArray( ); 
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );		
		
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );				
	}
	
	/**
	 * Part 1: sum frequencie differences to get resulting frequency displacement
	 * 
	 * @param df The frequency differences
	 * @return The resulting frequencyy
	 */
	protected static int part1( final int[] df ) {
		return IntStream.of( df ).sum( );
	}
	
	/**
	 * Return the first frequency that it encounters twice
	 * 
	 * @param df The frequency differences
	 * @return The first frequency that occurs twice
	 */
	protected static int part2( final int[] df ) {
		final Set<Integer> seen = new HashSet<Integer>( );
		
		int freq = 0;
		int i = 0;
		while( true ) {
			final int d = df[i]; 
			if( seen.contains( freq ) ) return freq;
			seen.add( freq );
			freq += d;
			i = (i + 1) % df.length;
		}
	}
}
