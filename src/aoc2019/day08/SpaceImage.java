package aoc2019.day08;

import java.util.ArrayList;
import java.util.List;

import aocutil.grid.CoordGrid;
import aocutil.number.NumberUtil;

/**
 * A curious encoding of images
 * 
 * @author Joris
 */
public class SpaceImage {
	/** The image size */
	private final int height, width;
	
	/** The layered image data */
	private final List<CoordGrid<Integer>> layers;
	
	/**
	 * Creates a new image
	 * 
	 * @param layers The layers of image data
	 */
	private SpaceImage( final List<CoordGrid<Integer>> layers ) {
		this.layers = new ArrayList<>( layers );
		this.height = layers.get( 0 ).size( ).y;
		this.width = layers.get( 0 ).size( ).x;
	}
	
	/**
	 * Finds the layer that has the least occurrences of the specified value
	 * 
	 * @param value The value to find
	 * @return The layer that has the least of the specified value
	 */
	public CoordGrid<Integer> getLayerWithFewest( final int value ) {
		CoordGrid<Integer> bestlayer = null;
		int lowest = Integer.MAX_VALUE;
		for( final CoordGrid<Integer> l : layers ) {
			final int count = (int)l.count( value );
			if( count < lowest ) {
				lowest = count;
				bestlayer = l;
			}
		}
		
		return bestlayer;
	}
	
	/**
	 * De-layers the image by using the first pixel value in its layers that
	 * does not equal 2 (transparent)
	 * 
	 * @return The single-layer image
	 */
	public CoordGrid<Integer> flatten( ) {
		final CoordGrid<Integer> flat = new CoordGrid<>( 0 );
		for( int y = 0; y < height; y++ )
			for( int x = 0; x < width; x++ ) {
				for( int l = 0; l < layers.size( ); l++ ) {
					final int p = layers.get( l ).get( x, y );
					if( p == 2 ) continue;
					flat.set( x, y, p );
					break;
				}
			}
		
		return flat;
	}
	
	/**
	 * Reconstructs the image's layers from input data
	 * 
	 * @param input The input data
	 * @param width The width of each layer
	 * @param height The height of each layer
	 * @return The SpaceImage
	 */
	public static SpaceImage fromString( final String input, final int width, final int height ) {
		final List<CoordGrid<Integer>> layers = new ArrayList<>( input.length( ) / (width * height) );
		final int[] digits = NumberUtil.toDigits( input );
		int idx = 0;
		
		while( idx < input.length( ) ) {
			final CoordGrid<Integer> l = new CoordGrid<>( 0 );
			for( int y = 0; y < height; y++ ) {
				for( int x = 0; x < width; x++ ) {
					l.set( x, y, digits[idx] );
					idx++;
				}
			}
			layers.add( l );
		}
		
		return new SpaceImage( layers );
	}

	/** @return The image layers */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		int i = 0;
		for( final CoordGrid<Integer> l : layers ) {
			sb.append( "Layer " + (++i) + ":\n" );
			sb.append( l.toString( ) );
			sb.append( "\n" );
		}
		return sb.toString( );
	}
}
