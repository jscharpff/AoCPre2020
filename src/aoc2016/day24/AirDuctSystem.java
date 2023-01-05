package aoc2016.day24;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.algorithm.BreadthFirstSearch;
import aocutil.collections.CollectionUtil;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.object.LabeledObject;

/**
 * Class that models a system of air ducts and a robot that can move through
 * it
 * 
 * @author Joris
 */
public class AirDuctSystem {
	/** The actual system layout as a map of ducts, walls and locations */
	private final CoordGrid<Integer> ducts;
	
	/** The locations of the exposed wires */
	private final Map<Wire, Coord2D> wires;
	
	/** The wire distance matrix */
	private final long[][] Dwires;
	
	/** Grid values for ducts and walls */
	private final static int T_EMPTY = -1, T_WALL = 99;  

	/**
	 * Creates a new airducts system
	 * 
	 * @param ducts The map of elements in the system
	 */
	private AirDuctSystem( final CoordGrid<Integer> ducts ) {
		this.ducts = ducts;
		
		// build map of wires
		wires = new HashMap<>( );
		for( final Coord2D c : ducts ) {
			final int i = ducts.get( c );
			if( i == T_EMPTY || i == T_WALL ) continue;
			wires.put( new Wire( i ), c );
		}
		
		// build distance map from each wire to each other
		Dwires = new long[ wires.size( ) ][ wires.size( ) ];
		buildDistanceMatrix( );
	}
	
	/**
	 * Finds the minimal distance from the given wire to each other wire
	 * 
	 * @param w The wire we are starting from
	 * @return The distance matrix
	 */
	private void buildDistanceMatrix( ) {
		for( final Wire w : wires.keySet( ) ) {
			Dwires[ w.ID ][ w.ID ] = -1;
			
			final Map<Coord2D, Long> D = BreadthFirstSearch.getDistances( wires.get( w ), wires.values( ), c -> ducts.getNeighbours( c, false, x -> ducts.get( x ) != T_WALL ) );
			for( final Coord2D c : D.keySet( ) ) {
				for( final Wire w2 : wires.keySet( ) ) {
					if( w.equals( w2 ) ) continue;
					if( wires.get( w2 ).equals( c ) ) Dwires[w.ID][w2.ID] = Dwires[w2.ID][w.ID] = D.get( c );
				}
			}
		}
	}
	
	
	/**
	 * Finds the minimal total distance the robot needs to travel to visit all
	 * wires once
	 * 
	 * @param roundtrip True if the robot should return to its starting position
	 * @return The minimal distance
	 */
	public long getTravelDistance( final boolean roundtrip ) {
		// we always start at wire 0 so remove that from the search list
		final Wire w0 = new Wire( 0 );
		final Set<Wire> wiresearch = new HashSet<>( wires.keySet( ) ); 
		wiresearch.remove( w0 );
		
		// generate all permutations of remaining wires
		final List<List<Wire>> perms = CollectionUtil.generatePermutations( wiresearch );
		long mindist = Long.MAX_VALUE;
		for( final List<Wire> p : perms ) {
			p.add( 0, w0 );
			if( roundtrip ) p.add( w0 );
			long dist = 0;
			for( int i = 1; i < p.size( ); i++ ) dist += Dwires[p.get( i-1 ).ID][p.get( i ).ID];
			if( dist < mindist ) {
				mindist = dist;
			}
		}
		return mindist;
	}
	
	

	/**
	 * Creates a new duct system from its visual representation
	 * 
	 * @param input The list of strings that models the duct system
	 * @return  The AirDuctSystem	 * 
	 */
	public static AirDuctSystem fromStringList( final List<String> input ) {
		return new AirDuctSystem( CoordGrid.fromStringList( input, "", x -> {
			switch( x ) {
				case "#": return T_WALL;
				case ".": return T_EMPTY;
				default: return Integer.parseInt( x );
			}
		}, T_EMPTY ) );
	}
	
	/**
	 * @return The visualisation of the duct system
	 */
	@Override
	public String toString( ) {
		return ducts.toString( x -> {
			switch( x ) {
				case T_EMPTY: return ".";
				case T_WALL: return "#";
				default: return "" + x;
			}
		});
	}
	
	/**
	 * A single wire
	 */
	private class Wire extends LabeledObject {
		/** The wire ID */
		public final int ID;
		
		/**
		 * Creates a new wire
		 * 
		 * @param ID The wire ID
		 */
		public Wire( final int ID ) {
			super( "Wire " + ID );
			this.ID = ID;
		}
	}
}
