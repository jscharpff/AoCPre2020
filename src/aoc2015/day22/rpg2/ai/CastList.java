package aoc2015.day22.rpg2.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import aoc2015.day22.rpg2.spells.RPGSpellType;

/**
 * A set of spells to cast
 */
public class CastList {
	/** The set of spells to cast */
	private List<RPGSpellType> spelllist;
	
	/** The total mana cost of the cast list */
	private int manacost;
	
	/** The index of the next spell to cast */
	private int nextidx;
	
	/**
	 * Creates a new list of spells to cast
	 * 
	 * @param spells The list of spells to cast
	 */
	public CastList( final Collection<RPGSpellType> spells ) {
		this.spelllist = new ArrayList<>( spells.size( ) );
		this.manacost = 0;
		
		// add all spells
		for( final RPGSpellType spell : spells )
			if( !addSpell( spell ) ) throw new RuntimeException( "Failed to add spell" );
		
		this.nextidx = 0;
	}
	
	/**
	 * Creates a new list of spells to cast
	 * 
	 * @param spells The array of spells to cast
	 */
	public CastList( final RPGSpellType... spells ) {
		this( Arrays.asList( spells ) );
	}
	
	/**
	 * Creates a new empty cast list
	 */
	public CastList( ) {
		this.spelllist = new ArrayList<>( );
		this.manacost = 0;
		this.nextidx = 0;
	}
	
	/**
	 * Adds a new spell to the cast list. Checks spell rules to see if it can be
	 * added
	 * 
	 * @param spell The spell to add
	 * @return False if the spell cannot be added, true otherwise
	 */
	public boolean addSpell( final RPGSpellType spell ) {
		// check if this is a spell with a periodic timer and, if so, it will not
		// lead to a double casting of the effect
		if( spell.isPeriodic( ) ) {
			// check if there is not a similar spell activated half the ticks ago
			final int turnsactive = (int)Math.ceil( (double)spell.getPeriodicTimer( ) / 2.0) - 1;
			for( int i = 1; i <= turnsactive; i++ ) {
				final int idx = spelllist.size( ) - i;
				if( idx < 0 ) break; // no more spells in the list
				if( spelllist.get( idx ) == spell ) return false;
			}
			
		}
		
		spelllist.add( spell );
		manacost += spell.getManaCost( );
		return true;
	}
	
	/**
	 * Removes the last spell from the cast list
	 */
	public void popSpell( ) {
		final RPGSpellType spell = spelllist.remove( spelllist.size( ) - 1 );
		manacost -= spell.getManaCost( );
	}
	
	
	/** @return The next spell to cast */
	protected RPGSpellType next( ) {
		if( nextidx >= spelllist.size( ) ) throw new RuntimeException( "No more spells to cast!" );
		return spelllist.get( nextidx++ );
	}
	
	/** @return The total mana cost of this list */
	public int getManaCost( ) {
		return manacost;
	}
	
	/** @return The number of spells in this list */
	public int size( ) { return spelllist.size( ); }
	
	/**
	 * Creates a copy of this cast list
	 * 
	 * @return The copy
	 */
	public CastList copy( ) {
		return new CastList( this.spelllist );
	}

	/** @return The list of spells and their total mana cost */
	@Override
	public String toString( ) {
		return spelllist.toString( ) + ": " + manacost;
	}
}