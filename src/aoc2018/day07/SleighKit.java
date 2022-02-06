package aoc2018.day07;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import aocutil.graph.Edge;
import aocutil.graph.Graph;
import aocutil.graph.Node;
import aocutil.string.RegexMatcher;

/**
 * Class that models the straighforward, IKEA-like assembly instructions to
 * assemble a sled. As always, it is unclear in what order steps should be
 * performed and parts are likely missing.
 * 
 * @author Joris
 */
public class SleighKit {
	/** The graph that represents the steps in the kit's instruction */
	private final Graph instructions;
	
	/**
	 * Creates a new SleighKit
	 * 
	 * @param instrgraph The graph that models the order of assembly instructions
	 */
	private SleighKit( final Graph instrgraph ) {
		this.instructions = instrgraph;
	}
	
	/**
	 * Find the order of instructions to perform so that the sleigh can be
	 * assembled successfully
	 * 
	 * @return The string of node labels that describes the order of instructions
	 */
	public String getInstructionPlan( ) {
		// use a BFS algorithm to produce the order
		final List<Node> ordering = new ArrayList<>( );
		final PriorityQueue<Node> explore = new PriorityQueue<>( (x,y)-> x.getLabel( ).compareTo( y.getLabel( ) ) );
		
		// find all "root nodes", i.e. those without any predecessors
		instructions.getNodes( ).stream( ).filter( n -> n.getPredecessors( ).size( ) == 0 ).forEach( explore::offer );;
		
		final Set<Node> stuck = new HashSet<>( );
		while( !explore.isEmpty( ) ) {
			final Node n = explore.poll( );
			
			// check if all its predecessors are explored
			if( !ordering.containsAll( n.getPredecessors( ) ) ) {
				stuck.add( n );
				continue;
			}
			
			// all the predecessors are explored, add it!
			ordering.add( n );
			
			// add all its successors, but only if not already in the queue
			for( final Node nb : n.getNeighbours( ) )
				if( !explore.contains( nb ) ) {
					explore.offer( nb );
					// also remove it from stuck queue, if it was there
					stuck.remove( nb ); 
				}
		}
			
		if( stuck.size( ) > 0 ) throw new RuntimeException( "Some nodes are stuck: " + stuck );
		
		// convert node list into instruction plan
		final StringBuilder sb = new StringBuilder( );
		for( final Node n : ordering ) sb.append( n.getLabel( ) );
		return sb.toString( );
	}
	
	/**
	 * Gets the total execution time of following all instructions with the 
	 * number of workers available to perform parallel tasks
	 * 
	 * @param workers The number of workers available
	 * @param basetasktime The base amount of time a task takes, increased by 
	 *   the per-task duration
	 * @return The minimal execution time of the assembly
	 */
	public int getExecutionTime( final int workers, final int basetasktime ) {
		
		final PriorityQueue<Node> tasks = new PriorityQueue<>( (x,y)-> x.getLabel( ).compareTo( y.getLabel( ) ) );
		final Set<Node> completed = new HashSet<>( );
		
		// find all "root nodes", i.e. those without any predecessors
		instructions.getNodes( ).stream( ).filter( n -> n.getPredecessors( ).size( ) == 0 ).forEach( tasks::offer );
		
		// keep track of work in progress and the current time
		final PriorityQueue<Task> wip = new PriorityQueue<>( );
		int time = 0;
		final int N = instructions.size( );
		while( completed.size( ) < N ) {
			
			// free workers can perform one of the tasks
			while( wip.size( ) < workers && !tasks.isEmpty( ) ) {
				final Node task = tasks.poll( );
				final int completiontime = time + basetasktime + (int)(task.getLabel( ).charAt( 0 ) - 'A' );
				wip.offer( new Task( task, completiontime ) );
			}
			
			// either no more workers are available or all the tasks have ran out
			// "complete" the first task and increase time
			if( wip.size( ) == workers || tasks.isEmpty( ) ) {
				final Task t = wip.poll( );
				completed.add( t.node );
				time = t.completiontime + 1;
				
				// also check if this unlocks new tasks
				for( final Node n : t.node.getNeighbours( ) ) {
					if( completed.containsAll( n.getPredecessors( ) ) ) tasks.add( n );
				}
			}
		}		
		
		return time;
	}
	
	
	/**
	 * Reconstructs a SleighKit from a list of its assembly instructions
	 * 
	 * @param input The list of instructions and their dependencies
	 * @return The {@link SleighKit}
	 */
	public static SleighKit fromStringList( final List<String> input ) {
		final Graph g = new Graph( );
		
		// parse nodes and edges
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "Step (\\w+) must be finished before step (\\w+) can begin.", s );
			final Node from = g.addNode( rm.get( 1 ) );
			final Node to = g.addNode( rm.get( 2 ) );
			g.addEdge( new Edge( from, to, 0, true ) );
		}
		
		return new SleighKit( g );
	}

	/**
	 * @return The extended description of the instruction set graph
	 */
	public String toLongString( ) {
		return instructions.toString( );
	}
	
	/**
	 * Class that holds a work unit
	 */
	public class Task implements Comparable<Task>  {
		private final Node node;
		
		private final int completiontime;
		
		public Task( final Node node, final int completiontime ) {
			this.node = node;
			this.completiontime = completiontime;
		}
		
		@Override
		public int compareTo( Task t ) {
			return completiontime - t.completiontime;
		}
	}
}
