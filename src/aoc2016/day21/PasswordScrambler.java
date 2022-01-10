package aoc2016.day21;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.Util;
import aocutil.string.StringUtil;

/**
 * Scrambled passwords using the specified list of instructions
 * 
 * @author Joris
 */
public class PasswordScrambler {
	/** The scrambling instructions */
	private final List<String> instructions;
	
	/** The current password that is being scrambled */
	private String password;
	
	/**
	 * Creates a new password scrambler with the list of instructions to scramble
	 * 
	 * @param instructions The list of scrambling instructions
	 */
	public PasswordScrambler( final List<String> instructions ) {
		this.instructions = new ArrayList<>( instructions );
	}
	
	/**
	 * Tries to unscramble the password by testing various input keys and see
	 * which one leads to the requested password
	 * 
	 * @param passwrd The password we have
	 * @return The key that lead to the password
	 */
	public String unscramble( final String passwrd ) {
		// generate all combinations of characters
		final Set<Character> passchars = new HashSet<Character>( );
		for( final char c : "abcdefgh".toCharArray( ) ) passchars.add( c );
		final List<List<Character>> keys = Util.generatePermutations( passchars );
		
		// and try them all
		for( final List<Character> key : keys ) {
			final StringBuilder skey = new StringBuilder( );
			for( final char c : key ) skey.append( c );
			
			final String pkey = skey.toString( );
			if( scramble( pkey ).equals( passwrd ) ) return pkey;
		}
		
		throw new RuntimeException( "Failed to unscramble the password" );
	}

	/**
	 * Performs all the instructions to scramble the password from the input
	 * 
	 * @param input The input key to scramble
	 * @return The scrambled password
	 */
	public String scramble( final String input  ) {
		password = "" + input;
		for( final String instr : instructions ) {
			Matcher m = Pattern.compile( "swap position (\\d+) with position (\\d+)" ).matcher( instr );
			if( m.find( ) ) {
				swapPosition( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
				continue;
			}
			
			m = Pattern.compile( "swap letter (\\w) with letter (\\w)" ).matcher( instr );
			if( m.find( ) ) {
				swapLetters( m.group( 1 ).charAt( 0 ), m.group( 2 ).charAt( 0 ) );
				continue;
			}
				
			m = Pattern.compile( "rotate (left|right) (\\d+) step" ).matcher( instr );
			if( m.find( ) ) {
				rotate( m.group( 1 ).equals( "left" ), Integer.parseInt( m.group( 2 ) ) );
				continue;
			}
	
			m = Pattern.compile( "rotate based on position of letter (\\w)" ).matcher( instr );
			if( m.find( ) ) {
				rotate( m.group( 1 ).charAt( 0 ) );
				continue;
			}
	
			m = Pattern.compile( "reverse positions (\\d+) through (\\d+)" ).matcher( instr );
			if( m.find( ) ) {
				reverse( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
				continue;
			}
	
			m = Pattern.compile( "move position (\\d+) to position (\\d+)" ).matcher( instr );
			if( m.find( ) ) {
				move( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
				continue;
			}

			
			// no instruction was matched
			throw new IllegalArgumentException( "Invalid instruction: " + instr );
		}

		return password;
	}
	
	/**
	 * Swap position X with position Y means that the letters at indexes X and Y
	 * (counting from 0) should be swapped.
	 * 
	 * @param x The index of the first char
	 * @param y The index of the second char
	 */
	private void swapPosition( final int x, final int y ) {
		final char[] pwd = password.toCharArray( );
		final char c = pwd[x];
		pwd[x] = pwd[y];
		pwd[y] = c;
		password = String.valueOf( pwd );
	}
	
	/**
	 * Swap letter X with letter Y means that the letters X and Y should be
	 * swapped (regardless of where they appear in the string).
	 * 
	 * @param x The first char
	 * @param y The second char
	 */
	private void swapLetters( final char x, final char y ) {
		swapPosition( password.indexOf( x ), password.indexOf( y ) );
	}
	
	/**
	 * Rotate left/right X steps means that the whole string should be rotated;
	 * for example, one right rotation would turn abcd into dabc.
	 * 
	 * @param left True for left rotation, false for right
	 * param steps The number of positions to rotate
	 */
	private void rotate( final boolean left, final int steps ) {
		// make sure we rotate once
		final int s = steps % password.length( );

		if( left ) 
			password = password.substring( s ) + password.substring( 0, s );
		else
			password = password.substring( password.length( ) - s ) + password.substring( 0, password.length( ) - s );
	}
	
	/**
	 * Rotate based on position of letter X means that the whole string should be
	 * rotated to the right based on the index of letter X (counting from 0) as
	 * determined before this instruction does any rotations. Once the index is
	 * determined, rotate the string to the right one time, plus a number of
	 * times equal to that index, plus one additional time if the index was at
	 * least 4.
	 * 
	 * @param x The character to rotate around
	 */
	private void rotate( final char x ) {
		final int i = password.indexOf( x );
		rotate( false, 1 + i + (i >= 4 ? 1 : 0 ) );
	}
	
	/**
	 * Reverse positions X through Y means that the span of letters at indexes X
	 * through Y (including the letters at X and Y) should be reversed in order.
	 * 
	 * @param x The start index of the part to reverse
	 * @param y The end index of the part to reverse
	 */
	private void reverse( final int x, final int y ) {
		final int a = Math.min( x, y );
		final int b = Math.max( x, y );
		
		password = password.substring( 0, a ) + StringUtil.reverse( password.substring( a, b + 1 ) ) + password.substring( b + 1 );
	}
	
	/**
	 * Move position X to position Y means that the letter which is at index X
	 * should be removed from the string, then inserted such that it ends up at
	 * index Y.
	 */
	private void move( final int x, final int y ) {
		final String remove = password.substring( x, x+1 );
		password = password.substring( 0, x ) + password.substring( x + 1 );
		password = password.substring( 0, y ) + remove + password.substring( y );
	}
}
