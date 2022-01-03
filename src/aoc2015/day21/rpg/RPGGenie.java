package aoc2015.day21.rpg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2015.day21.rpg.RPGItem.RPGItemType;

/**
 * Game Genie for the RPG game!
 * 
 * @author Joris
 */
public class RPGGenie {
	/** The fle that contains the game data */
	private URL gamefile;
	
	/**
	 * Creates a new Game Genie
	 * 
	 * @param gamefile The file that contains the game data
	 */
	public RPGGenie( final URL gamefile ) {
		this.gamefile = gamefile;
	}
	
	/**
	 * Finds the set of items for the player that make him defeat the boss while
	 * minimising his expenditure
	 * 
	 * @return The minimal amount of gold to spend and still win the game
	 * @throws IOException 
	 */
	public int getMinimumGoldToWin( ) throws IOException {
		final RPG game = RPG.fromFile( gamefile );
				
		// now try all combinations, store gold cost of winning one
		ItemSet cheapest = null;
		final List<ItemSet> itemsets = generateItemSets( game, 100 );
		for( final ItemSet I : itemsets ) {
			// too expensive to consider?
			if( cheapest != null && I.cost > cheapest.cost ) continue;
			
			game.reset( );
			
			// try to buy the entire set, abandon if not possible
			boolean canbuy = true;
			for( final RPGItem item : I.items ) {
				// skip dummy items
				if( item.cost == 0 ) continue;
				
				// can we buy it?
				if( !game.buyItem( game.getPlayer( ), item ) ) {
					canbuy = false;
					break;
				}
			}
			if( !canbuy ) continue;
			
			// check if this equipment makes the player win
			if( game.battle( ).equals( game.getPlayer( ) ) ) {
				cheapest = I;
			}			
		}
		
		return cheapest.cost;
	}
	
	/**
	 * Finds the most costly set of items for the player that still allows the
	 * boss to defeat the player
	 * 
	 * @return The minimal amount of gold to spend and still win the game
	 * @throws IOException 
	 */
	public int getMaximumGoldToLose( ) throws IOException {
		final RPG game = RPG.fromFile( gamefile );
		final int INFINITE_GOLD = 99999;
		
		// now try all combinations, store gold cost of most expensive loss
		ItemSet mostexpensive = null;
		final List<ItemSet> itemsets = generateItemSets( game, INFINITE_GOLD );
		for( final ItemSet I : itemsets ) {
			if( mostexpensive != null && I.cost <= mostexpensive.cost ) continue;
		
			// reset the game but give the player enough gold to be able to buy all
			// the items he wants
			game.reset( );
			game.getPlayer( ).giveGold( INFINITE_GOLD );
			
			// buy the entire set!
			for( final RPGItem item : I.items ) {
				// dummy item?
				if( item.cost == 0 ) continue;
				
				// try and buy it, should always work
				if( !game.buyItem( game.getPlayer( ), item ) ) throw new RuntimeException( "Failed to buy item " + item );
			}
			
			// check if this equipment makes the player win, if so discard it
			if( game.battle( ).equals( game.getPlayer( ) ) ) continue;
			
			// the boss wins, is this the most expensive set?
			mostexpensive = I;
		}
		
		return mostexpensive.cost;
	}
	
	/**
	 * Generates all the possible item sets that the player may equip for the
	 * given maximum total cost
	 * 
	 * @param game The game that is being played
	 * @param maxcost The maximum amount of gold to spend on the items
	 * @return All unique item sets for the player
	 */
	private List<ItemSet> generateItemSets( final RPG game, final int maxcost ) {
		// first get all item sets per type
		final Set<RPGItem> weapons = game.shop.getItems( RPGItemType.Weapon );
		final Set<RPGItem> armour = game.shop.getItems( RPGItemType.Armour );
		final Set<RPGItem> rings = game.shop.getItems( RPGItemType.Ring );

		// add dummy items to make it possible to select no armour or ring
		armour.add( new RPGItem( "No armour", RPGItemType.Armour, 0, 0, 0 ) );
		rings.add( new RPGItem( "No ring 1", RPGItemType.Ring, 0, 0, 0 ) );
		rings.add( new RPGItem( "No ring 2", RPGItemType.Ring, 0, 0, 0 ) );
		
		// generate all unique combinations that stay below cost price
		final List<ItemSet> itemsets = new ArrayList<>( );
		for( final RPGItem weap : weapons ) {
			for( final RPGItem arm : armour ) {
				for( final RPGItem r1 : rings ) {
					for( final RPGItem r2 : rings ) {
						if( r1.equals( r2 ) ) continue;
						
						final ItemSet I = new ItemSet( weap, arm, r1, r2 );
						if( I.cost > maxcost ) continue;
						itemsets.add( I );
					}
				}
			}
		}
		return itemsets;
	}
	
	/**
	 * Holds a set of items and their cost
	 */
	private class ItemSet {
		/** The items in this set */
		private final Set<RPGItem> items;
		
		/** The total cost of the set */
		private final int cost;
		
		/**
		 * Creates a new set of items that can be equipped by the player
		 * 
		 * @param items The items in the equipment set
		 */
		public ItemSet( final RPGItem... items ) {
			this.items = new HashSet<>( );
			int totalcost = 0;
			for( final RPGItem i : items ) {
				this.items.add( i );
				totalcost += i.cost;
			}
			cost = totalcost;
		}
		
		/** @return The items in the set and their total cost */
		@Override
		public String toString( ) {
			return items + ": "  + cost;
		}
	}
}
