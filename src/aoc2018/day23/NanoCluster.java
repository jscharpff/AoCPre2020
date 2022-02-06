package aoc2018.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cluster of nanobots
 * 
 * @author Joris
 */
public class NanoCluster {
	/** The nanobots in this cluster */
	private final Set<Nanobot> bots;
	
	/** Cache the hash code */
	private final int hashcode;
		
	/**
	 * Creates an empty cluster
	 */
	private NanoCluster( ) {
		this.bots = new HashSet<>( );
		this.hashcode = 0;
	}
	
	/**
	 * Creates a new cluster with a single bot in it
	 * 
	 * @param bot The single bot in the cluster
	 */
	public NanoCluster( final Nanobot bot ) {
		this.bots = new HashSet<>( );
		bots.add( bot );
		hashcode = toString( ).hashCode( );
	}
	
	/**
	 * Creates a copy of the specified cluster and adds the bot to it
	 * 
	 * @param cluster The cluster to copy
	 * @param bot The bot to add
	 */
	private NanoCluster( final NanoCluster cluster, final Nanobot bot ) {
		bots = new HashSet<>( cluster.bots );
		bots.add( bot );
		hashcode = toString( ).hashCode( );
	}
	
	/**
	 * Check if the bot can be added to the cluster
	 * 
	 * @param bot The bot to add
	 * @return True if the bot was added. This is the case when its scanner
	 *   overlaps with all others and it is not in the cluster yet
	 */
	public boolean canAdd( final Nanobot bot ) {
		if( bots.contains( bot ) ) return false;
		if( bots.stream( ).anyMatch( b -> !b.overlaps( bot ) ) ) return false;
		
		return true;
	}
	
	/**
	 * Returns a new cluster with the specified bot now included
	 * 
	 * @param bot The bot to add
	 * @return The new cluster
	 */
	public NanoCluster add( final Nanobot bot ) {
		return new NanoCluster( this, bot );
	}
	
	/** @return The size of the cluster */
	public int size( ) { return bots.size( ); }
	
	/** @return The bots in this cluster */
	public Set<Nanobot> getBots( ) { return bots; }
	
	/**
	 * Finds the cluster nanobots with overlapping scanning ranges of the largest
	 * size
	 * 
	 * @param bots The set of bots to consider
	 * @return The largest cluster
	 */
	protected static NanoCluster biggest( final Set<Nanobot> nanobots ) {
		// get map of overlapping scanners
		final Map<Nanobot, Set<Nanobot>> overlap = new HashMap<>( );
		for( final Nanobot b : nanobots )
			overlap.put( b, new HashSet<>( nanobots.stream( ).filter( b2 -> b.overlaps( b2 ) && !b2.equals( b ) ).toList( ) ) );
		
		// order them on size
		final List<Nanobot> ordered = new ArrayList<>( nanobots );
		ordered.sort( (n1,n2) -> overlap.get( n2 ).size( ) - overlap.get( n1 ).size( ) );
	
		// now recursively try and find the largest set
		final BestCluster best = new BestCluster( );
		final Set<NanoCluster> taboo = new HashSet<NanoCluster>( );
		biggestAnytime( System.currentTimeMillis( ) + 5000, overlap, taboo, new NanoCluster( ), new HashSet<>( ordered ), best );		
		return best.cluster;
	}
	
	/**
	 * Performs an anytime approximation to find the biggest clique of nanobots
	 * of which their scanning ranges overlap
	 * 
	 * @param endtime The time at which the algorithm must terminate
	 * @param overlap The map that describes the overlapping nanobot ranges per
	 *   nanonbot
	 * @param taboo The set of taboo cliques that are already explored
	 * @param curr The current cluster that is being build
	 * @param curroverlap The current remaining set of (potentially) overlapping
	 *   nanobots
	 * @param best The largest known cluster so far
	 */
	private static void biggestAnytime( final long endtime, final Map<Nanobot, Set<Nanobot>> overlap, final Set<NanoCluster> taboo, final NanoCluster curr, final Set<Nanobot> curroverlap, final BestCluster best ) {		
		// prevent exploring the same sets multiple times
		if( taboo.contains( curr ) ) return;
		taboo.add( curr );
		
		
		// no need to keep on searching if my overlap is equal to the best cluster size
		if( curroverlap.size( ) <= best.size( ) ) return;		
		
		// update best solution if possible
		if( curr.size( ) > best.size( ) ) best.update( curr );
		
				
		for( final Nanobot b : curroverlap ) {
			if( System.currentTimeMillis( ) > endtime ) return;
			if( !curr.canAdd( b ) ) continue;

			// quick fail: no need to keep on searching if my overlap is equal to the best cluster size
			if( curroverlap.size( ) <= best.size( ) ) break;			

			// do a quick count of overlapping bots to see if this will improve the cluster
			final Set<Nanobot> bov = overlap.get( b );
			final long count = curroverlap.stream( ).filter( b1 -> bov.contains( b1 ) ).count( );
			if( count <= best.size( ) ) continue;
			
			// try adding this bot
			final Set<Nanobot> newoverlap = new HashSet<>( curroverlap );
			newoverlap.retainAll( bov );
			
			// yes, try to expand the cluster
			biggestAnytime( endtime, overlap, taboo, curr.add( b ), newoverlap, best );
		}
	}
	
	/**
	 * @return The string that describes the cluster
	 */
	@Override
	public String toString( ) {
		return "[" + bots.size( ) + "] " + bots.toString( );
	}

	/**
	 * Tests equality to another object
	 *
	 * @param obj The other object
	 * @return True iff the object is a valid NanoCluster
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( obj == null || !(obj instanceof NanoCluster) ) return false;
		final NanoCluster cluster = (NanoCluster) obj;

		return bots.equals( cluster.bots );
	}
	
	/**
	 * @return The unique hash code of this cluster
	 */
	@Override
	public int hashCode( ) {
		return hashcode;
	}
	
	/**
	 * Class that holds the currently largest cluster
	 */
	private static class BestCluster {
		/** The actual cluster */
		private NanoCluster cluster;
		
		/**
		 * Creates a new empty container for the best cluster
		 */
		public BestCluster( ) {
			cluster = new NanoCluster( );
		}
		
		/**
		 * Updates the best known cluster
		 * 
		 * @param newbest The new best cluster
		 */
		private void update( final NanoCluster newbest ) {
			this.cluster = newbest;
		}
		
		/** @return The size of the best known cluster */
		private int size( ) { return cluster.size( ); }
	}
}
