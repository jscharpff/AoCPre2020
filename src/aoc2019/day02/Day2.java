package aoc2019.day02;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICException;

public class Day2 extends IntCodeChallenge2019 {
	/**
	 * Day 2 of the AoC 2019 challenge
	 * 
	 * @param args
	 * @throws Exception 
	 */	
	public static void main( final String[] args ) throws Exception {
		final IntCodeChallenge2019 day2 = new Day2( );
		day2.run( false );
	}
		
	/**
	 * Part 1, run the program and return the value at index 0
	 * 
	 * @param input
	 * @return
	 * @throws ICERuntimeException
	 */
	@Override
	public String part1( ) throws ICException {
		// parse instruction set, may contain expected result
		final IntCode IC = newIntCode( "1202 Program" );
 
		// change instructions at position 1 and 2
		IC.setIntcodeAt( 1, 12 );
		IC.setIntcodeAt( 2, 2 );

		IC.run( );

		return "" + IC.getIntcodeAt( 0 );
	}
	
	/**
	 * Determine the set of inputs (x,y) to the program that lead to a value of
	 * 19690720 at index 0 after program execution
	 * 
	 * @param input The program to run
	 * @return x * 100 + y for the et x,y that leads to the desired value
	 * @throws ICERuntimeException
	 */
	@Override
	public String part2( ) throws ICException {
		// change instructions at position 1 and 2
		for( int noun = 0; noun < 99; noun++ ) {
			for( int verb = 0; verb < 99; verb++ ) {
				// parse instruction set, may contain expected result
				final IntCode IC = newIntCode( "1202 Program" );

				IC.setIntcodeAt( 1, noun );
				IC.setIntcodeAt( 2, verb );

				// check if we got the desired output value
				IC.run( );
				if( IC.getIntcodeAt( 0 ) == 19690720 ) return "" + (noun * 100 + verb);
			}
		}
		
		throw new RuntimeException( "Failed to find correct input values" );
	}	
	
}
