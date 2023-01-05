package aoc2019.day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A NanoFactory that can produce fuel from ore 
 * 
 * @author Joris
 *
 */
public class NanoFactory {
	/** The ruleset, indexed by the name of the element they produce */
	private final Map<String, NFRule> rules;
	
	/**
	 * Creates a new NanoFactory with the given ruleset
	 * 
	 * @param rules The set of reduction rules
	 */
	private NanoFactory( final Map<String, NFRule> rules ) {
		this.rules = new HashMap<>( rules );
	}
	
	/**
	 * Determine minimum amount of element X required to produce one element Y
	 * 
	 * @param X The input element
	 * @param Y The goal element
	 * @param amount The amount of Y to produce
	 * @return The minimum amount of element X required to produce 1 Y
	 */
	public long getMinimumRequired( final String X, final String Y, final long amount ) {
		final Map<String, Long> mats = new HashMap<>( );
		getMinimumRequired( X, Y, amount, mats );
		return mats.get( X );
	}
	
	/**
	 * Determine minimum amount of element X required to produce one element Y
	 * 
	 * @param X The input element
	 * @param Y The goal element
	 * @param amount The amount of Y to produce
	 * @return The minimum amount of element X required to produce 1 Y
	 */
	private void getMinimumRequired( final String X, final String Y, final long amount, final Map<String, Long> mats ) {
		// we have reached the element we are looking for?
		if( Y.equals( X ) ) {
			mats.put( X, mats.getOrDefault( X, 0l ) + amount );
			return;
		}
		
		// find rule to produce element Y and reverse it, store surplus ingredients
		final NFRule r = rules.get( Y );
		final long existing = mats.getOrDefault( Y, 0l );
		final long multiple = (long)Math.ceil( Math.max( amount - existing, 0 ) / (double)r.result.count );
		final long surplus = r.result.count * multiple - (amount - existing);
		mats.put( Y, surplus );
		
		// recurse on the ingredients now required because of the rule
		for( final NFElem e : r.elements ) {
			getMinimumRequired( X, e.name, multiple * e.count, mats );
		}
	}
	
	/**
	 * Determines the maximum amount of Y that can be produced from the given
	 * amount of X
	 * 
	 * @param X The source element name
	 * @param amount The amount of element X
	 * @param Y The target element name
	 * @return The maximum amount of Y that can be produced from X
	 */
	public long getMaximumProduced( final String X, final long amount, final String Y ) {
		long min = 0, max = amount;
		long ore = 0;
		while( min != max ) {
			final long half = (long)Math.ceil( (max + min) / 2.0 );
			
			ore = getMinimumRequired( X, Y, half );
			if( ore > amount ) 
				max = half - 1; 
			else
				min = half;
			
		}
		return min;
	}
	
	/**
	 * Reconstructs the NanoFactory from a list of strings
	 * 
	 * @param input The list of strings that describe the rules
	 * @return The NanoFactory
	 */
	public static NanoFactory fromStringList( final List<String> input ) {
		final Map<String, NFRule> ruleset = new HashMap<>( input.size( ) );
		for( final String s : input ) {
			final NFRule r = NFRule.fromString( s );
			ruleset.put( r.result.name, r );
		}
		return new NanoFactory( ruleset );
	}
	
	/** @return The ruleset of this nanofactory */
	@Override
	public String toString( ) {
		return rules.toString( );
	}
	

	/** A single rule */
	private static class NFRule {
		/** The list of required elements and their counts */
		private final List<NFElem> elements;
		
		/** The produced result of the rule */
		private final NFElem result;
		
		/**
		 * Creates a new NFRule
		 * 
		 * @param elements The elements required
		 * @param result The element produced
		 */
		public NFRule( final List<NFElem> elements, final NFElem result ) {
			this.elements = new ArrayList<>( elements );
			this.result = result;
		}

		/**
		 * Reconstructs a rule from a string description
		 * 
		 * @param input The rule as a string
		 * @return The NFRule
		 */
		public static NFRule fromString( final String input ) {
			final String[] i = input.split( " => " );
			return new NFRule( Stream.of( i[0].split( ", " ) ).map( NFElem::fromString ).toList( ), NFElem.fromString( i[1] ) );
		}
		
		/** @return The string describing the rule */
		@Override
		public String toString( ) {
			return elements.toString( ) + " => " + result;
		}
	}
	
	/** A single element and its requirement/production count */
	private static class NFElem {
		/** The element name */
		private final String name;
		
		/** The number required/produced of this element */
		private final long count;
		
		/**
		 * Creates a new NFElem
		 * 
		 * @param name The element name
		 * @param count The count produced/required
		 */
		public NFElem( final String name, final long count ) {
			this.name = name;
			this.count = count;
		}

		/**
		 * Reconstructs the element from a string
		 * 
		 * @param input The element as a string "# NAME"
		 * @return The NFElem
		 */
		public static NFElem fromString( final String input ) {
			final String[] s = input.split( " " );
			return new NFElem( s[1], Integer.parseInt( s[0] ) );
		}
		
		/** @return The string representation of the element */
		@Override
		public String toString( ) {
			return count + " " + name;
		}
	}

}
