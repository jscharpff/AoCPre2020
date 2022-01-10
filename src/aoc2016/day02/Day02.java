package aoc2016.day02;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.io.FileReader;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2016
	 * 
	 * https://adventofcode.com/2016/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> pad1 = new FileReader( Day02.class.getResource( "day02_pad1.txt" ) ).readLines( );
		final List<String> pad2 = new FileReader( Day02.class.getResource( "day02_pad2.txt" ) ).readLines( );
		final List<String> ex_input = new FileReader( Day02.class.getResource( "day02_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "day02_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + findCode( pad1, ex_input ) );
		System.out.println( "Answer : " + findCode( pad1, input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + findCode( pad2, ex_input ) );
		System.out.println( "Answer : " + findCode( pad2, input ) );
	}
	
	/**
	 * Finds the code by following the movement instructions on a given numpad
	 * lay out
	 * 
	 * @param numpad The layout of the numpad
	 * @param input The movement instruction as one string per code number  
	 * @return The code that results from processing the instructions
	 */
	private static String findCode( final List<String> numpad, final List<String> input ) {
		final CoordGrid<Character> pad = CoordGrid.fromCharGrid( numpad, '.' );
		StringBuilder code = new StringBuilder( );
		
		// start at number 5
		Coord2D c = null;
		for( final Coord2D start : pad.getKeys( ) )
			if( pad.get( start ) == '5' ) c = start;
		
		// the process the moves
		for( final String s : input ) {
			for( final char move : s.toCharArray( ) ) {
				final Coord2D newcoord;
				switch( move ) {
					case 'U': newcoord = c.move( 0, -1 ); break;
					case 'D': newcoord = c.move( 0, 1 ); break;
					case 'L': newcoord = c.move( -1, 0 ); break;
					case 'R': newcoord = c.move( 1, 0 ); break;
					default: throw new RuntimeException( "Invalid move: " + move );
				}
				if( pad.hasValue( newcoord ) ) c = newcoord;
			}

			code.append( pad.get( c ) );
		}
		
		return code.toString( );
	}
}
