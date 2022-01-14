package aoc2017.day16;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import aocutil.string.RegexMatcher;

/**
 * Class that facilitates the dancing of programs
 * 
 * @author Joris
 */
public class ProgramDance {
	/** The current program listing */
	private String listing;
	
	/**
	 * Creates a new program dance with initially the given number of programs in
	 * alphabetical orders, labelled from a to z
	 * 
	 * @param programs The number of programs
	 */
	public ProgramDance( final int programs ) {
		final StringBuilder s = new StringBuilder( );
		for( int i = 0; i < programs; i++ ) s.append( (char)('a' + i) );
		listing = s.toString( );
	}
	
	/**
	 * Performs the specified dance instructions and updates program listing
	 * accordingly
	 * 
	 * @param moves The dance move to process
	 * @return The resulting program listing
	 */
	public String dance( final Collection<String> moves, final long rounds ) {
		// keep dancing until we detect a cycle. If found we can skip a lot of
		// rounds
		final Map<String, Long> seen = new HashMap<>( );
		long cyclelen = -1;
		for( long i = 0; i < rounds; i++ ) {			
			if( cyclelen == -1 && seen.containsKey( listing ) ) {
				// found a cycle, increase current index until no more cycles can be
				// completed
				cyclelen = i - seen.get( listing );
				while( i < rounds - cyclelen ) i += cyclelen;
			}
			
			seen.put( listing, i );
			dance( moves );
		}
		
		return listing;
	}
	
	/**
	 * Performs one round of program dancing
	 * 
	 * @param moves The list of dance moves to perform
	 */
	private void dance( final Collection<String> moves ) {
		for( final String m : moves ) {
			RegexMatcher rm = new RegexMatcher( "s(\\d+)" );
			if( rm.match( m ) ) { spin( rm.getInt( 1 ) ); continue; }
			
			rm = new RegexMatcher( "x(\\d+)/(\\d+)" );
			if( rm.match( m ) ) { swap( rm.getInt( 1 ), rm.getInt( 2 ) ); continue; }

			rm = new RegexMatcher( "p(\\w)/(\\w)" );
			if( rm.match( m ) ) { swap( rm.get( 1 ).charAt( 0 ), rm.get( 2 ).charAt( 0 ) ); continue; }
			
			throw new RuntimeException( "Unsupported move: " + m );
		}
	}
	
	/**
	 * Spin, written sX, makes X programs move from the end to the front, but 
	 * maintain their order otherwise.
	 * 
	 * @param x The number of programs to move from the end to the front
	 */
	private void spin( final int x ) {
		final int y = listing.length( ) - x;
		listing = listing.substring( y ) + listing.substring( 0, y );
	}
	
	/**
	 * Exchange, written xA/B, makes the programs at positions A and B swap
	 * places.
	 * 
	 * @param a The first position
	 * @param b The second position
	 */
	private void swap( final int a, final int b ) {
		final char[] chars = listing.toCharArray( );
		final char swp = chars[a];
		chars[a] = chars[b];
		chars[b] = swp;
		listing = String.valueOf( chars );
	}
	
	/**
	 * Partner, written pA/B, makes the programs named A and B swap places
	 * 
	 * @param a The first program
	 * @param b The second program
	 */
	private void swap( final char a, final char b ) {
		swap( listing.indexOf( a ), listing.indexOf( b ) );
	}
	
	
	/** @return The current program listing */
	@Override
	public String toString( ) {
		return listing;
	}

}
