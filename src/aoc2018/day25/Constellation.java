package aoc2018.day25;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.CoordND;

/**
 * A group of 4D coordinates that together form a constellation
 * 
 * @author Joris
 *
 */
public class Constellation {
	/*** The set of points in this constellation */
	private final Set<CoordND> points;
	
	/**
	 * Creates a  constellation with a single point
	 * 
	 * @param point The single point to add
	 */
	public Constellation( final CoordND point ) {
		points = new HashSet<>( );
		points.add( point );
	}
	
	/**
	 * Checks if the specified coordinate belongs to this constellation
	 * 
	 * @param point The point to check
	 * @return True iff there is at least one point in the constellation that is
	 *   at most 3 units away of the point
	 */
	private boolean canAdd( final CoordND point ) {
		return points.stream( ).anyMatch( p -> p.getManhattanDist( point ) <= 3 );
	}
	
	/** @return The size of the constellation */
	public int size( ) { return points.size( ); }
	
	/** @return The constellation as a string */
	@Override
	public String toString( ) {
		return "[" + size( ) + "] " + points;
	}
	
	/**
	 * Builds the list of all unique constellations from the given list of points
	 * 
	 * @param points The list of points
	 * @return The list of unique, non-overlapping constellations
	 */
	public static List<Constellation> fromPoints( final List<CoordND> points ) {
		final List<CoordND> p = new ArrayList<>( points );
		final List<Constellation> C = new ArrayList<>( );
		
		while( !p.isEmpty( ) ) {
			// pick a random point and build a constellation from there
			final Constellation c = new Constellation( p.remove( p.size( ) - 1 ) );
			
			// keep trying until the constellation does not grow
			int lastsize = 0;
			while( lastsize != c.size( ) ) {
				lastsize = c.size( );
				
				for( int i = p.size( ) - 1; i >= 0; i-- ) {
					final CoordND point = p.get( i );
					if( c.canAdd( point ) ) {
						c.points.add( point );
						p.remove( i );
					}
				}
			}
			
			// add it
			C.add( c );
		}
		
		return C;
	}
}