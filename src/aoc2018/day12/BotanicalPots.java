package aoc2018.day12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import aocutil.string.RegexMatcher;

/**
 * A botanical garden composed of infinite pots that potentially hold a plant.
 * Offers a simulation to determine the spread of plants over generations.
 * 
 * @author Joris
 */
public class BotanicalPots {
	/** The index of the '0' pot */
	private long potzero;
	
	/** The array of pots, with value true if they contain a plant */
	private List<Boolean> pots;

	/** The rule set for plant growth */
	private final List<Rule> rules;
	
	/**
	 * Creates a new botanical garden of pots
	 * 
	 * @param initial The string describing the initial state
	 * @param rules The list of rules
	 */
	private BotanicalPots( final Collection<Boolean> initial, final Collection<Rule> rules ) {
		this.pots = new ArrayList<>( initial );
		this.rules = new ArrayList<>( rules );
		potzero = 0;
	}
	
	/**
	 * Runs the simulation for the given number of generations
	 * 
	 * @param gens The number of generations
	 */
	public void run( final int gens ) {
		for( int i = 0; i < gens; i++ ) step( );
	}
	
	/**
	 * Runs a single step in the simulation, producing a new generation of plants
	 */
	private void step( ) {
		// extend current pots list to facilitate matching
		final int numextra = 4;
		potzero += numextra - 2;
		for( int i = 0; i < numextra; i++ ) {
			pots.add( 0, false );
			pots.add( false );
		}

		// now perform window-based matching over the entire pot set
		final List<Boolean> newpots = new ArrayList<>( pots.size( ) );
		for( int idx = 2; idx < pots.size( ) - 2; idx++ ) {
			// find matching rule
			final int i = idx;
			
			final Optional<Rule> rule = rules.stream( ).filter( r -> r.matches( pots, i ) ).findFirst( );
			newpots.add( rule.isPresent( ) ? rule.get( ).result : false );
		}
		
		// remove extra pots to the left and right
		for( int i = 0; i < numextra; i++ ) {
			if( newpots.get( 0 ) ) break;
			potzero--;
			newpots.remove( 0 );
		}
		for( int idx = newpots.size( ) - 1; idx >= 0; idx-- ) {
			if( newpots.get( idx ) ) break;
			newpots.remove( idx );
		}
		
		pots.clear( );
		pots.addAll( newpots );
	}
	
	/**
	 * Sums the pot numbers that contain a plant in the current state
	 * 
	 * @return The sum of pot numbers
	 */
	public long sumPots( ) {
		long sum = 0;
		for( int i = 0; i < pots.size( ); i++ ) {
			if( pots.get( i ) ) sum += (i - potzero);
		}
		return sum;
	}
	
	/**
	 * "Runs" the simulation for an extended period of time and returns the sum
	 * of pots that contain a plant. Only works if the simulation converges!
	 * 
	 * @param gens The number generations to simulate
	 * @param maxconvergesteps The maximum number of steps to check for
	 * 	convergence
	 * @return The sum of pot numbers of pots that contain a plant after the
	 *   number of generations have been simulated
	 */
	public long runLongterm( final long gens, final long maxconvergesteps ) {
		// run the simulation until the pot array converges 
		String prev = "";
		long prevzero = -1;
		long rounds;
		boolean converged = false;
		for( rounds = 0; rounds < gens && rounds < maxconvergesteps; rounds++ ) {
			step();
			
			// check for changes
			final String curr = potsToString( pots );
			converged = curr.equals( prev );
			if( converged ) break;
			
			prev = curr;
			prevzero = potzero;
		}
		
		// check if we should have converged by now
		if( !converged && rounds >= maxconvergesteps ) throw new RuntimeException( "Not converged" );
		
		// increase index of pot '0' based upon remaining generations if we converged
		if( converged ) potzero += (gens - rounds - 1) * (potzero - prevzero);
		return sumPots( );
	}
	
	/**
	 * Creates a new array of pots and plants from a list of strings describing
	 * its initial configuration and the rule set
	 * 
	 * @param input The input as a list of strings
	 * @return The botanical pots garden
	 */
	public static BotanicalPots fromStringList( final List<String> input ) {
		final List<String> in = new ArrayList<>( input );
		final String initial = RegexMatcher.extract( "initial state: ([#\\.]+)", in.remove( 0 ) );
		in.remove( 0 );
		
		return new BotanicalPots( potsFromString( initial ), in.stream( ).map( Rule::fromString ).toList( ) );
	}
	
	/**
	 * @return The current array of pots as a string
	 */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "Pots: " );
		sb.append( pots.size( ) );
		sb.append( ", zeropot: " );
		sb.append( potzero );
		sb.append( "\n" );
		for( final boolean b : pots ) sb.append( b ? "#" : "." );
		sb.append( "\n" );
		for( int i = 0; i < potzero; i++ ) sb.append( " " );
		sb.append( "^" );
		return sb.toString( );
	}
	
	/**
	 * Converts a string description of pots into a boolean array such that it
	 * contains a true value for every pot that has a plant
	 * 
	 * @param s The input string
	 * @return The array of booleans
	 */
	private static List<Boolean> potsFromString( final String s ) {
		final List<Boolean> pots = new ArrayList<Boolean>( s.length( ) );
		for( int i = 0; i < s.length( ); i++ )
			pots.add( s.charAt( i ) == '#' );
		return pots;
	}
	
	/**
	 * Converts a list of pots into a string description
	 * 
	 * @param pots The array of booleans
	 * @return The string representing the array
	 */
	private static String potsToString( final List<Boolean> pots ) {
		final StringBuilder sb = new StringBuilder( );
		for( final boolean b : pots ) sb.append( b ? "#" : "." );
		return sb.toString( );
	}

	/**
	 * Class that represents a single plant growth rule
	 */
	private static class Rule {
		/** The plant configuration to match */
		private final List<Boolean> match;
		
		/** The result of the rule */
		private final boolean result;
		
		/**
		 * Creates a new plant growth rule
		 * 
		 * @param list The matching array
		 * @param result The resulting plant growth if the rule is matched
		 */
		private Rule( final List<Boolean> list, final boolean result ) {
			this.match = list;
			this.result = result;
		}
		
		/**
		 * Checks whether the rule matches the current input, using the index as
		 * the middle position of the pots to check
		 * 
		 * @param pots The array describing the current pot configuration
		 * @param index The index of the pots to check
		 * @return True if the rule matches
		 */
		public boolean matches( final List<Boolean> pots, final int index ) {
			final int N = (match.size( ) - 1) / 2;
			for( int i = 0; i < match.size( ); i++ )
				if( !pots.get( index + i - N ).equals( match.get( i ) ) ) return false;
			return true;
		}
		
		/** @return The string description of the rule */
		@Override
		public String toString( ) {
			final StringBuilder sb = new StringBuilder( );
			sb.append( BotanicalPots.potsToString( match ) );
			sb.append( " => " );
			sb.append( result ? "#" : "." );
			return sb.toString( );
		}
		
		/**
		 * Creates a new rule from a string description
		 * 
		 * @param input The string description of the rule
		 * @return The rule
		 */
		public static Rule fromString( final String input ) {
			final String[] s = input.split( " => " );
			return new Rule( BotanicalPots.potsFromString( s[0] ), s[1].equals( "#" ) );
		}
	}
}
