package aoc2015.day24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A little balancer for Santa's sleigh that helps him produce the packing list
 * that allows him the most leg room while minimising the risk of 
 * "complications".
 * 
 * @author Joris
 */
public class SleighBalancer {
	/** The weights of the packages to pack */
	private Set<Integer> packages;

	/** The group weight that are looking for */
	private final int groupweight;
	
	/**
	 * Creates a new balancing aid for the given set of package weights
	 * 
	 * @param packageweights The weights of the packages to consider
	 * @param groups The number of groups to divider the packages into
	 */
	public SleighBalancer( final List<String> packageweights, final int groups ) {
		this.packages = new HashSet<>( packageweights.size( ) );
		packageweights.forEach( w -> packages.add( Integer.parseInt( w ) ) );

		this.groupweight = packages.stream( ).mapToInt( x -> x ).sum( ) / groups;
	}
	
	/**
	 * Optimises the packing of packages into groups such that the size is
	 * minimal, leaving Santa room to sit, and the quantum entanglement is also
	 * minimal, minimising the risk of "complications"
	 *  
	 * @return The minimal quantum entanglement value of the smallest set size
	 */
	public long optimiseQuantumEntanglement( ) {

		// generate all subsets of packages with the max weight per group
		final List<PackageGroup> groups = new ArrayList<>( );
		generateSubSets( groups, new ArrayList<>( packages ), new PackageGroup( ), 0 );
		
		// keep only smallest groups
		final int smallest = groups.stream( ).mapToInt( x -> x.size( ) ).min( ).getAsInt( );
		groups.removeIf( x -> x.size( ) > smallest );
		
		// return the smallest quantum entanglement of the remaining groups
		return groups.stream( ).mapToLong( x -> x.getQuantumEntanglement( ) ).min( ).getAsLong( );
	}
	
	/**
	 * Generates all package groups that can be constructed from the set of
	 * input weights such that exactly equal the total weight divided by the
	 * number of packing groups. In other words, the sub sets that equally
	 * distribute package weights
	 * 
	 * @param subsets The current list of generated package groups
	 * @param weights The set of package weights 
	 * @param current The package group we are currently building
	 * @param curridx The index of the next weight to consider
	 */
	private void generateSubSets( final List<PackageGroup> subsets, final List<Integer> weights, final PackageGroup current, final int curridx ) {
		if( current.getWeight( ) > groupweight ) return;
		if( curridx == weights.size( ) ) {
			if( current.getWeight( ) != groupweight ) return;
			subsets.add( current.copy( ) );
			return;
		}
		
		final int w = weights.get( curridx );
		current.add( w );
		generateSubSets( subsets, weights, current, curridx + 1 );
		current.pop( );
		generateSubSets( subsets, weights, current, curridx + 1 );
	}
	
	/**
	 * Group of packages
	 */
	private class PackageGroup {
		/** The packages in the group */
		private List<Integer> packs;
		
		/** The total weight of the packages */
		protected int weight;
		
		/**
		 * Creates a new, empty package group
		 */
		public PackageGroup( ) {
			this.packs = new ArrayList<>( );
			this.weight = 0;
		}
		
		/** @return The total weight of the packages in the group */
		public int getWeight( ) { return weight; }
		
		/** @return The quantum entanglement of the packages */
		public long getQuantumEntanglement( ) {
			return packs.stream( ).mapToLong( x -> x ).reduce( Math::multiplyExact ).getAsLong( );
		}
		
		/**
		 * Adds a weight to the group
		 * 
		 * @param w The weight to add
		 */
		public void add( final int w ) {
			packs.add( w );
			weight += w;
		}
		
		/**
		 * Pops the last weight from the group
		 * 
		 * @return The weight popped
		 */
		public int pop( ) {
			final int w = packs.remove( packs.size( ) - 1 );
			weight -= w;
			return w;
		}
		
		/** @return The size of the group */
		public int size( ) {
			return packs.size( );
		}
		
		/** @return A copy of this package group */
		public PackageGroup copy( ) {
			final PackageGroup pg = new PackageGroup( );
			for( final Integer w : packs ) pg.add( w );
			return pg;
		}
	}
}
