package aoc2019.day10;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * A 2D map of asteroids in space
 * 
 * @author Joris
 */
public class AsteroidMap {
	/** The actual map */
	private final CoordGrid<Boolean> asteroids;

	/**
	 * Creates a new AsteroidMap
	 * 
	 * @param asteroids The coordgrid that describes the asteroids
	 */
	private AsteroidMap( final CoordGrid<Boolean> asteroids ) {
		this.asteroids = asteroids;
	}
	
	/**
	 * Finds the best position to put a monitoring station. Here, the best
	 * position is defined as being the asteroid that has the most other 
	 * asteroids in its direct line of sight
	 * 
	 * @return The number of asteroids in the direct line of sight from the
	 *   positions that maximises this number
	 */
	public Coord2D placeMonitoringStation( ) {
		Coord2D bestcoord = null;
		int bestcount = -1;
		for( final Coord2D c : asteroids.getKeys( ) ) {
			final int astcount = getAsteroidsInLOS( c ).size( );
			if( astcount > bestcount ) {
				bestcount = astcount;
				bestcoord = c;
			}
		}
		
		return bestcoord;
	}
	
	/**
	 * Determines the asteroids in the line of sight from the given coordinate
	 * 
	 * @param coord The coordinate
	 * @return The asteroids directly in line of sight
	 */
	public Set<Coord2D> getAsteroidsInLOS( final Coord2D coord ) {
		return getAsteroidsInLOS( this.asteroids.getKeys( ), coord );
	}
	
	/**
	 * Determines the asteroids in the line of sight from the given coordinate,
	 * considering only the asteroids in the set
	 * 
	 * @param ast The set of (remaining) asteroids
	 * @param coord The coordinate
	 * @return The asteroids directly in line of sight
	 */
	private Set<Coord2D> getAsteroidsInLOS( final Set<Coord2D> ast, final Coord2D coord ) {
		// for each asteroid find the smallest integer vector on which it can be
		// seen from the coordinate. The number of such unique vectors is equal to
		// the count of asteroids that can be seen
		final Set<Coord2D> vectors = new HashSet<>( );
		for( final Coord2D c : ast ) {
			if( c.equals( coord ) ) continue;
			
			vectors.add( getLOSVector( coord, c ) );
		}
		return vectors;		
	}
	
	/**
	 * Finds the smallest vector that will lead to (eventually) reaching the 
	 * target coordinate from the origin
	 * 
	 * @param origin The origin coordinate
	 * @param target The target coordinate
	 * @return The vector that intersects with the target from the origin
	 */
	private Coord2D getLOSVector( final Coord2D origin, final Coord2D target ) {
		Coord2D vec = origin.diff( target );
		
		// special case, either axis difference is zero or same coordinate
		if( vec.x == 0 && vec.y == 0 ) return new Coord2D( 0, 0 );
		else if( vec.x == 0 ) return new Coord2D( 0, vec.y > 0 ? 1 : -1 );
		else if( vec.y == 0 ) return new Coord2D( vec.x > 0 ? 1 : -1, 0 );
		
		// nope, find highest common denominator
		for( int i = Math.min( Math.abs( vec.x ), Math.abs( vec.y ) ); i > 1; i-- ) {
			if( vec.x % i == 0 && vec.y % i == 0 ) return new Coord2D( vec.x / i, vec.y / i );
		}
		
		// none found, return original vector (both values are prime)
		return vec;
	}
	
	
	/**
	 * Install a laser gun at the specified coordinate and fires it for the given
	 * number of times
	 * 
	 * @param coord The coordinate to install the laser at
	 * @param shots The number of lasers to shoot
	 * @return The coordinate of the last hit asteroid
	 */
	public Coord2D fireLaser( final Coord2D coord, final int shots ) {
		// get the set of asteroids we are shooting at
		final Set<Coord2D> ast = new HashSet<>( asteroids.getKeys( ) );
		ast.remove( coord );
		
		// set up the laser and identify unique target vectors
		int shot = 0;
		Coord2D lasthit = null;
		while( !ast.isEmpty( )) {
			// get vectors of all asteroids within LoS and sort on angle
			final List<Coord2D> targetvecs = new ArrayList<>( getAsteroidsInLOS( ast, coord ) );
			targetvecs.sort( (c1,c2) -> getAngle( c1 ) == getAngle( c2 ) ? 0 : getAngle( c1 ) > getAngle( c2 ) ? 1 : -1 );
			
			// fire at all vectors for this round
			for( final Coord2D vec : targetvecs ) {
				// FIRE!!!
				lasthit = fireLaser( ast, coord, vec );
				if( ++shot == shots ) return lasthit;
			}
		}
		
		// we've run out of asteroids, return the last hit
		return lasthit;
	}
	
	/**
	 * Computes the angle of the vector, where 0 means that the vector is facing
	 * North (because the laser starts in that direction)
	 * 
	 * @param vec The vector to compute the angle of
	 * @return The angle (in degrees) of the vector 
	 */
	private double getAngle( final Coord2D vec ) {
		// corner cases...
		if( vec.x == 0 ) return vec.y > 0 ? 180 : 0;
		else if( vec.y == 0 ) return vec.x > 0 ? 90 : 270;
		
		// return the tan-1 of the vectors, corrected by 90 degrees to align 0 
		// degrees with a North-facing laser 
		return 90.0 + Math.atan( (double)vec.y / (double)vec.x ) * (360.0 / (2 * Math.PI)) + (vec.x > 0 ? 0 : 180.0);		
	}
	
	/**
	 * Fires the laser along the target vector and destroys the asteroid it hits,
	 * that is, it is removed from the set
	 * 
	 * @param ast The set of (remaining) asteroids
	 * @param origin The origin from which the laser is fired
	 * @param targetvec The vector to fire along
	 * @return The asteroid that was hit, null if no asteroid was encountered
	 */
	private Coord2D fireLaser( final Set<Coord2D> ast, final Coord2D origin, final Coord2D targetvec ) {
		// try finite number of times, otherwise just give up (e.g. the laser range)
		int maxsteps = 100;
		Coord2D p = origin;
		while( maxsteps-- > 0 ) {
			// did we hit something?
			if( ast.contains( p ) ) {
				ast.remove( p );
				return p;
			}
			
			// nope, trace along the vector
			p = p.move( targetvec );
		}
		
		return null;
	}
	
	/**
	 * Reconstructs the asteroid map from a list of strings
	 * 
	 * @param input The list of strings
	 * @return The AsteroidMap
	 */
	public static AsteroidMap fromStringList( final List<String> input ) {
		return new AsteroidMap( CoordGrid.fromBooleanGrid( input, '#' ) );
	}
}
