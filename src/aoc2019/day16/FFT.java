package aoc2019.day16;

import aocutil.number.NumberUtil;
import aocutil.string.StringUtil;

/**
 * The Flawed Frequency Transmission protocol for communication signals 
 * 
 * @author Joris
 */
public class FFT {
	
	/**
	 * Transforms the input signal for the given number of phases using the FFT
	 * protocol
	 * 
	 * @param input The input signal
	 * @param phases The number of phases
	 * @return The output signal after all phases
	 */
	public static String transform( final String input, final int phases ) {
		int[] signal = input.chars( ).map( c -> (int)c - '0' ).toArray( );
		for( int i = 0; i < phases; i++ ) signal = phase( signal );
		
		// reconstruct string from the array
		final StringBuilder sb = new StringBuilder( );
		for( final int i : signal ) sb.append( i );
		return sb.toString( );
	}
	
	/**
	 * Computes the next phase
	 * 
	 * @param input The input array
	 * @return The new array of signals
	 */
	private static int[] phase( final int[] input ) {
		// new phase
		final int[] next = new int[ input.length ];
		
		// compute summation table
		final int[] S = new int[ input.length ];
		S[0] = input[0];
		for( int i = 1; i < input.length; i++ ) S[i] = S[i-1] + input[i];

		// get next value for every signal
		for( int i = 0; i < input.length; i++ ) next[i] = getNext( input, S, i );
		
		
		return next;		
	}
	
	/**
	 * Computes the next value for the signal at position i
	 * 
	 * @param input The input signals
	 * @param S The summation table of the signals
	 * @param i The index to get the next signal value for
	 * @return The next signal value
	 */
	private static int getNext( final int[] input, final int[] S, final int i ) {
		final int n = i + 1;
		final int maxidx = input.length - 1;
		int next = 0;
		
		// add ranges with +1 pattern
		for( int idx = -1; idx + n < input.length; idx += 4 * n ) {
			final int s = idx + n;
			final int e = Math.min( idx + n * 2 - 1, maxidx );
			if( s >= input.length ) break;

			next += S[e] - (s > 0 ? S[s - 1] : 0); 
		}
		
		// and remove ranges with -1 pattern
		for( int idx = -1; idx < input.length; idx += 4 * n ) {
			final int s = idx + n * 3;
			final int e = Math.min( idx + n * 4 - 1, maxidx );
			if( s >= input.length ) break;
			
			next -= S[e] - (s > 0 ? S[s - 1] : 0); 
		}
		
		return Math.abs( next ) % 10;
	}
	
	/**
	 * Decodes the message hidden within the repeated signal
	 * 
	 * @param input The input signal
	 * @param repeat The number of repetitions
	 * @param phases The number of phases to apply to the repeated signal
	 * @return The message that is hidden within the signal
	 */	
	public static String decode( final String input, final int repeat, final int phases ) {
		// convert to digits and get offset
		int[] in = input.chars( ).map( c -> (int)c - '0' ).toArray( );
		final int offset = Integer.parseInt( input.substring( 0, 7 ) );		

		// apply repetition
		int[] signal = new int[ in.length * repeat ];
		for( int i = 0; i < repeat; i++ )
			for( int j = 0; j < in.length; j++ )
				signal[ i * in.length + j ] = in[j];
		
		// apply phases
		for( int i = 0; i < phases; i++ ) {
			signal = phase( signal );
		}
		
		// get offset of target value
		return StringUtil.padLeft( "" + NumberUtil.fromDigits( signal, offset, 8 ), 8, '0' );
	}
}
