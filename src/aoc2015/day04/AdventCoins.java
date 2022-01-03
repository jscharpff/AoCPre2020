package aoc2015.day04;

import java.math.BigInteger;
import java.security.MessageDigest;

public class AdventCoins {
	
	/**
	 * Finds the smallest advent coin hash number that, together with the keym
	 * generates an AdventCoin Hash that has the specified prefix
	 * 
	 * @param key The private key
	 * @return The hashed coin
	 */
	public long getSmallestHashNumber( final String key, final String prefix ) {
		for( long i = 0; i < 1000000000l; i++ ) {
			final String hash = MD5( key + i );
			if( hash.startsWith( prefix ) ) return i;
		}
		
		throw new RuntimeException( "Failed to produce valid AdventCoin hash" );
	}

	/**
	 * Converts the input string into a hash
	 * 
	 * @param input The input string
	 * @return The hashed string
	 */
	private String MD5( final String input ) {
		try {
			
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] messageDigest = md.digest( input.getBytes("UTF-8") );

			// Convert byte array into signum representation
      final BigInteger no = new BigInteger(1, messageDigest);
      
      // Convert message digest into hex value
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
          hashtext = "0" + hashtext;
      }
      return hashtext;
		} catch( Exception e ) {
			throw new RuntimeException( "Failed to produce MD5 hash: " + e.toString( ) );
		}
	}
}
