package aoc2018.day06;

import aocutil.geometry.Coord2D;

/**
 * A labelled coordinate
 * 
 * @author Joris
 */
public class Node {
	/** The node ID */
	protected final int ID;
	
	/** The node coordinate */
	protected final Coord2D pos;
	
	/**
	 * Creates a new node at the specified position
	 * 
	 * @param ID The node ID
	 * @param position The coordinate 
	 */
	public Node( final int ID, final Coord2D position ) {
		this.ID = ID;
		this.pos = position;
	}
	
	/** @return The node ID */
	public int getID( ) { return ID; }
	
	/** @return The node position */
	public Coord2D getPosition( ) { return pos; }
	
	/**
	 * Computes Manhattan distance to this node from the specified coordinate
	 * 
	 * @param coord The other coordinate
	 * @return The Manhattan distance from the coordinate to the node
	 */
	public int getDistance( final Coord2D coord ) {
		return this.pos.getManhattanDistance( coord );
	}

	@Override
	public int hashCode( ) {
		return ID;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Node) ) return false;
		return ((Node)obj).ID == ID;
	}
	
	@Override
	public String toString( ) {
		return ID + " @ " + pos;
	}
}
