package aoc2019.day15;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class ShipSection {
	public final static char TILE_UNEXPLORED = '?';
	public final static char TILE_EMPTY = ' ';
	public final static char TILE_WALL = '#';
	public final static char TILE_OXYGEN = '+';
	public final static char TILE_DROID = 'D';
	public final static char TILE_START = 'S';

	/** The currently known map of the section */
	protected final CoordGrid<Character> map;
	
	/** Position of the oxygen tank */
	protected Coord2D oxygen;
	
	/**
	 * Creates new unexplored ship section
	 */
	public ShipSection( ) {
		// create "unlimited" grid
		map = new CoordGrid<Character>( TILE_UNEXPLORED );
		map.set( new Coord2D( 0, 0 ), TILE_EMPTY );
	}
	
	/** @return The map of the section */
	public CoordGrid<Character> getMap( ) { return map; }
	
	/**
	 * Processes a single robot move, using its output status to further update
	 * the section map
	 * 
	 * @param droid The droid to move
	 * @param direction The direction to move the droid in
	 * @return True iff the droid has moved
	 * @throws ICERuntimeException 
	 */
	public boolean tryMove( final RepairDroid droid, int direction ) throws ICERuntimeException {
		// process a single move
		droid.setDirection( direction );
		final Coord2D newpos = droid.checkMove( );
		
		// the droid outputs the result of its operation, update maze and position
		// accordingly
		final long status = droid.consume( );		
		if( status == 0 ) {
			// droid hit a wall
			map.set( newpos, TILE_WALL );
		} else if( status == 1 ) {
			// droid has moved
			map.set( newpos, TILE_EMPTY );
			droid.setPosition( newpos );
		} else if( status == 2 ) {
			// droid found the oxygen system
			oxygen = newpos;
			map.set( newpos, TILE_OXYGEN );
			droid.setPosition( newpos );				
		} else throw new RuntimeException( "Droid reported an invalid status" );
		
		// if the maze is updated, also update the visualisation
		if( Day15.isVisual( ) ) {
			final char prev = map.set( droid.getPosition( ), TILE_DROID );
			Day15.display( map.toString( ) );
			map.set( droid.getPosition( ), prev );
			Day15.tick( 1 );
		}

		return status != 0;
	}
	
	/**
	 * Determines the distance from the start position to the oxygen tank
	 * 
	 * @return The number of moves required to get to the oxygen tank 
	 */
	public int getDistanceToOxygen( ) {
		final Map<Coord2D, Integer> distances = buildDistanceMatrix( new Coord2D( 0, 0 ), oxygen );
		return distances.get( oxygen );
	}
	
	/**
	 * Determines the time it takes the oxygen tank to fill all empty spaces by
	 * determining the distance to the furthest open space tile. The tank fills
	 * the section by one tile per minute. 
	 * 
	 * @return The number of minutes required to fill all empty spaces with oxygen 
	 */
	public int getTimeToFillWithOxygen( ) {
		final Map<Coord2D, Integer> distances = buildDistanceMatrix( oxygen, null );
		
		// go over the distances matrix and return largest distance
		int max = -1;
		for( final Integer dist : distances.values( ) )
			if( dist > max ) max = dist;
		return max;
	}
	
	/**
	 * Builds a distance matrix that contains the travel distance for every
	 * coordinate with respect to the starting position
	 * 
	 * @param start The starting position
	 * @param target Stops when the target coordinate is reached, null for full BFS
	 * @return Map of all distances
	 */
	protected Map<Coord2D, Integer> buildDistanceMatrix( final Coord2D start, final Coord2D target ) {
		// keep track of distance to every coordinate encountered
		final Map<Coord2D, Integer> distances = new HashMap<>( );
		
		// use additional map for visualisation only
		final CoordGrid<Character> visual = map.copy( );
		
		// tiles to explore with their distance value
		Set<Coord2D> next = new HashSet<>( );
		int currdist = 0;
		next.add( start );
		
		// populate distances map
		while( next.size( ) > 0 ) {
			final Set<Coord2D> newnext = new HashSet<>( );
			
			// first set all distances of the current set
			for( final Coord2D c : next ) {
				distances.put( c, currdist );
				visual.set( c, '*' );
				
				// check if the target is reached, we can stop
				if( c.equals( target ) ) {
					if( Day15.isVisual( ) ) {
						visual.set( c, '+' );
						Day15.display( "Found the target at distance: " + currdist + "\n" + visual.toString( ) );
						Day15.tick( 3000 );
					}
					return distances;
				}
			}
			
			// visualise the progress
			if( Day15.isVisual( ) ) {
				Day15.display( "Distance: " + currdist + "\n" + visual.toString( ) );
				Day15.tick( 30 );
			}
			
			// increase distance
			currdist++;
			
			// and build set of new coordinates to explore
			for( final Coord2D c : next ) {
				for( int dir = 0; dir <= 270; dir += 90 ) {
					final Coord2D newpos = c.moveDir( dir, 1 );
					// already known?
					if( distances.containsKey( newpos ) ) continue;

					// cannot move through walls
					if( map.get( newpos ) == TILE_WALL ) continue;
					
					// not explored, add it for the next round
					newnext.add( newpos );
				}
			}
			
			// swap sets
			next = newnext;
		}
		
		
		// return distance to oxygen tank
		return distances;
	}
}
