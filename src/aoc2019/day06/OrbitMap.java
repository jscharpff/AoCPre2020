package aoc2019.day06;

import java.util.List;
import java.util.Stack;

import aocutil.graph.Edge;
import aocutil.graph.Graph;
import aocutil.graph.Node;
import aocutil.graph.Path;

/**
 * Map of planetary orbits
 * 
 * @author Joris
 */
public class OrbitMap {
	/** The graph representing the orbits */
	private final Graph orbits;
	
	/** The root node */
	private final Node root;
	
	/**
	 * Creates a new orbit map
	 * 
	 * @param orbits The graph that captures the orbiting relations 
	 */
	private OrbitMap( final Graph orbits ) {
		this.orbits = orbits;
		if( !orbits.contains( "COM" ) ) throw new RuntimeException( "Orbittal graph must contain a COM node" );
		this.root = orbits.getNode( "COM" );
	}
	
	/**
	 * Computes the checksum of the orbit map
	 * 
	 * @return The checksum
	 */
	public long checksum( ) {
		// do a BFS from the root to find the checksum
		long checksum = 0;
		int dist = 0;
		Stack<Node> next = new Stack<>( );
		next.add( root );
		
		while( !next.isEmpty( ) ) {
			// keep exploring until all leaves have been found
			final Stack<Node> newset = new Stack<>( );
			while( !next.isEmpty( ) ) {
				final Node n = next.pop( );
				checksum += dist;
				newset.addAll( n.getSuccessors( ) );
			}
			
			// swap set and increase distance for next iteration
			next = newset;
			dist++;
		}
		return checksum;
	}
	
	/**
	 * Counts the number of orbital transfers required to get from orbit start
	 * to goal
	 * 
	 * @param start The label of the starting planetary orbit position
	 * @param goal The label of the target position
	 * @return The number of orbits to transfer 
	 */
	public int countTransfers( final String start, final String goal ) {
		// get path to root for both nodes and see where the first common node
		// occurs, that's where a transfer from the start branch to the goal
		// branch can be made
		final Path sp = getPath( orbits.getNode( start ), root );
		final Path gp = getPath( orbits.getNode( goal ), root );
		
		// find first common node in path from start
		int transfers = 0;
		final Node common;
		while( true ) {
			if( gp.contains( sp.getNodes( ).get( transfers ) ) ) {
				common = sp.getNodes( ).get( transfers );
				break;
			}
			transfers++;
		}
		
		// and add number of steps required to get there in the goal path
		for( final Node n : gp.getNodes( ) ) {
			if( n.equals( common ) ) break;
			transfers++;
		}

		// return transfers
		return transfers;		
	}
	
	/**
	 * Finds the "upward" path from orbit A to B where B is a (indirect) parent
	 * of A
	 * 
	 * @param A The child node
	 * @param B The parent node
	 * @return The path from A to B, containing both
	 */
	private Path getPath( final Node A, final Node B ) {
		Path p = new Path( A );
		Node curr = A;
		while( !curr.equals( B ) ) {
			// get its parent
			curr = curr.getPredecessors( ).iterator( ).next( );
			p = p.extend( curr );
		}
		return p;
	}
	
	/**
	 * Creates a new orbit map from a list of string
	 * 
	 * @param input The input strings
	 * @return The OrbitMap
	 */
	public static OrbitMap fromStringList( final List<String> input ) {
		final Graph g = new Graph( );		
		for( final String in : input ) {
			final String[] s = in.split( "\\)" );
			g.addEdge( new Edge( g.addNode( s[0] ), g.addNode( s[1] ), 0, true ) );
		}
		return new OrbitMap( g );
	}

}
