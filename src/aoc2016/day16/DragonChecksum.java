package aoc2016.day16;

/**
 * Class to generate data via a modified dragon curve and enables computing
 * checksums of generated data to fool the security at the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class DragonChecksum {
	
	/**
	 * Generates a string of length n given the initial input string using the
	 * modified dragon curve
	 * 
	 * @param input The input string
	 * @param n The required string length 
	 * @return The new string of length n
	 */
	public static String generateData( final String input, final int n ) {
		String bits = "" + input;
		
		while( bits.length( ) < n ) {
			final StringBuilder b2 = new StringBuilder( );
			for( int i = bits.length( ) - 1; i >= 0; i-- ) b2.append( (char)('1' - bits.charAt( i ) + '0') );
			
			bits = bits + "0" + b2.toString( ); 
		}
		
		return bits.substring( 0, n );
	}
	
	/**
	 * Runs an algorithm to determine the checksum of the input data
	 * 
	 * @param input The data as string
	 * @return The checksum of the data 
	 */
	public static String checkSum( final String input ) {
		String sum = "" + input;
		while( sum.length( ) % 2 == 0 ) {
			final StringBuilder newsum = new StringBuilder( );
			for( int i = 0; i < sum.length( ); i+= 2 ) {
				newsum.append( sum.charAt( i ) == sum.charAt( i + 1 ) ? "1" : "0" );
			}
			sum = newsum.toString( );
		}
		
		return sum;
	}
}
