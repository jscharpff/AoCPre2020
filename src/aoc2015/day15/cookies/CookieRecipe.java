package aoc2015.day15.cookies;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a recipe for baking cookies with its score 
 * 
 * @author Joris
 */
public class CookieRecipe {
	/** The mapping that describes the mix */
	private Map<Ingredient, Integer> mix;
	
	/* The score of the mix */
	private long score;
	
	/**
	 * Creates a new empty mix
	 */
	public CookieRecipe( ) {
		mix = null;
		score = -1;
	}
	
	/**
	 * Sets the mix of the recipe
	 * 
	 * @param mix The mix
	 * @param score The score of this mix
	 */
	public void set( final Map<Ingredient, Integer> mix, final long score ) {
		this.mix = new HashMap<Ingredient, Integer>( mix );
		this.score = score;
	}
	
	/** @return The score of the mix */
	public long getScore( ) {
		return score;
	}
	
	@Override
	public String toString( ) {
		return mix.toString( ) + " => " + score;
	}
}
