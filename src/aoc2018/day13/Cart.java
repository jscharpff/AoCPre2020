package aoc2018.day13;

import aocutil.geometry.Direction;

import aocutil.geometry.Coord2D;

/**
 * A single cart that moves along a track
 * 
 * @author Joris
 */
public class Cart {
	/** The ID of the cart */
	private final int ID;
	
	/** The current position of the cart */
	private Coord2D pos;
	
	/** The direction of the cart */
	private Direction dir;
	
	/** The new direction when hitting the next intersection */
	private Direction nextdir;
	
	/**
	 * Creates a new cart
	 * 
	 * @param pos The starting position
	 * @param dir The initial direction it is facing
	 */
	protected Cart( final Coord2D pos, final Direction dir ) {
		this.ID = nextID++;
		this.pos = pos;
		this.dir = dir;
		this.nextdir = Direction.West;
	}
	
	/** @return The current position */
	public Coord2D getPosition( ) { return pos; }
	
	/** @return The current direction */
	public Direction getDirection( ) { return dir; }
	
	/**
	 * Processes a move
	 * 
	 * @param track The track layout
	 */
	public void move( final TrackLayout track ) {
		// move the cart
		pos = pos.move( dir, 1 );

		
		final char t = track.get( pos );
		switch( t ) {
			// regular track
			case '|': case '-': break;
			
			// corner
			case '/':
				switch( dir ) {
					case North: dir = Direction.East; break;
					case East: dir = Direction.North; break;
					case South: dir = Direction.West; break;
					case West: dir = Direction.South; break;
				}
				break;
			
			// corner
			case '\\': 
				switch( dir ) {
					case North: dir = Direction.West; break;
					case West: dir = Direction.North; break;
					case South: dir = Direction.East; break;
					case East: dir = Direction.South; break;
				}
				break;
				
			// intersection, perform a turn
			case '+':
				switch( nextdir ) {
					case West: dir = dir.turn( -1 ); break;
					case North: break;
					case East: dir = dir.turn( 1 ); break;
					case South: throw new RuntimeException( "Carts cannot reverse!" );
					
				}
				nextdir = nextdir.turn( 1 );
				if( nextdir == Direction.South ) nextdir = Direction.West;
				break;
				
			// unknown piece of track
			default: throw new RuntimeException( "Invalid track piece encountered: " + t + " at " + pos );
		}
	}
	
	/**
	 * Compares cart IDs
	 * 
	 * @param obj The other object
	 * @return True if the cart IDs equal
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Cart) ) return false;
		return ((Cart)obj).ID == ID;
	}

	/** @return The cart description */
	@Override
	public String toString( ) {
		return "C" + ID;
	}
	
	/** The next ID to assign */
	private static int nextID = 0;
}
