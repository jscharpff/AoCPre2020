package aoc2019.day10;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day10 {
	
	/**
	 * Day 10 of the AoC2019
	 * 
	 * https://adventofcode.com/2019/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "example.txt" ) ).readLines( ); 
		final List<String> ex2_input = new FileReader( Day10.class.getResource( "example2.txt" ) ).readLines( ); 
		final List<String> input = new FileReader( Day10.class.getResource( "input.txt" ) ).readLines( );
	
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
	}
	
	/**
	 * Finds the asteroids from which the most other asteroids are within line of
	 * sight
	 *  
	 * @param input The strings that describe the asteroid positions
	 * @return The number of asteroids in LoS from the asteroid that maximises
	 *   this value
	 */
	protected static long part1( final List<String> input ) {
		final AsteroidMap am = AsteroidMap.fromStringList( input );
		final Coord2D mon = am.placeMonitoringStation( );
		return am.getAsteroidsInLOS( mon ).size( );
	}
	
	/**
	 * Now equipped with a laser canon, blast the asteroids! 
	 * 
	 * @param input The strings describing the asteroid positions
	 * @return The position of the 200th asteroid that is destroyed by the laser  
	 */
	protected static long part2( final List<String> input ) {
		final AsteroidMap am = AsteroidMap.fromStringList( input );
		final Coord2D mon = am.placeMonitoringStation( );
		final Coord2D hit = am.fireLaser( mon, 200 );
		return hit.x * 100 + hit.y;
	}

}
