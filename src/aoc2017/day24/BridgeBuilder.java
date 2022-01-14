package aoc2017.day24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Class that offers algorithms to build bridges
 * 
 * @author Joris
 */
public class BridgeBuilder {
	/** The list of available components */
	private final List<BridgeComponent> components;
	
	/** The strongest bridge found so far */
	private Bridge best;
	
	/**
	 * Creates a new bridge builder using the descriptions of components to
	 * reconstruct its set of available bridge pieces
	 * 
	 * @param components The set of available components
	 */
	public BridgeBuilder( final List<String> components ) {
		this.components = new ArrayList<>( components.stream( ).map( BridgeComponent::fromString ).toList( ) );
	}
	

	/**
	 * Algorithm to determine the strongest possible bridge from the given set of
	 * components
	 * 
	 * @param components The list of available components
	 * @return The bridge that maximises total strength
	 */
	public Bridge getStrongest( ) {
		best = new Bridge( );
		build( new HashSet<>( components ), new Bridge( ), curr -> curr.getStrength( ) > best.getStrength( ) );
		return best;
	}

	/**
	 * Algorithm to determine the longest possible bridge from the given set of
	 * components
	 * 
	 * @param components The list of available components
	 * @return The bridge that maximises the length
	 */
	public Bridge getLongest( ) {
		best = new Bridge( );
		build( new HashSet<>( components ), new Bridge( ), curr -> curr.length( ) > best.length( ) || (curr.length( ) >= best.length( ) && curr.getStrength( ) > best.getStrength( )) );
		return best;
	}
	/**
	 * DFS algorithm to determine the strongest/longest bridge
	 * 
	 * @param remaining The set of remaining bridge components
	 * @param curr The bridge we are currently building
	 * @param optfunc The function that determines the optimality of the bridge
	 */
	private void build( final Set<BridgeComponent> remaining, final Bridge curr, final Function<Bridge, Boolean> optfunc ) {
		// get the out pin of the bridge
		final int out = curr.outPin( );
		// find components to extend the current bridge
		final List<BridgeComponent> next = remaining.stream( ).filter( c -> c.fits( out ) ).toList( );
		if( next.size( ) == 0 ) {
			// no more extension possible
			if( optfunc.apply( curr ) ) best = new Bridge( curr );
			return;
		}
		
		// add component and check again
		for( final BridgeComponent c : next ) {
			remaining.remove( c );
			curr.extend( c );
			build( remaining, curr, optfunc );
			curr.pop( );
			remaining.add( c );
		}
	}	
	
	
}