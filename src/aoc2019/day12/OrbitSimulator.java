package aoc2019.day12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

import aocutil.number.NumberUtil;

public class OrbitSimulator {
	/** The orbiting moons in this simulation */
	private List<Moon> moons;
	
	/**
	 * Creates a new simulation with the given list of moons
	 * 
	 * @param moons The moons to simulate
	 */
	private OrbitSimulator( final List<Moon> moons ) {
		this.moons = new ArrayList<>( moons );
	}
	
	/**
	 * Simulates the orbiting of moons
	 * 
	 * @param steps The number of steps to simulate
	 * @return The total energy at the end of the simulation
	 */
	public long simulate( final int steps ) {
		for( int i = 0; i < steps; i++ ) step( );
		return getEnergy( );
	}
	
	/**
	 * Simulates a single step
	 */
	private void step( ) {
		// apply gravity for every pair of moons
		for( int i = 0; i < moons.size( ) - 1; i++ ) {
			for( int j = i + 1; j < moons.size( ); j++ ) {
				applyGravity( moons.get( i ), moons.get( j ) );
			}
		}
		
		// apply velocity to each moon's position
		for( final Moon m : moons ) m.pos = m.pos.move( m.vel.x, m.vel.y, m.vel.z );
	}
	
	/**
	 * Finds the first step of the simulation that exactly matches a previously
	 * encountered state
	 * 
	 * @return The step at which the first repetition occurs
	 */
	public long findRepetition( ) {
		// as the moons affect each other per axis, simulate a couple of steps
		// until we encounter the first state at which the positions at a single
		// axis are the same as the beginning. The repetition for the entire state
		// is then the LCM of the per-axis period		
		int steps = 1;
		final long[] intervals = new long[ 3 ];
		final Set<Integer> axes = new HashSet<>( );
		axes.add( 0 ); axes.add( 1 ); axes.add( 2 );
		while( !axes.isEmpty( ) ) {			
			step( );
			steps++;
			
			for( final int axis : new HashSet<>( axes ) ) {
				if( !moons.stream( ).allMatch( m -> m.pos.values[axis] == m.initpos.values[axis] ) ) continue;
				intervals[axis] = steps;
				axes.remove( axis );
			}
		}
		
		return LongStream.of( intervals ).reduce( (x,y) -> NumberUtil.lowestCommonMultiplier( x, y ) ).getAsLong( );		
	}
	
	/**
	 * Applies the gravity effect that happens between a pair of moons. This
	 * updates the velocity of both moons
	 *  
	 * @param m1
	 * @param m2
	 */
	private void applyGravity( final Moon m1, final Moon m2 ) {
		// determine change in velocity per axis
		final int[] delta = new int[ 3 ];
		for( final int axis : new int[] { 0, 1, 2 } ) {
			if( m1.pos.values[axis] == m2.pos.values[axis] ) delta[axis] = 0;
			else delta[axis] = m1.pos.values[axis] < m2.pos.values[axis] ? 1 : -1;
		}
		
		// and apply to both moons
		m1.vel = m1.vel.move( delta[0], delta[1], delta[2] );
		m2.vel = m2.vel.move( -delta[0], -delta[1], -delta[2] );
	}
	
	/**
	 * Computes the total energy of the current state of the simulation
	 * 
	 * @return The total energy
	 */
	public long getEnergy( ) {
		return moons.stream( ).mapToLong( Moon::getEnergy ).sum( );
	}

	/**
	 * Recreates a simulation from the list of strings
	 * 
	 * @param input The description of the moons
	 * @return The {@link OrbitSimulator}
	 */
	public static OrbitSimulator fromStringList( final List<String> input ) {
		return new OrbitSimulator( input.stream( ).map( Moon::fromString ).toList( ) );
	}
	
	/** @return The description of the current states of the moons */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		for( final Moon m : moons ) {
			sb.append( m.toString( ) );
			sb.append( "\n" );
		}
		return sb.toString( );
	}
}
