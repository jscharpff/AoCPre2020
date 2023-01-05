package aoc2018.day15.rts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2018.day15.rts.Unit.UnitType;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * A simulation of an epic battle between elves and goblins
 * 
 * @author Joris
 */
public class CaveBattle {
	/** The coordgrid that models the map */
	protected final CoordGrid<Character> map;
	
	/** The units on the map */
	protected final List<Unit> units;
	
	/**
	 * Creates a new cave battle!
	 * 
	 * @param map The map layout
	 * @param units The initial set of units
	 */
	private CaveBattle( final CoordGrid<Character> map, final Collection<Unit> units ) {
		this.map = map.copy( );
		this.units = new ArrayList<>( units );
	}
	
	/**
	 * Provides a little bit of help to the side of the Elves by increasing their
	 * attack power... This function finds the minimal attack power that will
	 * help the Elves win without any losses
	 * 
	 * @param initmap The initial map of the battle
	 * @return The outcome of the winning battle with minimal ap
	 */
	public static long winningBattle( final List<String> initmap ) {		
		for( int ap = 4; ap < 200; ap++ ) {
			System.out.println( "Trying AP " + ap );
			final CaveBattle B = CaveBattle.fromStringList( initmap );
			final List<Unit> elves = new ArrayList<>( B.units.stream( ).filter( u -> u.getType( ) == UnitType.Elf ).toList( ) );
			for( final Unit e : elves ) e.setAP( ap );
			
			final long score = B.battle( );
			if( elves.stream( ).allMatch( Unit::isAlive ) ) {
				return score;
			}
		}
		
		throw new RuntimeException( "Failed to find a winning battle" );
	}
	
	/**
	 * Runs the cave battle process until one team has been vanquished
	 * 
	 * @return The battle outcome as the product of round times the total hp left
	 */
	public long battle( ) {
		long turn = 0;
		while( true ) {
			if( step( ) ) turn++;
			
			// check if only one team remains
			final Map<UnitType, Integer> teamhealth = new HashMap<>( );
			for( final UnitType type : UnitType.values( ) )
				teamhealth.put( type, units.stream( ).filter( u -> u.getType() == type ).mapToInt( Unit::getHealth ).sum( ) );
			if( teamhealth.values( ).contains( 0 ) ) {
				return turn * teamhealth.entrySet( ).stream( ).filter( e -> e.getValue( ) != 0 ).findFirst( ).get( ).getValue( );
			}
		}
	}
	
	/**
	 * Perform a single round in the cave battle
	 * 
	 * @return True if the battle is still on
	 */
	private boolean step( ) {
		// units get a turn in their "reading order"
		units.sort( (c1,c2) -> c1.getPosition( ).compareTo( c2.getPosition( ) ) );
		
		boolean result = true;
		for( final Unit u : units ) {
			if( u.isAlive( ) ) result &= u.turn( this );
		}
		return result;
	}
	
	/**
	 * Recreates a battle situation from a string description
	 * 
	 * @param input The map as a list of strings
	 * @return The Map object
	 */
	public static CaveBattle fromStringList( final List<String> input ) {
		final CoordGrid<Character> map = CoordGrid.fromCharGrid( input, '.' );
		
		// extract units from the map
		final List<Unit> units = new ArrayList<>( );
		final Set<Coord2D> keys = new HashSet<>( map.getKeys( ) );
		for( final Coord2D c : keys ) {
			final char ch = map.get( c );
			if( ch == 'E' || ch == 'G' ) {
				units.add( new Unit( ch == 'E' ? UnitType.Elf : UnitType.Goblin, c ) );
				map.unset( c );
			}
		}
		
		return new CaveBattle( map, units );
	}
	
	/** @return The visual representation of the map and the units */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "[CaveMap]\n" );
		sb.append( units.toString( ) );
		sb.append( "\n" );
		
		final Map<Coord2D, String> unitpos = new HashMap<>( );
		for( final Unit u : units ) if( u.getHealth( ) > 0 ) unitpos.put( u.getPosition( ), "" + u.toChar( ) );
		sb.append( map.toString( c -> "" + c, unitpos ) );
		
		return sb.toString( );
	}

}
