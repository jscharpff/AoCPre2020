package aoc2016.day19;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A nice and friendly game of White Elephant Party
 * 
 * @author Joris
 */
public class WhiteElephantParty {
	/**
	 * Plays a single game of WEP and returns the elf that will get all the
	 * presents when the game ends
	 * 
	 * @param N The number of players
	 * @return The index of the elf with all the winning presents (starting at 1)
	 */
	public static int playSimple( final int N ) {
		// initiate array of elves
		final int[] elves = new int[ N ];
		IntStream.range( 0, N ).forEach( i -> elves[i] = 1 );
		
		int curr = 0;
		while( true ) {
			int next = (curr + 1) % N;
			while( elves[next] == 0 ) {
				next = (next + 1) % N;
				// if no other player has presents, this player wins
				if( next == curr ) return (curr + 1);
			}

			if( elves[curr] > 0 ) {
				elves[curr] += elves[next];
				elves[next] = 0;
			}
			
			curr = next;
		}
	}
	
	/**
	 * Plays a single game of WEP and returns the elf that will get all the
	 * presents when the game ends. This time players steal presents from the
	 * player on the other side of the circle and players without presents are
	 * removed from the circle.
	 * 
	 * @param N The number of players
	 * @return The index of the elf with all the winning presents (starting at 1)
	 */
	public static int playComplex( final int N ) {
		// initiate presents list, which doubles a a list of active players
		final List<Integer> elves = new ArrayList<>( N );
		IntStream.range( 0, N ).forEach( i -> elves.add( i + 1 ) );
		
		// the index of the current player
		int curr = 0;
		while( elves.size( ) > 1 ) {
			final int rem = elves.size( );
			if( rem % 10000 == 0 ) System.out.println( elves.size( ) );

			final int target = (curr + (int)( rem / 2 )) % rem;
			elves.remove( target );
			curr = (curr + (target > curr ? 1 : 0)) % (rem - 1);
			
		}
		
		// get the index of the last remaining player
		return elves.get( 0 );
	}
}
