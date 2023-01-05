package aoc2019.day24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Simulation of bug growth on planet Eris
 * 
 * @author Joris
 */
public class ErisBugs {
	/** The grid that holds the current state of the simulation */
	protected CoordGrid<Boolean> grid;
	
	/**
	 * Creates a new bug simulation from the given initial state
	 * 
	 * @param initialgrid The grid describing the initial state of the simulation
	 */
	private ErisBugs( final CoordGrid<Boolean> initialgrid ) {
		this.grid = initialgrid;
	}
	
	/**
	 * Recreates an Eris bug simulation from a list of strings that describe the 
	 * initial state of the world
	 * 
	 * @param input The list of string describing the initial world grid
	 * @return The ErisBugs simulation with specified grid as its initial state
	 */
	public static ErisBugs fromStringList( final List<String> input ) {
		return new ErisBugs( CoordGrid.fromBooleanGrid( input, '#' ) );
	}
	

	/**
	 * Runs the bug simulation until a state re-occurs for the first time
	 * 
	 * @return The bio diversity rating for the state that appears twice or -1 if
	 *   such a state does not occur
	 */
	public long findRepeating( ) {
		final Map<Long, Integer> H = new HashMap<>( ); 
		
		for( int r = 0; r < Integer.MAX_VALUE; r++ ) {
			// determine biodiversity score for the current world state
			// and check if we've seen it before
			final long score = getBioDiversity( );
			if( H.containsKey( score ) ) return score;
			H.put( score, r );
			
			// nope, simulate another round
			sim( );
		}

		// not found
		return -1;
	}
	
	/**
	 * Performs a single round of the bug simulation
	 */
	private void sim( ) {
		final CoordGrid<Boolean> newgrid = new CoordGrid<Boolean>( grid.window( ).getWidth( ), grid.window( ).getHeight( ), false );
		for( final Coord2D c : grid ) {
			final boolean bug = grid.get( c );
			final int n = grid.getNeighbours( c, false ).stream( ).mapToInt( b -> grid.get( b ) ? 1 : 0 ).sum( );
			
			// A bug dies (becoming an empty space) unless there is exactly one bug 
			// adjacent to it.
			if( bug && n == 1 ) newgrid.set( c, true );
			
			// An empty space becomes infested with a bug if exactly one or two bugs
			// are adjacent to it
			if( !bug && (n == 1 || n == 2) ) newgrid.set( c, true );
		}
		
		grid = newgrid;
	}
	
	/**
	 * Determines the bio diversity score for the current state of the world
	 * 
	 * @return The score
	 */
	private long getBioDiversity( ) {
		long score = 0;
		for( final Coord2D c : grid.getKeys( ) ) {
			int n = c.x + c.y * grid.window( ).getWidth( );
			score += Math.pow( 2, n );
		}
		return score;
	}
	
	/** @return The current state of the simulation */
	@Override
	public String toString( ) {
		return grid.toString( b -> b ? "#" : "." );
	}

}
