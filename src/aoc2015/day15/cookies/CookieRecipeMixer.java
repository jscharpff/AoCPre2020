package aoc2015.day15.cookies;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Class that mixes a set of given ingredients until a perfect mix is found 
 * 
 * @author Joris
 */
public class CookieRecipeMixer {
	/** The available ingredients */
	private final Set<Ingredient> ingredients;
 
	/**
	 * Creates a new CookieRecepie that find the composition of the perfect
	 * cookie using to-determine amounts of the available ingredients
	 * 
	 * @param ingredients The collection of available ingredients
	 */
	public CookieRecipeMixer( final Collection<Ingredient> ingredients ) {
		this.ingredients = new HashSet<>( ingredients );
	}
	
	/**
	 * Finds the optimal mix that maximises the ingredient score
	 * 
	 * @param calories The exact calorie score it needs to match, -1 for any
	 * @return The perfect cookie recipe
	 */
	public CookieRecipe findOptimalMix( final int calories ) {
		// create maps to hold the current and optimal mix
		final CookieRecipe optimal = new CookieRecipe( );
		final Map<Ingredient, Integer> mix = new HashMap<>( ingredients.size( ) );
		
		// go over all possible mixes
		final Stack<Ingredient> remaining = new Stack<>( );
		remaining.addAll( ingredients );
		mix( optimal, calories, mix, remaining, 100 );
		
		return optimal;
	}
	
	/**
	 * Recursively adds amounts of every ingredient to the mix until the perfect
	 * mix has been found
	 * 
	 * @param optimal The current optimal recipe
	 * @param calories The exact calorie score it needs to match, -1 for any
	 * @param mix The current mix of ingredients
	 * @param remaining The set of remaining ingredients
	 * @param teaspoons The remaining amount of teaspoons to complete the recipe
	 */
	private void mix( final CookieRecipe optimal, final int calories, final Map<Ingredient, Integer> mix, final Stack<Ingredient> remaining, final int teaspoons ) {
		// no more ingredients remain, compute the score of the mix and save it if
		// it is better than our current best
		if( remaining.size( ) == 0 ) {
			// is this solution valid?
			if( teaspoons != 0 ) return;
			if( calories != -1 && getCalorieScore( mix ) != calories ) return;
			
			// compute score of this mix and return it
			final long score = getMixScore( mix );
			if( score > optimal.getScore( ) ) optimal.set( mix, score );
			return;
		}
		
		// mix next remaining ingredient and try all mixing ratios
		final Ingredient i = remaining.pop( );
		for( int amount = teaspoons; amount >= 1; amount-- ) {
			mix.put( i, amount );
			mix( optimal, calories, mix, remaining, teaspoons - amount );
		}
		remaining.add( i );
	}
	
	/**
	 * Computes the mix score for the given mixing ratio
	 * 
	 * @param mix The mixing as ratio per ingredient
	 * @return The mix score
	 */
	public long getMixScore( final Map<Ingredient, Integer> mix ) {
		long score = 1;
		for( final int prop : new int[] { Ingredient.Capacity, Ingredient.Durability, Ingredient.Flavour, Ingredient.Texture } ) {
			final long propscore = Math.max( 0, mix.keySet( ).stream( ).mapToLong( x -> x.properties[prop] * mix.get( x ) ).reduce( 0, Math::addExact ) );
			score *= propscore;
		}
		
		return score;
	}
	
	/**
	 * Computes the calorie score for the given mixing ratio
	 * 
	 * @param mix The mixing as ratio per ingredient
	 * @return The calorie score
	 */
	public long getCalorieScore( final Map<Ingredient, Integer> mix ) {
		long score = 1;
		for( final int prop : new int[] { Ingredient.Calories } ) {
			final long propscore = Math.max( 0, mix.keySet( ).stream( ).mapToLong( x -> x.properties[prop] * mix.get( x ) ).reduce( 0, Math::addExact ) );
			score *= propscore;
		}
		
		return score;
	}
	
	/**
	 * @return The list of ingredients available to this recipe
	 */
	@Override
	public String toString( ) {
		return ingredients.toString( );
	}
	
	/**
	 * Reads the ingredients from their description and returns a new, empty 
	 * CookieRecipe that holds them
	 * 
	 * @param input The list of ingredient descriptions
	 * @return The new CookieRecipe
	 */
	public static CookieRecipeMixer fromStringList( final List<String> input ) {
		final Set<Ingredient> ingredients = new HashSet<>( input.size( ) );
		for( final String s : input ) {
			ingredients.add( Ingredient.fromString( s ) );
		}
		return new CookieRecipeMixer( ingredients );
	}
}
