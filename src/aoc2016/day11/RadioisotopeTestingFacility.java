package aoc2016.day11;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadioisotopeTestingFacility {
	/** The number of floors in the facility */
	private final int floors;
	
	/** The (short) element names */
	private final String[] elements;
	
	/** The initial state of the RTF */
	final RTFState state;
	
	/**
	 * Creates a new RTF, using the given list of floor contents to reconstruct
	 * its current state
	 * 
	 * @param input The floor contents, one string per floor
	 */
	public RadioisotopeTestingFacility( final List<String> input ) {
		floors = input.size( );
		
		// first collect all element names and give them an index
		final Map<String, Integer> elmap = new HashMap<>( );
		int elidx = 0;
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "(\\w+)-compatible microchip" ).matcher( s );
			while( m.find( ) ) elmap.put( m.group( 1 ), elidx++ );
		}
		
		// generate element names
		elements = new String[ elmap.size( ) * 2 ];
		for( final String e : elmap.keySet( ) ) {
			final String elpfx = e.substring( 0, 1 ).toUpperCase( );
			elements[ elmap.get( e ) * 2 ] = elpfx + "G";
			elements[ elmap.get( e ) * 2 + 1 ] = elpfx + "M";
		}
		
		// allocate element to floor array and scan input again to fill it
		final int[] items = new int[ elmap.size( ) * 2 ];
		int floor = 0;
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "(\\w+)(-compatible microchip| generator)" ).matcher( s );
			while( m.find( ) ) {
				items[ elmap.get( m.group( 1 ) ) * 2 + (m.group( 2 ).contains( "generator" ) ? 0 : 1) ] = floor;
			}
			floor++;
		}
		
		this.state = new RTFState( floors, 0, items );
	}
	
	/**
	 * Moves every item in the factory to the assembly machine on the fourth
	 * floor
	 * 
	 * @return The minimal number of moves required to safely move everything to
	 * the fourth floor
	 */
	public long getMinimalMoves( ) {
		final Set<RTFState> visited = new HashSet<>( );
		final Stack<RTFState> nextstates = new Stack<>( );		
		nextstates.add( state );
		
		// explore states in BFS until we reach the target state
		int steps = 0;
		while( !nextstates.empty( ) ) {
			
			// generate set of new states that are reachable from the current set of states
			final Set<RTFState> explore = new HashSet<>( );
			while( !nextstates.empty( ) ) {
				final RTFState s = nextstates.pop( );
				if( visited.contains( s ) ) continue;
				visited.add( s );
				
				// do we need to explore further?
				if( s.isTarget( ) ) return steps;
				
				final Set<RTFState> next = s.getNextStates( visited );
				explore.addAll( next );
			}
			
			// increase step count and set new states to explore
			nextstates.addAll( explore );			
			steps++;
		}
		
		throw new RuntimeException( "Failed to find a solution" );
	}

	/** @return The current factory lay out */
	@Override
	public String toString( ) {
		return state.toLongString( elements );
	}
}
