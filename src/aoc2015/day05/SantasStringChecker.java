package aoc2015.day05;

import java.util.regex.Pattern;

import aocutil.string.StringUtil;

public class SantasStringChecker {
	/**
	 * Checks whether a given string is naughty or nice, according to Santa's
	 * rules:
	 * 
	 * 1) It contains at least three vowels (aeiou only), like aei, xazegov, or
	 *    aeiouaeiouaeiou
	 * 2) It contains at least one letter that appears twice in a row, like xx,
	 *    abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
	 * 3) It does not contain the strings ab, cd, pq, or xy, even if they are
	 *    part of one of the other requirements.
	 * 
	 * @param input The input string
	 * @return True iff the string is nice, naughty otherwise
	 */
	public boolean check( final String input ) {
		final String vowels = "aeiou";
		final String[] forbidden = new String[] { "ab", "cd", "pq", "xy" };
		
		// 1) check if it contains at least three vowels
		final String d = StringUtil.union( input, vowels );
		if( d.length( ) < 3 ) return false;
		
		// 2) check if at least one character appears twice in a row
		boolean twice = false;
		for( int i = 0; i < input.length( ) - 1; i++ )
			twice |= input.charAt( i ) == input.charAt( i + 1 );
		if( !twice ) return false;
		
		// 3) it does not contain any of the forbidden strings
		for( final String s : forbidden )
			if( input.contains( s ) ) return false;
		
		// passed all tests!
		return true;
	}
	
	/**
	 * Checks whether a given string is naughty or nice, according to Santa's
	 * NEW rules:
	 * 
	 * 1) It contains a pair of any two letters that appears at least twice in
	 *    the string without overlapping, like xyxy (xy) or aabcdefgaa (aa), but
	 *    not like aaa (aa, but it overlaps).
	 * 2) It contains at least one letter which repeats with exactly one letter
	 *    between them, like xyx, abcdefeghi (efe), or even aaa.
	 * 
	 * @param input The input string
	 * @return True iff the string is nice, naughty otherwise
	 */
	public boolean checkNew( final String input ) {
		// 1) it contains at least one pair that occurs twice
		if( !Pattern.compile( "(\\w{2}).*\\1.*" ).matcher( input ).find( ) ) return false;
		
		// 2) it contains at least one letter that repeats with exactly one letter in between
		if( !Pattern.compile( "(\\w).\\1" ).matcher( input ).find( ) ) return false;
				
		// all rules passed!
		return true;
	}
}
