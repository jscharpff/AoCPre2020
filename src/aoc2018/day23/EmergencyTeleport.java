package aoc2018.day23;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Coord3D;

/**
 * An emergency teleporter that uses nanobots
 * 
 * @author Joris
 */
public class EmergencyTeleport {
	/** The set of nanobots used in the device */
	private Set<Nanobot> bots;
	
	/**
	 * Creates a new emergency teleport device
	 * 
	 * @param bots The set of nanobots that the device uses
	 */
	private EmergencyTeleport( final Set<Nanobot> bots ) {
		this.bots = new HashSet<>( bots );
	}
	
	/**
	 * @return The number of bots in range of the strongest nanobot's scanner
	 */
	public long countInRangeOfStrongest( ) {
		// get the strongest
		final long maxr = bots.stream().mapToLong( Nanobot::getRadius ).max( ).getAsLong( );
		final List<Nanobot> strongest = bots.stream( ).filter( b -> b.getRadius( ) == maxr ).toList( );

		// count all bots within range of the strongest nanobot(s) and return the
		// highest count
		return strongest.stream( ).mapToLong( b -> bots.stream( ).filter( b2 -> b.inRange( b2 ) ).count( ) ).max( ).getAsLong( );
	}
	
	/**
	 * Finds the set of coordinates in range of the most scanners and returns
	 * the smallest Manhattan distance from any of these coordinates to the given
	 * coordinate
	 * 
	 * @param pos The position
	 * @return The smallest Manhattan distance from the position to any of the
	 *   positions with the best nanobot scanner range coverage
	 */
	public long getBestCoverageDistance( final Coord3D pos ) {
		// first determine the largest cluster of bots with overlapping scanner ranges
		final NanoCluster cluster = NanoCluster.biggest( bots );

		// then find largest Manhattan distance to the scanners of this cluster
		return cluster.getBots( ).stream( ).mapToInt( b -> b.getDistanceToScanRange( pos ) ).max( ).getAsInt( );
	}

	/**
	 * Reconstructs the emergency teleporter from the list of strings
	 * 
	 * @param input The list of strings describing the nanobots of the device
	 * @return The device
	 */
	public static EmergencyTeleport fromStringList( final List<String> input ) {
		final Set<Nanobot> nanos = new HashSet<>( input.size( ) );
		for( int i = 0; i < input.size( ); i++ ) nanos.add( Nanobot.fromString( i, input.get( i ) ) );
		return new EmergencyTeleport( nanos );
	}
}
