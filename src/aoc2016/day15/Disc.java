package aoc2016.day15;

import aocutil.string.RegexMatcher;

/**
 * A rotating disc with a single open slot
 * 
 * @author Joris
 */
public class Disc {
	/** The ID of the disc, also its position in the sculpture */
	private final int ID;
	
	/** The number of positions on the disc (0 being the open slot) */
	private final long positions;
	
	/** The initial slot in view as a result of the disc rotating */
	private final long initial;
	
	/**
	 * Creates a new rotating disc
	 * 
	 * @param id The disc ID number 
	 * @param slots The number of slots it has
	 * @param initial The initial slot
	 */
	private Disc( final int ID, final long positions, final long initial ) {
		this.ID = ID;		
		this.positions = positions;
		this.initial = initial;
	}
	
	/**
	 * Rotates the disc for t steps and returns the slot that is in front
	 * 
	 * @param t The time steps to rotate
	 * @return The slot that is in front at the specified time step
	 */
	public long getSlot( final long t ) {
		return (initial + t) % positions;
	}
	
	/**
	 * Determines the next time step at which the open slot will be in the
	 * right position for the capsule to fall through, acccounting for the time
	 * it has to travel
	 * 
	 * @return The next time step at which the capsule may be dropped so that it
	 *   falls through the open slot
	 */
	public long getDropTime( ) {
		return positions - initial - ID;
	}
	
	/**
	 * Aligns two discs by creating a new "virtual" disc that models the interval
	 * at which both discs have their open slots in front in subsequent time
	 * steps
	 * 
	 * @param other The disc to align with
	 * @return The disc that models the combination of the two 
	 */
	protected Disc align( final Disc other ) {
		// their rotations will align at every common product (only valid for primes!)
		final long rot = positions * other.positions;
		
		// now search for the first t at which both disc are aligned properly
		// within this new interval
		long tstart = -1;
		for( long t = 0; t < rot; t++ ) {
			if( getSlot( t + ID ) == 0 && other.getSlot( t + other.ID ) == 0 ) {
				tstart = t;
				break;
			}
		}
		
		// build the function that aligns their open slots accordingly
		return new Disc( ID, rot, (rot - tstart - ID) % rot);
	}
	
	/**
	 * Creates a disc from a textual description
	 *  
	 * @param input The input description
	 * @return The disc
	 */
	public static Disc fromString( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+)", input );		
		return new Disc( rm.getInt( 1 ), rm.getLong( 2 ), rm.getLong( 3 ) );
	}
	
	/** @return The string describing the disc */
	@Override
	public String toString( ) {
		return "Disc #" + ID + " " + initial + "/" + positions;
	}

}
