package aoc2018.day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;

/**
 * Simulates a lumber collection process
 * 
 * @author Joris
 */
public class LumberCollection {
	/** The grid that models the current state of the area */
	private CoordGrid<Character> area;
	
	/** The various tile types */
	private final static char C_OPEN = '.', C_WOOD = '|', C_YARD = '#';
	
	/**
	 * Creates a new LumberCollection area
	 * 
	 * @param area The initial state of the area
	 */
	private LumberCollection( final CoordGrid<Character> area ) {
		this.area = area;
	}
	
	/**
	 * Runs the specified number of simulation steps. Uses a cycle detection to
	 * speed up the process if the simulation is a cyclic process  
	 * 
	 * @param steps The steps to simulate
	 * @return True iff there are still trees left after the number of steps
	 */
	public long run( final int steps ) {
		final Map<Long, Integer> values = new HashMap<>( );
		final List<Long> currcyle = new ArrayList<>( );
		for( int i = 0; i < steps; i++ ) {
			step( );
			
			// get the resource value, also check for exhaustion
			final long val = getResourceValue( );
			if( val == 0 ) return 0;
			
			// seen before? keep track of the values as a current cycle
			if( values.containsKey( val ) ) {
				// check if the cycle is repeating, if so we can skip
				if( currcyle.contains( val ) ) {
					// skip as many cycles as we can
					final int cyclelen = currcyle.size( );
					i += (steps - i) / cyclelen * cyclelen;
				}
				
				// nope, add to current cycle
				currcyle.add( val );
			} else {
				// value was not seen before, clear the cycle detection list
				currcyle.clear( );
			}
			
			// store values for cycle detection
			values.put( val, i );
		}
		return getResourceValue( );
	}
	
	/**
	 * Runs a single step of the simulation that will update the area using the
	 * simulation rules
	 */
	private void step( ) {
		final CoordGrid<Character> newarea = area.copy( );
		
		// determine new terrain type based upon its current type and the
		// neighbouring tiles
		for( final Coord2D c : area ) {
			final char ch = area.get( c );
			final char newch;
			switch( ch ) {
				case C_OPEN:
					newch = area.getNeighbours( c, true, x -> area.get( x ) == C_WOOD ).size( ) >= 3 ? C_WOOD : ch;
					break;
					
				case C_WOOD:
					newch = area.getNeighbours( c, true, x -> area.get( x ) == C_YARD ).size( ) >= 3 ? C_YARD : ch;
					break;
					
				case C_YARD:
					final Set<Character> N = new HashSet<>( area.getNeighbours( c, true ).stream( ).map( area::get ).toList( ) );
					newch = N.contains( C_WOOD ) && N.contains( C_YARD ) ? C_YARD : C_OPEN; 
					break;
					
				default:
					throw new RuntimeException( "Invalid tile type: " + ch );
			}
			newarea.set( c, newch );
		}
		
		// swap grids
		area = newarea;
	}
	
	/** @return The resource value */
	public long getResourceValue( ) {
		return area.count( C_WOOD ) * area.count( C_YARD );
	}
	
	/**
	 * Creates a new LumberCollection from a grid visualisation of the area
	 * 
	 * @param input The list of strings that visualises the grid
	 * @return The {@link LumberCollection} object
	 */
	public static LumberCollection fromStringList( final List<String> input ) {
		final CoordGrid<Character> grid = CoordGrid.fromCharGrid( input, C_OPEN );
		grid.fixWindow( new Window2D( input.get( 0 ).length( ), input.size( ) ) );
		return new LumberCollection( grid );
	}
	
	/** @return The area as a string */
	@Override
	public String toString( ) {
		return area.toString( );
	}
}
