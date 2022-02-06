package aoc2018.day17;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Line2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.string.RegexMatcher;

/**
 * Class that models and simulates water flow beneath the surface
 * 
 * @author Joris
 */
public class WaterFlow {
	/** The grid that is a 2D model of the flow */
	final CoordGrid<Character> flowgrid;
	
	/** The coordinate of the spring at the surface */
	private final Coord2D spring;
	
	/** The set of tiles */
	private final static char C_SAND = '.', C_CLAY = '#', C_WATERFLOW = '|', C_WATERREST = '~';
	
	/**
	 * Creates a new WaterFlow model
	 * 
	 * @param springpos The position of the spring
	 * @param veins The lines modelling the basins of clay
	 */
	private WaterFlow( final Coord2D springpos, final List<Line2D> veins ) {
		this.spring = springpos;
		
		// construct the empty flow grid from the veins
		flowgrid = new CoordGrid<>( C_SAND );
		for( final Line2D v : veins ) {
			for( final Coord2D c : v.getPoints( ) )
				flowgrid.set( c, C_CLAY );
		}

		// fix it but still allow some x expansion for water overflowing the sides 
		final Window2D win = flowgrid.window( );
		flowgrid.fixWindow( win.getMinCoord( ).move( -1, 0 ), win.getMaxCoord( ).move( 1, 0 ) );
	}
	
	/**
	 * Simulates the flow of water from the spring
	 */
	public void flow( ) {
		flow( spring );
	}
	
	/**
	 * Simulates the flow of water from the given origin
	 * 
	 * @param origin The origin of water flowing
	 */
	private void flow( final Coord2D origin ) {		
		// trace the water from the origin until we hit something
		Coord2D curr = origin;
		while( flowgrid.get( curr.x, curr.y + 1 ) == '.' ) {
			curr = curr.move( 0, 1 );
			if( curr.y > flowgrid.window( ).getMaxY( ) ) return;
			if( flowgrid.contains( curr ) ) flowgrid.set( curr, C_WATERFLOW );
		}
		
		// already traced here?
		if( flowgrid.get( curr.move( 0, 1 ) ) == C_WATERFLOW )
			return;		
		
		// we hit something, simulate horizontal water movement
		boolean basin = true;
		while( basin ) {
			// check if the water will remain in a basin or it flows of the side
			final Coord2D left = flowHorizontal( curr, true );
			final Coord2D right = flowHorizontal( curr, false );
			
			// is this a basin?
			basin = flowgrid.get( left ) == C_CLAY && flowgrid.get( right ) == C_CLAY;
			if( basin ) {
				// yes, fill it with water and repeat the process at one level higher
				for( int x = left.x + 1; x <= right.x - 1; x++ ) flowgrid.set( x, curr.y, C_WATERREST );
				curr = curr.move( 0, -1 );
				flowgrid.set( curr, C_WATERFLOW );
			} else {
				// no, continue flowing process from the edges of the veins
				if( flowgrid.get( left ) != C_CLAY ) flow( left );
				if( flowgrid.get( right ) != C_CLAY ) flow( right );
			}
		}
	}
	
	/**
	 * Traces the horizontal water flow until either a wall of clay is 
	 * encountered or there is no more support below the flow
	 * 
	 * @param coord The coordinate to start the horizontal trace from
	 * @param left The horizontal movement direction
	 * @return The coordinate at which the flow changes direction again or hits
	 *   a wall
	 */
	private Coord2D flowHorizontal( final Coord2D coord, final boolean left) {
		Coord2D c = coord;
		while( true ) {
			c = c.move( left ? -1 : 1, 0 );
			if( flowgrid.get( c ) == C_CLAY ) return c;
			flowgrid.set( c, C_WATERFLOW );
			final char ch = flowgrid.get( c.x, c.y + 1 );
			if( ch != C_CLAY && ch != C_WATERREST ) return c;
		}
	}
	
	/**
	 * Counts the total amount of water tiles in the flow
	 * 
	 * @param flowing True to include flowing water in the count
	 * @return The count of all water tiles
	 */
	public long countWater( final boolean flowing ) {
		long count = 0;
		for( final Coord2D c : flowgrid.getKeys( ) ) {
			if( c.y == 0 ) System.out.println( c + ": "+ flowgrid.get( c ));
			
			final char ch = flowgrid.get( c );
			if( ch == C_WATERREST || (flowing && ch == C_WATERFLOW) ) count ++;
		}
		return count;
	}	
	
	/**
	 * Creates a new WaterFlow from a list of veins from a 2D scan
	 * 
	 * @param scan The scan
	 * @return The WaterFlow
	 */
	public static WaterFlow fromScan( final List<String> scan ) {
		final List<Line2D> veins = new ArrayList<>( scan.size( ) );
		
		// parse scan information
		for( final String s : scan ) {
			final int x1,x2,y1,y2;
			
			// vertical vein?
			RegexMatcher rm = new RegexMatcher( "x=#D,\\s+y=#D..#D" );
			if( rm.match( s ) ) {
				x1 = x2 = rm.getInt( 1 );
				y1 = rm.getInt( 2 );
				y2 = rm.getInt( 3 );
			} else {
				rm = RegexMatcher.match( "y=#D,\\s+x=#D..#D", s );
				y1 = y2 = rm.getInt( 1 );
				x1 = rm.getInt( 2 );
				x2 = rm.getInt( 3 );
			}
			
			// add the vein
			veins.add( new Line2D( new Coord2D( x1, y1 ), new Coord2D( x2, y2 ) ) );
		}
		
		return new WaterFlow( new Coord2D( 500, 0 ), veins );
	}
	
	/**
	 * @return A 2D grid-like representation of the WaterFlow 
	 */
	@Override
	public String toString( ) {
		return flowgrid.toString( );
	}
}
