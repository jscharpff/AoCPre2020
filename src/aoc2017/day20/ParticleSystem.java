package aoc2017.day20;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.geometry.Coord3D;

/**
 * System of particles that simulates their movement and collisions
 * 
 * @author Joris
 */
public class ParticleSystem {
	/** The particles it manages */
	private final List<Particle> particles;
	
	/**
	 * Creates a new particle system
	 * 
	 * @param particles The particles in this system
	 */
	private ParticleSystem( final Collection<Particle> particles ) {
		this.particles = new ArrayList<>( particles );
	}
	
	/**
	 * Determines the particle that will, in the long(est) run, remain closest to
	 * the origin.
	 * 
	 * @return The particle
	 */
	public Particle findClosest( ) {
		Particle slowest = null;
		
		for( final Particle p : particles ) {
			if( slowest == null || p.getAccelerationForce( ) < slowest.getAccelerationForce( ) ) slowest = p;
		}
		return slowest;		
	}
	
	/**
	 * Simulates all particle collisions and returns the set of particles that
	 * remain after all collisions have been handled
	 * 
	 * @return The set of remaining particles
	 */
	public Collection<Particle> simulateCollisions( ) {
		// start with all particles initially
		final List<Particle> remaining = new ArrayList<>( particles );

		// generate coordinate positions of each remaining particle at time t
		for( int t = 1; t <= 10000; t++ ) {
			final Map<Coord3D, Set<Particle>> collisions = new HashMap<>( remaining.size( ) );
			for( final Particle p : remaining ) {
				final Coord3D pos = p.getPosition( t );
				if( !collisions.containsKey( pos ) ) collisions.put( pos, new HashSet<>( ) );
				collisions.get( pos ).add( p );
			}

			// check for collisions
			collisions.values( ).stream( ).filter( x -> x.size( ) > 1 ).forEach( x -> remaining.removeAll( x ) );
		}
		
		return remaining;
	}
	
	/**
	 * Creates a new particle system from a list of particles
	 * 
	 * @param input The list of particles, one string each
	 * @return The {@link ParticleSystem}
	 */
	public static ParticleSystem fromStringList( final List<String> input ) {
		final List<Particle> particles = new ArrayList<>( input.size( ) );
		for( int i = 0; i < input.size( ); i++ ) {
			particles.add( Particle.fromString( input.get( i ), i ) );
		}
		return new ParticleSystem( particles );
	}
	
	/** @return All particles in this system */
	@Override
	public String toString( ) {
		final StringBuilder res = new StringBuilder( );
		for( final Particle p : particles ) res.append( p.toString( ) + "\n" );
		res.delete( res.length( ) - 1, res.length( ) );
		return res.toString( );
	}

}
