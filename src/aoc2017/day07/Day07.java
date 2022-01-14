package aoc2017.day07;

import java.util.List;

import aocutil.io.FileReader;

public class Day07 {

	/**
	 * Day 7 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Finds the label of the program tree root node
	 * 
	 * @param input The tree nodes as flat strings 
	 * @return The root of the reconstructed tree
	 */
	private static String part1( final List<String> input ) {
		final ProgramTree pt = ProgramTree.fromStringList( input );
		return pt.getRootLabel( );
	}
	
	/**
	 * Finds the new weight of the node that is causing an imbalance in the 
	 * program tree 
	 * 
	 * @param input The program tree nodes as flat strings 
	 * @return The new weight of the node that causes the imbalance
	 */
	private static long part2( final List<String> input ) {
		final ProgramTree pt = ProgramTree.fromStringList( input );
		return pt.getImbalanceWeight( );
	}
}
