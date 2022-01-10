package aoc2016.day08;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;

/**
 * A door screen that displays a code at the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class DoorScreen {
	/** The grid op pixels in the screen */
	private final CoordGrid<Boolean> pixels;
	
	/**
	 * Creates a new door screen
	 * 
	 * @param width The width of the screen
	 * @param height The height of the screen
	 */
	public DoorScreen( final int width, final int height ) {
		pixels = new CoordGrid<>( false );
		pixels.fixWindow( new Coord2D( 0, 0 ), new Coord2D( width - 1, height - 1 ) );
	}
	
	/**
	 * Activates the pixels in the rectangular area WxB, starting from the top
	 * left pixel
	 * 
	 * @param W The width of the area
	 * @param H The height of the area 
	 */
	private void on( final int W, final int H ) {
		pixels.set( new Window2D( 0, 0, W - 1, H - 1 ), true );
	}
	
	/**
	 * Rotates the pixels at the specified row or column by the given number.
	 * Pixels that are rotated off the screen will be wrapped around.
	 * 
	 * @param index The row index to rotate about
	 * @param rotations The amount to rotate
	 * @param horizontal True for column rotation, false for row 
	 */
	private void rotate( final int index, final int rotations, final boolean horizontal ) {
		final int N = horizontal ? pixels.size( ).x : pixels.size( ).y;
		
		// first build new row/column values by rotating
		boolean[] rotated = new boolean[ N ];
		for( int i = 0; i < N; i++ ) {
			final int pos = (i + N - rotations) % N;
			rotated[ i ] = pixels.get( new Coord2D( horizontal ? pos : index, !horizontal ? pos : index ) ); 
		}
		
		// then update the grid with the new row/column
		for( int i = 0; i < N; i++ )
			pixels.set( horizontal ? i : index, !horizontal ? i : index, rotated[i] );		
	}
	
	/**
	 * Process a list of commands to update the screen
	 * 
	 * @param input The list of commands, one per string
	 */
	public void process( final List<String> input ) {
		for( final String s : input ) {
			// rect: activates pixels in a rectangular area
			Matcher m = Pattern.compile( "rect (\\d+)x(\\d+)" ).matcher( s );
			if( m.find( ) ) {
				on( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
				continue;
			}
			
			// rotate: rotates a row or column
			m = Pattern.compile( "rotate (column|row) [xy]=(\\d+) by (-?\\d+)" ).matcher( s );
			if( m.find( ) ) {
				rotate( Integer.parseInt( m.group( 2 ) ), Integer.parseInt( m.group( 3 ) ), "row".equals( m.group( 1 ) ) );
				continue;
			}
			
			// unknown!
			throw new IllegalArgumentException( "Invalid command: " + s );
		}
	}
	
	/**
	 * @return The number of active pixels
	 */
	public long count( ) {
		return pixels.count( true );
	}
	
	/**
	 * @return The textual representation of the screen's pixels
	 */
	@Override
	public String toString( ) {
		return pixels.toString( x -> (x ? "#" : "." ) );
	}
}
