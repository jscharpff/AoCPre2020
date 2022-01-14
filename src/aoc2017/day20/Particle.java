package aoc2017.day20;

import aocutil.geometry.Coord3D;
import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

public class Particle extends LabeledObject {
	/** The particle ID */
	private int ID;
	
	/** The current position */
	private final Coord3D pos;
	
	/** The current velocity */
	private final Coord3D vel;
	
	/** The acceleration */
	private final Coord3D acc;
	
	/**
	 * Creates a new Particle
	 * 
	 * @param pos The initial position
	 * @param vel The initial velocity
	 * @param acc The acceleration
	 */
	private Particle( final int ID, final Coord3D pos, final Coord3D vel, final Coord3D acc ) {
		super( "p" + ID );
		
		this.ID = ID;
		this.pos = pos;
		this.vel = vel;
		this.acc = acc;
	}
	
	/** @return The ID */
	public int getID( ) { return ID; }
	
	/** @return The acceleration magnitude of the particle */
	public double getAccelerationForce( ) {
		return acc.size( );
	}
	
	/**
	 * Determines the position of this particle at time t
	 * 
	 * @param t The time (integer step) 
	 * @return The position of this particle at the specified time
	 */
	public Coord3D getPosition( final int t ) {
		return new Coord3D( 
				acc.x * t * (t + 1) / 2 + vel.x * t + pos.x, 
				acc.y * t * (t + 1) / 2 + vel.y * t + pos.y, 
				acc.z * t * (t + 1) / 2 + vel.z * t + pos.z
		);
	}
	
	/**
	 * Creates a particle from a string description
	 * 
	 * @param input The particle string
	 * @param ID The particle ID
	 * @return The particle
	 */
	public static Particle fromString( final String input, final int ID ) {
		final RegexMatcher rm = RegexMatcher.match( "p=<#D,#D,#D>,\\s*v=<#D,#D,#D>,\\s*a=<#D,#D,#D>", input );
		final int[] i = rm.getInts( );
		
		return new Particle( ID, new Coord3D( i[0], i[1], i[2] ), new Coord3D( i[3], i[4], i[5] ), new Coord3D( i[6], i[7], i[8] ) );
	}
	
	/** @return The string describing the particle */
	public String toLongString( ) {
		return super.toString( ) + ": p=" + pos + ", v=" + vel + ", " + acc;
	}
}
