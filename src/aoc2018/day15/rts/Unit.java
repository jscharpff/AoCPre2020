package aoc2018.day15.rts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import aocutil.algorithm.BreadthFirstSearch;
import aocutil.geometry.Coord2D;
import aocutil.object.LabeledObject;

/**
 * A single unit on the battlefield
 * 
 * @author Joris
 */
public class Unit extends LabeledObject {
	/** The type of unit */
	private final UnitType type;
	
	/** The position of the unit */
	private Coord2D pos;
	
	/** The health of the unit */
	private int hp;
	
	/** The attack power of the unit */
	private int ap;
	
	/**
	 * Creates a new unit
	 * 
	 * @param type The type of unit
	 * @param initialpos The starting position of the unit
	 */
	public Unit( final UnitType type, final Coord2D initialpos ) {
		super( "Unit " + nextID++ );
		
		this.type = type;
		this.pos = initialpos;
		this.hp = 200;
		this.ap = 3;
	}
	
	/** @return The position of the unit */
	public Coord2D getPosition( ) { return pos; }
	
	/** @return The type of the unit */
	public UnitType getType( ) { return type; }
	
	/** @return The health of the unit */
	public int getHealth( ) { return hp; }
	
	/** @return True iff the unit is alive (i.e. hp > 0) */
	public boolean isAlive( ) { return hp > 0; }
	
	/**
	 * Sets the attack power of the unit
	 * 
	 * @param power The new attack power
	 */
	public void setAP( final int power ) {
		ap = power;
	}
	
	/**
	 * Performs a single turn of this unit
	 * 
	 * @param battle The current battle going on
	 * @return True if the unit performed an action, false if it skipped
	 */
	protected boolean turn( final CaveBattle battle ) {
		if( !isAlive( ) ) throw new RuntimeException( "Unit is dead!" );
		
		// identify enemy targets
		final List<Unit> enemies = new ArrayList<>( battle.units.stream( ).filter( u -> u.type != type && u.isAlive( ) ).toList( ) );
		enemies.sort( (e1,e2) -> e1.hp == e2.hp ? e1.getPosition( ).compareTo( e2.getPosition( ) ) : e1.hp - e2.hp );
		if( enemies.size( ) == 0 ) return false;

		// am I in range of an enemy? if so, attack him!
		if( tryAttack( battle, enemies ) ) return true;
		
		// not in range, close in on an enemy
		tryMove( battle, enemies );
		
		// am I in range of an enemy now? if so, attack him!
		tryAttack( battle, enemies );
		
		// successfully completed my turn
		return true;
	}
	
	/**
	 * Tries to attack an enemy
	 * 
	 * @param battle The battle that is going on!
	 * @param enemies The list of enemies
	 * @return True if the unit has attacked
	 */
	private boolean tryAttack( final CaveBattle battle, final List<Unit> enemies ) {
		for( final Unit u : enemies ) {
			if( u.getPosition( ).getManhattanDistance( pos ) == 1 ) {
				attack( u );
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Tries to move towards the closest enemy
	 * 
	 * @param battle The battle that is going on!
	 * @param enemies The list of enemies
	 * @return True if the unit has moved
	 */
	private boolean tryMove( final CaveBattle battle, final List<Unit> enemies ) {
		// not in range, close in on an enemy. First get all available positions
		// and then keep only those adjacent to an enemy
		final Set<Coord2D> occupied = new HashSet<>( battle.units.stream( ).filter( Unit::isAlive ).map( u -> u.getPosition( ) ).toList( ) );
		final Function<Coord2D, Boolean> validspaces = crd -> battle.map.get( crd ) == '.' && !occupied.contains( crd );
		final Map<Coord2D, Long> D = BreadthFirstSearch.getReachable( getPosition( ), c -> battle.map.getNeighbours( c, false, validspaces ) );
		for( final Coord2D k : new HashSet<>( D.keySet( ) ) ) {
			if( !enemies.stream( ).anyMatch( e -> e.getPosition( ).getManhattanDistance( k ) <= 1 ) )	D.remove( k );
		}
		
		// no reachable positions, we cannot move...
		if( D.size( ) == 0 ) return false;
		
		// okay, now keep closest positions only, sort on "readabilty" and then
		// pick the first as the new target position
		final long mindist = D.values( ).stream( ).min( Long::compareTo ).get( );
		final Coord2D targetpos = D.entrySet( ).stream( )
				.filter( e -> e.getValue( ) == mindist )
				.sorted( (e1,e2) -> e1.getKey( ).compareTo( e2.getKey( ) ) )
				.findFirst( ).get( ).getKey( );
		
		// compute path towards the position and move along the shortest
		final List<List<Coord2D>> paths = BreadthFirstSearch.getShortestPaths( getPosition( ), targetpos, c -> battle.map.getNeighbours( c, false, validspaces ) );
		
		// follow the path that has as its next step the coordinate that is first
		// in "reading order"
		pos = paths.stream( ).map( l -> l.get( 1 ) ).sorted( (c1,c2) -> c1.compareTo( c2 ) ).findFirst( ).get( );
		return true;
	}

	/**
	 * Hits the specified target
	 * 
	 * @param target The unit to attack
	 * @return True if the target died of the attack
	 */
	public boolean attack( final Unit target ) {
		return target.takeDamage( ap );
	}
	
	/**
	 * Takes the specified amount of damage
	 * 
	 * @param damage The damage to receive
	 * @return True if the unit has died
	 */
	public boolean takeDamage( final int damage ) {
		hp = Math.max( 0, hp - damage );
		return hp == 0;
	}
	
	/** @return The single char describing the unit type */
	public char toChar( ) {
		return type.toString( ).charAt( 0 );
	}
	
	/** @return The string describing the unit */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( type );
		sb.append( " " );
		sb.append( pos );
		sb.append( " " );
		sb.append( hp > 0 ? "hp: " + hp : "(DEAD)" );
		return sb.toString( );
	}
	
	/** The next available unit ID */
	private static int nextID = 0;
	
	/**
	 * Unit types
	 */
	protected enum UnitType {
		Elf, Goblin;
	}
}
