package aoc2019.day04;

import aocutil.number.NumberUtil;
import aocutil.string.StringUtil;

/**
 * Simple password validator
 * 
 * @author Joris
 */
public class PasswordValidator {
	/** The range of the password values */
	private final int min, max;
	
	/** The version of the validator */
	protected enum Version { v1, v2 };
	private final Version version;
	
	/**
	 * Creates a new Password validator
	 * 
	 * @param min The minimal password value
	 * @param max The maximal password value
	 * @param version The version of the validator
	 */
	public PasswordValidator( final int min, final int max, final Version version ) {
		this.min = min;
		this.max = max;
		this.version = version;
	}

	/**
	 * Counts the valid number of passwords within the validator range
	 * 
	 * @return The number of valid passwords
	 */
	public int countValid( ) {
		int count = 0;
		for( int i = min; i <= max; i++ )
			if( isValid( i ) ) count++;
		
		return count;
	}

	/**
	 * Tests a password
	 * 
	 * @param pwd The password to test
	 * @return True iff the password is valid
	 */
	public boolean isValid( final int pwd ) {
		final int[] d = NumberUtil.toDigits( pwd );
		
		// digits must be non-decreasing
		for( int i = 1; i < d.length; i++ ) if( d[i] < d[i-1] ) return false;
		
		// there must be at least one pair of digits in it
		boolean haspair = false;
		if( version == Version.v1 ) for( int i = 1; i < d.length; i++ ) haspair |= d[i] == d[i-1];
		if( version == Version.v2 ) haspair = StringUtil.getRepeatingSequences( "" + pwd, 2 ).stream( ).anyMatch( s -> s.length( ) == 2 );
		if( !haspair ) return false;

		// passed validation
		return true;
	}

}
