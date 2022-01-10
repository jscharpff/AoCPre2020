package aoc2016.day09;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Decompressor that decompresses experimentally compressed messages found
 * within the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class Decompressor {

	/**
	 * Decompresses the string using special markers to indicate repetition
	 * 
	 * @param input The input string to decompress
	 * @return The decompressed output string
	 */
	public static String decompress( final String input ) {
		final Pattern p = Pattern.compile( "\\((\\d+)x(\\d+)\\)" );
		final StringBuilder output = new StringBuilder( );
		String remaining = "" + input;
		
		// replace every matching occurrence
		Matcher m = null;
		while( (m = p.matcher( remaining )).find( ) ) {
			// append part before marker to output
			output.append( remaining.substring( 0, m.start( ) ) );
			
			// repeat the part described by the marker
			final int repeatlen = Integer.parseInt( m.group( 1 ) );
			final String repeat = remaining.substring( m.end( ), m.end( ) + repeatlen );
			for( int i = 0; i < Integer.parseInt( m.group( 2 ) ); i++ )
				output.append( repeat );
			
			// and keep scanning for markers after the repeated data
			remaining = remaining.substring( m.end( ) + repeatlen );
		}
		
		// append remaining part that no longer contains a marker
		output.append( remaining );		
		return output.toString( );
	}
	
	/**
	 * Determines the length of the string after decompressing it, now also
	 * processing markers within the decompressed output
	 * 
	 * @param input The input string
	 * @return The length, in characters, of the decompressed string
	 */
	public static long getDecompressionLenght( final String input ) {
		
		final Pattern p = Pattern.compile( "\\((\\d+)x(\\d+)\\)" );
		long outsize = 0;
		String remaining = "" + input;
		
		// replace every matching occurrence
		Matcher m = null;
		while( (m = p.matcher( remaining )).find( ) ) {
			// append part before marker to output
			outsize += m.start( );
			
			// repeat the part described by the marker
			final int repeatlen = Integer.parseInt( m.group( 1 ) );
			final String repeat = remaining.substring( m.end( ), m.end( ) + repeatlen );
			outsize += getDecompressionLenght( repeat ) * Integer.parseInt( m.group( 2 ) );
			
			// and keep scanning for markers after the repeated data
			remaining = remaining.substring( m.end( ) + repeatlen );
		}
		
		// append remaining part that no longer contains a marker
		outsize += remaining.length( );		
		return outsize;
	}
}
