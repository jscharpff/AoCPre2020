package aoc2015.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class to compute the optimal distribution of Eggnog over the available
 * containers
 * 
 * @author Joris
 */
public class EggnogDistributor {
	/** The set of available cup sizes, ordered from small too large */
	private List<Integer> cups;

	/**
	 * Creates a new EggnogDistributor from the list of cup sizes
	 * 
	 * @param cups The available cup sizes
	 */
	public EggnogDistributor( final List<String> cups ) {
		this.cups = new ArrayList<>( cups.size( ) );
		for( final String s : cups ) {
			this.cups.add( Integer.parseInt( s ) );
		}
		
		// make sure the cups are ordered from smallest to largest
		this.cups.sort( Integer::compareTo );
	}
	
	/**
	 * Determines the number of (unique) combinations of cups that can exactly
	 * hold the specified volume
	 * 
	 * @param volume The volume of eggnog to store
	 * @return The number of unique cup combinations that together lead to the
	 *   volume
	 */
	public long countCombinations( final int volume ) {
		final List<List<String>> combinations = new ArrayList<>( );
		generateCombinations( combinations, new Stack<>( ), 0, volume );
		return combinations.size( );
	}
	
	/**
	 * Determines the number of (unique) combinations of cups of the smallest
	 * size that can exactly hold the specified volume
	 * 
	 * @param volume The volume of eggnog to store
	 * @return The number of unique cup combinations that together lead to the
	 *   volume, such that the combinations are the smallest possible
	 */
	public long countSmallestCombinations( final int volume ) {
		final List<List<String>> combinations = new ArrayList<>( );
		generateCombinations( combinations, new Stack<>( ), 0, volume );

		// find the minimum combination size
		final int minsize = combinations.stream( ).mapToInt( x -> x.size( ) ).reduce( Integer.MAX_VALUE, Math::min );
		return combinations.stream( ).filter( x -> x.size( ) == minsize ).count( );
	}

	
	/**
	 * Generates all unique combinations of cups that together equal the
	 * required volume and store them in the combinations parameter list 
	 * 
	 * @param combinations The set of combinations found
	 * @param curr The current combination of cups already included
	 * @param idx The index of the current cup to consider
	 * @param volume The target volume
	 */
	private void generateCombinations( final List<List<String>> combinations, final Stack<String> curr, final int idx, final int volume ) {
		if( volume == 0 ) {
			combinations.add( new ArrayList<>( curr ) );
			return;
		}
		if( volume < 0 || idx >= cups.size( ) ) return;
		
		// try both options: either the current cup is or is not included
		generateCombinations( combinations, curr, idx + 1, volume );
		curr.push( "" + cups.get( idx ) );
		generateCombinations( combinations, curr, idx + 1, volume - cups.get( idx ) );
		curr.pop( );
	}
}
