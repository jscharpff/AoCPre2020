package aoc2019.day19;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICException;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class Day19 extends IntCodeChallenge2019 {
	/** Tile definitions */
	private static final char TILE_UNKNOWN = ' ';
	private static final char TILE_EMPTY = '.';
	private static final char TILE_PULL = '#';
	private static final char TILE_CHECKING = 'O';
	
	/** The currently known space map */
	protected CoordGrid<Character> spacemap;
	
	/**
	 * Day 19 of the AoC 2019
	 *
	 * @param args The command line arguments
	 */
	public static void main( final String[] args ) {
		final IntCodeChallenge2019 day19 = new Day19( );
		day19.useWindow( "Day19 - Tractor beam" );
		day19.run( );
	}
	
	/**
	 * Part 1: find out which tiles in a 50x50 area are under the influence of
	 * the tractor beam. Returns the count of tiles 
	 */
	@Override
	public String part1( ) throws ICException {
		// create map with fixed size
		spacemap = new CoordGrid<Character>( TILE_UNKNOWN );
		spacemap.fixWindow( new Coord2D( 0, 0 ), new Coord2D( 50, 50 ) );
		
		// explore space!
		for( int y = 0; y < spacemap.size( ).x; y++ ) {
			for( int x = 0; x < spacemap.size( ).y; x ++  ) {
				// check the position, space map will be updated
				final char tile = isPulled( new Coord2D( x, y ) ) ? TILE_PULL : TILE_EMPTY;
				
				// show result
				if( isVisual( ) )	{
					screen.write( tile );			
				}
			}
			if( isVisual( ) ) screen.write( '\n' );
		}
				
		return "" + spacemap.count( TILE_PULL );
	}
	
	@Override
	public String part2( ) throws ICException {
		// use map of part 1 to determine starting position, any point within the beam
		// suffices
		Coord2D start = null;
		for( int x = spacemap.size( ).x - 1; start == null && x > 0; x-- ) {
			for( int y = spacemap.size( ).y - 1; start == null && y > 0; y-- ) {
				if( isPulled( new Coord2D( x, y ) ) ) {
					start = new Coord2D( x, y );
				}
			}
		}
		
		// unfix the window
		spacemap.unfixWindow( );

		// look for first coordinate that satisfies the requirement
		final Coord2D result = exploreBeam( start );
		return "" + (result.x * 10000 + result.y);
	}

	protected Coord2D exploreBeam( final Coord2D coord ) throws ICERuntimeException {
		// set of options
		Set<Coord2D> options = new HashSet<>( );
		options.add(  coord );
		
		// viewpoint coordinate
		Coord2D vp = coord.move( 0, 0 ); 
				
		while( options.size( ) > 0 ) {
			Set<Coord2D> newoptions = new HashSet<>( );
			
			// go over all current options
			for( final Coord2D c : options ) {
				// not in tractor beam, remove it
				if( !isPulled( c ) ) continue;
				
				// update viewpoint
				vp = vp.max( c );
				
				// does it solve the puzzle?
				if( isPulled( c.move( 99, 0 ) ) && isPulled( c.move( 0, 99 ) ) ) return c;
				
				newoptions.add( c.move( 1, 0 ) );				
				newoptions.add( c.move( 0, 1 ) );				
				newoptions.add( c.move( 1, 1 ) );
			}
			
			// swap sets and try new options
			newoptions.removeAll( options );
			options = newoptions;

			// visualise algorithm
			if( isVisual( ) ) {
				// visualise only a specific area
				final Coord2D topleft = vp.move( -30, -30 );
				final Coord2D bottomright = vp.move( 10, 10 );
				
				// visualise the next search options
				final Map<Coord2D, Character> old = new HashMap<>( 3 );
				for( final Coord2D n : newoptions ) old.put( n, spacemap.set( n, TILE_CHECKING ) );
				display( topleft + " - " + bottomright + "\n" + spacemap.extract( topleft, bottomright ).toString( ), 5 );
				for( final Coord2D n : newoptions ) if( old.get( n ) != null ) spacemap.set( n, old.get( n ) );
			}
		}		
		
		throw new RuntimeException( "Failed to find coordinate" );
	}
	
	
	/**
	 * Checks whether the position is pulled using a drone
	 * 
	 * @param coord The coordinate
	 * @return True iff the position is under the influence of the tractor beam
	 * @throws ICERuntimeException 
	 */
	protected boolean isPulled( final Coord2D coord ) throws ICERuntimeException {
		// checks spacemap first if already explored?
		final char c = spacemap.get( coord );
		if( c == TILE_PULL || c == TILE_EMPTY ) return c == TILE_PULL;
		
		// not known yet, explore
		final Drone drone = new Drone( newIntCode( "Drone" ) );
		drone.getProgram( ).setInput( coord.x, coord.y );
		drone.activate( );
		
		final boolean pulled = drone.consume( ) == 1;
		spacemap.set( coord, pulled ? TILE_PULL : TILE_EMPTY );
		return pulled;
	}
}
