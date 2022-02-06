package aoc2018.day24.iss;

import java.util.EnumSet;

/**
 * Attributes of a unit group
 * 
 * @author Joris
 */
public class UnitStats {
	/** The number of units in the group */
	protected int size;
	
	/** The hit points of individual units */
	protected final int hp;
	
	/** Its attack type */
	protected final DamageType attacktype;
	
	/** Its attack power */
	protected final int attackpower;
	
	/** The boost bonus to its attack power */
	protected int boost;
	
	/** The unit's initiative */
	protected final int initiative;
	
	/** The unit's weaknesses */
	protected final EnumSet<DamageType> weakto;
	
	/** The unit's immunities */
	protected final EnumSet<DamageType> immuneto;
	
	/**
	 * Creates a new UnitStats object
	 * 
	 * @param initialsize The initial size of group
	 * @param hp The hit points of every unit in the group 
	 * @param ap The unit's attack power
	 * @param attacktype The unit's attack type
	 * @param initiative The unit's initiative
	 */
	protected UnitStats( final int initialsize, final int hp, final int ap, final DamageType attacktype, final int initiative ) {
		this.size = initialsize;
		this.hp = hp;
		this.attackpower = ap;
		this.boost = 0;
		this.attacktype = attacktype;
		this.initiative = initiative;
		
		this.weakto = EnumSet.noneOf( DamageType.class );
		this.immuneto = EnumSet.noneOf( DamageType.class );
	}
	
	/** @return The stats of the unit group as a string */
	@Override
	public String toString( ) {
		final StringBuilder s = new StringBuilder( );
		s.append( "hp: " ); s.append( hp );
		s.append( ", attack: " );	s.append( attackpower ); if( boost > 0 ) s.append( " (+ " + boost + ")" );
		s.append( " [" ); s.append( attacktype ); s.append( "]" );
		s.append( ", initiative: " ); s.append( initiative );
		s.append( ", weak to: " ); s.append( weakto );
		s.append( ", immune against: " ); s.append( immuneto );
		return s.toString( );
	}
}
