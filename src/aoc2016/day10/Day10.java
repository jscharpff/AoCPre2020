package aoc2016.day10;

import java.util.List;

import aocutil.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "day10_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day10.class.getResource( "day10_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, new int[] { 2, 5 } ) );
		System.out.println( "Answer : " + part1( input, new int[] { 17, 61 } ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sends a list of commands to the factory bots and finds the ID of the bot
	 * that is handling the specified chip numbers
	 * 
	 * @param input The list of commands
	 * @param chips The array of chip numbers to find the bot responsible for
	 * @return The ID of the bot that handles the chip IDs
	 */
	private static long part1( final List<String> input, final int[] chips ) {
		final FactoryBots fb = new FactoryBots( );
		fb.process( input );
		
		// find the bot responsible for comparing the chips
		return fb.getBotHandling( chips[0], chips[1] );
	}
	
	/**
	 * Processes the list of commands again but now returns the product of the
	 * first three output bins
	 * 
	 * @param input The list of bot commands   
	 * @return The product of the chip values in the first three
	 */
	private static long part2( final List<String> input ) {
		final FactoryBots fb = new FactoryBots( );
		fb.process( input );		
		return fb.getOutputs( 0, 1, 2 ).stream( ).reduce( Math::multiplyExact ).get( );
	}
}
