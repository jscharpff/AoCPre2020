package aoc2019.day03;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.geometry.Line2D;

/**
 * A simple wire composed of connected 2D line segments
 * 
 * @author Joris
 */
public class Wire {
	/** The list of 2D line segments describing this wire */
	private final List<Line2D> segments;
	
	/**
	 * Creates a new Wire
	 * 
	 * @param segments The 2D line segments
	 */
	private Wire( final List<Line2D> segments ) {
		this.segments = new ArrayList<>( segments );
	}
	
	/**
	 * Finds and returns all coordinates at which a line segment of this wire
	 * intersects with the given wire
	 * 
	 * @param wire The other wire to test intersection with
	 * @return A list of all coordinates at which both wires intersect
	 */
	public List<Coord2D> getIntersections( final Wire wire ) {
		final List<Coord2D> I = new ArrayList<>( );
		for( final Line2D l1 : segments )
			for( final Line2D l2 : wire.segments ) {
				final Coord2D c = l1.intersect( l2 );
				if( c != null ) I.add( c );
			}
				
		return I;
	}
	
	/**
	 * Traces the wire until the specified point is reached
	 * 
	 * @param coord The coordinate to trace until
	 * @return The distance traversed on the wire
	 */
	public long trace( final Coord2D coord ) {
		long dist = 0;
		for( final Line2D s : segments ) {
			// check if the coordinate is on this line segment
			if( s.contains( coord ) ) {
				// yes, compute remaining steps and return
				dist += s.A.getManhattanDistance( coord );
				return dist;
			} else {
				// nope, simply add the whole length minus one for the point already
				// included by the previous segment
				dist += s.length( );
			}
		}
		
		throw new RuntimeException( "Target coordinate not on wire" );
	}
	
	/**
	 * Creates a new wire from a comma-separated string of segments in the format
	 * L|R|U|D(#steps)
	 * 
	 * @param input The list of segments
	 * @return The wire
	 */
	public static Wire fromString( final String input ) {
		final String[] i = input.split( "," );
		Coord2D pos = new Coord2D( 0, 0 );
		final List<Line2D> segments = new ArrayList<>( ); 
		
		for( final String s : i ) {
			final Coord2D newpos = pos.move( Direction.fromLetter( s.charAt( 0 ) ), Integer.parseInt( s.substring( 1 ) ) );
			segments.add( new Line2D( pos, newpos ) );
			pos = newpos;
		}
		
		return new Wire( segments );
	}

	/** @return The string description of the wire */
	@Override
	public String toString( ) {
		return segments.toString( );
	}
}
