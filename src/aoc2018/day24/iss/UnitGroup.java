package aoc2018.day24.iss;

import java.util.List;
import java.util.stream.Stream;

import aocutil.string.RegexMatcher;

/** 
 * A group with units of the same type
 */
public class UnitGroup {
	/** The group's ID */
	private final int ID;
	
	/** The team it belongs to */
	protected Team team;
	
	/** The unit's stats */
	protected final UnitStats stats;

	/** The unit's current target */
	protected UnitGroup target;
	
	/**
	 * Creates a new UnitGroup
	 * 
	 * @param ID The ID of the unit group
	 * @param stats The unit group stats
	 */
	private UnitGroup( final int ID, final UnitStats stats ) {
		this.ID = ID;
		this.stats = stats;
	}
	
	/**
	 * Sets the team of the unit group
	 * 
	 * @param team The team to which the unit belongs 
	 */
	protected void setTeam( final Team team ) {
		this.team = team;
	}
	
	/**
	 * Boosts the attack power of this unit by the specified amount
	 * 
	 * @param amount The amount to boost
	 *
	 */
	protected void boost( final int amount ) {
		stats.boost += amount;
	}
	
	/** @return The effective hit points of the group */
	protected int getHealth( ) {
		return stats.size * stats.hp;
	}
	
	/** @return The effective power of the group */
	protected int getPower( ) {
		return stats.size * (stats.attackpower + stats.boost);
	}
	
	/** @return True iff the unit is still alive */
	protected boolean isAlive( ) {
		return stats.size > 0;
	}
	
	/**
	 * Selects one of the enemies as the target of this unit's attack
	 * 
	 * @param enemies The list of enemies
	 * @return The target of the unit group
	 */
	protected UnitGroup selectTarget( final List<UnitGroup> enemies ) {
		// order potential targets based on the effective damage they will receive
		enemies.sort( (e1,e2) -> {
			// first compare based on effective damage
			final int ed1 = e1.getEffectiveDamageFrom( this );
			final int ed2 = e2.getEffectiveDamageFrom( this );			
			if( ed1 != ed2 ) return ed2 - ed1; 
			
			// in case of a tie, check effective power
			final int ep1 = e1.getPower( );
			final int ep2 = e2.getPower( );
			if( ep1 != ep2 ) return ep2 - ep1;
			
			// still a tie? use initiative to break it
			return e2.stats.initiative - e1.stats.initiative;			
		} );
		
		// pick the first target to which the unit can deal damage
		target = null;
		for( final UnitGroup e : enemies )
			if( e.getEffectiveDamageFrom( this ) > 0 ) {
				target = e;
				break;
			}
		return target;
	}
	
	/**
	 * Attacks the target of this unit, if no target is selected it passes
	 * 
	 * @return The number of units killed by the attack
	 */
	protected int attack( ) {
		if( !isAlive( ) ) throw new RuntimeException( "Unit is attacking while dead" );
		if( target == null ) return 0;
		if( !target.isAlive( ) ) throw new RuntimeException( "Unit is attacking a dead target" );
		
		final int damage = target.getEffectiveDamageFrom( this );
		ImmuneSystemSimulator.log( "[AttackPhase] " + this + " attacks " + target + " for " + damage + " " + stats.attacktype + " damage" );
		final int killed = target.takeDamage( damage );
		ImmuneSystemSimulator.log( "[AttackPhase] " + target + " lost " + killed + " unit(s)" + (!target.isAlive( ) ? " and has died!" : "") );
		
		// clear the target
		target = null;
		return killed;
	}
	
	/**
	 * Takes the specified amount of damage
	 * 
	 * @param damage The damage incurred to this unit
	 * @return The number of killed units
	 */
	private int takeDamage( final int damage ) {
		// kill an integer number of units based upon the damage
		final int currsize = stats.size;
		stats.size = Math.max( stats.size - damage / stats.hp, 0);
		
		// check if this killed me, if so let the group know
		if( !isAlive( ) ) team.loseUnit( this );
		
		// return number of killed units
		return currsize - stats.size;
	}
	
	/**
	 * Computes the effective damage the unit would receive if attacked by the
	 * specified attacker
	 * 
	 * @param enemy The enemy unit group that is (potentially) attacking
	 * @return The effective damage this unit would take if the enemy attacked
	 */
	protected int getEffectiveDamageFrom( final UnitGroup enemy ) {
		// immune to this damage type?
		final DamageType attacktype = enemy.stats.attacktype; 
		if( stats.immuneto.contains( attacktype ) ) return 0;
		
		// nope, check damage
		return enemy.getPower( ) * (stats.weakto.contains( attacktype ) ? 2 : 1);		
	}
	
	/**
	 * Creates a group of units from a string description
	 * 
	 * @param input The string describing the group's units 
	 * @param ID The group ID
	 * @return The unit group
	 */
	public static UnitGroup fromString( final String input, final int ID ) {
		// get base stats first
		final RegexMatcher rm = RegexMatcher.match( "(\\d+) units each with (\\d+) hit points.*with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)", input );		
		final UnitStats st = new UnitStats( rm.getInt( 1 ), rm.getInt( 2 ), rm.getInt( 3 ), DamageType.fromString( rm.get( 4 ) ), rm.getInt( 5 ) );
		
		// then add weaknesses and strengths
		final RegexMatcher rm1 = new RegexMatcher( "weak to ([\\w, ]+)+[;)]" );
		if( rm1.match( input ) ) st.weakto.addAll( Stream.of( rm1.get( 1 ).split( ", " ) ).map( DamageType::fromString ).toList( ) );
		final RegexMatcher rm2 = new RegexMatcher( "immune to ([\\w, ]+)+[;)]" );
		if( rm2.match( input ) ) st.immuneto.addAll( Stream.of( rm2.get( 1 ).split( ", " ) ).map( DamageType::fromString ).toList( ) );
		
		return new UnitGroup( ID, st );
	}
	
	/** @return The string description of the unit group */
	@Override
	public String toString( ) {
		final StringBuilder s = new StringBuilder( );
		s.append( "Group " );
		s.append( team.getName( ).subSequence( 0, 3 ) );
		s.append( ID );
		s.append( " (" );
		s.append( stats.size );
		s.append( ")" );
		
		return s.toString( );
	}
	
	/** @return The long description of the unit group */
	public String toLongString( ) {
		return this.toString( ) + ": " + stats.toString( );
	}

}
