package aoc2015.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class that determines the presents a house will get, given infinite elves
 * such that each elf xn delivers presents to all houses n + 2n + 3n + ...
 * 
 * @author Joris
 */
public class PresentDelivery {
	
	/**
	 * Determines the first house to get the specified number of presents
	 * 
	 * @param presents The number of presents that the house is to receive
	 * @param maxhouses The maximum number of houses that the elves will visit
	 * @return The first house number that has received at least the specified
	 *   number of presents
	 */
	public static int getFirstHouseToGet( final long presents, final int maxhouses ) {
		// simply try next house number until we found the required amount of
		// presents
		long currpresents = 0;
		int house = 0;
		while( currpresents < presents ) {
			currpresents = getPresentsForHouse( ++house, maxhouses );
			if( house % 10000 == 0 ) System.out.println( house + ": " + currpresents );
		}
		return house;
	}
	
	/**
	 * Determines the number of presents the house with the given number will
	 * receive from all elves that visit the house.
	 * 
	 * @param house The house number
	 * @param maxhouses The maximum number of houses visited by the elves
	 * @return The number of presents that the house will receive
	 */
	private static long getPresentsForHouse( final int house, final int maxhouses ) {
		// find and all non-fractional divisors of the house number, this list
		// equals the intervals of the visiting elves
		if( maxhouses == -1 )
			// no maximum number of visits, return all divisors times 10
			return calcPresents( house, -1, 10 );
		else
			// with a maximum number of visits, we are only interested in the
			// divisors up to the limit of visits. We do give them 11 presents per
			// visit though...
			return findIntegerDivisors( house, maxhouses ).stream( ).map( x -> house / x ).reduce( Math::addExact ).get( ) * 11;
	}
	
	private static long calcPresents( final long number, long maxdivisor, final long numpresents ) {
		// collections containing the lower and upper half of all integer divisors
		long presents = 0;

		// set limit to the number if not specified
		if( maxdivisor <= 0 ) maxdivisor = number;

		// start with 0 and an unbounded upper
		long l = 0;
		long u = Long.MAX_VALUE;
		
		// then increase the lower value until we have reached halfway (or the max)
		while( l < u ) {
			l++;
			
			if( l > maxdivisor ) break;

			// integer division?
			if( number % l == 0 ) {
				presents += (l+u) * numpresents;
				u = number / l;
			}
		}
		return presents;
	}
	
	/**
	 * Simple algorithm to find all divisors of a number that, when divided by
	 * the number, result in an integer value. May include some numbers twice if 
	 * 
	 * @param number The number to find integer divisors for
	 * @param maxdivisor The maximum value of the divisor, the algorithm stops
	 *   once this value has been reached. If this value is not a positive
	 *   number, all divisors will be returned
	 * @return
	 */
	public static List<Long> findIntegerDivisors( final long number, long maxdivisor ) {
		// collections containing the lower and upper half of all integer divisors
		final List<Long> lower = new ArrayList<>( );
		final Stack<Long> upper = new Stack<>( );

		// set limit to the number if not specified
		if( maxdivisor <= 0 ) maxdivisor = number;

		// start with 1 and the number itself
		long l = 1;
		lower.add( l );
		upper.push( number );
		
		// then increase the lower value until we have reached halfway (or the max)
		while( l < upper.peek( ) ) {
			l++;
			
			if( l > maxdivisor ) break;

			// integer division?
			if( number % l == 0 ) {
				final long u = number / l;
				lower.add( l );
				upper.push( u );
			}
		}

		// done, merge two collections into a single one (if no maximum is used)
		while( !upper.empty( ) && upper.peek( ) < maxdivisor ) lower.add( upper.pop( ) );
		return lower;
	}

}
