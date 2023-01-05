package aoc2019.day22;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.number.NumberUtil;

public class SpaceDeck2 {
	/** The number of cards in the deck */
	private final long decksize;
	
	/** The shuffle instructions to perform */
	private List<String> instr;
	
	/**
	 * Creates a new deck of size N with cards numbered 0 to N-1
	 * 
	 * @param N The number of cards in the deck
	 * @param instr The instruction on how to shuffle the deck
	 */
	public SpaceDeck2( final long N, final List<String> instr ) {
		this.instr = new ArrayList<>( instr );
		this.decksize = N;
	}
	
	/**
	 * Shuffles the deck according to the specified instructions
	 * 
	 * @param card The card to follow during shuffle
	 * @param shuffles The amount of times to shuffle the deck
	 * @return The final position of the card after shuffling
	 */
	public long shuffle( final long card, final long shuffles ) {
		long pos = card;
		
		for( long s = 0; s < shuffles; s++ ) {
			for( final String i : instr ) {
	
				// new stack
				if( i.equals( "deal into new stack" ) ) {
					pos = decksize - pos - 1;
					continue;
				}
				
				// cut top/bottom N cards
				if( i.startsWith( "cut" ) ) {
					long cutoff = Long.parseLong( i.split( " " )[1] );
					if( cutoff < 0 ) cutoff = decksize + cutoff;
					
					// is the position part of the set that was moved?
					if( pos <= cutoff ) {
						pos += (decksize - cutoff);
					} else {
						// nope, just shift its position
						pos -= cutoff;
					}
					pos = Long.remainderUnsigned( pos, decksize );
					continue;
				}
				
				// deal increment
				if( i.startsWith( "deal with increment" ) ) {
					final long inc = Long.parseLong( i.substring( i.lastIndexOf( ' ' ) + 1 ) );
					pos = Long.remainderUnsigned( pos * inc, decksize );
					continue;
				}
				
				throw new IllegalArgumentException( "Invalid shuffle istruction: " + i );
			}
		}
		
		return pos;
	}


	/**
	 * Finds the card that ends up at the given position after applying a number
	 * of shuffles. Useful when dealing with large card decks
	 * 
	 * @param decksize The number of cards in the deck
	 * @param position The position of the card to return
	 * @param shuffles The number of shuffles to perform
	 * @param instr The shuffle instructions
	 * @return The value of the card at the specified position after all shuffles
	 *   have been applied
	 */
	public long trace( final long position, final long shuffles ) {
		// trace the position of the card backwards, i.e. start with the given
		// position and reverse shuffling until we find the position of the
		// original card.
		long pos = position;
		
		final Map<Long, Long> H = new HashMap<>( );
		H.put( pos, 0l );
		
		// reverse shuffles until we find a cycle
		for( long s = 0; s < shuffles; s++ ) {
			if( s % 10000 == 0 ) System.out.println( "s = " + s + ": " + pos );

			pos = reverseShuffle( pos );
			
			if( H.containsKey( pos ) ) {
				final long ival = s - H.get( pos );
				final long rem = Long.remainderUnsigned( shuffles - s - 1, ival );
				System.err.println( "Repeated position " + pos + " in shuffle " + s + " at interval " + ival + ", remaining " + rem );
				if( rem == 0 ) return pos;
			}
			H.put( pos, s );
		}
		
		// return the resulting position, i.e. the card we started from
		return pos;
	}
	
	/**
	 * Performs a single reverse shuffling, keeping track of the specified 
	 * position while reversing
	 * 
	 * @param position The position of card to follow
	 * @return The position the card was originally in, before shuffling
	 */
	private long reverseShuffle( final long position ) {
		long pos = position;
		
		// go over shuffle instructions backwards
		for( int idx = instr.size( ) - 1; idx >= 0; idx-- ) {
			final String i = instr.get( idx );
						
			// new stack, simply "reverse" position 
			if( i.equals( "deal into new stack" ) ) {
				pos = decksize - pos - 1;
				continue;
			}
			
			// cut top/bottom N cards
			if( i.startsWith( "cut" ) ) {
				long cutoff = Long.parseLong( i.split( " " )[1] );
				if( cutoff < 0 ) cutoff = decksize + cutoff;
				
				// is the position part of the set that was moved?
				if( pos >= decksize - cutoff ) {
					pos -= decksize - cutoff;
				} else {
					// nope, just shift its position
					pos += cutoff;
				}
				pos = Long.remainderUnsigned( pos, decksize );
				
				continue;
			}
			
			// deal increment
			if( i.startsWith( "deal with increment" ) ) {
				final long inc = Long.parseLong( i.substring( i.lastIndexOf( ' ' ) + 1 ) );
				
				// use extended Euclidean algorithm to determine the Bézour coefficient
				// that says how many times the original modulo was applied
				final long[] ee = NumberUtil.extendedEuclidean( Long.remainderUnsigned( inc, decksize ), decksize );
				
				// the product might be very big, use BigInteger for arithmetic. The 
				// last module operation will ensure it is within the long range again
				pos = BigInteger.valueOf( pos ).multiply( BigInteger.valueOf( ee[1] ) ).mod( BigInteger.valueOf( decksize ) ).longValue( );
				continue;
			}
			
			throw new IllegalArgumentException( "Invalid shuffle istruction: " + i );
		}		
		
		return pos;
	}
}
