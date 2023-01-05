package aoc2019.day12;

import aocutil.geometry.Coord3D;
import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

/**
 * A single moon of the orbit simulator
 * 
 * @author Joris
 */
public class Moon extends LabeledObject {
	/** Its initial position */
	protected final Coord3D initpos;
	
	/** Its current position */
	protected Coord3D pos;
	
	/** Its velocity */
	protected Coord3D vel;
	
	/**
	 * Creates a new moon
	 * 
	 * @param initpos The initial position
	 */
	protected Moon( final Coord3D initpos ) {
		super( "Moon " + (lastID++) );
		
		this.initpos = initpos;
		this.pos = initpos;
		this.vel = new Coord3D( 0, 0, 0 );
	}
		
	/**
	 * Computes the kinetic energy of this moon
	 * 
	 * @return The moon's kinetic energy
	 */
	public long getEnergy( ) {
		final long epot = Math.abs( pos.x ) + Math.abs( pos.y ) + Math.abs( pos.z );
		final long ekin = Math.abs( vel.x ) + Math.abs( vel.y ) + Math.abs( vel.z );
		return epot * ekin;
	}
	
	/**
	 * Reconstructs a moon from a string description
	 * 
	 * @param input The input string
	 * @return The Moon
	 */
	public static Moon fromString( final String input ) {
		final RegexMatcher r = RegexMatcher.match( "x=#D, y=#D, z=#D", input );
		return new Moon( new Coord3D( r.getInts( ) ) );
	}

	/** @return The decription of this moon's state */
	@Override
	public String toString( ) {
		return "pos=" + pos + ", vel=" + vel;
	}
	
	/** The last assigned unique ID */
	private static int lastID = 0;
}
