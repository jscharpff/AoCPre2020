package aoc2017.day10;

import aocutil.string.StringUtil;

/**
 * Algorithm to produce hashes bashed on a string-knotting process of a
 * numbered ring
 * 
 * @author Joris
 */
public class HashKnot {
	/** The current hash */
	private final int[] hash;
	
	/** The length sequences to use in the algorithm */
	private final int[] lengths;
	
	/** The size of the hashing ring */
	private final int size;
	
	/** The current position of the hashing algorithm */
	private int pos;
	
	/** The current skip length */
	private int skip;

	/**
	 * Creates a new HashKnot algorithm with the given string that describes the
	 * length sequences
	 * 
	 * @param lengthseq The length sequences
	 * @param size The size of the keyring
	 * @param ASCII True to use ASCII mode in translating the length string
	 */
	public HashKnot( final String lengthseq, final int size, final boolean ASCII ) {
		// initialise hash ring
		this.size = size;
		hash = new int[ size ];
		for( int i = 0; i < size; i++ ) hash[i] = i;
				
		
		if( !ASCII ) {
			final String[] s = lengthseq.split( "," );
			lengths = new int[ s.length ];
			for( int i = 0; i < s.length; i++ ) this.lengths[i] = Integer.parseInt( s[i].trim( ) );
		} else {
			// convert input to ASCII values
			lengths = new int[ lengthseq.length( ) + 5 ];
			for( int i = 0; i < lengthseq.length( ); i++ ) 
				lengths[i] = (int)lengthseq.charAt( i );
			
			// add 5 additional ASCII values
			final int N = lengths.length;
			lengths[ N - 5 ] = 17;
			lengths[ N - 4 ] = 31;
			lengths[ N - 3 ] = 73;
			lengths[ N - 2 ] = 47;
			lengths[ N - 1 ] = 23;
		}
		
		pos = 0;
		skip = 0;
	}
	
	/**
	 * Runs the simple HashKnot algorithm for one round
	 * 
	 * @return The int array hashing of the input
	 */
	public int[] hash( ) {
		// run hashing process
		for( final int l : lengths ) {
			// reverse hash entries within length l
			int start = pos;
			int end = pos + l - 1;
			while( start < end ) {
				int temp = hash[start % size];
				hash[start % size ] = hash[end % size];
				hash[end % size] = temp;
				start++; end--;
			}
			
			// set new position value
			pos = (pos + l + skip) % size;
			skip = (skip + 1) % size;
		}
		
		return hash;
	}
	
	/**
	 * Runs the hashknot algorithm for the specified number of rounds
	 * 
	 * @param rounds The number of hashing rounds to perform 
	 * @return The resulting hash as a string
	 */
	public String hash( final int rounds ) {				
		// run the hashing algorithm for N times
		pos = 0; skip = 0;
		for( int i = 0; i < rounds; i++ ) {
			hash( );
		}
				
		// then convert resulting sparse hash into a dense hash
		final int[] newhash = new int[ 16 ];
		for( int i = 0; i < newhash.length; i++ ) {
			final int offset = i * 16;
			int h = hash[offset];
			for( int j = 1; j < newhash.length; j++ ) h ^= hash[offset + j];
			newhash[i] = h;
		}
		
		// build string from ASCII values in dense hash
		final StringBuilder sb = new StringBuilder( );
		for( int i = 0; i < newhash.length; i++ )
			sb.append( StringUtil.padLeft( Integer.toHexString( newhash[i] ), 2, '0' ) );
		
		return sb.toString( );
	}	
	
	/**
	 * Convenient function that constructs a KnotHash object and uses it to hash
	 * the input string
	 * 
	 * @param key The hash key input
	 * @return The resulting hash
	 */
	public static String hash( final String input ) {
		final HashKnot h = new HashKnot( input, 256, true );
		return h.hash( 64 );
	}
}
