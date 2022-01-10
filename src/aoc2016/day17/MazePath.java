package aoc2016.day17;

import aocutil.geometry.Coord2D;
import aocutil.object.LabeledObject;

/**
 * Representation of a path through the maze
 * 
 * @author Joris
 */
public class MazePath extends LabeledObject {
	/** The actual moves */
	private final String path;
	
	/** The point we end up in */
	private final Coord2D pos;
	
	/**
	 * Creates a new empty path
	 * 
	 * @param startpos The position this path starts in
	 */
	public MazePath( final Coord2D startpos ) {
		this( "(empty)", startpos );
	}
	
	/**
	 * Creates a new path of the given moves
	 * 
	 * @param newpath The moves in the path
	 */
	private MazePath( final String newpath, final Coord2D newpos ) {
		super( newpath );
		
		this.path = newpath;
		this.pos = newpos;
	}
	
	/**
	 * Moves one step in the specified direction, returns a new path
	 * 
	 * @param dir The direction to move in
	 * @return The new path
	 */
	public MazePath move( final Move dir ) {
		return new MazePath( (!isEmpty( ) ? path : "") + dir.mchar, pos.moveDir( dir.angle, 1 ) );
	}

	/** @return The current position after traversing the path */
	public Coord2D getPosition( ) {
		return pos;
	}
	
	/** @return The size of the path */
	public int size( ) { return isEmpty( ) ? 0 : path.length( ); }
	
	/** @return True iff the path is empty */
	public boolean isEmpty( ) { return path.equals( "(empty)" ); }
	
	/** @return The path as a string */
	@Override
	public String toString( ) {
		return isEmpty( ) ? "" : label;
	}
	
	/** The set of available moves */
	protected enum Move {
		Up, Right, Down, Left; // DO NOT REORDER!
		
		/** The path character */
		protected final char mchar = this.toString( ).charAt( 0 );
		
		/** The path direction angle */
		protected final int angle = this.ordinal( ) * 90;
	}
}
