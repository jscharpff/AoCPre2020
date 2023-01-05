package aoc2019.day11;

import java.util.HashSet;
import java.util.Set;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.exceptions.ICException;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class Day11 extends IntCodeChallenge2019 {
	private final static char TILE_BLACK = '.';
	private final static char TILE_WHITE = '#';	
	
	/**
	 * Day 11 of the AoC 2019
	 * 
	 * @param args The command line arguments
	 */
	public static void main( String[] args ) {
		final IntCodeChallenge2019 day11 = new Day11( );
		day11.useWindow( "Robot painter" );
		day11.run( );		
	}
	
	/**
	 * Part 1: Move a painter robot over the hull that uses an IntCode program as
	 * its brain and paint the hull elements according to its output. When the
	 * robot terminates, return the number of hull tiles it painted.
	 * 
	 * @return The number of tiles painted at least once by the robot
	 */
	
	@Override
	public String part1( ) throws ICException {
		// create a new hull grid
		final CoordGrid<Character> hull = new CoordGrid<>( TILE_BLACK );
		
		// create a new paint robot
		final Robot painter = new Robot( newIntCode( "Robot" ) ); 
		painter.activate( );
		
		// keep track of coordinates visited
		final Set<Coord2D> visited = new HashSet<>( );
		
		// keep moving the robot according to its signals until it terminates
		while( painter.isActive( ) ) {
			final Coord2D pos = painter.getPosition( );
			visited.add( pos );

			// feed the hull colour of the current position to the robot
			final long signal = hull.get( pos ) == TILE_BLACK ? 0 : 1;
			painter.feed( signal );
			painter.activate( );
			
			// get response (twice) to get the movement and new colour
			final long colour = painter.consume( );
			final long movement = painter.consume( );
			
			// process movement and colour
			painter.turnAndMove( movement == 0 ? -90 : 90 );
			hull.set( pos, colour == 0 ? TILE_BLACK : TILE_WHITE );
			
			// update display
			if( isVisual( ) ) {
				display( "Painted at least once: " + visited.size( ) + "\n" + hull.toString( ) );
			}
		}
		
		// hold output for a sec when done
		if( isVisual( ) )
			display( "DONE! Painted at least once: " + visited.size( ) + "\n" + hull.toString( ), 3000 );
				
		return "" + visited.size( );
	}
	
	/**
	 * Part 2: same as part 1 but now the robot starts on a white tile instead
	 * of a black tile. Also, the goal is not to count the number of tiles 
	 * painted, but to return the resulting image
	 * 
	 * @return The drawing as a result of the robot doing its thing
	 */
	@Override
	public String part2( ) throws ICException {
		// create a new hull grid
		final CoordGrid<Character> hull = new CoordGrid<>( TILE_BLACK );
		hull.set( new Coord2D( 0, 0 ), TILE_WHITE );
		
		// create a new paint robot, start it so it will wait for its input
		final Robot painter = new Robot( newIntCode( "Robot" ) );
		painter.activate( );
		
		// keep moving the robot according to its signals until it terminates
		while( painter.isActive( ) ) {
			// feed the hull colour of the current position to the robot
			final Coord2D pos = painter.getPosition( );
			final long signal = hull.get( pos ) == TILE_BLACK ? 0 : 1;
			painter.feed( signal );
			painter.activate( );
			
			// await response (twice) to get the movement and new colour
			final long colour = painter.consume( );
			final long movement = painter.consume( );
			
			// process movement and colour
			painter.turnAndMove( movement == 0 ? -90 : 90 );
			hull.set( pos, colour == 0 ? TILE_BLACK : TILE_WHITE );
			
			// visualise the painting
			if( isVisual( ) )
				display( hull.toString( ), 10 );
		}
				
		return "(drawing)\n\n" + hull.toString( );
	}
}
