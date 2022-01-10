package aoc2016.day22;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class NodeGrid {
	/** The grid of storage nodes */
	private final CoordGrid<Node> nodes;
	
	/**
	 * Creates a new NodeGrid
	 * 
	 * @param nodes The grid of nodes to use
	 */
	private NodeGrid( final CoordGrid<Node> nodes ) {
		this.nodes = nodes;
	}
	
	/**
	 * Finds all viable pairs (n1,n2) such that:
	 * - n1 is not empty
	 * - n1 != n2
	 * - n1.used <= n2.available
	 * 
	 * @return The count of viable pairs
	 */
	public int countViablePairs( ) {
		return getViablePairs( ).size();
	}
	
	private List<Node[]> getViablePairs( ) {
		final List<Node[]> pairs = new ArrayList<>( );
		for( final Node n1 : nodes.getValues( ) ) {
			for( final Node n2: nodes.getValues( ) ) {
				if( n1.equals( n2 ) ) continue;
				
				// check conditions for viable pair
				if( n1.getUsed( ) == 0 ) continue;
				if( n1.getUsed( ) > n2.getAvailable( ) ) continue;
				
				// this is a viable pair!
				pairs.add( new Node[] { n1, n2 } );
			}
		}
		
		return pairs;
	}
	
	/**
	 * Go over the nodes in this grid and flag nodes as stuck if the data can 
	 * never be moved to one of its neighbours
	 */
	private void flagStuckNodes( ) {		
		// look for any node that has no other node to drop its data onto
		final List<Node[]> pairs = getViablePairs( );
		for( final Node n : nodes.getValues( ) ) {
			final long count = pairs.stream( ).filter( x-> x[0].equals( n ) || x[1].equals( n ) ).count( );
			if( count == 0 ) n.flagAsStuck( );
		}
	}
	
	/**
	 * Determine the number of data moves required to get all data from the
	 * source node to node (0,0)
	 * 
	 * @param source The node to get data from
	 * @return The minimal number of data moves required
	 */
	public long getMinimalDataMoves( final Coord2D source ) {
		// nodes can be considered as stuck when no node can receive their data
		flagStuckNodes( );
				
		// track the node on which the data resides, the empty node and the target
		final Node target = nodes.get( new Coord2D( 0, 0 ) );
		Node datanode = nodes.get( source );
		Node empty = nodes.getValues( ).stream( ).filter( x -> x.getUsed( ) == 0 ).findFirst( ).get( );
		
		long nummoves = 0;
		while( !datanode.equals( target ) ) {
			// find "shortest path" to empty the node to the left of the datasource
			final NodePath moves = getShortestPath( empty, nodes.get( datanode.getPosition( ).move( -1, 0 ) ), datanode );
			
			// move the data alogn the shortest path
			for( int i = 1; i < moves.length( ); i++ )
				moves.get( i ).moveData( moves.get( i - 1 ) );
			
			// then move the data into the newly empty node
			empty = moves.tail( );
			datanode.moveData( empty );
			empty = datanode;
			datanode = nodes.get( datanode.getPosition( ).move( -1, 0 ) );
			
			// and update move count
			nummoves += (moves.length( ) - 1) + 1;
		}
		
		return nummoves;
	}
	
	/**
	 * Determines the shortest viable path between the two nodes (BFS) but prevent
	 * moving the data node itself. That is, that node is excluded from the path
	 * 
	 * @param start The starting node
	 * @param goal The target node
	 * @param datanode The node containing the data
	 * @return The shortest NodePath between the two
	 */
	private NodePath getShortestPath( final Node start, final Node goal, final Node datanode ) {
		// add the data node to the visited list so it is itself not considered
		final Set<Node> visited = new HashSet<>( );
		visited.add( datanode );
		
		// now find the shortest path
		Stack<NodePath> paths = new Stack<>( );
		paths.add( new NodePath( start ) );
		while( paths.size( ) > 0 ) {
			final Stack<NodePath> explore = new Stack<>( );
			while( paths.size( ) > 0 ) {
				final NodePath p = paths.pop( );
				if( p.tail( ).equals( goal ) ) return p;
				
				// check neighbours
				for( final Coord2D nc : nodes.getNeighbours( p.tail( ).getPosition( ), false ) ) {
					final Node n = nodes.get( nc );
					if( visited.contains( n ) || n.isStuck( ) ) continue;
					visited.add( n );
					explore.add( p.extend( n ) );					
				}
			}
			
			paths = explore;
		}
		
		throw new RuntimeException( "Failed to find a path from " + start + " to " + goal );
	}
	
	/**
	 * Creates a node grid from a set of node descriptions
	 * 
	 * @param input The list of nodes
	 * @return The node grid
	 */
	public static NodeGrid fromStringList( final List<String> input ) {
		final CoordGrid<Node> nodes = new CoordGrid<>( null );
		
		for( final String s : input ) {
			// skip non-node data
			if( !s.startsWith( "/dev/grid/node"  ) ) continue;
			
			final Node n = Node.fromString( s );
			nodes.set( n.getPosition( ), n );
		}
		nodes.fixWindow( );
		
		return new NodeGrid( nodes );
	}
	
	/** @return The grid of nodes */
	@Override
	public String toString( ) {
		return nodes.toString( x -> x.toString( ) + " " );
	}
	
	/**
	 * Visualises the movement of data using a simple grid of empty/full and
	 * stuck nodes
	 *  
	 * @param sourcedata The node that currently holds the source data
	 * @return The grid of nodes as a simple visual
	 */
	public String toDatarid( final Node sourcedata ) {
		return nodes.toString( x -> {
			if( x.equals( sourcedata ) ) return "D";
			if( x.isStuck( ) ) return "#";
			if( x.getUsed( ) == 0 ) return "_";
			return ".";
		} );
	}
}
