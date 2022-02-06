package aoc2018.day14;

public class Day14 {
	
	/**
	 * Day 14 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new int[] { 5, 9, 18, 2018 };
		final String[] ex2_input = new String[] { "51589", "01245", "92510", "59414" };
		final int input = 505961;
		
		System.out.println( "---[ Part 1 ]---" );
		for( final int in : ex_input )
			System.out.println( "Example " + in + ": " + part1( in ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		for( final String in : ex2_input )
			System.out.println( "Example: " + part2( in ) );
		System.out.println( "Part 2 : " + part2( "" + input ) );
	}

	/**
	 * Mix recipes and return the scores of the last 10
	 * 
	 * @param input The number of recipes to mix
	 * @return The score of the last 10 recipes
	 */
	protected static String part1( final int input ) {
		final RecipeMixer r = new RecipeMixer( 2 );
		r.mix( input + 10 );
		return r.get( input, 10 );
	}
	
	/**
	 * Mix recipes until we have found the string of scores
	 * 
	 * @param input The string of scores to find
	 * @return The number of recipes before the scores are found
	 */
	protected static long part2( final String input ) {
		final RecipeMixer r = new RecipeMixer( 2 );
		return r.mixUntil( input );
	}
	
}
