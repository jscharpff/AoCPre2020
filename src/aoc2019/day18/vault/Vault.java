package aoc2019.day18.vault;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2019.day18.vault.VaultGraph.VGPath;
import aocutil.cache.Cache;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that models the inners of a vault with keys to be collected
 * 
 * @author Joris
// */
public class Vault {
	/** The internal vault structure as connected graph */
	protected final VaultGraph graph;
	
	/** The set of keys remaining in the vault */
	protected final Set<Character> keys;
	
	/** Keep track of global optimum to prune costly paths */
	protected int g_opt;
	
	/**1
	 * Creates a new Vault with the given internal structure
	 * 
	 * @param graph The graphical layout of the vault
	 */
	public Vault( final VaultGraph graph ) {
		this.keys = new HashSet<>( graph.keys.keySet( ) );
		this.graph = graph;		
	}
	
	/**
	 * Collects all the keys in the vault
	 * 
	 * @return The minimal number of steps required to collect all the keys in
	 *   the vault
	 */
	public int collectKeys( ) {
		// create new initial state and cache
		final VState initial = new VState( graph.portals.size( ), graph.keys.keySet( ) );
		final Cache<VState, Integer> M = new Cache<>( );
		g_opt = Integer.MAX_VALUE;
		
		// and run DFS algorithm to find minimal steps required to collect all keys
		return collect( M, initial );
	}
	
	/**
	 * Navigates the vault form the given current state and finds the least
	 * number of steps required to collect all remaining keys
	 * 
	 * @param M The cache of already visited states
	 * @param state The current state to explore from
	 * @return The least number of steps required from this state to collect all
	 *   keys in the vast
	 */
	private int collect( final Cache<VState, Integer> M, final VState state ) {
		// have we been in this state before, but faster?
		if( M.contains( state ) && M.get( state ) <= state.getSteps( ) ) return Integer.MAX_VALUE;
		
		// nope, store this as fastest way to get into this state
		M.set( state, state.getSteps( ) );
		
		// check if this is a terminal state
		if( state.isTerminal( ) ) {
			M.set( state, state.getSteps( ) );
			return state.getSteps( );
		}
		
		// nope, generate new states reachable from here and keep exploring
		final Set<VState> newstates = getNewStates( state );
		int best = Integer.MAX_VALUE;
		for( final VState s : newstates ) {
			// compute LB on steps and skip the state if it takes to many steps
			if( s.getSteps( ) >= g_opt ) continue;

			// explore it!
			final int steps = collect( M, s );

			// update bounds is better
			if( steps < best ) best = steps;
			if( best < g_opt ) g_opt = best;
		}

		// return least number of steps
		return best;
	}

	/**
	 * Builds the set of new states that are reachable from the given current
	 * state
	 * 
	 * @param state The current state
	 * @return The set of new states
	 */
	private Set<VState> getNewStates( final VState state ) {
		final Set<VState> N = new HashSet<>( );
		
		// generate new states by considering a new key to pick up for every player
		// (including doing nothing)
		final int n = state.getPlayers( );
		generateStates( N, state, new ArrayList<>( state.getKeysRemaining( ) ), new char[ n ], new int[ n ], 0, false );
		
		return N;
	}

	/**
	 * Recursive function to enumerate all new states as a result of considering
	 * all (combinations of) actions from the current state
	 * 
	 * @param N The set of states being generated
	 * @param state The current state from which we generate
	 * @param keysrem The remaining set of keys to collect
	 * @param targets The targets chosen for each player
	 * @param steps The steps traversed for each player
	 * @param index The index of the player for which we have to pick a target
	 * @param acted True iff any of the players chose a target
	 */
	private void generateStates( final Set<VState> N, final VState state, final List<Character> keysrem, final char[] targets, final int[] steps, final int index, final boolean acted ) {
		// done picking actions, generate state and add it
		if( index >= targets.length ) {
			if( acted ) N.add( state.extend( targets, steps ) );
			return;
		}
		
		// pick any of the keys as new target for the current player
		for( int i = keysrem.size( ) - 1; i >= 0; i-- ) {
			// check if the path to the key exists and is open
			final char key = keysrem.get( i );
			final VGPath path = graph.getPath( state.getPosition( index ), key );
			if( path == null || !state.hasKeys( path.keysreq ) ) continue;
			
			// yes, generate the new state that results
			keysrem.remove( i );
			targets[ index ] = key;
			steps[ index ] = path.length;
			generateStates( N, state, keysrem, targets, steps, index + 1, true );
			keysrem.add( i, key );
		}
		
		// include not moving as outcome for this player
		targets[ index ] = ' ';
		steps[ index ] = 0;
		generateStates( N, state, keysrem, targets, steps, index + 1, acted );
	}
	
	/**
	 * Reconstructs the vault from a grid of strings
	 * 
	 * @param input The list of strings describing the vault layout
	 * @param multibot True to replace the single portal by 4 new ones, thus
	 *   enabling collection with multiple bots 
	 * @return The Vault
	 */
	public static Vault fromStringList( final List<String> input, final boolean multibot ) {
		// reconstruct vault layout from input
		final CoordGrid<Character> map = CoordGrid.fromCharGrid( input, '.' );
		
		// for the multi-bot scenario, replace the portal
		if( multibot ) {
			// find the portal and replace it with 4 new ones
			for( final Coord2D c : map ) {
				if( map.get( c ) == '@' ) {
					map.set( c.move( -1, -1 ), '@' );
					map.set( c.move(  0, -1 ), '#' );
					map.set( c.move(  1, -1 ), '@' );
					for( final int x : new int[] { -1, 0, 1 } ) map.set( c.move( x,  0 ), '#' );
					map.set( c.move( -1,  1 ), '@' );
					map.set( c.move(  0,  1 ), '#' );
					map.set( c.move(  1,  1 ), '@' );
					break;
				}
			}
		}
		
		// the rest is processed as before
		System.err.print( "Building map..." );
		final VaultGraph vaultmap = new VaultGraph( map );
		System.err.println( "done!" );
		
		return new Vault( vaultmap );		
	}
}
