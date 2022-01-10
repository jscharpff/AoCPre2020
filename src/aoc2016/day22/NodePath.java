package aoc2016.day22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A path of nodes
 * 
 * @author Joris
 */
public class NodePath {
	/** The nodes in the path */
	private final List<Node> nodes;
	
	/**
	 * Creates a new empty path starting from the given node
	 */
	public NodePath( final Node node ) {
		this( new ArrayList<>( ) );
		nodes.add( node );
	}
	
	/**
	 * Creates a new path with the given node list
	 * 
	 * @param nodes The nodes to set in this path
	 */
	private NodePath( final Collection<Node> nodes ) {
		this.nodes = new ArrayList<>( nodes );
	}
	
	/**
	 * Creates a new path by extending this one with the given node
	 * 
	 * @param node The node to add
	 */
	public NodePath extend( final Node node ) {
		final NodePath n = new NodePath( nodes );
		n.nodes.add( node );
		return n;
	}
	
	/**
	 * Retrieves a specific node from the path
	 * 
	 * @param index The index of the node
	 * @return The node at the index in the path
	 */
	public Node get( final int index ) {
		return nodes.get( index );
	}
	
	
	/**
	 * @return The last node in the path
	 */
	public Node tail( ) { return nodes.get( nodes.size( ) - 1 ); }
	
	/** @return The length of the path */
	public int length( ) { return nodes.size( ); }

	/** @return The nodes in the path */
	@Override
	public String toString( ) {
		return nodes.toString( );
	}
}
