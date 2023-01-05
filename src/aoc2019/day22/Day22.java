package aoc2019.day22;

import java.util.List;

import aocutil.io.FileReader;

public class Day22 {
	
	/**
	 * Day 22 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day22.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 10, 3 ) );
		System.out.println( "Part 1 : " + part1( input, 10007, 2019 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Part 2 : " + part2( input, 119315717514047l, 101741582076661l ) );
	}

	
	/**
	 * Shuffles a deck of space cards once and returns the position of the card
	 * with the specified face value
	 * 
	 * @param input The shuffling instructions
	 * @param decksize The size of the deck to shuffle
	 * @param card The card we want to know the final position of
	 * @return The position of the card after shuffling
	 */
	protected static long part1( final List<String> input, final int decksize, final int card ) {
		final SpaceDeck d = new SpaceDeck( decksize, input );
		return d.shuffle( card, 1, false );
	}
	
	/**
	 * Finds out the card that ends up in position 2020 after shuffling the deck
	 * for the specified amount of times. This is performed by executing the
	 * shuffling in reverse for the given number of times while keeping track of
	 * the position of the card that was in position 2020 after all shuffles 
	 *   
	 * @param input The shuffling instructions
	 * @param decksize The size of the deck of space cards
	 * @param shuffles The number of shuffles to perform 
	 * @return The position of the card (which is equal to its face value when
	 *   starting the shuffling) that will end up in position 2020 after applying
	 *   the shuffling for n times
	 */
	protected static long part2( final List<String> input, final long decksize, final long shuffles ) {
		final SpaceDeck d = new SpaceDeck( decksize, input );
		return  d.shuffle( 2020, shuffles, true );
	}

}
