package aoc2019.day18.vault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Summarises the vault topology into a graph
 * 
 * @author Joris
 */
public class VaultGraph {
	/** The portal positions */
	protected final List<Coord2D> portals;
	
	/** The map of keys in the graph and their positions */
	protected final Map<Character, Coord2D> keys;
	
	/** The map of shortest paths, from each key to each other key */
	protected final Map<Character, Map<Character, VGPath>> D;
	
	/**
	 * Creates a VaultGraph from the given grid
	 * 
	 * @param grid The CoordGrid that visually describes the vault layout
	 */
	public VaultGraph( final CoordGrid<Character> grid ) {
		this.portals = new ArrayList<>( );
		this.keys = new HashMap<>( );
		this.D = new HashMap<>( keys.size( ) );
		
		// extract positions of keys
		for( final Coord2D c : grid.getKeys( ) ) {
			final char ch = grid.get( c ).charValue( );
			if( ch >= 'a' && ch <= 'z' ) keys.put( ch, c );
			else if( ch == '@' ) portals.add( c );
		}		
		
		// the determine the distance matrix for every pair of keys
		for( final char key : keys.keySet( ) )
			D.put( key, buildDistanceMatrix( key, keys.get( key ), grid ) );
		
		// and from every starting position
		for( int i = 0; i < portals.size( ); i++ ) {
			final char p = (char)('1' + i);
			D.put( p, buildDistanceMatrix( p, portals.get( i ), grid ) );
		}
	}
	
	/**
	 * Returns the path between two points in the vault
	 * 
	 * @param from The starting point
	 * @param to The target
	 * @return The path between the points
	 */
	public VGPath getPath( final char from, final char to ) {
		return D.get( from ).get( to );
	}
	
	/**
	 * Finds the shortest path from the key to every other key
	 * 
	 * @param key The key to start the paths from
	 * @param pos The starting position
	 * @param map The grid with the visual description of the vault
	 * @return The map of distances to all other keys, including doors in between
	 */
	protected Map<Character, VGPath> buildDistanceMatrix( final char key, final Coord2D pos, final CoordGrid<Character> map ) {
		// initialise map to with list for all other keys
		final Map<Character, VGPath> paths = new HashMap<>( );
		
		// start paths from the key's initial position
		final Set<VGPath> visited = new HashSet<>( );
		final Stack<VGPath> E = new Stack<>( );
		E.push( new VGPath( pos ) );
		
		// and explore all other reachable keys
		while( !E.isEmpty( ) ) {
			final Set<VGPath> nextE = new HashSet<>( );
			
			while( !E.isEmpty( ) ) {
				final VGPath curr = E.pop( );
				visited.add( curr );
				
				// if we've hit a key, store the path
				final char k = map.get( curr.end );
				if( keys.containsKey( k ) ) paths.put( k, curr );
				
				// check neighbouring coordinates
				for( final Coord2D n : curr.end.getAdjacent( false ) ) {
					// skip if out of bounds or a wall
					if( !map.contains( n ) || map.get( n ) == '#' ) continue;
					
					// extend the path and check if it makes sense to explore
					final VGPath next = curr.extend( n, map.get( n ) );
					
					// check if we already visited this position with a (sub)set of the
					// keys and a shorter length
					if( visited.stream( ).anyMatch( p -> p.betterThan( next ) ) ) continue;
					nextE.add( next );
				}
				
			}
			E.addAll( nextE );
		}
		
		return paths;
	}
	
	/**
	 * Represents a single path through the vault 
	 */
	protected class VGPath {
		/** The coordinate that is reached by traversing this path */
		protected final Coord2D end;
		
		/** The keys required to traverse the path */
		protected final Set<Character> keysreq;
		
		/** The length of the path in steps */
		protected final int length;
		
		/**
		 * Internal constructor to create a path
		 * 
		 * @param pos The position the path ends in
		 * @param keysreq The set of keys required to traverse this path
		 * @param length The length of the path
		 */
		private VGPath( final Coord2D pos, final Set<Character> keysreq, final int length ) {
			this.end = pos;
			this.keysreq = new HashSet<>( keysreq );
			this.length = length;		
		}
		
		/**
		 * Creates a new, empty path
		 * 
		 * @param startpos The starting position
		 */
		protected VGPath( final Coord2D startpos ) {
			this( startpos, new HashSet<>( ), 0 );
		}
		
		/**
		 * Creates a new path by extending it with one more step
		 * 
		 * @param newend The new end position
		 * @param maptile The tile at the end of the path
		 * @return The new path
		 */
		protected VGPath extend( final Coord2D newend, final char maptile ) {
			final VGPath newpath = new VGPath( newend, keysreq, this.length + 1 );
			if( maptile >= 'A' && maptile <= 'Z' ) newpath.keysreq.add( (char)(maptile - 'A' + 'a') );
			return newpath;
		}
		
		/**
		 * Checks if this path is better than another path. This is true if the
		 * length is shorter or the same and the set of keys is a subset or equal
		 * to that of the other path
		 *  
		 * @param path The path to compare against 
		 * @return True iff this path is better than the other path
		 */
		public boolean betterThan( final VGPath path ) {
			if( !end.equals( path.end ) ) return false;
			if( length > path.length ) return false;
			return path.keysreq.containsAll( keysreq );
		}

		/**
		 * Checks if this path equals the given object
		 * 
		 * @param obj The other object to test against
		 * @return True iff the object is a valid path, has the same end point,
		 * key set and length
		 */
		@Override
		public boolean equals( final Object obj ) {
			if( obj == null || !(obj instanceof VGPath) ) return false;
			final VGPath p = (VGPath) obj;
			
			return end.equals( p.end ) && keysreq.equals( p.keysreq ) && length == p.length;
		}
		
		/** @return The hash code of its string */
		@Override
		public int hashCode( ) {
			return toString( ).hashCode( );
		}		
		
		/** @return The string that uniquely describes the path */
		@Override
		public String toString( ) {
			return end + ": " + keysreq + " (" + length + ")";
		}
	}
}
