package aoc2019.day07;

import java.util.List;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCode.IntCodeState;
import aoc2019.intcode.exceptions.ICException;
import aocutil.collections.CollectionUtil;

public class Day7 extends IntCodeChallenge2019 {

	/**
	 * Day 7 of the AoC 2018 challenge
	 * 
	 * @param args The command lien arguments
	 */
	public static void main( String[] args ) {
		final IntCodeChallenge2019 day7 = new Day7( );
		day7.run( true );
	}

	/**
	 * Part 1: find highest output signal value of a chain of 5 programs by trying
	 * all permutations of the input {1,2,3,4,5}.
	 * 
	 * @return The value of the highest output signal value
	 */
	@Override
	public String part1( ) throws ICException {
		// generate all combinations first
		final List<List<Integer>> inputs = CollectionUtil.generatePermutations( new Integer[] { 0, 1, 2, 3, 4 } );

		// for every combination, run the five programs. Store the highest signal value
		long maxsignal = Long.MIN_VALUE;		
		for( List<Integer> inp : inputs ) {
			long in = 0;
			for( int i : inp )
				in = runIntCode( "AMP", (long)i, in ).getIO( ).consume( );
			
			if( in > maxsignal ) maxsignal = in;
		}

		// return maximal value for output signal
		return "" + maxsignal;
	}

	/**
	 * Part 2: same as part 1 but now use 5 machines in a feedback loop configuration
	 * that keep re-using each others output until they terminate.
	 * 
	 * @return The maximal signal output value
	 */
	@Override
	public String part2( ) throws ICException {
		// generate all combinations
		final List<List<Integer>> inputs = CollectionUtil.generatePermutations( new Integer[] { 5, 6, 7, 8, 9 } );

		// for every combination, run the five programs in feedback loop configuration
		// determine the highest output value once all intcode machines are done
		long maxsignal = Long.MIN_VALUE;		
		for( List<Integer> inp : inputs ) {		
			// now initialise machines first, they will rerun using the same listing
			final IntCode[] AMP = new IntCode[ 5 ];
			for( int i = 0; i < 5; i++ ) {
				// create the machine and feed it the phase configuration
				AMP[i] = newIntCode( "AMP " + i, true );
				AMP[i].setInput( (long)inp.get( i ) );
				AMP[i].run( );
			}

			long in = 0;
			int running = AMP.length;
			int curramp = 0;
			while( running > 0 ) {
				// feed input in current machine and await its output
				AMP[curramp].feed( in );
				final IntCodeState state = AMP[curramp].resume( );
				if( state == IntCodeState.Ended ) running--;
				
				// get output of the machine and switch to the next
				in = AMP[curramp].consume( );
				curramp = (curramp + 1) % AMP.length;
			}
			
			System.out.println( inp  + ": " + in );
			
			if( in > maxsignal ) maxsignal = in;
		}

		// return maximal value for output signal
		return "" + maxsignal;
	}
}
