package aoc2018.day22;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.string.RegexMatcher;

public class CaveMaze {
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
	public CaveMaze( final int depth, final Coord2D target ) {
		this.depth = depth;
		this.entrance = new Coord2D( 0, 0 );
		this.target = target;
		this.erosion = new CoordGrid<>( -1l );
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
	 * @return The minimal required time to reach the target position
	 */
	public long getRescueTime( ) {
		// perform a BF search to find the target position, prioritising
		// coordinates to explore based upon their heuristic distance to the goal
		State best = new State( target, Equipment.Torch, Long.MAX_VALUE, 0 );
		final Queue<State> Q = new PriorityQueue<>( );
		Q.offer( new State( entrance, Equipment.Torch, 0, 0 ) );
		
		// use a taboo list to prevent double checking similar states
		final Map<Coord2D, Set<State>> taboo = new HashMap<>( );
		
		while( !Q.isEmpty( ) ) {
			final State node = Q.poll( );

			// no need to search further, this will not help us find the goal
			if( node.getTime( ) + node.coord.getManhattanDistance( target ) >= best.getTime( ) ) continue;
			
			// skip this node if we already seen it or a similar one we can reach faster
			if( !taboo.containsKey( node.coord ) ) taboo.put( node.coord, new HashSet<>( ) );
			if( taboo.get( node.coord ).stream( ).anyMatch( q -> q.isFaster( node ) ) ) continue;
			taboo.get( node.coord ).add( node );
			
			// are we there yet?
			if( node.coord.equals( target ) ) {
				// do we have the right tool equipped? if not, add another switch
				final State tnode = node.newState( target, Equipment.Torch );
				
				// yes, update best known solution
				if( tnode.getTime( ) < best.getTime( ) ) {
					best = tnode;
					System.err.println( "NEW BEST: " + best );
				}
				continue;
			}
			
			// get the possible next states to explore
			final Set<State> newnodes = new HashSet<>( );
			final MazeTile currtile = MazeTile.fromErosion( getErosion( node.coord ) );
			for( final Coord2D n : node.coord.getAdjacent( false ) ) {
				if( n.x < 0 || n.y < 0 ) continue;
				
				// add all new nodes we can reach from here
				final MazeTile newtile = MazeTile.fromErosion( getErosion( n ) );
				for( final Equipment neweq : newtile.getAllowedEquipment( ) ) {
					// can we swap to the desired equipment in this tile?
					if( !currtile.getAllowedEquipment( ).contains( neweq ) ) continue;
					newnodes.add( node.newState( n, neweq ) );
				}
			}
			
			// prevent adding nodes that we have seen before with lower time to reach
			for( final State newnode : newnodes ) {
				if( taboo.getOrDefault( newnode.coord, new HashSet<>( ) ).stream( ).anyMatch( q -> q.isFaster( newnode ) ) ) continue;
				Q.offer( newnode );
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
	public static CaveMaze fromStringList( final List<String> input ) {
		final int depth = RegexMatcher.match( "depth: (\\d+)", input.get( 0 ) ).getInt( 1 );
		final Coord2D target = Coord2D.fromString( input.get( 1 ).split( ": " )[1] );
		return new CaveMaze( depth, target );
	}
	
	/**
	 * Determines the erosion level for the given coordinate
	 * 
	 * @param coord The coordinate
	 * @return The erosion level
	 */
	private long getErosion( final Coord2D coord ) {
		// try and get the erosion level from the map
		if( erosion.hasValue( coord ) )	return erosion.get( coord );
		
		// not available, compute it
		final long geoindex;
		if( coord.equals( entrance ) ) geoindex = 0;
		else if( coord.equals( target ) ) geoindex = 0;
		else if( coord.y == 0 ) geoindex = coord.x * 16807;
		else if( coord.x == 0 ) geoindex = coord.y * 48271;
		else geoindex = getErosion( coord.move( -1, 0 ) ) * getErosion( coord.move( 0, -1 ) );
		
		// store it for future use and return the value
		final long ero = Math.floorMod( geoindex + depth, 20183 );
		erosion.set( coord, ero );
		return ero;
	}
	
	/**
	 * Priority queue entry for a coordinate to explore 
	 */
	private class State implements Comparable<State> {
		/** The coordinate */
		private final Coord2D coord;
		
		/** The equipment that is active */
		private final Equipment equipped;
		
		/** The time spent in travelling to this coord */
		private final long walktime;
		
		/** The number of equipment switches so far */
		private final long switches;
		
		/** The cost of switching equipment */
		private final static int SWITCH_COST = 7;
		
		/**
		 * Creates a new State
		 * 
		 * @param coord Our position
		 * @param equipped The tool we have currently equipped
		 * @param walktime The total time spent in moving
		 * @param switches The number of times we've switched equipment
		 */
		public State( final Coord2D coord, final Equipment equipped, final long walktime, final long switches ) {
			this.coord = coord;
			this.equipped = equipped;
			this.walktime = walktime;
			this.switches = switches;
		}
		
		/**
		 * Returns the new state by moving to the coordinate and optionally switching gear
		 * 
		 * @param c The new coordinate
		 * @param neweq The new equipment to equip
		 * @return The new state
		 */
		public State newState( final Coord2D c, final Equipment neweq ) {
			return new State( c, neweq, walktime + coord.getManhattanDistance( c ), switches + (neweq != equipped ? 1 : 0) );
		}
		
		/** @return The total time */
		public long getTime( ) { return walktime + switches * SWITCH_COST; }
		
		/**
		 * Compares the total time with that of another state
		 * 
		 * @param state The state to compare against
		 * @return The compareTo value, i.e. if the times equal it returns 0, if
		 *   the time of this state if lower than the other state it will return <0
		 *   otherwise it will return a value >0 
		 */
		@Override
		public int compareTo( final State state ) {
			return Long.compare( getTime( ), state.getTime( ) );
		}
		
		/**
		 * @return The string describing the state
		 */
		@Override
		public String toString( ) {
			return coord + ": " + equipped + " -> " + getTime( ) + " (" + walktime + "/" + switches + ")";
		}
		
		/** @return The hashcode of th unique state string */
		@Override
		public int hashCode( ) {
			return (coord.toString( ) + "," + equipped.toString( ).charAt( 0 ) + "," + walktime + "," + switches).hashCode( );
		}
		
		/**
		 * Tests whether this state has the same coordinate and equipment but has a
		 * lower total time to get to the state 
		 * 
		 * @param s The other state to test against
		 * @return True iff the coordinate and equipment equal but the total time
		 *   of this state is lower, false otherwise
		 */
		public boolean isFaster( final State s ) {
			return coord.equals( s.coord ) && equipped == s.equipped && getTime( ) <= s.getTime( ); 
		}
		
		/**
		 * Tests equality to another valid state object
		 * 
		 * @param obj The other object to test against
		 * @return True iff the other object is exactly the same as the other state
		 */
		@Override
		public boolean equals( final Object obj ) {
			if( obj == null || !(obj instanceof State) ) return false;
			final State q = (State) obj;
			
			return q.coord.equals( coord ) && q.walktime == walktime && q.switches == switches && q.equipped == equipped;
		}
	}
	
	/**
	 * Tile types in the maze
	 */
	private enum MazeTile {
		Unknown, Rocky, Wet, Narrow;

		/**
		 * Determines the tile type from a given erosion level
		 *  
		 * @param erosion The erosion level
		 * @return The corresponding tile type
		 */
		public static MazeTile fromErosion( final long erosion ) {
			final int t = Math.floorMod( erosion, 3 );
			switch( t ) {
				case 0: return Rocky;
				case 1: return Wet;
				case 2: return Narrow;
			}
			return Unknown;
		}
		
		/** @return The character that represents the tile */
		private char toChar( ) {
			switch( this ) {
				case Unknown: return '?';
				case Rocky: return '.';
				case Wet: return '=';
				case Narrow: return '|';
				default: throw new IllegalArgumentException( "Invalid tile type: " + this );
			}
		}
		
		/** @return The risk level of the tile */
		private int getRisk( ) {
			switch( this ) {
				case Unknown: return 0;
				case Rocky: return 0;
				case Wet: return 1;
				case Narrow: return 2;
				default: throw new IllegalArgumentException( "Invalid tile type: " + this );
			}
		}
		
		/** @return The set of equipment allowed on this tile */
		private EnumSet<Equipment> getAllowedEquipment( ) {
			switch( this ) {
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
