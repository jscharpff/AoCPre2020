package aoc2016.day07;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An IPv7 address used at the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class IPv7 {
	/** The original IPv7 address */
	private final String address;
	
	/** The hypernet sequence */
	private final List<String> hypernets;
	
	/** The non-hypernet parts */
	private final List<String> normalnets;
	
	/**
	 * Creates a new IPv7 address
	 * 
	 * @param ipv7 The IPv7 address string
	 */
	public IPv7( final String ipv7 ) {
		this.address = "" + ipv7;
		
		// extract hypernets from input
		final Matcher m1 = Pattern.compile( "\\[(\\w+)\\]" ).matcher( ipv7 );
		hypernets = new ArrayList<>( );
		while( m1.find( ) ) hypernets.add( m1.group( 1 ) );

		// and the regular nets		
		normalnets = new ArrayList<>( );
		int idx = -1;
		int closeidx = 0; 
		while( (idx = address.indexOf( '[', idx + 1 ) ) > -1 ) {
			normalnets.add( address.substring( closeidx, idx ) );
			closeidx = address.indexOf( ']', idx);
		}
		normalnets.add( address.substring( closeidx + 1 ) );
	}
	
	/**
	 * Checks if the address has Transport-Layer Snooping support
	 * 
	 * @return True iff the address supports TLS
	 */
	public boolean supportsTLS( ) {
		// the hypernet part must not contain a symmetric 4 char string
		if( hypernets.stream( ).filter( IPv7::hasABBA ).count( ) > 0 ) return false;
		
		// then one of the remaining address parts needs to contain a symmetric 4 char string
		return normalnets.stream( ).filter( IPv7::hasABBA ).count( ) > 0;
	}
	
	/**
	 * Checks if the address has Super-Secret Listening support
	 * 
	 * @return True if the address supports SSL
	 */
	public boolean supportsSSL( ) {
		final List<String> BAB = new ArrayList<>( );
		for( final String hyper : hypernets ) {
			if( hyper.length( ) < 3 ) continue;
			
			// find ABA sequences and add their BAB inverse to the search list
			final char[] h = hyper.toCharArray( );			
			for( int i = 0; i < h.length - 2; i++ ) {
				if( h[i] == h[i+2] && h[i] != h[i+1] ) BAB.add( "" + h[i+1] + h[i] + h[i+1] );
			}
		}

		// at least one normal net must contain any of the BAB sequences
		for( final String nor : normalnets )
			if( BAB.stream( ).filter(  x -> nor.contains( x ) ).count( ) > 0 ) return true;
		return false;
	}
	
	/**
	 * Checks if the given input string contains a 4-character symmetric
	 * subsequence
	 * 
	 * @param input the input to test
	 * @return True if such a sequence is present in the string, false if not
	 */
	private static boolean hasABBA( final String input ) {
		if( input.length( ) < 4 ) return false;
		final char[] in = input.toCharArray( ); 
		
		for( int i = 0; i < in.length - 3; i++ )
			if( in[i] == in[i+3] && in[i+1] == in[i+2] && in[i] != in[i+1] )
				return true;

		return false;
	}
	
	/** @return The IPv7 Address */
	@Override
	public String toString( ) {
		return address;
	}
}
