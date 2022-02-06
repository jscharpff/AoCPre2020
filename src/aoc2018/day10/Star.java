package aoc2018.day10;

import aocutil.geometry.Coord2D;
import aocutil.string.RegexMatcher;

/**
 * A single, moving star
 * 
 * @author Joris
 */
public class Star {
	/** The current position */
	private Coord2D pos;
	
	/** The star's velocity */
	private Coord2D vel;
	
	/**
	 * Creates a new star
	 * 
	 * @param pos The initial position
	 * @param vel The velocity
	 */
	public Star( final Coord2D pos, final Coord2D vel ) {
		this.pos = pos;
		this.vel = vel;
	}
	
	/** @return The current position */
	public Coord2D getPosition( ) { return pos; }
	
	/**
	 * Moves the star a single step
	 * 
	 * @return The new position
	 */
	protected Coord2D move( ) {
		pos = pos.move( vel );
		return pos;
	}
	
	/**
	 * Creates a star from a string description
	 * 
	 * @param input The description of the star
	 * @return The star
	 */
	public static Star fromString( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "position=<\\s*(-?\\d+),\\s*(-?\\d+)>\\s*velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>", input );
		return new Star( new Coord2D( rm.getInt( 1 ), rm.getInt( 2 ) ), new Coord2D( rm.getInt( 3 ), rm.getInt( 4 ) ) );
	}

	/** @return The star's description */
	@Override
	public String toString( ) {
		return "pos=" + pos + ", vel=" + vel;
	}
}
