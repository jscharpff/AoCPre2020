package aoc2019.day18;

import java.util.List;

import aoc2019.day18.vault.Vault;
import aocutil.io.FileReader;

public class Day18 {
	
	/**
	 * Day 18 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day18.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day18.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + navigateVault( ex_input, false ) );
		System.out.println( "Part 1 : " + navigateVault( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + navigateVault( ex2_input, true ) );
		System.out.println( "Part 2 : " + navigateVault( input, true ) );
	}
	
	/**
	 * Navigates through a vault, represented by a grid that describes its layout,
	 * and collects all the keys within the least amount of steps
	 *   
	 * @param input The vault layout, given by a list of strings
	 * @param multibot True to use 4 instead of one bot to collect the keys
	 * @return The least amount of steps required (summed over all bots) to
	 *   collect all the keys in the vault
	 */
	protected static long navigateVault( final List<String> input, final boolean multibot ) {
		final Vault v = Vault.fromStringList( input, multibot );
		return v.collectKeys( );
	}
}
