package aoc2019.day22;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * A deck of space cards that can be shuffled
 * 
 * @author Joris
 */
public class SpaceDeck {	
	/** The shuffle instructions */
	private final List<String> instr;
	
	/** The size of the deck as Big Integer */
	private final BigInteger decksize;
	
	/**
	 * Creates a new deck of size N with cards numbered 0 to N-1
	 * 
	 * @param N The number of cards in the deck
	 * @param instr The instruction on how to shuffle the deck
	 */
	public SpaceDeck( final long N, final List<String> instr ) {
		this.decksize = BigInteger.valueOf( N );
		this.instr = new ArrayList<>( instr );
	}
	
	/**
	 * Shuffles the deck according to the specified instructions
	 * 
	 * @param card The card to follow during shuffle
	 * @param shuffles The amount of times to shuffle the deck
	 * @param reverse True to perform reverse shuffling
	 * @return The final position of the card after shuffling
	 */
	public long shuffle( final long card, final long shuffles, final boolean reverse ) {
		// build function that corresponds to shuffling n times
		final BigLinearFunc f = getShuffleFunction( shuffles, reverse );

		// and apply it on the card or target position if reversed
		// use BigInteger to be sure that we are not overflowing
		return f.apply( BigInteger.valueOf( card ) ).mod( decksize ).longValueExact( );
	}	
	
	/**
	 * Creates the function that is the result of repeating the shuffle function
	 * n times
	 * 
	 * @param shuffles The number of shuffles to perform
	 * @return The resulting shuffle function 
	 */
	private BigLinearFunc getShuffleFunction( final long shuffles, final boolean reverse ) {
		// get function that corresponds to shuffling once
		// and apply it the specified number of times
		final BigLinearFunc func = createShuffleFunc( reverse );
		return getShuffleFunction( func, shuffles );
	}
	
	/**
	 * Implementation of the repeated squaring algorithm to construct the linear
	 * function f^n(x) that corresponds to aggregating function f(x) n times
	 *  
	 * @param f The current function we are building
	 * @param shuffles The number of shuffles remaining
	 * @return The (intermediate) aggregate function
	 */
	private BigLinearFunc getShuffleFunction( final BigLinearFunc f, final long shuffles ) {
		// determine recursion based upon the number of shuffles left
		if( shuffles == 0 ) {
			// no more shuffles, we are done
			return new BigLinearFunc( 1, 0 );
		} else if( shuffles % 2 == 0 ) {
			// number of shuffles is perfectly divisible by two, square its
			// coefficient and halve its remaining shuffles 
			return getShuffleFunction( new BigLinearFunc( f.a.multiply( f.a ).mod( decksize ), f.a.multiply( f.b ).add( f.b ).mod( decksize ) ), shuffles / 2 );
		} else {
			// number of shuffles is not a power of two, simply apply one more
			// aggregation of f until it is divisible by 2 again or no more shuffles
			// remain
			final BigLinearFunc g = getShuffleFunction( f, shuffles - 1 );
			return new BigLinearFunc( f.a.multiply( g.a ).mod( decksize ), f.a.multiply( g.b ).add( f.b ).mod( decksize ) );
		}
	}
	
	/**
	 * Constructs the linear function that corresponds to the list of shuffle
	 * instructions, or its reverse
	 * 
	 * @param reverse True to return the inverse of the shuffle function
	 * @return The linear function y = ax + b that corresponds to performing all
	 *   shuffles, such that x is the original card position and y the position
	 *   it ends up in after performing all shuffles. If reverse is set to true,
	 *   this function will return the linear function that corresponds to
	 *   performing the inverse of each operation, aggregated in reverse order.
	 */
	private BigLinearFunc createShuffleFunc( final boolean reverse ) {
		// start with identity function and aggregate with functions corresponding
		// to the instructions in the shuffle set
		BigLinearFunc f = new BigLinearFunc( 1, 0 );
		
		for( int idx = 0; idx < instr.size( ); idx++ ) {
			final String i = instr.get( !reverse ? idx : instr.size( ) - idx - 1 );

			// convert the instruction into a linear function
			final BigLinearFunc g;
			if( i.equals( "deal into new stack" ) ) {
				// new stack, simply "reverse" position 
				g = new BigLinearFunc( -1, -1 );
			} else 			
			if( i.startsWith( "cut" ) ) {
				// cut top/bottom N cards, add/subtract the function offset
				long cutoff = Long.parseLong( i.split( " " )[1] );
				g = new BigLinearFunc( 1, cutoff * (!reverse ? -1 : 1) );
			} else 			
			if( i.startsWith( "deal with increment" ) ) {
				// deal increment
				final long inc = Long.parseLong( i.substring( i.lastIndexOf( ' ' ) + 1 ) );
				if( !reverse ) {
					// multiply position by the increment factor
					g = new BigLinearFunc( inc, 0 );
				} else {
					// revert the increment by finding the inverse of the modulo
					g = new BigLinearFunc( BigInteger.valueOf( inc ).modInverse( decksize ).mod( decksize ), BigInteger.valueOf( 0 )  );
				}
			} else {
				throw new IllegalArgumentException( "Invalid shuffle istruction: " + i );
			}
			
			// combine the new function with the current aggregate and apply a modulo
			// by deck size to keep its coefficients manageable
			f = f.aggregate( g ).mod( decksize );
		}

		// return the resulting function
		return f;
	}
}
