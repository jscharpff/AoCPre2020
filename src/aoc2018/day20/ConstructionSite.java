package aoc2018.day20;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Construction site of the Elves around the North Pole
 * 
 * @author Joris
 */
public class ConstructionSite {
	/** The distance to each of the rooms in the site */
	private final CoordGrid<Integer> dist;
	
	/**
	 * Creates a new construction site from a Regex string
	 * 
	 * @param input The regex string
	 */
	public ConstructionSite( final String input ) {
		dist = new CoordGrid<Integer>( null );
		dist.set( new Coord2D( 0, 0 ), 0 );
		
		// then reconstruct the complex
		reconstructDoors( new Coord2D( 0, 0 ), input.substring( 1, input.length( ) - 1 ), 0 );		
	}

	/**
	 * Reconstructs the doors on the construction site from a regex string
	 * 
	 * @param pos The current position we are tracing from
	 * @param regex The (branching) regex string
	 */
	private void reconstructDoors( Coord2D pos, final String regex, int pathlen ) {		
		// follow directions until we find a branch
		int idx = -1;
		int depth = 0;
		int startidx = -1;
		final List<String> branches = new ArrayList<>( );
		while( ++idx < regex.length( ) ) {
			final char c = regex.charAt( idx );
			
			switch( c ) {
				// start of branch
				case '(':
					if( depth == 0 ) startidx = idx + 1;
					depth++;
					break;
					
				// branching option (if at depth 1, otherwise ignore in this iteration)
				case '|':
					if( depth != 1 ) continue;
					branches.add( regex.substring( startidx, idx ) );
					startidx = idx + 1;
					break;
					
				// end of branch
				case ')':
					depth--;
					if( depth == 0 ) {
						branches.add( regex.substring( startidx, idx ) );
						final String regexrem = regex.substring( idx + 1 );
						final Coord2D curr = pos;
						final int currlen = pathlen;
						for( final String b : branches ) {
							// do not branch on empty optional paths, we already seen the coordinate
							// while traversing the longer path
							if( b.equals( "" ) ) continue;
							reconstructDoors( curr, b + regexrem, currlen );
						}
						return;
					}
					break;
					
				// actual movement
				case 'N': case 'S': case 'E': case 'W':
					if( depth > 0 ) continue;
					
					final Direction dir = Direction.fromLetter( c );
					pos = pos.move( dir, 1 );
					pathlen++;
					if( !dist.hasValue( pos ) || dist.get( pos ) > pathlen ) {
						// new location or shorter route, set minimal distance
						dist.set( pos, pathlen );
					} else {
						// shorter route is possible, update pathlen
						pathlen = dist.get( pos );
					}
					break;
					
				default: throw new RuntimeException( "Illegal character in regex: " + c );	
			}
		}
	}
	
	/**
	 * Finds the room that takes the most doors to get to
	 * 
	 * @return The number of doors needed to pass 
	 */
	public long findPathWithMostDoors( ) {
		return dist.getValues( ).stream( ).mapToInt( i -> i ).max( ).getAsInt( );
	}

	/**
	 * Finds all rooms that have a shortest path of at least a specified number
	 * of doors in it
	 *
	 * @param doors The number of doors 
	 * @return The number of such rooms
	 */
	public long countRoomsWithNumberOfDoors( final int doors ) {
		return dist.getValues( ).stream( ).mapToInt( i -> i ).filter( i -> i >= doors ).count( );
	}
}
