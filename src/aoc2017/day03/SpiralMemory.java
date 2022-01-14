package aoc2017.day03;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that corresponds to a memory that is layd out in a spiral.
 * @author Joris
 *
 */
public class SpiralMemory {
	
	/**
	 * Computes the distance from the center of the memory to the entry at the
	 * specified numerical index
	 * 
	 * @param index The index of the memory entry
	 * @return The Manhattan distance from the entry to (0,0)
	 */
	public static int getDistance( final int index ) {
		return getCoord( index ).getManhattanDistance( new Coord2D( 0, 0 ) );
	}
	
	/**
	 * Converts the given numerical index to the position in the spiral memory
	 * 
	 * @param index The numerical index
	 * @return The coordinate in the memory
	 */
	public static Coord2D getCoord( final int index ) {
		// determine the ring the index is on
		int ring = 1;
		int ringsize = 1;
		while( index > ringsize * ringsize ) {	ring++; ringsize += 2; }
		
		// and the index within that ring
		int ringmax = ringsize * ringsize;
		int side = 0;
		while( ringmax - ringsize + 1 > index ) {
			ringmax -= ringsize - 1;
			side++;
		}
		final int ringidx = ringmax - index - (int)Math.floorDiv( ringsize - 1, 2 );

		// translate indexes to a coordinate
		if( side == 0 ) return new Coord2D( -ringidx, ring - 1 );
		if( side == 1 ) return new Coord2D( -1 * (ring - 1), -ringidx );
		if( side == 2 ) return new Coord2D( ringidx, -1* (ring - 1) );
		if( side == 3 ) return new Coord2D( ring - 1, ringidx );
		
		throw new RuntimeException( "Should not be here..." );
	}
	
	
	/**
	 * Generates sufficient memory values until a value is encountered that i
	 * larger than the specified input value 
	 * 
	 * @param value The input value
	 * @return The index of the first memory element that is larger than the
	 *   given value
	 */
	public static int getFirstLarger( final int value ) {
		// use a coordgrid to represent the memory
		final CoordGrid<Integer> mem = new CoordGrid<Integer>( 0 );
		int index = 1;
		Coord2D curr = getCoord( index );
		mem.set( curr, 1 );
		
		// and keep generating values until we find it
		while( mem.get( curr ) <= value ) {
			curr = getCoord( ++index );
			mem.set( curr, curr.getAdjacent( true ).stream( ).mapToInt( mem::get ).sum( ) );
		}
		
		return mem.get( curr );
	}
}
