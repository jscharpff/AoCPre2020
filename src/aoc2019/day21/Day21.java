package aoc2019.day21;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.exceptions.ICException;
import aocutil.string.StringUtil;

public class Day21 extends IntCodeChallenge2019 {
	/**
	 * Day 21 of the AoC 2019
	 *
	 * @param args The command line arguments
	 */
	public static void main( final String[] args ) {
		final IntCodeChallenge2019 day21 = new Day21( );
		day21.run( );
	}
	
	/**
	 * Part 1: Feed the jumpbot instructions to jump over all holes, return result
	 * if all holes have been negotiated successfully
	 */
	@Override
	public String part1( ) throws ICException {
		
		final JumpBot bot = new JumpBot( newIntCode( "JumpBot" ) );
		bot.activate( );
		
		// input program to the bot to make the jump at the right moment
		bot.feedASCII( 
				"NOT A J",
				"NOT B T",
				"OR T J",
				"NOT C T",
				"OR T J",
				"AND D J"
			);
		
		// and start the movement
		bot.feedASCII( "WALK" );
		bot.activate( );
		
		// if the robot was successful, its last output is the hull damage
		if( !bot.getProgram( ).getIO( ).hasOutput( ) ) return "(failed)";
		return "" + bot.consume( );
	}
	
	/**
	 * Part 2: Same thing but now with extra registers for additional look-ahead
	 */	
	@Override
	public String part2( ) throws ICException {
		final JumpBot bot = new JumpBot( newIntCode( "JumpBot" ) );
		bot.activate( );
		
		// input program to the bot to make the jump at the right moment
		bot.feedASCII(

				"NOT C J", 
				"AND D J", 
				"AND H J",

				"NOT B T", 
				"AND D T", 
				"OR T J",

				"NOT A T", 
				"OR T J"

			);
		
		// and start the movement
		bot.feedASCII( "RUN" );
		bot.activate( );
		
		// if the robot was successful, its last output is the hull damage
		if( !bot.getProgram( ).getIO( ).hasOutput( ) ) return "(failed)";
		return StringUtil.fromArray( bot.getProgram( ).getOutput( ) );
	}
}
