package aoc2019.day11;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCodeMachine;
import aocutil.geometry.Coord2D;

/**
 * The paint robot
 * 
 * @author Joris
 */
public class Robot extends IntCodeMachine {	
	/** The current position of the robot */
	protected Coord2D pos;
	
	/** The direction it is facing (in degrees, 0 being North) */
	protected int dir;
	
	
	/**
	 * Creates a new Robot
	 * 
	 * @param program The IntCode program that serves as the brain
	 */
	public Robot( final IntCode program ) {
		super( program, false );
		
		this.pos = new Coord2D( 0, 0 );
		this.dir = 0;
	}

	/** @return The current position */
	public Coord2D getPosition( ) { return pos; }
	
	/**
	 * Sets the position of the robot
	 * 
	 * @param pos The new position
	 */
	public void setPosition( final Coord2D pos ) {
		this.pos = pos;
	}
	
	/**
	 * Turns the robot by the specified amount of degrees and moves it forward
	 * one tile
	 * 
	 * @param rotation The degrees to rotate
	 */
	public void turnAndMove( final int rotation ) {
		setDirection( dir + rotation );
		setPosition( pos.moveDir( dir, 1 ) );
	}
	
	/**
	 * Sets the current direction of the robot
	 * 
	 * @param direction The new direction
	 */
	public void setDirection( final int direction ) {
		if( direction % 90 != 0 ) throw new RuntimeException( "Invalid direction for the robot: " + direction );
		this.dir = direction % 360;
		while( this.dir < 0 ) this.dir += 360;
	}
}
