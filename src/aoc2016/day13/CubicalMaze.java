package aoc2016.day13;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import aocutil.algorithm.BreadthFirstSearch;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.string.BitString;

/**
 * A maze of office cubicals
 * 
 * @author Joris
 */
public class CubicalMaze {
	/** The seed to generate walls */
	private final int wallseed;
	
	/**
	 * Generates a new maze from the seed number
	 * 
	 * @param seed The initialisation seed
	 */
	public CubicalMaze( final int seed ) {
		this.wallseed = seed;
	}
	
	/**
	 * Checks if the given coordinate is a wall
	 * 
	 * @param coord The coordinate to check
	 * @return True if the coordinate is a wall
	 */
	public boolean isWall( final Coord2D coord ) {
		// prevent walking outside!
		if( coord.x < 0 || coord.y < 0 ) return true;
		
		final long iswall = coord.x * coord.x + 3 * coord.x + 2 * coord.x * coord.y + coord.y + coord.y * coord.y + wallseed;
		final BitString b = BitString.fromLong( iswall );
		return b.countOnes( ) % 2 == 1;
	}
	
	/***
	 * Performs a BFS search from the given start position to the goal to find the
	 * shortest route
	 *  
	 * @param start The start position
	 * @param goal The target position
	 * @return The minimal number of steps required to get to the goal 
	 */
	public long findDistanceTo( final Coord2D start, final Coord2D goal ) {
		return BreadthFirstSearch.getDistance( start, goal, c -> {
			// only consider non-wall coordinates
			final Set<Coord2D> set = new HashSet<>( );
			for( final Coord2D n : c.getAdjacent( false ) ) {
				if( !isWall( n ) ) set.add( n );
			}
			return set;
		} );
	}
	
	/***
	 * Performs a BFS search from the given start position to find the number of
	 * unique locations that we can visit in the number of steps
	 *  
	 * @param start The start position
	 * @param steps The number of steps to explore
	 * @return The minimal number of steps required to get to the goal 
	 */
	public long findUniqueLocations( final Coord2D start, final int steps ) {
		final Stack<Coord2D> remaining = new Stack<>( );
		remaining.add( start );
		
		final Set<Coord2D> visited = new HashSet<>( );
		int currstep = 0;
		while( !remaining.isEmpty( ) && currstep <= steps ) {
			// find new set of nodes to explore in next iteration
			final Set<Coord2D> explore = new HashSet<>( remaining.size( ) * 4 );
			
			// explore nodes one by one
			while( !remaining.isEmpty( ) ) {
				final Coord2D c = remaining.pop( );
				visited.add( c );
				
				// get all the nodes I can reach from here
				for( final Coord2D n : c.getAdjacent( false ) ) {
					if( !isWall( n ) && !visited.contains( n ) ) explore.add( n );
				}
			}
			
			// add all new nodes and try next step
			remaining.addAll( explore );
			currstep++;
		}

		return visited.size( );
	}

	/**
	 * Generates a textual representation of the maze, given the fixed dimensions
	 * 
	 * @param width The width of the maze
	 * @param height The height of the maze
	 * @return The maze as a wxh grid 
	 */
	public String toLongString( final int width, final int height ) {
		final CoordGrid<Boolean> maze = new CoordGrid<>( false ); 
		
		for( int y = 0; y < height; y++ )
			for( int x = 0; x < width; x++ ) {
				final Coord2D c = new Coord2D( x, y );
				maze.set( c, isWall( c ) );
			}
		
		return maze.toString( x -> x ? "#" : "." );
	}
}
