package aoc2017.day22;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;

/**
 * The malicious virus running havoc in the node grid
 * 
 * @author Joris
 */
public class Virus {
	/** The current position of the virus */
	private Coord2D pos;
	
	/** The direction it is facing */
	private Direction dir;
	
	/**
	 * Creates a new virus at the given initial coordinate
	 * 
	 * @param initpos The initial position
	 */
	public Virus( final Coord2D initpos ) {
		this.pos = initpos;
		this.dir = Direction.North;
	}
	
	/** @return The current position of the virus */
	public Coord2D getPosition( ) { return pos; }
	
	/**
	 * Moves in the direction it is currently facing
	 * 
	 * @param dist The distance to move
	 */
	public void move( final int dist ) {
		pos = pos.move( dir, dist );
	}
	
	/**
	 * Turns the direction of the virus based upon its current position
	 * 
	 * @param clockwise True if virus is to turn clockwise
	 */
	public void turn( final boolean clockwise ) {
		dir = dir.turn( clockwise ? 1 : 3 );		
	}
	
	/**
	 * Reverse the direction of the virus
	 */
	public void reverse( ) { 
		dir = dir.turn( 2 );
	}
	
	/** @return The virus position and direction */
	@Override
	public String toString( ) {
		return "V: " + pos + " " + dir;
	}	

}
