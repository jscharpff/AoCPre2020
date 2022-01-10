package aoc2015.day04;

import aocutil.Util;

public class AdventCoins {
	
	/**
	 * Finds the smallest advent coin hash number that, together with the keym
	 * generates an AdventCoin Hash that has the specified prefix
	 * 
	 * @param key The private key
	 * @param prefix The prefix that the hash needs to start with
	 * @return The hashed coin
	 */
	public long getSmallestHashNumber( final String key, final String prefix ) {
		for( long i = 0; i < 1000000000l; i++ ) {
			final String hash = Util.MD5( key + i );
			if( hash.startsWith( prefix ) ) return i;
		}
		
		throw new RuntimeException( "Failed to produce valid AdventCoin hash" );
	}
}
