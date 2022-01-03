package aoc2015.day21.rpg;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2015.day21.rpg.RPGItem.RPGItemType;
import aocutil.io.FileReader;

/**
 * A simple show that sells stats-enhancing items
 * 
 * @author Joris
 */
public class RPGShop {
	/** The items still available for sale */
	private final Map<RPGItem, Boolean> items;
	
	/**
	 * Creates a new shop with the given items for sale
	 * 
	 * @param items The map of items and their selling price
	 */
	private RPGShop( final Set<RPGItem> items ) {
		// start with a full shop, all items are available
		this.items = new HashMap<RPGItem, Boolean>( );
		for( final RPGItem item : items )
			this.items.put( item, true );		
	}
	
	/** Resets the shop, putting all items up for sale again */
	protected void reset( ) {
		for( final RPGItem item : items.keySet( ) )
			items.put( item, true );		
	}
	
	/**
	 * Returns the set of items of the specified type
	 * 
	 * @param type The item type to filter on
	 * @return The set of items of the given type
	 */
	public Set<RPGItem> getItems( final RPGItemType type ) {
		final Set<RPGItem> result = new HashSet<>( );
		for( final RPGItem i : items.keySet( ) )
			if( i.ofType( type ) ) result.add( i );
		
		return result;
	}
	
	/**
	 * Checks if the item is still available
	 * 
	 * @param item The item to check
	 * @return True iff it is still available for sale
	 */
	public boolean isAvailable( final RPGItem item ) {
		return items.get( item );
	}
	
	/**
	 * Mark an item as sold
	 * 
	 * @param item The item to sell
	 */
	public void sold( final RPGItem item ) {
		items.put( item, false );
	}
	
	/**
	 * Creates a new shop with the given list of items, described by strings
	 * 
	 * @param file The file that contains all items in the shop
	 * @throws IOException if the file reading failed
	 */
	public static RPGShop fromFile( final URL file ) throws IOException {
		final List<String> input = new FileReader( file ).readLines( );

		final Set<RPGItem> items = new HashSet<>( input.size( ) );
		for( final String s : input ) items.add( RPGItem.fromString( s ) );
		return new RPGShop( items );
	}

	/** @return The items in the shop */
	@Override
	public String toString( ) {
		final StringBuilder res = new StringBuilder( );
		for( final RPGItemType type : RPGItemType.values( ) ) {
			final Set<RPGItem> it = getItems( type );
			res.append( "--[" + type + "s]--\n" );
			for( final RPGItem item : it ) {
				res.append( item + " " + (isAvailable( item ) ? "(available)" : "(sold)") + "\n" );
			}
			res.append( "\n" );
		}
			
		return res.toString( );
	}
}
