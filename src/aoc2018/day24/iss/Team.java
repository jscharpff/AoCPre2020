package aoc2018.day24.iss;

import java.util.ArrayList;
import java.util.List;

import aocutil.object.LabeledObject;

/**
 * One of the teams in the Immune System Simulator
 * 
 * @author Joris
 */
public class Team extends LabeledObject {
	/** The unit groups in the team */
	private List<UnitGroup> groups;
	
	/**
	 * Creates a new team
	 * 
	 * @param name The team name
	 */
	private Team( final String name ) {
		super( name );
		this.groups = new ArrayList<>( );
	}
	
	/** @return The team name */
	protected String getName( ) { return label; }
	
	/**
	 * Adds a unit to this team
	 * 
	 * @param unit The unit to add
	 */
	private void addUnit( final UnitGroup unit ) {
		groups.add( unit );
		unit.setTeam( this );
	}
	
	/**
	 * Removes a unit from the game because it has died
	 * 
	 * @param unit The unit to remove
	 */
	protected void loseUnit( final UnitGroup unit ) {
		groups.remove( unit );
	}
	
	/**
	 * @return The list of unit groups that are still alive
	*/
	protected List<UnitGroup> getUnits( ) {
		return new ArrayList<>( groups );		
	}
	
	/** @return True iff the team has at least one living unit */
	public boolean hasAlive( ) { return groups.size( ) > 0; }
	
	/** @return The total number of living units in this team, regardless of group */
	public int getUnitCount( ) {
		return groups.stream( ).mapToInt( u -> u.stats.size ).sum( );
	}
	
	/**
	 * Creates a new team of units from a string description
	 * 
	 * @param input The team description
	 * @return The team
	 */
	public static Team fromString( final String input ) {
		final String[] s = input.split( "\n" );
		final String name = s[0].substring( 0, s[0].indexOf( ':' ) );
		final Team team = new Team( name );
		
		// parse and add all of its units
		for( int i = 1; i < s.length; i++ ) {
			team.addUnit( UnitGroup.fromString( s[i], i ) );
		}
		
		return team;
	}
	
	/**
	 * @return The description of this team and its units
	 */
	@Override
	public String toString( ) {
		final StringBuilder s = new StringBuilder( );
		s.append( label );
		s.append( ":" );
		for( final UnitGroup u : groups ) {
			s.append( "\n" );
			s.append( u.toString() );
		}
		return s.toString( );
	}
}
