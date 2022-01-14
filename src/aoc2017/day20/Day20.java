package aoc2017.day20;

import java.util.List;

import aocutil.io.FileReader;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day20.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day20.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
		
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the particle that, in the long run, will remain closest to the
	 * particle system origin 
	 * 
	 * @param input The list of particle descriptions 
	 * @return The ID of the closest particle
	 */
	private static long part1( final List<String> input ) {
		final ParticleSystem ps = ParticleSystem.fromStringList( input );
		return ps.findClosest( ).getID( );
	}
	
	/**
	 * Simulates the particle movements and collisions, and returns the number of
	 * particles that do not collide with any other
	 * 
	 * @param input The particle descriptions 
	 * @return The number of particles that never collide
	 */
	private static long part2( final List<String> input ) {
		final ParticleSystem ps = ParticleSystem.fromStringList( input );
		return ps.simulateCollisions( ).size( );
	}
}
