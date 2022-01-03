package aoc2015.day03;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that models the delivery of presents by Santa in a grid-like 
 * neighbourhood
 * 
 * @author Joris
 */
public class DeliveryTracker {
	/** The count of presents delivered per house */
	final CoordGrid<Integer> delivered;
	
	/**
	 * Creates a new delivery grid
	 */
	public DeliveryTracker( ) {
		delivered = new CoordGrid<Integer>( 0 );
	}
	
	/**
	 * Follows the given delivery instructions to deliver presents but now Santa
	 * uses a robot to help him. Every other instruction is processed by the robot
	 * 
	 * @param instr The delivery instructions
	 * @param usesrobot True to help Santa out with a delivery robot
	 */
	public void deliver( final String instr, final boolean usesrobot ) {
		int curr = 0;
		final Coord2D[] coords = usesrobot ? new Coord2D[ ] { new Coord2D( 0, 0 ), new Coord2D( 0, 0 ) } : new Coord2D[ ] { new Coord2D( 0, 0 ) }; 
		
		// start delivery at Santa's & the Robot's current position
		delivered.update( coords[0], x -> x + 1 );
		
		// now process instructions
		for( int i = 0; i < instr.length( ); i++ ) {
			final char c = instr.charAt( i );
			
			switch( c ) {
				case '<': coords[curr] = coords[curr].move( -1,  0 ); break;
				case '>': coords[curr] = coords[curr].move(  1,  0 ); break;
				case '^': coords[curr] = coords[curr].move(  0, -1 ); break;
				case 'v': coords[curr] = coords[curr].move(  0,  1 ); break;
			}
			
			// update count at new position
			delivered.update( coords[curr], x -> x + 1 );
			
			// and change delivery unit
			curr = (curr + 1) % coords.length;
		}
	}
	
	/** @return The count of houses that have at least one present */
	public int countHousesDelivered( ) {
		return delivered.getValues( ).size( );
	}
}
