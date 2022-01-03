package aoc2015.day19;

import java.util.ArrayList;
import java.util.List;

import aoc2015.day19.rnrplant.Molecule;
import aoc2015.day19.rnrplant.RNRPlant;
import aocutil.io.FileReader;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "day19_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day19.class.getResource( "day19_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
		
	}
	
	/**
	 * 
	 * @param input 
	 * @return 
	 */
	private static long part1( final List<String> input ) {
		// split the input in a rules and molecule part
		final List<String> rules = new ArrayList<>( input );
		final Molecule molecule = Molecule.fromString( rules.remove( rules.size( ) - 1 ) );
		rules.remove( rules.size( ) - 1 );
		
		// now build the Red-Nose Reindeer Plant
		final RNRPlant plant = RNRPlant.fromStringList( rules );
		return plant.countUniqueFission( molecule );
	}
	
	/**
	 * 
	 * @param input 
	 * @return 
	 */
	private static long part2( final List<String> input ) {
		// split the input in a rules and molecule part
		final List<String> rules = new ArrayList<>( input );
		final Molecule molecule = Molecule.fromString( rules.remove( rules.size( ) - 1 ) );
		rules.remove( rules.size( ) - 1 );
				
		// now build the Red-Nose Reindeer Plant
		final RNRPlant plant = RNRPlant.fromStringList( rules );
		return plant.stepsToGenerate( molecule );
	}
}
