package aoc2018.day10;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;

/**
 * A message that is transmitted using actual stars!
 * 
 * @author Joris
 */
public class StarMessage {
	/*** The stars */
	private final List<Star> stars;
	
	/** The window in which all stars reside */
	private Window2D window;
	
	/**
	 * Creates a new message from a description of stars
	 * 
	 * @param input The list of stars
	 */
	public StarMessage( final List<String> input ) {
		this.stars = new ArrayList<>( input.stream( ).map( Star::fromString ).toList( ) );
		this.window = new Window2D( );
		window.resize( stars.stream( ).map( Star::getPosition ).toList( ) );
	}
	
	/**
	 * Moves all the starts by a single step, also updates the range in which
	 * stars reside currently
	 */
	public void move( ) {
		final Window2D newwindow = new Window2D( );
		for( final Star s : stars ) {
			final Coord2D c = s.move( );
			newwindow.include( c );
		}
		window = newwindow;
	}
	
	/**
	 * Determines the window in which all stars are contained
	 * 
	 * @return The max distance as a coord
	 */
	public Window2D getWindow( ) {
		return window;
	}
	
	/**
	 * Creates a grid that visualises the 2D positions of the starts
	 * 
	 * @return The gird
	 */
	@Override
	public String toString( ) {
		final CoordGrid<Boolean> grid = new CoordGrid<>( false );
		for( final Star s : stars ) grid.set( s.getPosition( ), true );
		return grid.toString( x -> x ? "#" : "." );
	}
}
