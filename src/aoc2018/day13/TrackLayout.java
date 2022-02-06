package aoc2018.day13;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Class that models a set of tracks and carts moving along them
 * 
 * @author Joris
 */
public class TrackLayout {
	/** The grid that contains the corners and intersections of the track */
	private final CoordGrid<Character> tracks;
	
	/** The positions of the carts */
	private final List<Cart> carts;
	
	/**
	 * Creates a new track layout
	 * 
	 * @param tracks The grid of corners and intersections
	 * @param carts The cart positions
	 */
	private TrackLayout( final CoordGrid<Character> tracks, final Collection<Cart> carts ) {
		this.tracks = tracks.copy( );
		this.carts = new ArrayList<>( carts );
	}
	
	/** 
	 * Retrieves a track element
	 * 
	 * @param coord The coordinate of the element
	 * @return The track element at the given position
	 */
	protected char get( final Coord2D coord ) {
		return tracks.get( coord );
	}
	
	/**
	 * Moves the carts until a collision occurs
	 * 
	 * @return The coordinate at which the first collision occurs
	 */
	public Coord2D moveUntilCollision( ) {
		Map<Coord2D, List<Cart>> collisions = new HashMap<>( );
		while( collisions.size( ) == 0 ) {
			collisions = move( );
		}
		return collisions.keySet( ).iterator( ).next( );
	}

	/**
	 * Moves the carts until all but one remain
	 * 
	 * @return The coordinate at which the first collision occurs
	 */
	public Coord2D moveUntilAllCollided( ) {
		Map<Coord2D, List<Cart>> collisions = new HashMap<>( );
		while( carts.size( ) > 1 ) {
			collisions = move( );
			collisions.values( ).stream( ).forEach( v -> carts.removeAll( v ) );
		}
		return carts.get( 0 ).getPosition( );
	}
	
	/**
	 * Processes a single move of all carts
	 * 
	 * @return Coordinates at which collisions occur and the carts involved
	 */
	public Map<Coord2D, List<Cart>> move( ) {
		// sort on Y and then X position
		carts.sort( (c1,c2) -> c1.getPosition( ).compareTo( c2.getPosition( ) ) );
		
		// and process the moves in this order
		final Map<Coord2D, List<Cart>> collisions = new HashMap<>( );
		for( final Cart c : carts ) {
			c.move( this );
			if( carts.stream( ).map( c1 -> c1.getPosition( ) ).filter( c1 -> c1.equals( c.getPosition( ) ) ).count( ) > 1 ) {
				final List<Cart> C = carts.stream( ).filter( c1 -> c1.getPosition( ).equals( c.getPosition( ) ) ).toList( );
				collisions.put( c.getPosition( ), C );
			}
		}
		
		return collisions;
	}
	
	/**
	 * Processes a track map to recreate the tracks and carts
	 * 
	 * @param input The list of strings that describe the track layout
	 * @return The Tracks
	 */
	public static TrackLayout fromStringList( final List<String> input ) {
		final CoordGrid<Character> grid = CoordGrid.fromCharGrid( input, ' ' );
		final List<Cart> carts = new ArrayList<>( );

		// now separate carts from the grid
		for( final Coord2D k : grid.getKeys( ) ) {
			final char c = grid.get( k );
			if( c == '<' || c == '>' || c == '^' || c == 'v' ) {
				carts.add( new Cart( k, Direction.fromSymbol( c ) ) );
				grid.set( k, c == '<' || c == '>' ? '-' : '|' );
			}
		}
		
		return new TrackLayout( grid, carts );
	}
	
	/** @return The visualisation of the track layout */
	@Override
	public String toString( ) {
		final Map<Coord2D, String> cartmap = new HashMap<>( carts.size( ) );
		for( final Cart c: carts ) cartmap.put( c.getPosition( ), "" + c.getDirection( ).toSymbol( ) );
		return tracks.toString( c -> "" + c, cartmap );
	}
}
