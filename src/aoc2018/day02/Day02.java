package aoc2018.day02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.io.FileReader;

public class Day02 {
	
	/**
	 * Day 2 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day02.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day02.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Part 1: count occurrence of consecutive letters in the IDs. Return product
	 * of counts per length
	 * 
	 * @param input The IDs as list of string
	 * @return Product of number of IDs with exactly 2 letter sequences times
	 * 3 letter sequences times 4 letter sequences ...  	
	 */
	protected static long part1( final List<String> input ) {
		// counts per character (assuming equal size IDs)
		int counttwo = 0;
		int countthree = 0;

		// check per ID
		for( String ID : input ) {
			// count characters
			final Map<Character, Integer> count = new HashMap<>( ); 
			for( char c : ID.toCharArray( ) ) {
				if( count.containsKey( c ) ) count.put( c, count.get( c ) + 1 );
				else count.put( c, 1 );				
			}
			
			// check for ID's with exactly 2 and exactly 3 letters
			if( count.values( ).contains( 2 ) ) counttwo++;
			if( count.values( ).contains( 3 ) ) countthree++;
			
		}
		
		return counttwo * countthree;
	}
	
	/**
	 * Part 2: find the two box ID's that are at most one character away and return
	 * the characters they share
	 * 
	 * @param input The list of IDs
	 * @return The part of the two IDs that is shared 
	 */
	protected static String part2( final List<String> input ) {
		final String[] in = new String[ input.size( ) ];
		input.toArray( in );
		
		for( int i = 0; i < in.length; i++ )
			for( int j = i + 1; j < in.length; j++ ) {
				// retain shared characters
				final String ID1 = in[i];
				final String ID2 = in[j];
				String res = "";
				for( int k = 0; k < ID1.length( ); k++ )
					if( ID1.charAt( k ) == ID2.charAt( k ) ) res += ID1.charAt( k );
			
				// only one differs?
				if( res.length( ) == ID1.length( ) - 1 ) return res;
			}
		
		throw new RuntimeException( "Did not find the required IDs" );
	}
}
