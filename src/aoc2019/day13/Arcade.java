package aoc2019.day13;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCodeMachine;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.io.IOManager;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Arcade game that uses an intcode program to run its software
 * 
 * @author Joris
 */
public class Arcade extends IntCodeMachine {
	/** The current display */
	protected CoordGrid<Character> display;
	
	/** True if the display should be redrawn (new output available) */
	protected boolean dirty;
	
	/** The player score */
	protected int score;
	
	/**
	 * Creates a new arcade game
	 * 
	 * @param program The program to run
	 */
	public Arcade( final IntCode program ) {
		super( program, true );
		
		display = new CoordGrid<Character>( ' ' );
		dirty = true;
	}
	
	/**
	 * Activates the program and initialises the score counter
	 */
	@Override
	public void activate( ) throws ICERuntimeException {
		this.score = 0;
		super.activate( );
	}
	
	/** @return The current player score */
	public int getScore( ) { return score; }
	
	/**
	 * Performs a single tick of the arcade program
	 * 
	 * @param joypos The position of the joystick
	 * @return True iff the program is still running
	 * @throws ICERuntimeException 
	 */
	public boolean tick( final long joypos ) throws ICERuntimeException {
		program.feed( joypos );
		try {
			program.getIO( ).awaitReader( );
		} catch( ICEInputOutput e ) {
			return false;
		}
		dirty = true;
		return program.isRunning( );
	}
	
	/**
	 * @return The new display as a grid
	 * 
	 * @throws ICEInputOutput if no output was available
	 */
	public CoordGrid<Character> display( ) throws ICEInputOutput {
		// no updates?
		if( !dirty ) return display;
		
		// process all outputs and draw the screen
		final IOManager io = program.getIO( );
		while( io.hasOutput( ) ) {
			final int x = (int)io.consume( );
			final int y = (int)io.consume( );
			final int tilecode = (int)io.consume( );
			
			// a special tile is used to communicate score
			if( x == -1 && y == 0 ) {
				score = tilecode;
				continue;
			}

			// regular tile
			final char tile;
			if( tilecode == 0 ) tile = ' ';
			else if( tilecode == 1 ) tile = 'X';
			else if( tilecode == 2 ) tile = '#';
			else if( tilecode == 3 ) tile = '=';
			else if( tilecode == 4 ) tile = 'o';
			else throw new RuntimeException( "Unsupported tile code " + tilecode );
			
			display.set( new Coord2D( x, y ), tile );
		}
		
		// set dirty to false and return new display
		dirty = false;
		return display;
	}
}
