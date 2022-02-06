package aoc2017.day14;

import java.util.HashSet;
import java.util.Set;

import aoc2017.day10.HashKnot;
import aocutil.algorithm.BreadthFirstSearch;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.string.BitString;

/**
 * Defragmenter that generates a disk image from a hash key and identifies
 * regions of set bits
 * 
 * @author Joris
 */
public class Defragmenter {
	/** The knot hash key used for defragmentation */
	private final String hashkey;
	
	/**
	 * Creates a new defragmenter
	 * 
	 * @param hashkey The hash key used in the defragmentation process
	 */
	public Defragmenter( final String hashkey ) {
		this.hashkey = hashkey;
	}
	
	/**
	 * Generates the image of the disk from the hash key
	 * 
	 * @return The disk as grid
	 */
	public CoordGrid<Boolean> getDiskImage( ) {
		// create new CoordGrid to hold the outcome
		final CoordGrid<Boolean> disk = new CoordGrid<>( false );
		
		for( int row = 0; row < 128; row ++ ) {
			// get the hash of the row and convert it to 128 bits
			final String hash = HashKnot.hash( hashkey + "-" + row );
			final StringBuilder bs = new StringBuilder( );
			for( int i = 0; i < hash.length( ); i++ ) {
				bs.append( BitString.fromHex( "" + hash.charAt( i ) ).toString( ) );
			}
			
			// now build the disk grid from the bits
			final String bitstring = bs.toString( );
			for( int i = 0; i < bitstring.length( ); i++ ) {
				if( bitstring.charAt( i ) == '1' ) disk.set( i, row, true );
			}
		}
		
		return disk;
	}
	
	/**
	 * Counts the number of regions in the disk image
	 * 
	 * @param disk The disk image as a coord grid of bits
	 * @return The number of regions in the disk image
	 */
	public int getDiskRegions( final CoordGrid<Boolean> disk ) {
		final Set<Coord2D> visited = new HashSet<>( );
		int regions = 0;
		
		// explore the regions from every coordinate with bit value true
		for( final Coord2D c : disk.getKeys( ) ) {
			// already part of a region?
			if( visited.contains( c ) ) continue;
			
			// nope, explore all bits in this region
			final Set<Coord2D> bits = BreadthFirstSearch.getReachable( c, c1 -> disk.getNeighbours( c1, false, disk::get ) ).keySet( );
			visited.addAll( bits );
			regions++;
		}
		
		
		return regions;
	}

}
