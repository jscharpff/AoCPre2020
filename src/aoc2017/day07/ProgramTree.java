package aoc2017.day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

public class ProgramTree {
	/** The root node */
	private final PTNode root;
	
	/**
	 * Creates a new ProgramTree with the given root node
	 * 
	 * @param root The root node
	 */
	private ProgramTree( final PTNode root ) {
		this.root = root;
	}
	
	/**
	 * Reconstructs a tree structure from a list of nodes and their children
	 * 
	 * @param input The list of nodes
	 * @return The ProgramTree
	 */
	public static ProgramTree fromStringList( final List<String> input ) {
		// build node set while parsing
		final Map<String, PTNode> nodes = new HashMap<>( input.size( ) );
		
		final List<String> in = new ArrayList<>( input );
		while( !in.isEmpty( ) ) {
			for( int i = in.size( ) - 1; i >= 0; i-- ) {
				final String[] n = in.get( i ).split( " -> " );				
				
				// leaf node?
				if( n.length == 1 ) {
					final PTNode leaf = PTNode.fromString( n[0] );
					nodes.put( leaf.getLabel( ), leaf );
					in.remove( i );
				} else {
					// not a leaf node, check if all children are parsed already
					if( !Stream.of( n[1].split( ", " ) ).allMatch( x -> nodes.containsKey( x ) ) ) continue;
					
					// yes, process this node as well
					final PTNode node = PTNode.fromString( n[0] );
					nodes.put( node.getLabel( ), node );
					in.remove( i );
					
					// and set the children
					Stream.of( n[1].split( ", " ) ).map( nodes::get ).forEach( x -> node.addChild( x ) );
				}				
			}
		}
		
		// now pick a random node and traverse upward until we found the root
		PTNode n = nodes.values( ).iterator( ).next( );
		while( n.parent != null ) n = n.parent;
		return new ProgramTree( n );
	}
	
	/** @return The label of the root node */
	public String getRootLabel( ) {
		return root.getLabel( );
	}
	
	/**
	 * Determines the weight of node that imbalances the program tower
	 * 
	 * @return The weight that causes the imbalance
	 */
	public int getImbalanceWeight( ) {
		return root.getImbalanceWeight( );
	}
	
	
	/**
	 * @return The number of nodes in the tree
	 */
	public int size( ) {
		return 1 + root.countChildren( );
	}
	
	/** @return A string description of the tree and all of its nodes */
	@Override
	public String toString( ) {
		return root.toLongString( );
	}
	
	/**
	 * Class that stores a single node of the tree
	 */
	private static class PTNode extends LabeledObject {
		/** The weight of the node */
		private final int weight;
		
		/** The weight carried by this node and its children */
		private int carries;
		
		/** The parent of this node */
		private PTNode parent;
		
		/** The children of this node */
		private final Set<PTNode> children;
		
		/**
		 * Creates a new node
		 * 
		 * @param label The node label
		 * @param weight The node weight
		 */
		private PTNode( final String label, final int weight ) {
			super( label );
			this.weight = weight;
			this.children = new HashSet<>( );
			this.parent = null;
		}
		
		/**
		 * Adds a child node to this node, also sets the parent node in the child
		 * 
		 * @param node The node to add as child
		 */
		private void addChild( final PTNode node ) {
			children.add( node );
			node.parent = this;
		}
		
		/**
		 * Creates a new PTNode by parsing a string description
		 * 
		 * @param input The input string
		 * @return The PTNode
		 */
		private static PTNode fromString( final String input ) {
			final RegexMatcher rm = RegexMatcher.match( "(\\w+) \\((\\d+)\\)", input );
			return new PTNode( rm.get( 1 ), rm.getInt( 2 ) );
		}
		
		/**
		 * Determines which of the children of this node is causing an imbalance
		 * and returns the weight of that node (including its child nodes)
		 * 
		 * @return The weight of the imbalancing node, or -1 if not applicable
		 */
		private int getImbalanceWeight( ) {
			// determine the weight it is actually carrying
			carries = getCarryWeight( );
			return getImbalanced( );
		}
		
		/**
		 * Computes and sets the carry weight in all child nodes
		 * 
		 * @return The sum of child node carry weights
		 */
		private int getCarryWeight( ) {
			carries = weight + children.stream( ).mapToInt( PTNode::getCarryWeight ).sum( );
			return carries;
		}
		
		/**
		 * Computes the new weight of the node that is causing an imbalance
		 * 
		 * @return The new weight of that node
		 */
		private int getImbalanced( ) {
			if( children.size( ) == 0 ) return 0;
			
			// check if all my children are carrying an equal weight
			final Map<PTNode, Integer> weights = new HashMap<>( );
			for( final PTNode c : children ) weights.put( c, c.carries );
			
			// all the same?
			if( weights.values( ).stream( ).distinct( ).count( ) == 1 ) return 0;
			
			// no, check which one differs and continue process from there
			int goodweight = 0;
			PTNode badnode = null;
			int childweight = 0;
			for( final PTNode n : children ) {
				final int w = weights.get( n );
				
				// this is the weight it should be?
				if( weights.values( ).stream( ).filter( x -> x == w ).count( ) > 1 ) { goodweight = w; continue; }

				// nope, check if the imbalance is here or in any of the children
				childweight = n.getImbalanced( );
				badnode = n;
			}

			// return either the new weight of the child node that was causing the
			// imbalance, or my own new weight that would fix it
			return childweight != 0 ? childweight : badnode.weight + goodweight - badnode.carries; 
		}

		/** @return The total number of children of this node, recursively */
		private int countChildren( ) {
			return children.size( ) + children.stream( ).mapToInt( PTNode::countChildren ).sum( );
		}
		
		/** @return The simple node description string */
		@Override
		public String toString( ) {
			final StringBuilder res = new StringBuilder( );
			res.append( label + (carries != 0 ? " (" + carries + ")" : ""));
			for( final PTNode c : children ) res.append( " " + c.label + (c.carries != 0 ? " (" + c.carries + ")" : "") );
			return res.toString( );
		}
		
		/** @return The node description */
		public String toLongString( ) {
			return label + (carries != 0 ? " (" + carries + ")" : "") + (children.size( ) > 0 ? " -> [" + children.stream( ).map( PTNode::toLongString ).reduce( (x,y) -> { return x + ", " + y; } ).get( ) + "]" : "" );
		}
	}

}
