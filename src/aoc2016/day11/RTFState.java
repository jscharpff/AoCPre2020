package aoc2016.day11;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import aocutil.collections.CollectionUtil;

public class RTFState {	
	/** The unique state string */
	private final String state;
	
	/** The number of floors in the factory */
	private final int floors;
	
	/** The floor each item currently resides on */
	private final int[] items;
	
	/** The current elevator position */
	private final int elevator;

	/**
	 * Creates a new RTF layout state
	 * 
	 * @param floors The number of floors in the factory
	 * @param elevator The floor of the elevator
	 * @param items The floor at which the items reside
	 */
	public RTFState( final int floors, final int elevator, final int[] items ) {		
		this.floors = floors;
		this.elevator = elevator;
		this.items = new int[ items.length ];
		for( int i = 0; i < items.length; i++ ) this.items[i] = items[i];
		
		this.state = buildStateString( );
	}
	
	/**
	 * Generates a new state by applying the move
	 * 
	 * @param prevstate The state we are moving from
	 * @param dir The direction the elevator moves in
	 * @param itemindexes The indexes of the items to move
	 */
	private RTFState( final RTFState prevstate, final int dir, final Set<Integer> itemindexes ) {
		floors = prevstate.floors;
		elevator = prevstate.elevator + dir;
		
		items = new int[ prevstate.items.length ];
		for( int i = 0; i < items.length; i++ )
			items[i] = prevstate.items[i] + (itemindexes.contains( i ) ? dir : 0);
		
		state = buildStateString( );
	}

	private String buildStateString( ) {
		final StringBuilder st = new StringBuilder( );
		st.append( floors );
		st.append( elevator );
		for( final int i : items ) st.append( i );
		return st.toString( );
	}
	
	/**
	 * Checks if this is a valid state
	 * 
	 * @return True if the state is valid
	 */
	public boolean isValid( ) {
		// check elevator position
		if( elevator < 0 || elevator >= floors ) return false;
		
		// check for chips not paired with their generators
		for( int i = 1; i < items.length; i += 2 ) {
			// this one is safely paired?
			if( items[i] == items[i-1] ) continue;
			
			// no, check for other generators
			for( int j = 0; j < items.length; j += 2) {
				if( items[j] == items[i] ) return false;
			}
		}
		
		return true;
	}
	
	public boolean isTarget( ) {
		return elevator == floors - 1 && IntStream.of( items ).allMatch( x -> x == floors - 1 );
	}
	
	/**
	 * Generates the set of valid next states from this one
	 * 
	 * @param taboo The set of already visited states
	 * @return The set of all valid new states that can be reahed from the
	 *   current state
	 */
	public Set<RTFState> getNextStates( final Set<RTFState> taboo ) {
		final Set<RTFState> nextstates = new HashSet<>( );
		
		// generate possible moves
		final IntStream indexes = IntStream.range( 0, items.length ).filter( x -> items[x] == elevator );
		final List<Set<Integer>> moves = CollectionUtil.generateSubSets( new HashSet<>( indexes.boxed( ).toList( ) ), x -> x.size( ) == 1 || x.size( ) == 2 );
		
		// and try them both up and down, if valid
		for( final int flinc : new int[] { -1, 1 } ) {			
			if( elevator + flinc < 0 || elevator + flinc >= floors ) continue;
			
			for( final Set<Integer> move : moves ) {				
				// check if this is a valid state and not seen before
				final RTFState newstate = new RTFState( this, flinc, move );
				if( newstate.isValid( ) && !taboo.contains( newstate ) ) nextstates.add( newstate );
			}
		}
		return nextstates;
	}

	
	/**
	 * Generates a long visualisation of the current state
	 * 
	 * @param elements The element names, must correlate with indexes of the
	 *   items
	 * @return A visual representation of the factory lay-out
	 */
	public String toLongString( final String[] elements ) {
		final StringBuilder res = new StringBuilder( );
		res.append( state + "\n" );
		for( int fl = floors - 1; fl >= 0; fl-- ) {
			res.append( "F" + (fl+1) + ": " + (elevator == fl ? "E" : " ") ); 
			
			for( int i = 0; i < items.length; i++ )
				res.append( " " + (items[i] == fl ? elements[i] : ". " ) );
			
			res.append( "\n" );
		}
		return res.toString( );
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof RTFState) ) return false;
		return state.equals( ((RTFState)obj).state );
	}
	
	@Override
	public int hashCode( ) {
		return state.hashCode( );
	}
	
	@Override
	public String toString( ) {
		return state;
	}
}
