package aoc2016.day14;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aocutil.Util;
import aocutil.io.FileReader;
import aocutil.string.StringUtil;

/**
 * The One-Time Pad to securely communicate with Santa
 * 
 * @author Joris
 */
public class OneTimePad {
	/** The set of predefined keys, e.g. loaded from file */
	private final List<String> presetkeys;
	
	/**
	 * Creates a new OneTimePad
	 */
	public OneTimePad( ) {
		this.presetkeys = new ArrayList<>( );
	}
	
	/**
	 * Generates a set of N keys
	 * 
	 * @param salt The salt to generate the keys with
	 * @param stretching The number of times to rehash the key
	 * @param startIndex The first index to generate a key for
	 * @param size The number of keys to generate
	 * @return The list of generated keys
	 */
	public List<String> generateKeys( final String salt, final int stretching, final long startIndex, final int size ) {
		final List<String> keys = new ArrayList<>( );
		
		for( long i = startIndex; i < startIndex + size; i++ ) {
			// check if we already have this key
			if( presetkeys.size( ) - 1 >= i ) {
				keys.add( presetkeys.get( (int)i ) );
				continue;
			}
				
			// nope, generate it
			String hash = Util.MD5( salt + i );
			for( int j = 0; j < stretching; j++ )	{
				hash = Util.MD5( hash );
			}
			keys.add( hash );
		}
		return keys;
	}
	
	/**
	 * Generates new valid OTP keys for the given salt
	 * 
	 * @param salt The salt to initialise keys with
	 * @param amount The amount of valid keys to generate
	 * @param stretching The number of times the key is stretched
	 * @return The index that produces the last valid key
	 */
	public long generateValidKeys( final String salt, final int amount, final int stretching ) {
		// keep generating keys until we found 64 valid ones
		final List<CharTimer> chars = new LinkedList<>( );
		final List<Long> validIndexes = new ArrayList<>( amount + 5 );
		
		final int BATCH_SIZE = 1000;
		for( long i = 0; i < 1000000000l; i += BATCH_SIZE ) {
			// generate the next batch of keys
			final List<String> keys = generateKeys( salt, stretching, i, BATCH_SIZE );
			
			for( int j = 0; j < keys.size( ); j++ ) {
				final String key = keys.get( j );

				// validate this key
				validate( key, validIndexes, chars, i + j );
				
				// are we done generating hashes?
				if( validIndexes.size( ) >= amount ) {
					// we want the exact index that lead to the Nth valid key, we may
					// overshoot it if multiple keys are validated by a single hash 
					validIndexes.sort( Long::compareTo );
					while( validIndexes.size( ) > amount ) validIndexes.remove( validIndexes.size( ) - 1 );
					return validIndexes.get( validIndexes.size( ) - 1 );
				}
			}
		}
		
		throw new RuntimeException( "Failed to prroduce " + amount + " valid hashes" );
	}
	
	/**
	 * Checks if a certain key is valid
	 * 
	 * @param key The key to test
	 * @param valid The current list of successfully validated keys
	 * @param chars The list of characters that were seen before in a triplet
	 * @param index The index of the current key
	 * @return True if it is valid
	 */
	private void validate( final String key, final List<Long> valid, final List<CharTimer> chars, final long index ) {
		// first check for characters that have expired
		for( int i = 0; i < chars.size( ); i++ ) 
			if( chars.get( i ).expired( index ) ) chars.remove( i-- );
			else break; /* They are ordered FIFO, thus if this one doesn't expire, those after also wont */
		
		// check for sequences in the key
		final List<String> triplets = StringUtil.getRepeatingSequences( key, 3 );
				
		// if it contains a five character sequence, check if there is a valid
		// chartimer for that character, i.e. a key waiting for validation
		final List<String> quintets = triplets.stream( ).filter( x -> x.length( ) >= 5 ).toList( );
		for( final String q : quintets ) {
			final char c = q.charAt( 0 );
			for( int i = 0; i < chars.size( ); i++ ) 
				if( chars.get( i ).c == c ) {
					valid.add( chars.remove( i-- ).index );
				}
			break; // only first quintet
		}

		// add new timers for first triplet
		if( triplets.size( ) > 0 )
				chars.add( new CharTimer( triplets.get( 0 ).charAt( 0 ), index, 1000 ) );
	}
	
	/**
	 * Reads pre-set keys from a file
	 * 
	 * @param file The file to read from
	 */
	public void readKeys( final URL file ) {
		this.presetkeys.clear( );
		try {
			this.presetkeys.addAll( new FileReader( file ).readLines( ) );
		} catch( Exception e ) {
			System.err.println( "Failed to read keys: " + e.toString( ) );
		}
	}
	
	/**
	 * Generates keys and writes them to a file, for later use
	 * 
	 * @param file The file to write to
	 * @param amount The number of keys to generate
	 * @param salt The salt used to generate hashes
	 * @param stretching The number of times the key is to be rehashed
	 * @throws Exception if writing failed
	 */
	public static void writeKeys( final URL file, final long amount, final String salt, final int stretching ) throws Exception {
		final PrintWriter w = new PrintWriter( file.getFile( ) );
		final OneTimePad OTP = new OneTimePad( );
		
		// generate keys in batches
		final int BATCH_SIZE = 1000;
		for( long index = 0; index < amount; index += BATCH_SIZE ) {
			final List<String> keys = OTP.generateKeys( salt, stretching, index, BATCH_SIZE );
			for( final String key : keys ) w.println( key );
			w.flush( );
		}
		
		w.close( );
	}
	
	/**
	 * Private class that stores a tuple of char and expiry timer
	 */
	private static class CharTimer {
		/** The character */
		private final char c;
		
		/** The index at which the timer was generated  */
		private final long index;
		
		/** The timer value of this character */
		private final long ttl;
		
		/**
		 * Creates a new char/timer pair
		 * 
		 * @param c The character
		 * @param index The index at which this timer starts
		 * @param timer The ttl of this timer
		 */
		private CharTimer( final char c, final long index, final long timer ) {
			this.c = c;
			this.index = index;
			this.ttl = timer;
		}
		
		/**
		 * Checks if the validity of the char has expired
		 * 
		 * @param currindex The current index
		 * @return True if the timer has expired
		 */
		public boolean expired( final long currindex ) {
			return this.index + ttl < currindex;
		}
		
		/**
		 * Check if this equals another timer
		 * 
		 * @param obj The object to test against
		 * @return True iff the obj is a valid CharTimer and has the same character and index 
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof CharTimer) ) return false;
			final CharTimer ct = (CharTimer) obj;
			return ct.c == c && ct.index == index;
		}
		
		/** @return The string with the character and its timer */
		@Override
		public String toString( ) {
			return "" + c + ": "+ (index + ttl);
		}
	}
}
