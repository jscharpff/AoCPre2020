package aoc2015.day13;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.collections.CollectionUtil;

/**
 * Class that helps optimise the seating arrangement at Christmas dinner based
 * upon the liking rating of guests
 * 
 * @author Joris
 */
public class SeatingArranger {
	/** The person to index mapping */
	private final Map<String, Integer> peoplemap;
	
	/** The liking matrix between any two people */
	private final int[][] D;
	
	/**
	 * Creates a new SeatingArranger
	 * 
	 * @param peoplemap The map of people to their matrix IDs
	 * @param likings The likings ratings that describes for every pair of IDs
	 *   the happiness modifier of seating them adjacently
	 */
	private SeatingArranger( final Map<String, Integer> peoplemap, final int[][] likings ) {
		this.peoplemap = peoplemap;
		this.D = likings.clone( );
	}
	
	/**
	 * Creates a new SeatingArranger for the given set of likings
	 * 
	 * @param likings The list of inter-people relationships, described by their
	 *   happiness modifiers
	 * @return The SeatingArranger from the input list
	 */
	public static SeatingArranger fromStringList( final List<String> likings ) {
		int mapidx = 0;
		final Map<String, Integer> peoplemap = new HashMap<>( );
		
		// first go over input and parse all guests
		final Pattern p = Pattern.compile( "(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)" );
		for( final String s : likings ) {
			final Matcher m = p.matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid likings string: " + s );
			
			if( !peoplemap.containsKey( m.group( 1 ) ) ) peoplemap.put( m.group( 1 ), mapidx++ );
			if( !peoplemap.containsKey( m.group( 4 ) ) ) peoplemap.put( m.group( 4 ), mapidx++ );
		}

		// then build their happiness matrix
		final int[][] D = new int[ peoplemap.size( ) ][ peoplemap.size( ) ];
		for( final String s : likings ) {
			final Matcher m = p.matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid likings string: " + s );

			final int from = peoplemap.get( m.group( 1 ) );
			final int to = peoplemap.get( m.group( 4 ) );
			final int d = Integer.parseInt( m.group( 3 ) ) * (m.group( 2 ).equals( "gain" ) ? 1 : -1);
			D[ from ][ to ] = d;
		}
		
		return new SeatingArranger( peoplemap, D );
	}
	
	/**
	 * Adds one more person to the seating that is neutral to everybody else
	 * 
	 * @return The new seating arranger that includes this person
	 */
	public SeatingArranger addNeutral( ) {
		final int N = peoplemap.size( );
		final Map<String, Integer> newmap = new HashMap<>( peoplemap );
		
		// copy likings array
		final int[][] D2 = new int[ N + 1][ N + 1 ];
		for( int i = 0; i < N; i ++ )
			for( int j = 0; j < N; j++ )
				D2[i][j] = D[i][j];
		
		// and add a new person
		newmap.put( "Me", N );
		
		return new SeatingArranger( newmap, D2 );
	}
	
	/**
	 * Determines the seating of guests that maximises the total happiness rating
	 * over all guests
	 * 
	 * @return The seating as a string person1 -> person2 -> ... = liking
	 */
	public String getBestSeating( ) {
		// get all permutations of cities
		final List<List<String>> seatings = CollectionUtil.generatePermutations( peoplemap.keySet( ) );
		
		// find the permutation that has the best liking rating
		List<String> bestseating = null;
		int maxliking = 0;
		for( final List<String> seating : seatings ) {
			// compute distance of route
			final int liking = liking( seating );
			if( liking > maxliking ) {
				maxliking = liking;
				bestseating = seating;
			}
		}

		// return it as a string with its liking
		return bestseating.stream( ).reduce(  "", (x,y) -> x + " -> " + y ).substring( 4 ) + ": " + maxliking;
	}
	
	/**
	 * Computes the total liking rating of a seating arrangement
	 * 
	 * @param seating The seat arrangement around the table 
	 * @return The total liking rating
	 */
	public int liking( final List<String> seating ) {
		int liking = 0;
		for( int i = 0; i < seating.size( ) - 1; i++ ) {
			liking += liking( seating.get( i ), seating.get( i + 1 ) );
		}
		
		// and add liking rating from last to first to close the loop
		liking += liking( seating.get( seating.size( ) - 1 ), seating.get( 0 ) );
		
		return liking;
	}
	
	/** 
	 * Returns the liking rating between two people
	 * 
	 * @param p1 The first person
	 * @param p2 The second person
	 * @return The liking rating between the two people
	 */
	public int liking( final String p1, final String p2 ) {
		return D[ peoplemap.get( p1 ) ][ peoplemap.get( p2 ) ] + D[ peoplemap.get( p2 ) ][ peoplemap.get( p1 ) ];
	}
	
	/** @return The matrix of likings */
	@Override
	public String toString( ) {
		String res = "";
		for( final String from : peoplemap.keySet( ) ) {
			res += "\n" + from + " ";
			for( final String to : peoplemap.keySet( ) ) {
				res += liking( from, to ) + " ";
			}
		}
		return res.substring( 1 );
	}

}
