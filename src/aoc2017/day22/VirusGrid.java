package aoc2017.day22;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Computing node grid with a virus running loose!
 * 
 * @author Joris
 */
public class VirusGrid {
	/** The grid of infections */
	private CoordGrid<InfectionState> grid;
	
	/** Position of the virus carrier */
	private Virus virus;
	
	/**
	 * Creates a new virus grid from an initial grid description
	 * 
	 * @param grid The initial node grid as a list of strings
	 */
	public VirusGrid( final List<String> grid ) {
		this.grid = CoordGrid.fromStringList( grid, null, InfectionState::fromString, InfectionState.Clean );
		
		final int centerX = (int)Math.floor( grid.get( 0 ).length( ) / 2.0 );
		final int centerY = (int)Math.floor( grid.size( ) / 2.0 );
		
		virus = new Virus( new Coord2D( centerX, centerY ) );
	}
	
	/**
	 * Runs the virus grid simulation for the given number of steps
	 * 
	 * @param steps The number of steps
	 * @param evolved True if the virus has evolved
	 * @return The number of infections that happened during the simulation
	 */
	public long run( final int steps, final boolean evolved ) {
		long infections = 0;
		for( int i = 0; i < steps; i++ ) 
			infections += (evolved ? stepEvolved( ) : step( )) ? 1 : 0;
		return infections;
	}
	
	/**
	 * Runs a single step in the virus simulation
	 * 
	 * @return True if a clean node was infected
	 */
	private boolean step( ) {
		final Coord2D currpos = virus.getPosition( );
		final boolean infected = grid.get( currpos ) == InfectionState.Infected;
		
		// If the current node is infected, it turns to its right. Otherwise, it
		// turns to its left.
		virus.turn( !infected );
		
		// If the current node is clean, it becomes infected. Otherwise, it becomes
		// cleaned.
		final boolean newval = !infected;
		if( newval ) grid.set( currpos, InfectionState.Infected );
		else grid.unset( currpos );
		
		// The virus carrier moves forward one node in the direction it is facing.
		virus.move( 1 );
		return newval;
	}
	
	/**
	 * Runs a single step of the virus simulation, now with an evolved virus
	 * carrier
	 * 
	 * @return True if the carrier caused a new infection
	 */
	private boolean stepEvolved( ) {
		final Coord2D currpos = virus.getPosition( );
		final InfectionState state = grid.get( currpos );

		// decide which way to turn based on the current node:
		switch( state ) {
			case Clean: virus.turn( false ); break;
			case Weakened: break;
			case Infected: virus.turn( true ); break;
			case Flagged: virus.reverse( ); break;
		}			
		
		// affect the current node Clean -> Weakened -> Infected -> Flagged -> Clean
		final InfectionState newstate = state.next( );
		grid.set( currpos, newstate );
		
		// move the virus
		virus.move( 1 );
		
		return newstate == InfectionState.Infected;
	}
	
	/** @return The visualisation of the current grid */
	@Override
	public String toString( ) {
		final Map<Coord2D, String> map = new HashMap<>( );
		map.put( virus.getPosition( ), "[" + grid.get( virus.getPosition( ) ).toString( ) + "]"  );
		return grid.toString( s -> " " + s + " ", map );
	}
	
	/**
	 * State of the infection at a grid node
	 */
	private enum InfectionState {
		Clean, Weakened, Infected, Flagged;
		
		/** @return The string symbol that represents the state */
		@Override
		public String toString( ) {
			switch( this ) {
				case Clean: return ".";
				case Weakened: return "W";
				case Infected: return "#";
				case Flagged: return "F";
				default: throw new RuntimeException( "Invalid infection state: " + this );
			}
		}
		
		/**
		 * @param str The string value 
		 * @return The state from its description
		 */
		public static InfectionState fromString( final String str ) {
			for( final InfectionState state : values( ) )
				if( state.toString( ).equals( str ) ) return state;
			
			throw new RuntimeException( "Invalid state description: " + str );
		}
		
		/** @return The next state of the infection */
		public InfectionState next( ) {
			return values( )[ (this.ordinal( ) + 1) % values( ).length ];
		}
	}
}
