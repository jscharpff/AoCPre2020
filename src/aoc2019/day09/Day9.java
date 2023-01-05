package aoc2019.day09;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.exceptions.ICException;

public class Day9 extends IntCodeChallenge2019 {

	/**
	 * Day 9 of the AoC 2019
	 * @param args
	 */
	public static void main( String[] args ) {
		final IntCodeChallenge2019 day9 = new Day9( );
		day9.run( true );
	}
	
	/**
	 * Part 1: Simply run input program. If it terminates successfully, its 
	 *   output will be the required value
	 */
	@Override
	public String part1( ) throws ICException {
		return getOutput( runIntCode( "BOOST", 1 ) );
	}
	
	/**
	 * Part 2: Again simply run input program, now with a different input value
	 * to start a different routine. The output is the answer we are looking for
	 */
	@Override
	public String part2( ) throws ICException {
		return getOutput( runIntCode( "BOOST", 2 ) );
	}
}
