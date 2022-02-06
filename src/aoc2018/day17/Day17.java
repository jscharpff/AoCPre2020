package aoc2018.day17;

import java.util.List;

import aocutil.io.FileReader;

public class Day17 {
	
	/**
	 * Day 17 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day17.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day17.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + countWaterTiles( ex_input, true ) );
		System.out.println( "Part 1 : " + countWaterTiles( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + countWaterTiles( ex_input, false ) );
		System.out.println( "Part 2 : " + countWaterTiles( input, false ) );
	}

	/**
	 * Counts the number of tiles that are filled with (flowing) water after
	 * simulating the flow of water through the ground 
	 * 
	 * @param input The description of the underground clay basins
	 * @param countflowing True to include the flowing water in the count
	 * @return The total number of water tiles underground, determined by the
	 *   flow simulation. If countflowig is true, all water tiles are counted.
	 *	 Otherwise only the "resting" water tiles are counted.
	 */
	protected static long countWaterTiles( final List<String> input, final boolean countflowing ) {
		final WaterFlow wf = WaterFlow.fromScan( input );
		wf.flow( );
		return wf.countWater( countflowing );
	}
}
