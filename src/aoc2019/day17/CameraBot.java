package aoc2019.day17;

import aoc2019.day11.Robot;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aocutil.grid.CoordGrid;

public class CameraBot extends Robot {
	/** The current camera view of the robot */
	protected CoordGrid<Character> view;

	/**
	 * Creates a new robot that uses the program as its camera
	 * 
	 * @param program The camera program
	 */
	public CameraBot( final IntCode program ) {
		super( program );
	}
		
	/** @return The current view */
	public CoordGrid<Character> getView( ) { return view; }
	
	/**
	 * Processes the camera output and updates its view
	 * 
	 * @return The camera output
	 * @throws ICEInputOutput if the output failed
	 */
	public CoordGrid<Character> processOutput( ) throws ICEInputOutput {
		// create new grid that automatically expands
		final CoordGrid<Character> newview = new CoordGrid<>( Day17.TILE_OPEN );
		
		// process characters
		int x = 0; int y = 0;
		while( getProgram( ).getIO( ).hasOutput( ) ) {
			// process output character
			final char c = (char)consume( );
			
			if( c != '\n' ) {
				newview.set( x, y, c );
				x++;
			} else {
				// double new line ends the input
				if( x == 0 ) break;
				y++;
				x = 0;
			}
		}
		
		view = newview;
		return view;
	}
}
