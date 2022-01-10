package aoc2016.day17;

import java.util.EnumSet;
import java.util.Stack;

import aoc2016.day17.MazePath.Move;
import aocutil.Util;
import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;

/**
 * A maze of doors that respond to MD5 hashed pass codes
 * 
 * @author Joris
 */
public class HashMaze {
	/** The maze width and height */
	private final Window2D mazearea;
	
	/** The coordinate of the current position in the maze */
	protected Coord2D startpos;
	
	/** The pass key to use for the doors */
	private final String passkey;
	
	/**
	 * Creates a new HashMaze
	 * 
	 * @param height The height of the maze
	 * @param width The width of the maze
	 * @param passkey The pass key to use on the doors while traversing the maze
	 */
	public HashMaze( final int height, final int width, final String passkey ) {
		this.mazearea = new Window2D( width, height );
		startpos = new Coord2D( 0, 0 );
		this.passkey = passkey;
	}
	
	/**
	 * Finds the route that minimises the distance to the vault
	 * 
	 * @param vault The coordinate of the vault
	 * @return The route that minimises the number of steps to the vault
	 */
	public MazePath findShortestRoute( final Coord2D vault ) {
		Stack<MazePath> paths = new Stack<>( );
		paths.add( new MazePath( startpos ) );
		
		// keep going over paths until we can explore no more
		long failsafe = 100000l;
		while( !paths.empty( ) ) {
			if( --failsafe == 0 ) break; /* prevent infinite loop */
			
			// determine set of next paths to explore
			final Stack<MazePath> explore = new Stack<>( );
			while( !paths.empty( ) ) {
				final MazePath p = paths.pop( );
				
				// check if we are there yet, first we encounter must be shortest path
				if( p.getPosition( ).equals( vault ) ) return p;
				
				// return only moves that lead to valid positions
				final EnumSet<Move> nextmoves = nextMoves( p );
				for( final Move m : nextmoves ) {
					explore.add( p.move( m ) );
				}
			}
			
			// swap stacks
			paths = explore;
		}
		
		throw new RuntimeException( "Failed to find the shortest path" ); 		
	}
	
	/**
	 * Finds the longest route that leads to the vault
	 * 
	 * @param vault The coordinate of the vault
	 * @return The longest route to the vault
	 */
	public long findLongestRouteLength( final Coord2D vault ) {
		// perform search DFS by constructing a stack of moves and testing them
		final StringBuilder path = new StringBuilder( );
		final long pathlength = findLongestRouteLength( vault, path, startpos, 0 );
		if( pathlength == -1 ) throw new RuntimeException( "Failed to find the longest path to the vault" );
		return pathlength;
	}
	
	/**
	 * Performs a DFS search to find the length of the longest path (within the
	 * given maximum depth) that leads to the vault
	 * 
	 * @param vault The coordinates of the vault
	 * @param path The current path we are building
	 * @param pos The current position in the maze
	 * @param maxdepth The maximum search depth, any value of 0 or lower equates
	 *   to infinite depth
	 * @return The longest route that was found to the vault. If a max depth if
	 *   used, this length is bounded by the max depth and it may not be the
	 *   longest route possible. 
	 */
	private long findLongestRouteLength( final Coord2D vault, final StringBuilder path, final Coord2D pos, final long maxdepth ) {
		// are we there yet?
		if( pos.equals( vault ) ) return path.length( );
		if( maxdepth > 0 && path.length( ) > maxdepth ) return -1;
	
		// find longest path from here by trying all available moves
		long longest = -1;
		for( final Move m : nextMoves( pos, path.toString( ) ) ) {			
			// try this move
			path.append( m.mchar );
			long pathlength = findLongestRouteLength( vault, path, pos.moveDir( m.angle, 1 ), maxdepth );
			path.delete( path.length( ) - 1, path.length( ) );
			
			if( pathlength > longest ) { longest = pathlength; }
		}
		
		return longest;
	}
	
	/**
	 * Generates all valid next moves from the given path using the pass key to
	 * determine the open doors
	 * 
	 * @param path The path to extend
	 * @return The list of new paths
	 */
	private EnumSet<Move> nextMoves( final MazePath path ) {
		return nextMoves( path.getPosition( ), path.toString( ) );
	}
	
	/**
	 * Generates the set of valid moves from the current position and path
	 * traversed so far
	 * 
	 * @param pos The current position, used to check maze boundaries
	 * @param pathstr The current string of path moves (only UDLR allowed) 
	 * @return The set of next valid moves
	 */
	private EnumSet<Move> nextMoves( final Coord2D pos, final String pathstr ) {
		// check which doors are open with the given pass key
		final EnumSet<Move> moves = EnumSet.noneOf( Move.class );
		final int[] doors = Util.MD5( passkey + pathstr ).chars( ).limit( 4 ).map( i -> (i >='b' && i <= 'f') ? 1 : 0 ).toArray( );
		if( doors[0] == 1 && pos.y > mazearea.getMinX( ) ) moves.add( Move.Up );
		if( doors[1] == 1 && pos.y < mazearea.getMaxY( ) ) moves.add( Move.Down );
		if( doors[2] == 1 && pos.x > mazearea.getMinX( ) ) moves.add( Move.Left );
		if( doors[3] == 1 && pos.x < mazearea.getMaxX( ) ) moves.add( Move.Right );

		return moves;
	}
}