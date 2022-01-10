package aoc2016.day22;

import aocutil.geometry.Coord2D;
import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

/**
 * Contains a single storage node
 * 
 * @author Joris
 */
public class Node extends LabeledObject {	
	/** The position of this node on the grid */
	private final Coord2D position;
	
	/** The maximum storage capacity (in TB) */
	private final int capacity;
	
	/** The currently used capacity */
	private int used;
	
	/** 
	 * True if the data is stuck on the node because no neighbour will ever be
	 * able to receive this data
	 */
	private boolean datastuck;
	
	/**
	 * Creates a new node at the position with the specified capacity
	 * 
	 * @param label The node label
	 * @param position The position of the node in the storage grid
	 * @param capacity The available storage capacity
	 */
	private Node( final Coord2D position, final int capacity ) {		
		super( "n" + position.x + "-" + position.y );
		
		this.position = position;
		this.capacity = capacity;
		this.used = 0;
		this.datastuck = false;
	}
	
	/** @return The position of the node in the grid */
	public Coord2D getPosition( ) { return position; }
	
	/** @return The capacity of the node */
	public int getCapacity( ) { return capacity; }
	
	/** @return The available storage space */
	public int getAvailable( ) { return capacity - used; }
	
	/** @return The used space */
	public int getUsed( ) { return used; }
	
	/** @return True if the data on the node is stuck */
	public boolean isStuck( ) { return datastuck; }
	
	/**
	 * Flag this node as "stuck", i.e. its data will never move because its
	 * neighbours can never hold all its data.
	 */
	protected void flagAsStuck( ) { datastuck = true; }
	
	/**
	 * Moves the data from this node to the specified node
	 * 
	 * @param dest The node to move the data to
	 */
	public void moveData( final Node dest ) {
		if( dest.getUsed( ) != 0 ) throw new RuntimeException( "The destination node " + dest + " is not empty!" );
		dest.used = this.used;
		this.used = 0;
	}
	
	/**
	 * Creates a new storage node from string
	 * 
	 * @param input The string
	 * @return The node
	 */
	public static Node fromString( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "/dev/grid/node-x(\\d+)-y(\\d+)\s*(\\d+)T\\s*(\\d+)", input );
		
		final Node n = new Node( new Coord2D( rm.getInt( 1 ), rm.getInt( 2 ) ), rm.getInt( 3 ) );
		n.used = rm.getInt( 4 );
		return n;
	}

	/** @return A single node representation */
	@Override
	public String toString( ) {
		return super.toString( ) + ":" + used + "/" + capacity;
	}
}
