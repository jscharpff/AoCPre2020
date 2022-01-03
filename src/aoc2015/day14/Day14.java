package aoc2015.day14;

import java.util.List;

import aocutil.io.FileReader;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day14.class.getResource( "day14_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day14.class.getResource( "day14_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 1000 ) );
		System.out.println( "Answer : " + part1( input, 2503 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 1000 ) );
		System.out.println( "Answer : " + part2( input, 2503 ) );
	}
	
	/**
	 * Starts a reindeer race with the competitors as described in the input and
	 * returns the winning distance after having raced for the given time
	 * 
	 * @param input The reindeer competing in the race
	 * @param racetime The duration of the race
	 * @return The distance that the winning reindeer has traversed
	 */
	private static long part1( final List<String> input, final int racetime ) {
		final ReindeerRace race = ReindeerRace.fromStringList( input );
		return race.getWinningDistance( racetime );
	}
	
	/**
	 * Starts a reindeer race with the competitors as described in the input and
	 * this time returns the reindeer that accumulated the most points by being
	 * in the lead for the most time during the race
	 * 
	 * @param input The strings describing the competitors
	 * @param racetime The time duration of the race
	 * @return The maximum number of points that were accumulated by the winning
	 *   reindeer.
	 */
	private static long part2( final List<String> input, final int racetime ) {
		final ReindeerRace race = ReindeerRace.fromStringList( input );
		return race.getWinningPoints( racetime );
	}
}
