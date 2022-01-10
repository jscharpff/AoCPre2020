package aoc2016.day04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.string.RegexMatcher;
import aocutil.string.StringUtil;

/**
 * Represents a room in the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class Room {
	/** The string name of the room */
	private final String name;
	
	/** The checksum it should match */
	private final String checksum;
	
	/** The sector ID of the room */
	private final int sectorID;
	
	/**
	 * Creates a new room from the string description
	 * 
	 * @param roomname The name of the room
	 */
	public Room( final String roomname ) {
		this.name = roomname.substring( 0, roomname.indexOf( '[' ) );
		this.checksum = RegexMatcher.extract( "\\[(\\w{5})\\]", roomname );
		this.sectorID = Integer.parseInt( name.substring( name.lastIndexOf( '-' ) + 1 ) );
	}

	/**
	 * Checks if the room is a real room by validating its checksum
	 * 
	 * @return True iff the room is real
	 */
	public boolean isReal( ) {
		// determine actual checksum by getting the most common letters
		final Map<Character, Integer> occurrence = new HashMap<>( );
		name.chars( ).filter( x -> x >= 'a' && x <= 'z' ).distinct( )
		.forEach( x -> occurrence.put( (char)x, StringUtil.count( name, (char)x ) ) );

		// rebuild the checksum using the counts
		final List<Character> chars = new ArrayList<>( occurrence.keySet( ) );
		chars.sort( (x,y) -> { final int d = occurrence.get( y ) - occurrence.get( x ); return d == 0 ? x - y : d; } );
		
		// keep only enough characters to match the supposed checksum 
		StringBuilder realcheck = new StringBuilder( );
		while( realcheck.length( ) < checksum.length( ) ) {
			realcheck.append( chars.get( realcheck.length( ) ) );
		}
		
		return checksum.equals( realcheck.toString( ) );
	}
	
	/**
	 * Decrypts a room name
	 * 
	 * @return The decrypted room name
	 */
	public String decrypt( ) {
		final StringBuilder decrypted = new StringBuilder( name.length( ) );
		for( final char c : name.toCharArray( ) ) {
			// only decrypt letters
			if( c < 'a' || c > 'z' ) decrypted.append( c );
			else decrypted.append( (char)((c - 'a' + sectorID) % 26 + 'a') );
		}
		return decrypted.toString( );
	}
	
	/** @return The sector ID of the room */
	public int getSectorID( ) { return sectorID; }
	
	/** @return The room name and (supposed) checksum */
	@Override
	public String toString( ) {
		return name + "[" + checksum + "]";
	}
}
