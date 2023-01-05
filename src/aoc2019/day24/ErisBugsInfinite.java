package aoc2019.day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Simulation of bug growth on planet Eris, now in infinite dimensions
 * 
 * @author Joris
 */
public class ErisBugsInfinite {
	/** The grid that holds the initial state of the simulation */
	protected CoordGrid<Boolean> initialgrid;
	
	/** The grid size */
	private final int size;
	
	/**
	 * Creates a new bug simulation from the given initial state
	 * 
	 * @param initialgrid The grid describing the initial state of the simulation
	 */
	private ErisBugsInfinite( final CoordGrid<Boolean> initialgrid ) {
		this.initialgrid = initialgrid;
		this.size = initialgrid.window( ).getWidth( );
	}
	
	/**
	 * Recreates an Eris bug simulation from a list of strings that describe the 
	 * initial state of the world
	 * 
	 * @param input The list of string describing the initial world grid
	 * @return The ErisBugs simulation with specified grid as its initial state
	 */
	public static ErisBugsInfinite fromStringList( final List<String> input ) {
		return new ErisBugsInfinite( CoordGrid.fromBooleanGrid( input, '#' ) );
	}
	
	/**
	 * Simulates the development of bugs on eris in infinite dimensions
	 * 
	 * @param time The time steps to simulate
	 * @return The total bugs in all dimensions after simulating the specified
	 *   number of time steps
	 */
	public long sim( final int time ) {
		// initialise the multiverse!
		final int M = 2 * time + 3;
		final int g0 = time + 2;
		final List<CoordGrid<Boolean>> grids = new ArrayList<>( M );
		for( int i = 0; i < M - 1; i++ ) 
			grids.add( new CoordGrid<Boolean>( size, size, false ) );
		grids.add( g0, initialgrid );
		
		// simulate the bug growth for the specified time and dimension
		for( int t = 0; t < time; t++ ) {
			// first determine new grids without changing them
			final Map<Integer, CoordGrid<Boolean>> newgrids = new HashMap<>( );
			for( int i = 1; i < grids.size( ) - 1; i++ )
				newgrids.put( i, sim( grids, i ) );
			
			// then apply them all simultaneously
			for( final int i : newgrids.keySet( ) ) {
				grids.set( i, newgrids.get( i ) );
			}
		}
				
		// count the number of bugs in the observable multiverse
		grids.remove( 0 );
		grids.remove( grids.size( ) - 1 );
		return grids.stream( ).mapToLong( g -> g.count( true ) ).sum( );
	}
	
	/**
	 * Performs a single round of the bug simulation on the N-th dimension
	 * 
	 * @param grids The grids of bugs in every dimension
	 * @param index The index of the grid we are currently simulating
	 * @return The grid that will be in the next simulation round
	 */
	private CoordGrid<Boolean> sim( final List<CoordGrid<Boolean>> grids, final int index ) {
		final CoordGrid<Boolean> grid = grids.get( index );
		
		final CoordGrid<Boolean> newgrid = new CoordGrid<Boolean>( size, size, false );
		for( final Coord2D c : grid ) {
			// skip center coordinate
			if( c.x == 2 && c.y == 2 ) continue; 
			
			final boolean bug = grid.get( c );
			final int n = countNeighbours( grids, index, c );
			
			// A bug dies (becoming an empty space) unless there is exactly one bug 
			// adjacent to it.
			if( bug && n == 1 ) newgrid.set( c, true );
			
			// An empty space becomes infested with a bug if exactly one or two bugs
			// are adjacent to it
			if( !bug && (n == 1 || n == 2) ) newgrid.set( c, true );
		}
		
		return newgrid;
	}
	
	/**
	 * Counts the N-dimensional neighbour bugs of the given grid coordinate
	 * within the specified grid index
	 * 
	 * @param grids The multiverse of grids
	 * @param index The grid index we are simulating
	 * @param coord The coordinate to get neighbours of
	 * @return The number of bugs neighbouring the coordinate of the grid in all
	 *   N dimensions
	 */
	private int countNeighbours( final List<CoordGrid<Boolean>> grids, final int index, final Coord2D coord ) {
		final CoordGrid<Boolean> grid = grids.get( index );
		final List<Boolean> N = new ArrayList<>( );
		
		// first get horizontal neighbours
		if( coord.x == 0 ) {
			// leftmost column
			N.add( grids.get( index - 1 ).get( 1, 2 ) );
			N.add( grid.get( 1, coord.y ) );
		}	else if( coord.x == size - 1 ) {
			// rightmost column
			N.add( grids.get( index - 1 ).get( 3, 2 ) );
			N.add( grid.get( size - 2, coord.y ) );
		} else if( coord.y == 2 ) {
			// bordering center
			N.add( grid.get( coord.x == 1 ? 0 : size - 1, 2 ) );
			for( int i = 0; i < size; i++ )
				N.add( grids.get( index + 1 ).get( coord.x == 1 ? 0 : size - 1, i ) );
		} else {
			// any other x coordinate
			N.add( grid.get( coord.x - 1, coord.y ) );
			N.add( grid.get( coord.x + 1, coord.y ) );
		}

		// then vertical neighbours
		if( coord.y == 0 ) {
			// top row
			N.add( grids.get( index - 1 ).get( 2, 1 ) );
			N.add( grid.get( coord.x, 1 ) );
		}	else if( coord.y == size - 1 ) {
			// bottom row
			N.add( grids.get( index - 1 ).get( 2, 3 ) );
			N.add( grid.get( coord.x, size - 2 ) );
		} else if( coord.x == 2 ) {
			// bordering center
			N.add( grid.get( 2, coord.y == 1 ? 0 : size - 1 ) );
			for( int i = 0; i < size; i++ )
				N.add( grids.get( index + 1 ).get( i, coord.y == 1 ? 0 : size - 1 ) );
		} else {
			// any other y coordinate
			N.add( grid.get( coord.x, coord.y - 1 ) );
			N.add( grid.get( coord.x, coord.y + 1 ) );
		}		
		
		// finally count neighbours
		return (int)N.stream( ).filter( b -> b == true ).count( );
	}
	
	
	/** @return The current state of the simulation */
	@Override
	public String toString( ) {
		return initialgrid.toString( b -> b ? "#" : "." );
	}

}
