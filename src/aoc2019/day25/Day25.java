package aoc2019.day25;

import java.util.stream.Stream;

import aoc2019.day25.droids.AutoDroid;
import aoc2019.day25.droids.ManualDroid;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aocutil.io.FileReader;

public class Day25 {
	
	/**
	 * Day 25 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String input = new FileReader( Day25.class.getResource( "input.txt" ) ).readAll( );
	
		// manual or automatic mode?
		if( Stream.of( args ).anyMatch( s -> s.equals( "--manual" ) ) ) {
			System.out.println( "---[ Day 25 ]---" );
			manualMode( input );
			System.out.println( "Started manual mode, have fun!" );			
		} else {
			System.out.println( "---[ Day 25 ]---" );
			System.out.println( "Part 1 : " + part1( input ) );
		}
	}
	
	/**
	 * Runs the program using a manual droid such that it can be played as an
	 * interactive, text-based exploration game
	 * 
	 * @param input The IntCode program that the droid interacts with 
	 * @throws ICERuntimeException if the program halted unexpectedly 
	 */
	private final static void manualMode( final String input ) throws ICERuntimeException {
		final ManualDroid d = new ManualDroid( input );
		d.activate( );
	}
	
	/**
	 * Uses an automated droid to explore all rooms in the space ship, while
	 * concurrently picking up all (safe) items. Once all is collected, the droid
	 * moves to the security check point and starts the process to negotiate with
	 * the pressurised floor to find out the exact combination of items that will
	 * open the door to the password.
	 *   
	 * @param input The IntCode program that the droid interacts with 
	 * @return The passcode after successful exploring the ship and negotiating
	 *   the pressurised floor checkpoint 
	 * @throws ICERuntimeException if the IntCode program halted for any
	 *   unexpected reason
	 */
	protected static String part1( final String input ) throws ICERuntimeException {
		final AutoDroid d = new AutoDroid( input );
		return d.run( );
	}
}
