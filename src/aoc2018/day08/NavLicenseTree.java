package aoc2018.day08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Tree that decodes the Navigation software license
 * 
 * @author Joris
 */
public class NavLicenseTree {
	/** The root node of the tree */
	private final NTNode root;
	
	/**
	 * Creates a new NavTree
	 * 
	 * @param root The root node of the navigation tree
	 */
	private NavLicenseTree( final NTNode root ) {
		this.root = root;
	}
	
	/**
	 * @return The sum of all metadata in the tree
	 */
	public int sumMetadata( ) {
		return root.sumMetadata( );
	}
	
	/** @return The value of the root node */
	public int getValue( ) {
		return root.getValue( );
	}
	
	/**
	 * Creates a new NavTree from an integer array of values
	 * 
	 * @param input The input
	 * @return The NavTree
	 */
	public static NavLicenseTree fromIntArray( final int[] input ) {
		return new NavLicenseTree( NTNode.fromIntArray( new ArrayList<>( IntStream.of( input ).boxed( ).toList( ) ) ) );
	}
	
	
	/**
	 * Class that represents a single node in the nav tree
	 */
	private static class NTNode {
		/** The meta data stored in this node */
		private final List<Integer> metadata;
		
		/** The children of this node */
		private final List<NTNode> children;
		
		/**
		 * Creates a new node
		 *
		 * @param metadata The metadata values
		 * @param children The child nodes
		 */
		private NTNode( final Collection<Integer> metadata, final Collection<NTNode> children ) {			
			this.metadata = new ArrayList<>( metadata );
			this.children = new ArrayList<>( children );
		}
		
		/**
		 * The sum of metadata in this node and its children
		 * 
		 * @return The sum of metadata
		 */
		private int sumMetadata( ) {
			return metadata.stream( ).mapToInt( i -> i ).sum( ) + children.stream( ).mapToInt( NTNode::sumMetadata ).sum( );
		}
		
		/**
		 * @return The value of the node, computed recursively
		 */
		public int getValue( ) {
			// if it has no children, the value is the sum of metadata
			if( children.size( ) == 0 ) return sumMetadata( );
			
			// if it has children, the metadata determines which children to sum,
			// possibly multiple time
			return metadata.stream( ).filter( i -> i > 0 && i <= children.size( ) ).mapToInt( i -> children.get( i - 1 ).getValue( ) ).sum( );
		}
		
		/**
		 * Recreates a node, and child nodes, from an array of ints
		 * 
		 * @param input The int array
		 * @return The node
		 */
		private static NTNode fromIntArray( final List<Integer> input ) {
			// get number of children and metadata entries
			final int numchildren = input.remove( 0 );
			final int metalen = input.remove( 0 );
			
			// parse all child nodes first, they will consume parts of the input,
			// leaving only the metadata
			final List<NTNode> children = new ArrayList<>( numchildren );
			for( int i = 0; i < numchildren; i++ ) {
				children.add( NTNode.fromIntArray( input ) );
			}
			
			// consume the metadata part
			final List<Integer> meta = new ArrayList<>( metalen );
			for( int i = 0; i < metalen; i++ ) meta.add( input.remove( 0 ) );
			
			// reconstruct the node itself
			return new NTNode( meta, children );
		}
	}
}
