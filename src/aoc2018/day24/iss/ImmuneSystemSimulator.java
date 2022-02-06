package aoc2018.day24.iss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulation of a battle of immune system versus infectious intruders
 * 
 * @author Joris
 *
 */
public class ImmuneSystemSimulator {
	/** Enables/disables logging */
	private static boolean logging = false;
	
	/** The two teams in the simulation */
	protected final Team immune, infection;
	
	/**
	 * Creates a new ImmuneSystemSimulator
	 * 
	 * @param immune The immune system team
	 * @param infection The infection's units
	 */
	private ImmuneSystemSimulator( final Team immune, final Team infection ) {
		this.immune = immune;
		this.infection = infection;
	}
	
	/**
	 * Finds the lowest increase in attack power of the immune system team that
	 * will result in the immune system winning the battle
	 *  
	 * @param input The list of strings that describe the battle
	 * @return The remaining units of the immune systems in the battle with the
	 *   lowest attack power boost
	 */
	public static int findAPBoostToWin( final List<String> input ) {
		// use a binary search-style algorithm to split the search space in half
		// every iteration until we found the minimal AP boost value to win
		int min = 0, max = 1000000;
		int lastUnitCount = -1;
		ImmuneSystemSimulator.setLogging( false );
		while( min != max ) {
			final int half = (max + min) / 2;
			System.out.print( "Trying AP " + half + " [" + min + ", " + max + "]: " );
			final ImmuneSystemSimulator sim = ImmuneSystemSimulator.fromStringList( input );
			sim.boostImmuneSystem( half );
			
			// check the winner of the simulation
			lastUnitCount = sim.battle( );
			final boolean immunewins = lastUnitCount != -1 && sim.immune.hasAlive( );
			System.out.println( (immunewins ? "Immune system" : "Infection") + " wins" );
			if( immunewins ) { max = half; } else { min = half + 1; }
			
		}
		
		return lastUnitCount;
	}
	
	/**
	 * Boosts the attack power of all immune system's units by the given amount
	 * 
	 * @param boost The boost to add to their attack power
	 */
	private void boostImmuneSystem( final int boost ) {
		for( final UnitGroup u : immune.getUnits( ) ) u.boost( boost );
	}
	
	/**
	 * Simulates the battle until one of both teams has no more units left
	 * 
	 * @return The total number of remaining living units or -1 in case of draw
	 */
	public int battle( ) {
		// keep on fighting until one team has no units left
		while( immune.hasAlive( ) && infection.hasAlive( ) ) {
			if( !round( ) ) return -1;
		}
		
		// return the number of units on the surviving team
		log( "==[Battle ended, remaining units]==" );
		final Team winner = immune.hasAlive( ) ? immune : infection;
		log( "Winner is " + winner.toString( ) );
		final int count = winner.getUnitCount( );
		log( "Total remaining units: " + count );		
		return count;
	}
	
	/**
	 * Simulates a single round of the immune system battle
	 * 
	 * @return True if units were killed, false if not. I.e., we have a draw
	 */
	protected boolean round( ) {
		log( "==[NEW ROUND STARTING]==" );
		
		/** TARGET SELECTION PHASE */
		// let units select targets in order of descending effective power or, in
		// case of a tie, highest initiative
		final List<UnitGroup> groups = new ArrayList<>( );
		groups.addAll( immune.getUnits( ) );
		groups.addAll( infection.getUnits( ) );
		groups.sort( (u1,u2) -> u1.getPower( ) != u2.getPower( ) ? u2.getPower( ) - u1.getPower( ) : u2.stats.initiative - u1.stats.initiative );
		
		// keep track of remaining targets
		final Map<Team, List<UnitGroup>> enemies = new HashMap<>( );
		enemies.put( immune, infection.getUnits( ) );
		enemies.put( infection, immune.getUnits( ) );

		// select a target in the previously established order
		for( final UnitGroup g : groups ) {
			final List<UnitGroup> en = enemies.get( g.team );
			if( en.size( ) > 0 ) {
				final UnitGroup target = g.selectTarget( en );
				en.remove( target );
				log( "[TargetSelection] " + g + " targets " + target );
			}
		}
		
		
		/** ATTACK PHASE */
		// attacking happens in order of initiative
		groups.sort( (u1,u2) -> u2.stats.initiative - u1.stats.initiative );		
		int totalkilled = 0;
		for( final UnitGroup g : groups ) {
			// check if the unit did not die in the meantime
			if( !g.isAlive( ) ) continue;
			
			totalkilled += g.attack( );
		}
		
		log( "\n" );
		return totalkilled > 0;
	}

	
	/**
	 * Logs the message
	 * 
	 * @param msg The message to log
	 */
	protected static void log( final String msg ) {
		if( logging ) System.out.println( msg );
	}
	
	/**
	 * Enables/disables logging
	 * 
	 * @param enabled True to enable logging, false to disable
	 */
	public static void setLogging( final boolean enabled ) {
		logging = enabled;
	}
	
	/**
	 * Creates a new ImmuneSystemSimulator from the string description of the two
	 * teams
	 * 
	 * @param input The strings that describe the teams
	 * @return The simulator
	 */
	public static ImmuneSystemSimulator fromStringList( final List<String> input ) {
		final List<Team> teams = input.stream( ).map( Team::fromString ).toList( );
		return new ImmuneSystemSimulator( teams.get( 0 ), teams.get( 1 ) );
	}
	
	/**
	 * @return The strign describing the current state of the simulation 
	 */
	@Override
	public String toString( ) {
		return immune.toString( ) + "\n" + infection.toString( );
	}
}
