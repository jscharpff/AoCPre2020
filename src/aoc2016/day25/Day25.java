package aoc2016.day25;

import java.util.List;

import aoc2016.day25.AssemBunnyAntenna.OutputConsumer;
import aocutil.io.FileReader;

public class Day25 {

	/**
	 * Day 25 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> input = new FileReader( Day25.class.getResource( "day25_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Day 25 ]---" );
		System.out.println( "Answer : " + part1( input ) );
	}
	
	/**
	 * Finds the smallest integer input to the assembunny program that runs on
	 * the antenna that produces an alternating signals of ones and zeroes
	 * 
	 * @param input The antenna program   
	 * @return The smallest integer
	 */
	private static long part1( final List<String> input ) {
		final AssemBunnyAntenna aba = new AssemBunnyAntenna( );
		for( int i = 0; i < Integer.MAX_VALUE; i++ ) {
			
			// consume signals and interrupt if we have enough signals
			aba.setOutputConsumer( new OutputConsumer( ) {
				private int count = 0;
				private long prev = 1;
				
				@Override
				public void output( long value ) throws InterruptException {
					// interrupt with status 1, invalid sequence
					if( value != 1 - prev ) throw new InterruptException( 1 );

					// another correct signal, keep counting until we find 10
					prev = value;
					if( ++count >= 10 )	throw new InterruptException( 0 );
				}
			} );
			try { 
				aba.runProgram( input, i );
			} catch( InterruptException ie ) {
				// did we find enough subsequent alternating signals  
				if( ie.getCode( ) == 0 ) return i;
			}
		}
			
		return -1;
	}
}
