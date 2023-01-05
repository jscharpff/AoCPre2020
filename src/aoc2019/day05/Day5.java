package aoc2019.day05;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICException;
import aocutil.string.StringUtil;

public class Day5 extends IntCodeChallenge2019 {
	/**
	 * Day 5 of the AoC 2019 challenge
	 * 
	 * @param args
	 * @throws Exception 
	 */	
	public static void main( final String[] args ) {
		final IntCodeChallenge2019 day5 = new Day5( );
		
		day5.run( false );
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
		return StringUtil.fromArray( runIntCode( "TEST", 1 ).getOutput( ) );
	}
	
	@Override
	public String part2( ) throws ICException {
		return StringUtil.fromArray( runIntCode( "TEST", 5 ).getOutput( ) );
	}
}
