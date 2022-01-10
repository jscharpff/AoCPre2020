package aoc2016.day14;

import java.io.File;
import java.net.URL;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + validateKeys( "abc", 64, 0 ) );
		System.out.println( "Answer : " + validateKeys( "yjdafjpo", 64, 0 ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + validateKeys( "abc", 64, 2016 ) );
		System.out.println( "Answer : " + validateKeys( "yjdafjpo", 64, 2016 ) );
	}
	
	private static long validateKeys( final String salt, final int N, final int stretches ) throws Exception {
		final String keyfile = Day14.class.getResource( "." ).getFile( ) + "/keys_" + salt + "_" + stretches + ".txt";
		
		// generate hash keys once
		if( !new File( keyfile ).exists( ) ) {
			System.err.println( "Key fie does not exists, writing to " + keyfile );
			OneTimePad.writeKeys( new URL( "file://" + keyfile ), 30000, salt, stretches );
		}
		
		final OneTimePad OTP = new OneTimePad( );
		OTP.readKeys( new URL( "file://" + keyfile ) );
		
		return OTP.generateValidKeys( salt, N, stretches );
	}
}
