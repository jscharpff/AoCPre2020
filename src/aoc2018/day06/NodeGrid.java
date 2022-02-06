package aoc2018.day06;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Coord2D;

public class NodeGrid {
	/** The nodes in this grid */
	protected final Set<Node> nodes;
	
	/** The top left coordinate of this grid */
	protected final Coord2D min;
	
	/** The bottom right coordinate of this grid */
	protected final Coord2D max;
	
	/**
	 * Creates a new grid from the set of string coordinates
	 * 
	 * @param coords The list of coordinates
	 */
	public NodeGrid( final List<String> coords ) {
		// convert to nodes, keep track of min and max coordinates
		nodes = new HashSet<>( coords.size( ) );
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		int ID = 1;
		for( String s : coords ) {
			final Coord2D c = Coord2D.fromString( s );
			nodes.add( new Node( ID++, c ) );
			if( c.x < minX ) minX = c.x; if( c.x > maxX ) maxX = c.x;
			if( c.y < minY ) minY = c.y; if( c.y > maxY ) maxY = c.y;
		}
		
		this.min = new Coord2D( minX, minY );
		this.max = new Coord2D( maxX, maxY );
	}
	
	/** @return The width of the grid */
	protected int getWidth( ) { return max.x - min.x + 1; }

	/** @return The height of the grid */
	protected int getHeight( ) { return max.y - min.y + 1; }
	
	/**
	 * Determines the node that is closest to the coordinate
	 *  
	 * @param c The coordinate to get closest node to
	 * @return The node or null if multiple nodes are closest
	 */
	protected Node getClosestNode( final Coord2D c ) {
		Node closest = null;
		boolean multiple = false;
		for( Node n : nodes ) {
			if( closest == null ) {
				closest = n;
				continue;
			}

			// compare distances
			final int cdist = n.getDistance( c ) - closest.getDistance( c );
			if( cdist < 0 ) { closest = n; multiple = false; }
			else if( cdist == 0 ) { multiple = true; }
		}
		
		return (multiple ? null : closest);
	}
	
	/**
	 * Determines the size of the largest "finite area", i.e. the largest area
	 * that is fdully contained by the nodes in this grid
	 * 
	 * @return The size of the largest area
	 */
	public int getLargestFiniteArea( ) {		
		// go over "finite space" between min and max and label each position by the
		// ID of the closest node, leave to 0 if it is contested
		// immediately sums the count per node ID
		final int[] nodecount = new int[nodes.size( )+1];
		for( int y = 0; y < getHeight( ); y++ ) {
			for( int x = 0; x < getWidth( ); x++ ) {
				final Node closest = getClosestNode( new Coord2D( x + min.x, y + min.y ) );
				if( closest == null ) continue;
				nodecount[ closest.ID ]++;
			}
		}
		
		// return the highest node count 
		// NOTE: this relies on the assumption that there is no bounded infinite area
		//   larger than any of the finite ones
		int maxarea = -1;
		for( int i : nodecount ) if( i > maxarea ) maxarea = i;
		return maxarea;
	}
	
	/**
	 * Determines the number of "safe spots", i.e. those positions that have a
	 * summed distance of at most N to all nodes
	 * 
	 * @param n The maximal distance
	 * @return The number of spots thats are at most n away of all other coordinates 
	 */
	public int getSafeSpots( final int n ) {
		int safecount = 0;
		
		// go over all positions and check if the summed distance to all nodes is
		// within the safety margin 
		for( int y = 0; y < getHeight( ); y++ ) {
			for( int x = 0; x < getWidth( ); x++ ) {
				// sum the distance to all nodes
				int dist = 0;
				final Coord2D c = new Coord2D( x + min.x, y + min.y );
				for( Node node : nodes ) {
					dist += node.getDistance( c );
					// stop summing if are over the threshold 
					if( dist >= n ) break;
				}
				
				if( dist < n ) safecount++;
			}
		}
		
		// return the total count of safe spots
		// NOTE: this assumes all safe spots are WITHIN the node grid
		return safecount;
	}
}
