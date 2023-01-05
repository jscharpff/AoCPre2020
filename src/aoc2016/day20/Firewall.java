package aoc2016.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The firewall at the Easter Bunny's HQ
 * 
 * @author Joris
 */
public class Firewall {
	/* The set of IP rules in this firewall */
	private final List<IPRange> rules;

	/** The max IP value */
	private final long MAX_IP;
	
	/**
	 * Creates a new firewall from a ruleset
	 * 
	 * @param rules The rule set to initialise the firewall with
	 * @param maxIP The maximum IP address to consider (inclusive)
	 */
	public Firewall( final List<String> rules, final long maxIP ) {
		// parse IP ranges and sort on IP
		List<IPRange> rset = new ArrayList<>( rules.stream( ).map( IPRange::fromString ).toList( ) );
		
		// set max IP and merge rules 
		this.MAX_IP = maxIP;

		// make sure the rule set if as compact as it can be by merging ranges
		// try to merge each range with every other range
		for( int i = 0; i < rset.size( ) - 1; i++ ) {
			for( int j = i + 1; j < rset.size( ); j++ ) {

				// only merge overlapping ranges
				if( !rset.get( i ).overlaps( rset.get( j ) ) ) continue;

				// there is overlap, merge them
				final IPRange r1 = rset.remove( j );
				final IPRange r2 = rset.remove( i );
				
				// insert at index to preserve list sorting
				rset.add( i, r1.merge( r2 ) );
				
				// decrease i by one to consider the newly added rule and restart
				// pair-wise evaluation
				i--; 
				break;
			}
		}		
		
		// sort on IP
		rset.sort( IPRange::compareTo );
		this.rules = rset;
	}
	
	/**
	 * Determines the first available IP address not blocked by any of the FW's
	 * rules
	 * 
	 * @return The IP Address
	 */
	public long getFirstAllowedIP( ) {
		long ip = 0;
		int ruleidx = 0;
		
		while( ruleidx < rules.size( ) ) {
			// check if this rule blocks the IP
			final IPRange rule = rules.get( ruleidx );
			
			// yes, try the next IP outside this rule
			if( rule.contains( ip ) ) ip = rule.end + 1;
			
			// try next rule
			ruleidx++;
		}
		
		if( ip >= MAX_IP )		
			throw new RuntimeException( "Failed to find any non-blocked IP" );
		
		return ip;
	}
	
	/**
	 * Determines the number of available IP addresses given the current rule set
	 * 
	 * @return The number of IPs available
	 */
	public long getAvailableIPs( ) {		
		// okay, all mergers have been performed. Compute available IPs using only
		// IP ranges that do not overlap
		long blocked = 0;
		for( final IPRange range : rules ) blocked += range.size( );
		return new IPRange( 0, MAX_IP ).size( ) - blocked;
	}
	
	
	/**
	 * A range of IP addresses
	 */
	private static class IPRange implements Comparable<IPRange> {
		/** The start and end of the range */
		private final long start, end;
		
		/**
		 * Creates a new IP range
		 * 
		 * @param start The start of the range
		 * @param end The end of the range
		 */
		public IPRange( final long start, final long end ) {
			this.start = start;
			this.end = end;
			
			if( start >  end ) throw new RuntimeException( "TOo HIgh" );
		}
		
		/**
		 * Checks if this range contains an IP address
		 * 
		 * @param ip The IP address
		 * @return True iff this range covers the address
		 */
		public boolean contains( final long ip ) {
			return start <= ip && ip <= end;
		}
		
		/**
		 * Compares two IP address ranges upon their start addresses
		 * 
		 * @param ip The other IP
		 * @return The difference of this.start and ip.start
		 */
		@Override
		public int compareTo( IPRange ip ) {
			return Long.compare( start, ip.start );
		}
		
		/**
		 * Checks if the specified IP range (partially) overlaps this one
		 * 
		 * @param range The range to test
		 * @return True if there is overlap between the range
		 */
		public boolean overlaps( final IPRange range ) {
			return range.end >= start && range.start <= end;
		}
		
		/**
		 * Merges this IP range with the other (partially) overlapping ranges
		 * 
		 * @param range The other IP range
		 * @return The new combined range
		 */
		public IPRange merge( final IPRange range ) {
			if( !overlaps( range) ) throw new IllegalArgumentException( "The IP Range " + range + " does not overlap with " + this );
			
			return new IPRange( Math.min( start, range.start), Math.max( end, range.end ) );
		}
		
		/**
		 * Bound the 
		 */
		
		/** @return The size of this range (the IPs it spans) */
		public long size( ) { return end - start + 1; }
		
		/** @return The IP range */
		@Override
		public String toString( ) {
			return start + "-" + end;
		}
		
		/**
		 * Creates an IP range from a string
		 * 
		 * @param input The string input as start-end
		 * @return The IP Range
		 */
		public static IPRange fromString( final String input ) {
			final Matcher m = Pattern.compile( "(\\d+)-(\\d+)" ).matcher( input );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid IP range: " + input );
			
			return new IPRange( Long.parseLong( m.group( 1 ) ), Long.parseLong( m.group( 2 ) ) );
		}
	}
}
