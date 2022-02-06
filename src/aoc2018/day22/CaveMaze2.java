package aoc2018.day22;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.string.RegexMatcher;

public class CaveMaze2 {
	/** The depth of the maze */
	private final int depth;
	
	/** The cave entrance position */
	private final Coord2D entrance;
	
	/** The target coordinate of the end point within the maze */
	private final Coord2D target;
	
	/** The grid of computed erosion levels */
	private final CoordGrid<Long> erosion;
	
	/**
	 * Creates a new maze
	 * 
	 * @param depth The depth of the maze
	 * @param target The target coordinate
	 */
	public CaveMaze2( final int depth, final Coord2D target ) {
		this.depth = depth;
		this.entrance = new Coord2D( 0, 0 );
		this.target = target;
		this.erosion = new CoordGrid<>( -1l );
		erosion.set( entrance, (long)depth );
		erosion.set( target, (long)depth );
	}

	/**
	 * Computes the risk level of the region from the cave entrance to the
	 * target coordinate
	 * 
	 * @return The risk level
	 */
	public int getRiskLevel( ) {
		final Window2D region = new Window2D( entrance, target );

		int risk = 0;
		for( final Coord2D c : region ) {
			risk += MazeTile.fromErosion( getErosion( c ) ).getRisk( );
		}
		return risk;
	}
	
	/**
	 * Determines the minimal time required to rescue Santa's friend at the
	 * target coordinate
	 * 
	 * @param upper The upper bound on the time
	 * @return The minimal required time to reach the target position
	 */
	public long getRescueTime( long upper ) {
		if( upper <= 0 ) upper = Long.MAX_VALUE;
		
		// perform a A* search to find the target position, prioritising
		// coordinates to explore based upon their heuristic distance to the goal
		QCoord best = new QCoord( target, Equipment.Torch, upper, 0 );
		final Queue<QCoord> Q = new PriorityQueue<>( );
		Q.offer( new QCoord( entrance, Equipment.Torch, 0, 0 ) );
		
		// use a taboo list to prevent double checking the same node, also
		// occasionally flush it
		final Set<QCoord> taboo = new HashSet<>( );
		
		while( !Q.isEmpty( ) ) {
			final QCoord node = Q.poll( );

			// no need to search further, this will not help us find the goal
			if( node.getTime( ) + node.coord.getManhattanDistance( target ) > best.getTime( ) ) continue;
			
			// skip this node if we already seen it or a similar one we can reach faster
			if( taboo.stream( ).anyMatch( q -> q.isFaster( node ) ) ) continue;
			taboo.add( node );
			
			// are we there yet?
			if( node.coord.equals( target ) ) {
				// do we have the right tool equipped? if not, add another switch
				final QCoord tnode = new QCoord( target, Equipment.Torch, node.walktime, node.switches + (node.equipped != Equipment.Torch ? 1 : 0) );
				
				// yes, update best known solution and prune
				if( tnode.getTime( ) < best.getTime( ) ) {
					best = tnode;
					
					// do some pruning based upon this new information
					final QCoord qbest = best;
					final int Qsize = Q.size( ); final int taboosize = taboo.size( );
					Q.removeAll( Q.stream( ).filter( q -> taboo.stream( ).anyMatch( t -> t.isFaster( q ) ) ).toList( ) );
					Q.removeAll( Q.stream( ).filter( q -> q.getTime( ) + q.coord.getManhattanDistance( target ) >= qbest.getTime( ) ).toList( ) );				
					taboo.removeAll( taboo.stream( ).filter( q -> q.getTime( ) + q.coord.getManhattanDistance( target ) >= qbest.getTime( ) ).toList( ) );					
					System.err.println( "NEW BEST: " + best );
					System.err.println( "Q: " + Q.size( ) + " (-" + (Qsize - Q.size( )) + "), taboo: " + taboo.size( ) + " (-" + (taboosize - taboo.size( )) + ")" );
				}
				continue;
			}
			
			// get the possible next coordinates to explore
			final MazeTile currtile = MazeTile.fromErosion( getErosion( node.coord ) );
			for( final Coord2D n : node.coord.getAdjacent( false ) ) {
				if( n.x < 0 || n.y < 0 ) continue;
				
				// add a new node for both types of equipment allowed, if the terrain
				// type changes
				final MazeTile newtile = MazeTile.fromErosion( getErosion( n ) );
				if( currtile == newtile ) {
					final QCoord newnode = new QCoord( n, node.equipped, node.walktime + 1, node.switches );
					if( taboo.stream( ).anyMatch( q -> q.isFaster( newnode ) ) ) continue;
					Q.offer( newnode );
				} else {
					// switch equipment
					for( final Equipment neweq : newtile.getAllowedEquipment( ) ) {
						final QCoord newnode = new QCoord( n, neweq, node.walktime + 1, node.switches + (node.equipped != neweq ? 1 : 0) );
						if( taboo.stream( ).anyMatch( q -> q.isFaster( newnode ) ) ) continue;
						Q.offer( newnode );
					}
				}
			}
		}
		return best.getTime( );
	}

	
	/** @return Visualisation of the current erosion grid */
	@Override
	public String toString( ) {
		return erosion.toString( x -> "" + MazeTile.fromErosion( x ).toChar( ) );
	}
	
