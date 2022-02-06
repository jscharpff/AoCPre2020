package aoc2018.day09;

import java.util.stream.LongStream;

/**
 * A game of marbles!
 * 
 * @author Joris
 */
public class MarbleGame {
	/** The number of players participating */
	private int N;
	
	/** The player scores */
	private long[] score;
	
	/** The marble circle */
	private MarbleCircle marbles;

	/**
	 * Creates a new MarbleGame
	 * 
	 * @param players The number of players
	 */
	public MarbleGame( final int players ) {
		this.N = players;
		score = new long[ N ];
	}
	
	/**
	 * Plays the MarbleGame
	 * 
	 * @param rounds The number of rounds to play
	 */
	public void play( final int rounds ) {
		marbles = new MarbleCircle( );
		int pidx = -1;
		
		for( int i = 1; i <= rounds; i++ ) {
			// new turn, new player
			pidx = (pidx + 1) % N;

			// every 23rd marble is not placed and the marble 7 positions counter-
			// clockwise is also removed and added to the current player's score
			if( i % 23 == 0 ) {
				score[ pidx ] += i;
				marbles.move( -7 );
				score[ pidx ] += marbles.remove( );
				continue;
			}
			
			// simply place a new marble
			marbles.move( 1 );
			marbles.add( i );
		}
	}
	

	/** @return The winning score */
	public long getWinningScore( ) {
		return LongStream.of( score ).max( ).getAsLong( );
	}
}
