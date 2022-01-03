package aoc2015.day15;

import java.util.List;

import aoc2015.day15.cookies.CookieRecipe;
import aoc2015.day15.cookies.CookieRecipeMixer;
import aocutil.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2015
	 * 
	 * https://adventofcode.com/2015/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "day15_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "day15_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, -1 ) );
		System.out.println( "Answer : " + part1( input, -1 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part1( ex_input, 500 ) );
		System.out.println( "Answer : " + part1( input, 500 ) );
	}
	
	/**
	 * Finds the optimal mix of ingredients that produces the perfect cookie
	 * 
	 * @param input The description of available ingredients
	 * @param calories The exact number of calories that the recipe needs to have 
	 * @return The optimal cookie mix score 
	 */
	private static long part1( final List<String> input, final int calories ) {
		final CookieRecipeMixer recipe = CookieRecipeMixer.fromStringList( input );
		final CookieRecipe result = recipe.findOptimalMix( calories );
		return result.getScore( );
	}
	
}