	/**
	 * Reconstructs the maze from a list of strings describing it
	 * 
	 * @param input The input describing the maze
	 * @return The CaveMaze 
	 */
	public static CaveMaze2 fromStringList( final List<String> input ) {
		final int depth = RegexMatcher.match( "depth: (\\d+)", input.get( 0 ) ).getInt( 1 );
		final Coord2D target = Coord2D.fromString( input.get( 1 ).split( ": " )[1] );
		return new CaveMaze2( depth, target );
	}
	
	/**
	 * Determines the erosion level for the given coordinate
	 * 
	 * @param coord The coordinate
	 * @return The erosion level
	 */
	private long getErosion( final Coord2D coord ) {
		// try and get the erosion level from the map
		if( erosion.hasValue( coord ) ) return erosion.get( coord );
		
		// not available, compute it
		final long geoindex;
		if( coord.x == 0 && coord.y == 0 ) geoindex = depth;
		else if( coord.equals( target ) ) geoindex = depth;
		else if( coord.y == 0 ) geoindex = coord.x * 16807;
		else if( coord.x == 0 ) geoindex = coord.y * 48271;
		else geoindex = getErosion( coord.move( -1, 0 ) ) * getErosion( coord.move( 0, -1 ) );
		
		// store it for future use and return the value
		final long ero = (geoindex + depth) % 20183;
		erosion.set( coord, ero );
		return ero;
	}
	
	/**
	 * Priority queue entry for a coordinate to explore 
	 */
	private class QCoord implements Comparable<QCoord> {
		/** The coordinate */
		private final Coord2D coord;
		
		/** The equipment that is active */
		private final Equipment equipped;
		
		/** The actual time spent travelling to this coord */
		private final long walktime;
		private final long switches;
		
		/** It's heuristic value */
		private final long heurvalue;
		
		/**
		 * Creates a new QCoord
		 */
		public QCoord( final Coord2D coord, final Equipment equipped, final long walktime, final long switchtime ) {
			this.coord = coord;
			this.equipped = equipped;
			this.walktime = walktime;
			this.switches = switchtime;
			this.heurvalue = getHeuristicSimple( this, target );
		}
		
		/** @return The total time */
		public long getTime( ) { return walktime + switches * 7; }
		
		@Override
		public int compareTo( QCoord o ) {
			return Long.compare( heurvalue, o.heurvalue );
		}
		
		@Override
		public String toString( ) {
			return coord + ": " + equipped + " -> " + getTime( ) + " (" + walktime + "/" + switches + "/" + heurvalue + ")";
		}
		
		@Override
		public int hashCode( ) {
			return (coord.toString( ) + "," + equipped.ordinal( ) + "," + getTime( )).hashCode( );
		}
		
		public boolean isFaster( final QCoord q ) {
			return coord.equals( q.coord ) && getTime( ) <= q.getTime( ) + (equipped != q.equipped ? 7 : 0);
		}
		
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof QCoord) ) return false;
			final QCoord q = (QCoord) obj;
			
			return q.coord.equals( coord ) && q.walktime == walktime && q.switches == switches && q.equipped == equipped;
		}
		
		private long getHeuristicSimple( final QCoord node, final Coord2D c ) {
			return node.coord.getManhattanDistance( c ) * 2 + getTime( );
		}
	}
	
	/**
	 * Tile types in the maze
	 */
	private enum MazeTile {
		Unknown, Rocky, Wet, Narrow;
		
		public static MazeTile fromErosion( final long erosion ) {
			final int t = (int)(erosion % 3);
			switch( t ) {
				case 0: return Rocky;
				case 1: return Wet;
				case 2: return Narrow;
			}
			return Unknown;
		}
		
		private char toChar( ) {
			switch( this ) {
				case Unknown: return '?';
				case Rocky: return '.';
				case Wet: return '=';
				case Narrow: return '|';
				default: throw new IllegalArgumentException( "Invalid tile type: " + this );
			}
		}
		
		private int getRisk( ) {
			switch( this ) {
				case Unknown: return 0;
				case Rocky: return 0;
				case Wet: return 1;
				case Narrow: return 2;
				default: throw new IllegalArgumentException( "Invalid tile type: " + this );
			}
		}
		
		private EnumSet<Equipment> getAllowedEquipment( ) {
			switch( this ) {
				case Unknown: return EnumSet.noneOf( Equipment.class );
				case Rocky: return EnumSet.of( Equipment.ClimbingGear, Equipment.Torch );
				case Wet: return EnumSet.of( Equipment.ClimbingGear, Equipment.Neither );
				case Narrow: return EnumSet.of( Equipment.Neither, Equipment.Torch );
				default: throw new IllegalArgumentException( "Invalid tile type: " + this );
			}
		}
	}
	
	/**
	 * Available equipment
	 */
	private enum Equipment {
		Neither, Torch, ClimbingGear;
	}
}
