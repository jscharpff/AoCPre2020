package aoc2015.day18;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;

/**
 * Represents a grid of lights that are animated step-wise
 * 
 * @author Joris
 */
public class AnimatedGrid {
	/** The grid of lights */
	private CoordGrid<Boolean> grid;
	
	/** Use the "always on" mode for the corner lights */
	private boolean cornersAlwayOn;
	
	/**
	 * Creates a new AnimatedGrid
	 * 
	 * @param grid The initial light grid
	 */
	private AnimatedGrid( final CoordGrid<Boolean> grid ) {
		this.grid = grid;
		this.cornersAlwayOn = false;
	}
	
	/**
	 * Enables/disables the "always on" mode for the corner lights
	 * 
	 * @param alwayson The new value for the mode
	 */
	public void setCornerMode( final boolean alwayson ) {
		this.cornersAlwayOn = alwayson;
	}
	
	/**
	 * Simulates the grid animations for specified number of steps
	 * 
	 * @param steps The number of steps
	 */
	public void simulate( final int steps ) {
		for( int i = 0; i < steps; i++ ) step( );
	}
	
	/**
	 * Simulates a single step of the animation in the grid
	 */
	private void step( ) {
		// create a new grid to reflect the new state
		final CoordGrid<Boolean> newgrid = new CoordGrid<>( false );
		newgrid.fixWindow( grid.window( ) );
		
		// make sure that the corner lights are considered active in the "always on" mode
		if( cornersAlwayOn ) {
			final Window2D win = grid.window( );
			grid.set( win.getMinX( ), win.getMinY( ), true );
			grid.set( win.getMaxX( ), win.getMinY( ), true );
			grid.set( win.getMinX( ), win.getMaxY( ), true );
			grid.set( win.getMaxX( ), win.getMaxY( ), true );
		}
				
		// go over all lights to determine their new state
		for( final Coord2D c : grid ) {
			final int oncount = c.getNeighbours( true ).stream( ).mapToInt( x -> grid.get( x ) ? 1 : 0 ).reduce( 0, Math::addExact );
			final boolean currstate = grid.get( c );
			final boolean newstate;
			if( currstate && (oncount == 2 || oncount == 3) ) {
				newstate = true;
			} else if( !currstate && (oncount == 3) ) {
				newstate = true;
			} else {
				newstate = false;
			}
			
			if( newstate ) newgrid.set( c, true );
		}
		
		// if the always on mode is activated, the grid's corners again need to be lit
		if( cornersAlwayOn ) {
			final Window2D win = newgrid.window( );
			newgrid.set( win.getMinX( ), win.getMinY( ), true );
			newgrid.set( win.getMaxX( ), win.getMinY( ), true );
			newgrid.set( win.getMinX( ), win.getMaxY( ), true );
			newgrid.set( win.getMaxX( ), win.getMaxY( ), true );
		}
		
		// swap grids
		grid = newgrid;
	}
	
	/** @return The count of active lights */
	public long getLightCount( ) {
		return grid.count( true );
	}
	
	/**
	 * Creates a new AnimatedGrid from a list of strings that describe the 
	 * initial light configuration
	 * 
	 * @param input The list of strings that describe the initial grid
	 * @return The AnimatedGrid
	 */
	public static AnimatedGrid fromStringList( final List<String> input ) {
		// create coordinate grid and fix its size
		final CoordGrid<Boolean> grid = CoordGrid.fromBooleanGrid( input, '#' );
		grid.fixWindow( );
		return new AnimatedGrid( grid );
	}
	
	/** @return The current grid as string */
	@Override
	public String toString( ) {
		return grid.toString( x -> x ? "#" : "." );
	}
}
