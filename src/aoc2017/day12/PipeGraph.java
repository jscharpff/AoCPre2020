package aoc2017.day12;

import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.algorithm.BreadthFirstSearch;
import aocutil.graph.Edge;
import aocutil.graph.Graph;
import aocutil.graph.Node;

/**
 * Graph of pipe communication streams
 * 
 * @author Joris
 */
public class PipeGraph {
	/** The graph structure of the pipe streams */
	private final Graph graph;
	
	/**
	 * Creates a new pipe stream graph
	 * 
	 * @param graph The graph structure of the communication pipes
	 */
	private PipeGraph( final Graph graph ) {
		this.graph = graph;
	}
	
	/**
	 * Recreates a pipe stream graph from the textual description of its nodes
	 * and links
	 * 
	 * @param input The list of nodes and their links
	 * @return The PipeGraph
	 */
	public static PipeGraph fromStringList( final List<String> input ) {
		final Graph graph = new Graph( );
		
		// first reconstruct node list
		for( final String s : input )
			graph.addNode( s.split( " <-> " )[0] );
		
		// then reconstruct edges
		for( final String in : input ) {
			final String[] s = in.split( " <-> " );
			final Node n1 = graph.getNode( s[0] );
			for( final String ns : s[1].split( ", " ) ) {
				final Node n2 = graph.getNode( ns );
				graph.addEdge( new Edge( n1, n2, 0 ) );
			}
		}
		
		return new PipeGraph( graph );
	}
	
	/**
	 * Determine the number of nodes reachable from the specified node
	 * 
	 * @param nodeID The ID of the node to start from
	 * @return The number of reachable nodes
	 */
	public int getReachable( final int nodeID ) {
		final Node startnode = graph.getNode( "" + nodeID );
		if( startnode == null ) throw new RuntimeException( "No such node: " + nodeID );
		
		return getCluster( startnode ).size( );
	}
	
	/**
	 * Determines the number of clusters in the graph
	 * 
	 * @return The number of clusters
	 */
	public int getNumClusters( ) {
		final Stack<Node> remaining = new Stack<>( );
		remaining.addAll(  graph.getNodes( )  );
		int groups = 0;
		
		while( !remaining.empty( ) ) {
			final Node n = remaining.pop( );
			remaining.removeAll( getCluster( n ) );
			groups++;
		}
		return groups;
	}
	
	/**
	 * Determines all the nodes in the cluster starting from a node
	 * 
	 * @param startnode The node to find the cluster for
	 * @return The cluster of nodes all interconnected of which this node is a 
	 *   part
	 */
	private Set<Node> getCluster( final Node startnode ) {
		// BFS search over nodes
		return BreadthFirstSearch.getReachable( startnode, n -> n.getNeighbours( ) );
	}
	
	/** @return The graph as a string */
	@Override
	public String toString( ) {
		return graph.toString( );
	}

}
