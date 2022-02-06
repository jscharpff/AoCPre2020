package aoc2018.day11;

import aocutil.geometry.Window2D;

public class Day11 {
	
	/**
	 * Day 11 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int ex_input = 42;
		final int input = 9005;
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}

	/**
	 * Finds the top-left coordinate of the 3x3 that maximises the fuel cell
	 * power within the fuel grid 
	 * 
	 * @param input The serial number of the fuel grid
	 * @return Top-left coordinate of the 3x3 grid that maximises power
	 */
	protected static String part1( final int input ) {
		final FuelGrid grid = new FuelGrid( input );
		return grid.findMaximalPowerGrid( 3 ).move( 1, 1 ).toString( );
	}
	
	/**
	 * Finds the top-left coordinate of the grid that maximises the fuel cell
	 * power within the fuel grid, now of any size
	 * 
	 * @param input The serial number of the fuel grid
	 * @return Top-left coordinate of the grid that maximises power and the size
	 *   of the sub grid 
	 */
	protected static String part2( final int input ) {
		final FuelGrid grid = new FuelGrid( input );
		final Window2D best = grid.findMaximalPowerGrid( );
		return best.getMinCoord( ).move( 1, 1 ).toString( ) + "," + best.size( ).x;
	}
}
