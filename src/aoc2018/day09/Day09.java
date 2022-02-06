package aoc2018.day09;

import java.util.List;

import aocutil.io.FileReader;
import aocutil.string.RegexMatcher;

public class Day09 {
	
	/**
	 * Day 9 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "example.txt" ) ).readLines( );
		final String input = new FileReader( Day09.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		final String input2 = new FileReader( Day09.class.getResource( "input2.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Day 9 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example (" + s + "): " + playMarbles( s ) );
		System.out.println(  );
		System.out.println( "Part 1 : " + playMarbles( input ) );
		System.out.println( "Part 2 : " + playMarbles( input2 ) );
	}

	/**
	 * Plays the game of marbles using the input string to determine the number
	 * of players and marbles to place
	 * 
	 * @param input The input string that describes the game context
	 * @return The score of the winning player
	 */
	protected static long playMarbles( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "#D players; last marble is worth #D points", input );
		final MarbleGame mg = new MarbleGame( rm.getInt( 1 ) );
		mg.play( rm.getInt( 2 ) );
		return mg.getWinningScore( );
	}
}
