package aoc2019.day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import aocutil.collections.LabelMatrix;
import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.object.LabeledObject;

/**
 * A donut-shaped maze with recursive space portals
 * 
 * @author Joris
 */
public class DonutMaze {
	/** The maze layout */
	private final CoordGrid<Boolean> maze;

	/** The portals */
	private final Map<String, Portal> portals;
	
	/** The start and end of the maze */
	private final POI start, end;
	
	/**
	 * Creates a new maze
	 * 
	 * @param maze The maze layout
	 * @param portals The portals in the maze
	 * @param start The maze entrance
	 * @param end The maze exit
	 */
	public DonutMaze( final CoordGrid<Boolean> maze, final Map<String, Portal> portals, final POI start, final POI end ) {
		this.maze = maze;
		this.portals = new HashMap<>( portals );
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Finds the shortest path through the maze from start to end
	 * 
	 * @return The minimal number of steps required
	 */
	public long findShortestPath( ) {
		// BFS algorithm that incorporates portals
		Stack<Coord2D> exp = new Stack<>( );
		exp.add( start.coord );
		final Set<Coord2D> visited = new HashSet<>( );
		long dist = 0;
		
		while( !exp.isEmpty( ) ) {
			final Stack<Coord2D> newexp = new Stack<>( );
			
			while( !exp.isEmpty( ) ) {
				// explore next coordinate
				final Coord2D c = exp.pop( );
				if( visited.contains( c ) ) continue;
				visited.add( c );
				
				// found the target?
				if( c.equals( end.coord ) ) return dist;
				
				// nope, explore neighbouring coordinates and check portals
				newexp.addAll( maze.getNeighbours( c, false, maze::hasValue ) );
				newexp.addAll( portals.values( ).stream( ).filter( p -> p.isEntrance( c ) ).map( p -> p.getExit( c ) ).toList( ) );
			}
			
			exp = newexp;
			dist++;
		}
		
		throw new RuntimeException( "Failed to find the exit!" );
	}
	
	/**
	 * Finds the shortest path through the maze from start to end
	 * 
	 * @return The minimal number of steps required
	 */
	public long findShortestPathInRecursiveSpace( ) {
		// compute the APSP matrix between start, end and portal entrances
		final Set<POI> pois = new HashSet<>( );
		pois.add( start );
		pois.add( end );
		for( final Portal p : portals.values( ) ) { 
			pois.add( new POI( p.getLabel( ) + "-I", p.inner ) ); 
			pois.add( new POI( p.getLabel( ) + "-O", p.outer ) );
		}
		final LabelMatrix<POI, Integer> D = buildAPSP( pois );
		
		// okay, now search for the shortest path through recursive space using a
		// Queued BFS implementation
		final PriorityQueue<RSPath> Q = new PriorityQueue<>( );
		Q.offer( new RSPath( start ) );
		while( !Q.isEmpty( ) ) {
			final RSPath p = Q.poll( );
		
			// found the target?
			if( p.tail.equals( end ) && p.level == 0 ) return p.distance;
			
			// explore all POIs that we can visit from this point
			for( final POI next : D.getRow( p.tail ).keySet( ) ) {
				// cannot go back to start
				if( next.equals( start ) ) continue;
				
				// can only reach the end if we are at the outer most level
				if( next.equals( end ) ) {
					if( p.level == 0 )
						Q.offer( p.extend( D.get( p.tail, next ), next ) );

					continue;
				}
				
				// check if we can recurse upwards
				if( next.getLabel( ).endsWith( "O" ) && p.level == 0 ) continue;
				
				// go through the portal
				final Portal portal = portals.get( next.getLabel( ).substring( 0, 2 ) );
				Q.offer( p.extend( D.get( p.tail, next ), portal, next ) );
			}
			
		}
		
		throw new RuntimeException( "Failed to find a path through recursive space" );
	}
	
	/**
	 * Builds the APSP matrix between all the pairs of POIs in the set
	 * 
	 * @param poi The set of points of interest
	 * @return The APSP matrix
	 */
	private LabelMatrix<POI, Integer> buildAPSP( final Set<POI> poi ) {
		final LabelMatrix<POI, Integer> D = new LabelMatrix<>( );
		for( final POI point : poi ) {
			final Set<POI> targets = new HashSet<>( poi );
			targets.remove( point );
			
			// BFS algorithm to find distance to all other POIs
			Stack<Coord2D> exp = new Stack<>( );
			exp.add( point.coord );
			final Set<Coord2D> visited = new HashSet<>( );
			int dist = 0;
			
			while( !exp.isEmpty( ) ) {
				final Stack<Coord2D> newexp = new Stack<>( );
				
				while( !exp.isEmpty( ) ) {
					// explore next coordinate
					final Coord2D p = exp.pop( );
					if( visited.contains( p ) ) continue;
					visited.add( p );
					
					// found a target?
					final Optional<POI> t = targets.stream( ).filter( x -> x.coord.equals( p ) ).findFirst( );
					if( t.isPresent( ) ) {
						D.set( point, t.get( ), dist );
						targets.remove( t.get( ) );
					}
					
					// nope, explore neighbouring coordinates
					newexp.addAll( maze.getNeighbours( p, false, maze::hasValue ) );
				}
				
				exp = newexp;
				dist++;
			}
			
		}
		
		return D;
	}
	
	
	/**
	 * Reconstructs the Donut Maze layout from a list of strings
	 * 
	 * @param input The maze layout
	 * @return The DonutMaze
	 */
	public static DonutMaze fromStringList( final List<String> input ) {
		// convert to char grid for easier manipulation
		final CoordGrid<Character> chars = CoordGrid.fromCharGrid( input, ' ' );
		
		// get the tunnel system and size
		final CoordGrid<Boolean> path = CoordGrid.fromBooleanGrid( input, '.' );
		
		// parse portals, start and end from the input
		final Map<String, List<Coord2D>> objects = new HashMap<>( );
		for( final Coord2D c : chars ) {
			final char ch = chars.get( c );
			if( ch == '#' || ch == '.' || ch == ' ' ) continue;
			
			for( final Coord2D n : new Coord2D[] { c.move( 0, 1 ), c.move( 1, 0 ) } ) {
				final char ch2 = chars.get( n );
				if( ch >= 'A' && ch <= 'Z' && ch2 >= 'A' && ch2 <= 'Z'  ) {
					final String label = "" + ch + ch2;
					if( !objects.containsKey( label ) ) objects.put( label, new ArrayList<>( ) );
					
					// find actual portal entrance
					final Set<Coord2D> C = new HashSet<>( );
					C.addAll( c.getAdjacent( false ) );
					C.addAll( n.getAdjacent( false ) );
					for( final Coord2D ent : C )
						if( chars.get( ent ) == '.' ) {
							objects.get( label ).add( ent );
							break;
						}
				}
			}
		}
		
		// reconstruct portals
		final Map<String, Portal> portals = new HashMap<>( objects.size( ) - 2 );
		final Window2D w = path.window( );
		for( final String o : objects.keySet( ) ) {
			final List<Coord2D> cds = objects.get( o );
			if( cds.size( ) != 2 ) continue;
			
			// add portal but make sure to set inner and outer correctly
			final Coord2D c1 = cds.get( 0 ), c2 = cds.get( 1 );
			
			// c1 on the border of the maze? then it is the "outer" side of the portal
			if( w.onBorder( c1 ) ) {
				portals.put( o, new Portal( o, c2, c1 ) );		
			} else {
				portals.put( o, new Portal( o, c1, c2 ) );
			}
			
		}

		// and return the maze
		return new DonutMaze( path, portals, new POI( "AA", objects.get( "AA" ).get( 0 ) ), new POI( "ZZ", objects.get( "ZZ" ).get( 0 ) ) );
	}

	/** Captures a single portal of the maze */
	private static class Portal extends LabeledObject {
		/** The coordinates at either end of the portal */
		private final Coord2D inner, outer;
		
		/**
		 * Creates a new portal
		 * 
		 * @param label The label of the portal
		 * @param inner
		 * @param outer
		 */
		public Portal( final String label, final Coord2D inner, final Coord2D outer ) {
			super( label );
			
			this.inner = inner;
			this.outer = outer;
		}
		
		/**
		 * Checks if a coordinate is one of the portal's entrances
		 * 
		 * @param c The coordinate to check
		 * @return True iff the coordinate is an entrance of the portal
		 */
		public boolean isEntrance( final Coord2D c ) {
			return inner.equals( c ) || outer.equals( c );
		}
		
		/**
		 * Returns the other end of the portal
		 * 
		 * @param ent The entrance coordinate
		 * @return The exit coordinate
		 */
		public Coord2D getExit( final Coord2D ent ) {
			if( inner.equals( ent ) ) return outer;
			if( outer.equals( ent ) ) return inner;
			throw new IllegalArgumentException( "Coordinate is not part of this portal: " + ent );
		}
		
		/**
		 * Retrieves the entrance/exit POI at the other side of the portal
		 *  
		 * @param ent The entrance/exit
		 * @return The POI at the other side
		 */
		public POI getExitPOI( final POI ent ) {
			final Coord2D c = getExit( ent.coord );
			return new POI( label + (inner.equals( c ) ? "-I" :"-O"), c );
		}
		
		/** @return The portal description */
		@Override
		public String toString( ) {
			return "[" + label + "] in: " + inner + ", out: " + outer;
		}
	}
	
	/** Point of interest (a labelled coordinate) */
	private static class POI extends LabeledObject {
		/** The coordinate */
		private final Coord2D coord;
		
		/**
		 * Creates a new point of interest
		 * 
		 * @param label The label
		 * @param coord The coordinate
		 */
		public POI( final String label, final Coord2D coord ) {
			super( label );
			this.coord = coord;
		}
		
		@Override
		public String toString( ) {
			return super.label + " @ " + coord;
		}
	}
	
	/** A path through recursive space */
	private static class RSPath implements Comparable<RSPath> {
		/** The current position as a result of travelling the path i.e., the tail of the path */
		private final POI tail;
		
		/** The current level in recursive space */
		private final int level;
		
		/** The total distance travelled through recursive space */
		private final long distance;
		
		/**
		 * Creates a new path from the given starting point
		 * 
		 * @param start The starting POI
		 */
		public RSPath( final POI start ) {
			this( start, 0, 0 );
		}
		
		/**
		 * Creates a new path
		 * 
		 * @param tail The current tail of the path
		 * @param level The level in the donut maze
		 * @param distance The distance travelled by the path
		 */
		private RSPath( final POI tail, final int level, final long distance ) {
			this.tail = tail;
			this.level = level;
			this.distance = distance;			
		}
		
		/**
		 * Extends the path by going to the POI
		 * 
		 * @param dist The distance to the portal
		 * @param poi The point to travel to
		 * @return The new path
		 */
		public RSPath extend( final int dist, final POI poi ) {
			return new RSPath( poi, level, distance + dist );
		}
		
		/**
		 * Extends the path by going through the portal
		 * 
		 * @param dist The distance to the portal
		 * @param portal The portal to traverse through
		 * @param entrance The POI at which the portal is entered
		 * @return The new path 
		 */
		public RSPath extend( final int dist, final Portal portal, final POI entrance ) {
			final POI poi = portal.getExitPOI( entrance );
			return new RSPath( poi, level + (poi.getLabel( ).endsWith( "I" ) ? -1 : 1), distance + dist + 1 );
		}

		/**
		 * Compares this path to another in terms of length
		 * 
		 * @param path To compare to
		 * @return The compareTo value: negative if this path is shorter, 0 if
		 *   equal, positive if the other path is shorter 
		 */
		@Override
		public int compareTo( final RSPath path ) {
			return Long.compare( distance, path.distance );
		}
		
		/** @return The string description of the path */
		@Override
		public String toString( ) {
			return tail + ": " + distance + " (L " + level + ")";
		}
	}
}
