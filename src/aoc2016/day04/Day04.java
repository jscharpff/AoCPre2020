package aoc2016.day04;

import java.util.List;

import aocutil.io.FileReader;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "day04_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day04.class.getResource( "day04_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums the sector IDs of all the real rooms in the Eater Bunny's HQ
	 * 
	 * @param input The list of room names with their supposed checksums   
	 * @return The sum of room sector IDs over real rooms, e.g. where their
	 *   stated checksum equals their actual checksum 
	 */
	private static long part1( final List<String> input ) {
		return input.stream( ).map( Room::new ).filter( Room::isReal ).mapToInt( Room::getSectorID ).sum( );
	}
	
	/**
	 * Extracts the sector ID for the object storage at the Northpole by
	 * decrypting the valid rooms and searching for the right one 
	 * 
	 * @param input The list of room names
	 * @return The sector of the storage room at the Northpole
	 */
	private static long part2( final List<String> input ) {
		// okay, decode the input and extract the sector ID of the room
		return input.stream( ).map( Room::new )
				.filter( Room::isReal )
				.filter( x -> x.decrypt( ).startsWith( "northpole-object-storage" ) )
				.findFirst( ).get( ).getSectorID( );
	}
}
