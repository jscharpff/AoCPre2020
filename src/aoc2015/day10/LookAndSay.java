package aoc2015.day10;

/**
 * Class that models the game of LookAndSay in which players call out numbers
 * as n times m that are to be written down by the other player. Here, n is the
 * number of times m occurs next in the string, e.g. 12211 is called as "one
 * one, two twos and two ones". The next round thus starts with input 112221.  
 * 
 * @author Joris
 */
public class LookAndSay {

	/**
	 * Plays the game oF LookAndSay for the specified number of rounds
	 * 
	 * @param rounds The number of rounds to play
	 * @param initial The starting string
	 * @return The string that results when the game finishes 
	 */
	public String play( final int rounds, final String initial ) {
		String res = "" + initial;
		for( int i = 0; i < rounds; i++ )
			res = playRound( res );
		return res;
	}
	
	/**
	 * Plays a single round of LookAndSay
	 * 
	 * @param input The input string to start the round
	 * @return The string that results after a single round
	 */
	public String playRound( final String input ) {
		final StringBuilder res = new StringBuilder( );
		
		// go over the string and treat the groups of equal numbers
		char grpchar = input.charAt( 0 );
		int count = 1;
		for( int i = 1; i < input.length( ); i++ ) {
			final char curr = input.charAt( i );
			
			// create new group?
			if( curr != grpchar ) {
				// yes, process previous group and continue
				res.append( count );
				res.append( grpchar );
				grpchar = curr;
				count = 1;
			} else {
				// append one char to the count
				count++;
			}
		}
		
		// process last group and continue
		res.append( count );
		res.append( grpchar );

		return res.toString( );
	}
}
