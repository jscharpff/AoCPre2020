package aoc2019.day18.vault;

import java.util.HashSet;
import java.util.Set;

/**
 * Captures the state of the vault
 * 
 * @author Joris
 */
public class VState {
	/** The states of the players */
	private final PState[] ps;
	
	/** The set of keys to collect still */
	private final Set<Character> keys;
	
	/** The state string that uniquely describes it */
	private final String statestr;

	/**
	 * Creates a new initial state with n players
	 * 
	 * @param n The number of players in the state
	 * @param keys The keys to collect
	 */
	protected VState( final int n, final Set<Character> keys ) {
		this.ps = new PState[ n ];
		for( int i = 0; i < n; i++ ) ps[i] = new PState( i );
		this.keys = new HashSet<>( keys );
		
		statestr = toStateString( );
	}
	
	/**
	 * Creates a new state by processing the moves from the given state
	 * 
	 * @param state The state to start from
	 * @param targets The array of moves, one per player, that describe the new
	 * location of the player. Can be ' ' to signify no move
	 * @param steps The number of steps it takes the player to perform the move
	 */
	private VState( final VState state, final char[] targets, final int[] steps ) {
		// create new player states
		this.ps = new PState[ state.getPlayers() ];
		for( int i = 0; i < state.getPlayers( ); i++ ) {
			if( targets[i] == ' ' ) ps[i] = state.ps[i];
			else ps[i] = new PState( targets[i], state.ps[i].steps + steps[i] );
		}
		
		// copy keys and remove those that are picked up now
		this.keys = new HashSet<>( state.keys );
		for( final char c : targets ) if( c != ' ' ) this.keys.remove( c );
		
		statestr = toStateString( );
	}
	
	/**
	 * Extends the current state by processing the moves to the specified targets
	 * with associated distances
	 * 
	 * @param targets The array of moves, one per player, that describe the new
	 * location of the player. Can be null to signify no move
	 * @param steps The number of steps it takes the player to perform the move
	 * @return The resulting new state
	 */
	public VState extend( final char[] targets, final int[] steps ) {
		return new VState( this, targets, steps );
	}
	
	/**
	 * Checks if this state is a terminal state
	 * 
	 * @return True iff all keys are picked up
	 */
	public boolean isTerminal( ) {
		return keys.size( ) == 0;		
	}
	
	/** @return The number of players in the state */
	public int getPlayers( ) { return ps.length; }
	
	/** @return The set of remaining keys */
	public Set<Character> getKeysRemaining( ) {
		return keys;
	}
	
	/**
	 * Tests whether all keys in the set have been collected in this state
	 * 
	 * @param keysreq The required set of keys
	 * @return True iff none of the keys is present in the set of remaining key
	 */
	public boolean hasKeys( final Set<Character> keysreq ) {
		for( final char key : keysreq )
			if( keys.contains( key ) ) return false;
		
		return true;
	}
	
	/** @return The sum of all steps taken so far */
	public int getSteps( ) {
		int sum = 0;
		for( final PState p : ps ) sum += p.steps;
		return sum;
	}
	
	/** 
	 * Returns the position of the specified player (by index)
	 * 
	 * @param index The player index
	 * @return The current position of that player
	 */
	public char getPosition( final int index ) {
		return ps[ index ].pos;
	}
	
	
	/**
	 * Checks if this state is 'better' compared to another state. The state is
	 * considered better (or at least equally good) if it has at least the same
	 * keys, players are in the same positions and the number of steps is at
	 * least equal.
	 * 
	 * @param state The state to compare against
	 * @return True iff this state is considered better
	 */
	public boolean isBetterThan( final VState state ) {
		// number of steps must be lower or equal
		if( getSteps( ) >= state.getSteps( ) ) return false;
		
		// all keys must be present
		if( !this.hasKeys( state.keys ) ) return false;
		
		// and the positions must be the same
		for( int i = 0; i < ps.length; i++ ) 
			if( ps[i].pos != state.ps[i].pos ) return false;
		
		return true;
	}
	
	
	/**
	 * Compares this state against the object for equality
	 * 
	 * @param obj The object to compare against
	 * @return True iff the state has the same keys and player states
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( obj == null || !(obj instanceof VState) ) return false;
		final VState v = (VState) obj;
		
		// compare key ring and player states
		if( !keys.equals( v.keys ) ) return false;
		if( ps.length != v.ps.length ) return false;
		for( int i = 0; i < ps.length; i++ ) if( ps[i].pos != v.ps[i].pos ) return false;
		
		return true;
	}
	
	/** @return The unique hash code, using the state string */
	@Override
	public int hashCode( ) {
		return statestr.hashCode( );
	}
	
	/** @return The state string */
	@Override
	public String toString( ) {
		return statestr;
	}
	
	/** @return Generates the string that uniquely describes the state */
	private String toStateString( ) {
		final StringBuilder sb = new StringBuilder( );
		for( final PState p : ps ) {
			sb.append( p.toString( ) );
			sb.append( "," );
		}
		sb.append( keys.toString( ) );
		return sb.toString( );
	}
	
	
	
	/** The state of an individual player */
	protected class PState {
		/** The current position of the player, portal or key */
		protected final char pos;  
		
		/** The number of steps taken */
		protected final int steps;
		
		/**
		 * Creates a new state
		 * 
		 * @param pos The position of the player
		 * @param steps The steps taken so far
		 */
		protected PState( final char pos, final int steps ) {
			this.pos = pos;
			this.steps = steps;
		}
		
		/**
		 * Creates new initial player state
		 */
		protected PState( final int index ) {
			this( (char)('1' + index), 0 );
		}
		
		/**
		 * Compares this player state to another object for equality
		 * 
		 * @param obj The object to test against
		 * @return True iff the position and steps are the same
		 */
		@Override
		public boolean equals( final Object obj ) {
			if( obj == null || !(obj instanceof PState) ) return false;
			final PState p = (PState) obj;
			
			return pos == p.pos && steps == p.steps;
		}
		
		/** @return The string that describes the player state */
		@Override
		public String toString( ) {
			return "" + pos + "|" + steps;
		}
	}
}
