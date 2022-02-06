package aoc2018.day11;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.string.StringUtil;

/**
 * A cell-based fuel grid
 * 
 * @author Joris
 */
public class FuelGrid {
	/** The serial number of the grid */
	private final int serial;
	
	/** The grid size */
	private final int size;
	
	/** The power of each of the cells in the gird */
	private final CoordGrid<Integer> cells;
	
	/** The summation table over the fuel cells */
	private final CoordGrid<Integer> cellsums;
	
	/**
	 * Creates a new fuel grid
	 * 
	 * @param serial The serial used to generate the fuel cell capacities
	 */
	public FuelGrid( final int serial ) {
		this.serial = serial;
		this.size = 300;
		
		// generate cells
		cells = new CoordGrid<>( size, size, 0 ); 
		for( final Coord2D c : cells ) cells.set( c, getPower( c ) );
		
		// generate summation table
		cellsums = new CoordGrid<Integer>( size, size, 0 );
		for( int y = 0; y <= size; y++ ) {
			for( int x = 0; x <= size; x++ ) {
				cellsums.set( x, y, cells.get( x, y ) + cellsums.get( x-1, y ) + cellsums.get( x, y-1 ) - cellsums.get( x-1, y-1 ) );
			}
		}
	}
	
	/**
	 * Determines the power of the fuel cell at the given position
	 * 
	 * @param cell The position of the fuel cell
	 * @return The power of the fuel cell
	 */
	private int getPower( final Coord2D cell ) {
		final int rackid = cell.x + 10;
		int power = rackid * cell.y + serial;
		power = (power * rackid / 100) % 10;
		return power - 5;
	}
	
	/**
	 * Finds the sub grid of any size with that maximises total fuel cell power
	 * 
	 * @param N The sub grid size
	 * @return The top-left coordinate of the NxN grid that maximises power 
	 */
	public Window2D findMaximalPowerGrid( ) {
		int maxpower = -1;
		Window2D best = null;
		
		for( int N = 1; N <= size; N++ ) {
			final Coord2D maxcoord = findMaximalPowerGrid( N );
			final int power = getGridPower( maxcoord, N );
			if( power > maxpower ) {
				maxpower = power;
				best = new Window2D( maxcoord, maxcoord.move( N-1, N-1 ) );
			}
		}
		return best;
	}

	
	/**
	 * Finds the NxN sized sub grid with the maximal total fuel cell power
	 * 
	 * @param N The sub grid size
	 * @return The top-left coordinate of the NxN grid that maximises power 
	 */
	public Coord2D findMaximalPowerGrid( final int N ) {
		Coord2D best = null; 
		int maxpower = Integer.MIN_VALUE;
		for( final Coord2D c : cells ) {
			// only consider grids within the cell bounds
			if( !cells.contains( c.move( N-1, N-1 ) ) ) continue;
			
			final int power = getGridPower( c, N );
			if( power > maxpower ) {
				maxpower = power;
				best = c;
			}
		}
		
		if( best == null ) throw new RuntimeException( "No valid grid found for size " + N );
		return best;
	}
	
	/**
	 * Computes the total power of a NxN grid from a given top-left coordinate
	 * 
	 * @param topleft The top-left coordinate of the sub grid
	 * @param N The size of the grid
	 * @return The total power of the fuel cells within the grid
	 */
	private int getGridPower( final Coord2D topleft, final int N ) {
		if( N == 1 ) return cells.get( topleft );
		
		return cellsums.get( topleft.move( N, N ) ) + cellsums.get( topleft ) 
			- cellsums.get( topleft.move( N, 0 ) ) - cellsums.get( topleft.move( 0, N ) );
	}
	
	/**
	 * Returns a region of the grid as matrix string
	 * 
	 * @param region The region to convert to string
	 * @return The matrix that describes this region
	 */
	public String toString( final Window2D region ) {
		return cells.extract( region.getMinCoord( ), region.getMaxCoord( ) ).toString( i -> StringUtil.padLeft( "" + i, 3 ) );
	}
}
