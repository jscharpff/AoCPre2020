package aoc2015.day09;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.Util;

/**
 * Class that helps Santa optimise his delivery route
 * 
 * @author Joris
 */
public class DeliveryRouter {
	/** The city to index mapping */
	private final Map<String, Integer> citymap;
	
	/** The distance matrix between two cities */
	private final int[][] D;
	
	/**
	 * Creates a new DeliveryRouter for the given set of cities
	 * 
	 * @param distances The list of inter-city distances to route over
	 */
	public DeliveryRouter( final List<String> distances ) {
		int cityidx = 0;
		citymap = new HashMap<>( );
		
		// first go over input and parse all cities
		for( final String s : distances ) {
			final Matcher m = Pattern.compile( "(\\w+) to (\\w+) = (\\d+)" ).matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid distance string: " + s );
			
			if( !citymap.containsKey( m.group( 1 ) ) ) citymap.put( m.group( 1 ), cityidx++ );
			if( !citymap.containsKey( m.group( 2 ) ) ) citymap.put( m.group( 2 ), cityidx++ );
		}

		// then build distance matrix
		D = new int[ citymap.size( ) ][ citymap.size( ) ];
		for( final String s : distances ) {
			final Matcher m = Pattern.compile( "(\\w+) to (\\w+) = (\\d+)" ).matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid distance string: " + s );

			final int from = citymap.get( m.group( 1 ) );
			final int to = citymap.get( m.group( 2 ) );
			final int d = Integer.parseInt( m.group( 3 ) );
			D[ from ][ to ] = d;
			D[ to ][ from ] = d;
		}	
	}
	
	/**
	 * Finds the shortest route to travel to all cities exactly once. 
	 * 
	 * @return The shortest route as a string city1 -> city2 -> ... = distance
	 */
	public String getShortestRoute( ) {
		// get all permutations of cities
		final List<List<String>> routes = Util.generatePermutations( citymap.keySet( ) );
		
		// find the permutation that hasthe longest route length
		List<String> bestroute = null;
		int mindist = Integer.MAX_VALUE;
		for( final List<String> route : routes ) {
			// compute distance of route
			final int dist = dist( route );
			if( dist < mindist ) {
				mindist = dist;
				bestroute = route;
			}
		}

		// return it as a string with its distance
		return bestroute.stream( ).reduce(  "", (x,y) -> x + " -> " + y ).substring( 4 ) + ": " + mindist;
	}
	
	/**
	 * Finds the longest route to travel to all cities exactly once. 
	 * 
	 * @return The longest route as a string city1 -> city2 -> ... = distance
	 */
	public String getLongestRoute( ) {
		// get all permutations of cities
		final List<List<String>> routes = Util.generatePermutations( citymap.keySet( ) );
		
		// find the permutation that hasthe longest route length
		List<String> bestroute = null;
		int maxdist = 0;
		for( final List<String> route : routes ) {
			// compute distance of route
			final int dist = dist( route );
			if( dist > maxdist ) {
				maxdist = dist;
				bestroute = route;
			}
		}
		
		// return it as a string with its distance
		return bestroute.stream( ).reduce(  "", (x,y) -> x + " -> " + y ).substring( 4 ) + ": " + maxdist;
	}
	
	
	/**
	 * Computes the total distance of a route, given by a list of cities to
	 * travel in the specified order
	 * 
	 * @param cities The list of cities to travel in given order 
	 * @return The total distance of traversing this route
	 */
	public int dist( final List<String> cities ) {
		int dist = 0;
		for( int i = 0; i < cities.size( ) - 1; i++ ) {
			dist += dist( cities.get( i ), cities.get( i + 1 ) );
		}
		return dist;
	}
	
	/** 
	 * Computes the distance between two cities
	 * 
	 * @param from The starting city
	 * @param to The destination city
	 * @return The distance between the two cities
	 */
	public int dist( final String from, final String to ) {
		return D[ citymap.get( from ) ][ citymap.get( to ) ];
	}
	
	/** @return The matrix of distances */
	@Override
	public String toString( ) {
		String res = "";
		for( final String from : citymap.keySet( ) ) {
			res += "\n" + from + " ";
			for( final String to : citymap.keySet( ) ) {
				res += dist( from, to ) + " ";
			}
		}
		return res.substring( 1 );
	}

}
