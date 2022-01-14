package aoc2017.day21;

import java.util.HashSet;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.string.BitString;

/**
 * A single fractal pattern that can be matched
 * 
 * @author Joris
 */
public class FractalPattern {
	/** The pattern string */
	private final String patternstring;
	
	/** The pattern bit encoding */
	private final BitString pattern;
	
	/** The pattern size */
	private final int size;
	
	/** All pre-computed rotations/flips of this pattern to match against */
	private final Set<BitString> matchstrings;
	
	/**
	 * Creates a new fractal pattern from the string description
	 * 
	 * @param pattern The string describing the pattern
	 */
	public FractalPattern( final String pattern ) {
		this.patternstring = pattern;
		this.pattern = BitString.fromString( pattern.replaceAll( "\\.", "0" ).replaceAll( "#", "1" ).replaceAll( "/", "" ) );
		this.size = pattern.indexOf( "/" );
		this.matchstrings = buildMatchStrings( );
	}
	
	/**
	 * Builds the encoded pattern strings for all rotations and flips of the
	 * pattern, used in the matching function
	 * 
	 *  @return The array of all bitstring matches
	 */
	private Set<BitString> buildMatchStrings( ) {
		final Set<BitString> bm = new HashSet<>( 8 );
		
		// generate the grid of the pattern, apply transformations and store their
		// bit string encodings
		CoordGrid<Boolean> grid = toGrid( );
		for( int r = 0; r < 4; r++ ) {
			bm.add( fromGrid( grid.rotate( r ) ) );
			bm.add( fromGrid( grid.rotate( r ).flip( false ) ) );
		}

		return bm;
	}
	
	/**
	 * Checks whether the given pixels in the coordgrid matches the pattern, in
	 * any of its rotations or flips
	 * 
	 * @param pixels The pixels to match
	 * @return True if any of the transformations of the pattern matches the
	 *   pixels in the image 
	 */
	public boolean matches( final CoordGrid<Boolean> pixels ) {
		if( pixels.size( ).x != size ) return false;		
		return matchstrings.contains( fromGrid( pixels ) );
		
	}
	
	/**
	 * Reconstruct a BitString from a grid
	 * 
	 * @param grid The CoordGrid<Boolean> that captures the pixel data
	 * @return The bit string representation of the grid
	 */
	public static BitString fromGrid( final CoordGrid<Boolean> grid ) {
		final BitString b = new BitString( grid.size( ).x * grid.size( ).y );
		
		for( final Coord2D key : grid.getKeys( ) ) {
			final Coord2D c = grid.getRelative( key );
			b.set( b.length( ) - 1 - (c.x + c.y * grid.size( ).x ) );
		}
		
		return b;
	}
	
	/**
	 * Reconstructs a CoordGrid from the pattern
	 * 
	 * @return A CoordGrid that contains the pixels in this pattern
	 */
	public CoordGrid<Boolean> toGrid( ) {
		final CoordGrid<Boolean> image = new CoordGrid<Boolean>( size, size, false );
		
		for( int i = 0; i < pattern.length( ); i++ ) {
			if( pattern.get( pattern.length( ) - i - 1 ) ) image.set( new Coord2D( i % size, i / size), true );
		}
		return image;
	}
	
	/** @return The pattern as bitstring */
	public BitString toBits( ) {
		return pattern;
	}
	
	/** @return The size of the pattern (assuming equal width and height) */
	public int size( ) { return size; }
	
	/** @return The pattern in a single line string*/
	@Override
	public String toString( ) {
		return patternstring;
	}
}
