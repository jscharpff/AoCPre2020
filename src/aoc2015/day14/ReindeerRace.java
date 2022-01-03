package aoc2015.day14;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models a race of various reindeer, each with their own speeds and flight
 * times
 * 
 * @author Joris
 */
public class ReindeerRace {
	/** The set of reindeer that participate in the race */
	private final Set<Reindeer> reindeer;
	
	/**
	 * Creates a new reindeer race
	 * 
	 * @param reindeer The contestants
	 */
	private ReindeerRace( final Set<Reindeer> reindeer ) {
		this.reindeer = new HashSet<>( reindeer );
	}
	
	/**
	 * Returns the largest distance that any of the reindeer has travelled after
	 * the given number of seconds
	 * 
	 * @param timelimit The racing time in seconds
	 * @return The largest distance travelled
	 */
	public int getWinningDistance( final int timelimit ) {
		return reindeer.stream( ).mapToInt( x -> x.getDistanceTravelled( timelimit ) ).reduce( 0, Math::max );
	}
	
	/**
	 * Returns the maximum number of points any one reindeer scores during the
	 * race. Points are awarded at every second of the race to the reindeer that
	 * have travelled the farthest at that time step.
	 * 
	 * @param timelimit The maximum time to race
	 * @return The maximum score obtained by any one reindeer
	 */
	public int getWinningPoints( final int timelimit ) {
		final Map<Reindeer, Integer> points = new HashMap<>( );
		
		// simulate the race for every time step until the time limit is reached
		// use a set because of possible ties, awarding both reindeer a point
		Set<Reindeer> best = new HashSet<>( reindeer.size( ) );
		for( int t = 1; t <= timelimit; t++ ) {
			best.clear( );
			int bestdist = 0;
			for( final Reindeer r : reindeer ) {
				final int dist = r.getDistanceTravelled( t );
				if( dist > bestdist ) {
					best.clear( );
					best.add( r );
					bestdist = dist;
				} else if( dist == bestdist ) {
					best.add( r );
					bestdist = dist;
				}
			}
			
			// award point to the reindeer that has travelled the most
			for( final Reindeer r : best )
				points.put( r, points.getOrDefault( r, 0 ) + 1 );
		}
		
		// return the maximum number of points any one reindeer has
		return points.values( ).stream( ).reduce( 0, Math::max );
	}
	
	/**
	 * Creates a new ReindeerRace from the string description of its contestants
	 * 
	 * @param input The list of competing reindeer
	 * @return The ReindeerRace
	 */
	public static ReindeerRace fromStringList( final List<String> input ) {
		final Set<Reindeer> reindeer = new HashSet<>( input.size( ) );
		for( final String r : input ) reindeer.add( Reindeer.fromString( r ) );
		return new ReindeerRace( reindeer );
	}

	/** @return The competitors of the race */
	@Override
	public String toString( ) {
		return reindeer.toString( );
	}
}
