package aoc2019.day15;

import aoc2019.day11.Robot;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.geometry.Coord2D;

public class RepairDroid extends Robot {
	/**
	 * Creates a new RepairDroid
	 * 
	 * @param program The program it uses as its brain
	 */
	public RepairDroid( final IntCode program ) {
		super( program );
	}
	
	/**
	 * Tries to move droid in its current direction
	 * 
	 * @return The new position the droid is trying to move to
	 * @throws ICERuntimeException if the program failed
	 */
	public Coord2D checkMove( ) throws ICERuntimeException {
		// convert direction into correct signal
		final int signal;
		if( dir == 0 ) signal = 1; // N
		else if( dir == 90 ) signal = 4; // E
		else if( dir == 180 ) signal = 2; // S
		else if( dir == 270 ) signal = 3; // W
		else throw new RuntimeException( "Invalid direction: " + dir );
		
		feed( signal );
		activate( );
		
		// return the (would be) new position
		return pos.moveDir( dir, 1 );
	}

}
