package aoc2018.day14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import aocutil.string.StringUtil;

/**
 * Mixer of recipe scores
 * 
 * @author Joris
 */
public class RecipeMixer {
	/** The current recipe score list */
	private final List<Integer> scores;
	
	/** The current positions of the elves */
	private final int[] elves;
	
	/**
	 * Creates a new recipe mixer
	 * 
	 * @param N the number of elves
	 */
	public RecipeMixer( final int N ) {
		this.elves = new int[ N ];
		for( int i = 0; i < N; i++ ) elves[ i ] = i;
		scores = new ArrayList<>( );
		scores.add( 3 );
		scores.add( 7 );
	}
	
	/**
	 * Generate the scores of N new recipes
	 * 
	 * @param N The number of new recipes
	 */
	public void mix( final int N ) {
		for( int i = 0; i < N; i++ ) {
			// add the new recipe scores
			final String newscore = "" + IntStream.of( elves ).map( e -> scores.get( e ) ).sum( );
			for( int j = 0; j < newscore.length( ); j++ ) scores.add( Integer.parseInt( newscore.substring( j, j+1 ) ) );
			
			// update positions of the elves
			for( int j = 0; j < elves.length; j++ ) elves[j] = (elves[j] + 1 + scores.get( elves[j] )) % scores.size( );
		}
	}
	
	/**
	 * Generate recipes until a certain set of scores occurs
	 * 
	 * @param score The scores to observe
	 * @return The number of recipes before the score was observed
	 */
	public long mixUntil( final String score ) {
		final int BATCH_SIZE = 10;
		for( long i = 0; i < 10000000; i++ ) {
			// mix 10 new recipes and check if the score is in the last generated score set
			final int currsize = scores.size( );
			mix( BATCH_SIZE );
			final int lastidx = Math.min( scores.size(), (scores.size( ) - currsize) + score.length( ) );
			final String last = getLast( lastidx );
			
			final int idx = last.indexOf( score );
			if( idx != -1 ) {
				return scores.size( ) - lastidx + idx;
			}
		}
		
		throw new RuntimeException( "Score not found!" );
	}

	/**
	 * Retrieves a number of recipes after the specified one
	 * 
	 * @param N The recipe to start after
	 * @param len The number of recipes to return 
	 * @return The recipes after N
	 */
	public String get( final int N, final int len ) {
		final StringBuilder sb = new StringBuilder( );
		for( int i = N; i < N + len; i++ )
			sb.append( scores.get( i) );
		
		return sb.toString( );
	}
	
	/**
	 * Retrieves the last N generated recipe scores
	 * 
	 * @param N The number of recipe scores to fetch
	 * @return The string containing the scores as digits
	 */
	public String getLast( int N ) {
		if( N > scores.size( ) ) N = scores.size( );
		return get( scores.size( ) - N, N );
	}
	
	/** @return The recipe scores */
	@Override
	public String toString( ) {
		return StringUtil.fromArray( elves ) + ": " + getLast( scores.size( ) );
	}
}
