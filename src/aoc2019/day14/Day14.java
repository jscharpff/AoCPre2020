package aoc2019.day14;

import java.util.List;

import aocutil.io.FileReader;

public class Day14 {
	
	/**
	 * Day 14 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day14.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day14.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day14.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Example: " + part1( ex2_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Finds the number of ORE units required to produce a single unit of FUEL
	 *   
	 * @param input The list of NanoFactory reactions
	 * @return The number of ORE units
	 */
	protected static long part1( final List<String> input ) {
		final NanoFactory nf = NanoFactory.fromStringList( input );
		return nf.getMinimumRequired( "ORE", "FUEL", 1 );
	}
	
	/**
	 * Determines the amount of fuel that can be produced from a trillion units
	 * of ORE
	 * 
	 * @param input The list of NanoFactory reactions 
	 * @return The maximum number of FUEL units that can be produced from the ORE
	 */
	protected static long part2( final List<String> input ) {
		final NanoFactory nf = NanoFactory.fromStringList( input );
		return nf.getMaximumProduced( "ORE", 1000000000000l, "FUEL" );
	}

}
