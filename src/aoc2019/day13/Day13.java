package aoc2019.day13;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICException;
import aocutil.Util;
import aocutil.grid.CoordGrid;

public class Day13 extends IntCodeChallenge2019 {
	/**
	 * Day 13 of the AoC 2019
	 * 
	 * @param args The command line arguments
	 */
	public static void main( String[] args ) {
		final IntCodeChallenge2019 day13 = new Day13( );
		day13.useWindow( "Arcade" );
		day13.run( );
	}

	/**
	 * Part 1: run the arcade program and count the number of blocks on the screen
	 */
	@Override
	public String part1( ) throws ICException {
		final Arcade arcade = new Arcade( newIntCode( "Arcade" ) );
		arcade.activate( );
		
		// wait for the program to finish
		while( arcade.isActive( ) ) {
			Util.sleep( 10 );
		}
		
		// get resulting display
		final CoordGrid<Character> display = arcade.display( );
		return "" + display.count( '#' );
	}

	/**
	 * Part 2: play the arcade program!
	 */
	@Override
	public String part2( ) throws ICException {
		// load the program but hack the coin input!
		final IntCode program = newIntCode( "Arcade" );
		program.setIntcodeAt( 0, 2 );
		
		// now we can run the program
		final Arcade arcade = new Arcade( program );
		arcade.activate( );
		
		while( arcade.isActive( ) ) {
			// wait 10 msec for new input
			Util.sleep( 200 );
			
			// determine Joystick input position
			final long joystick = (long)Math.round( Math.random( ) * 2.0 - 1.0 );
			if( arcade.tick( joystick ) ) {
				output( "Score: " + arcade.getScore( ) + "\n" + arcade.display( ) );
			} else {
				output( "--- GAME OVER ---" );
			}
		}
		
		// get the resulting score
		return "" + arcade.getScore( );
	}
	
	protected static void output( final String text ) {
		if( isVisual( ) )
			display( text );
		else
			System.out.println( "\n\n" + text );
	}

}
