package aoc2018.day08;

import aocutil.io.FileReader;

public class Day08 {
	
	/**
	 * Day 8 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new FileReader( Day08.class.getResource( "example.txt" ) ).readIntArray( " " );
		final int[] input = new FileReader( Day08.class.getResource( "input.txt" ) ).readIntArray( " " );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + getNavTreeLicence( ex_input, true ) );
		System.out.println( "Part 1 : " + getNavTreeLicence( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + getNavTreeLicence( ex_input, false ) );
		System.out.println( "Part 2 : " + getNavTreeLicence( input, false ) );
	}

	/**
	 * Determines the license code for the Navigation system
	 * 
	 * @param input The license encoding
	 * @param summeta True to return the sum of metadata, false for the root
	 *   node value
	 * @return The license code
	 */
	protected static long getNavTreeLicence( final int[] input, final boolean summeta ) {
		final NavLicenseTree nt = NavLicenseTree.fromIntArray( input );
		return summeta ? nt.sumMetadata( ) : nt.getValue( );
	}
}
