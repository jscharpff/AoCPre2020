package aoc2017.day19;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.graph.Edge;
import aocutil.graph.Graph;
import aocutil.graph.Node;

/**
 * A maze of tubes
 * 
 * @author Joris
 */
public class TubeMaze {
	/** The graph representing the maze */
	protected final Graph tubes;
	
	/**
	 * Creates a new tube maze
	 * 
	 * @param graph The graph that describes the tube maze
	 */
	private TubeMaze( final Graph graph ) {
		this.tubes = graph;
	}
	
	/**
	 * @return Total length of the maze
	 */
	public int getLength( ) {
		return tubes.getEdges( ).stream( ).mapToInt( Edge::getWeight ).sum( );
	}
	
	/**
	 * Reconstructs a tube maze from a drawing
	 * 
	 * @param input The list of strings that visually describes the maze
	 * @return The TubeMaze
	 */
	public static TubeMaze fromStringList( final List<String> input ) {
		final Graph g = new Graph( );
		final Node root = g.addNode( "Start" );

		// convert input to maze matrix
		final int rows = input.size( );
		final int cols = input.get( 0 ).length( );
		final char[][] maze = new char[ cols ][ rows ];
		for( int row = 0; row < input.size( ); row++ ) {
			final String srow = input.get( row ); 
			for( int col = 0; col < srow.length( ); col++ )
				maze[col][row] = srow.charAt( col );
		}
		
		// start at the pipe in the first row
		Coord2D pos = null;
		for( int x = 0; x < cols; x++ )
			if( maze[x][0] == '|' ) pos = new Coord2D( x, 0 );
		Direction dir = Direction.South;

		// traverse the visual maze to reconstruct the tubes in the graph
		Node prev = root;
		int length = 0;
		while( true ) {
			char c = maze[ pos.x ][ pos.y ];

			// we are done!
			if( c == ' ' ) {
				final Node newnode = g.addNode( "End" );
				g.addEdge( new Edge( prev, newnode, length ) );
				break;
			}
			
			// corner, change direction
			if( c == '+' ) {
				final Node newnode = g.addNode( "Node " + g.getNodes( ).size( ) );
				g.addEdge( new Edge( prev, newnode, length ) );
				length = 0;
				prev = newnode;
				if( dir == Direction.North || dir == Direction.South ) dir = maze[pos.x-1][pos.y] != ' ' ? Direction.West : Direction.East;				
				else dir = maze[pos.x][pos.y -1] != ' ' ? Direction.North : Direction.South;
			}

			// is this a letter?
			if( c != '|' && c != '-' && c != '+' ) 
				System.out.print( c );
			
			// regular tile or letter, move a step
			pos = pos.moveDir( dir, 1 );
			length++;
		}
		System.out.println(  );
		
		return new TubeMaze( g );
	}
	
	/** @return The string that describes the tube graph */
	@Override
	public String toString( ) {
		return tubes.toString( );
	}
}
