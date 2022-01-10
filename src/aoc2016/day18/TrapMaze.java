package aoc2016.day18;

import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;
import aocutil.string.StringUtil;

/**
 * A maze of traps that protects the Easter Bunny's second HQ building 
 * @author Joris
 */
public class TrapMaze {
	/** The traps in the maze */
	protected final CoordGrid<Boolean> traps;
	
	/** The characters used to visualise the map */
	private final static char CH_TRAP = '^', CH_SAFE = '.';
	
	/**
	 * Creates a new TrapMaze from a description of its first row and the number
	 * of rows to generate from there
	 * 
	 * @param firstrow The first row of the maze
	 * @param rows The number of rows it has
	 */
	public TrapMaze( final String firstrow, final int rows ) {
		// initialise a new trap maze of fixed size
		final int cols = firstrow.length( );
		traps = new CoordGrid<>( false );
		traps.fixWindow( new Window2D( cols, rows ) );
		
		// set the traps as described by the first row
		String row = "" + firstrow;
		for( int i = 0; i < row.length( ); i++ ) traps.set( i, 0, firstrow.charAt( i ) == CH_TRAP );
		
		// and generate the next rows one by one	
		for( int r = 1; r < rows; r++ ) {
			row = getNextRow( row );
			for( int c = 0; c < cols; c++ ) traps.set( c, r, row.charAt( c ) == CH_TRAP );
		}
	}
	
	/**
	 * Builds the string that represents the next row of the maze, given the
	 * specified row
	 * 
	 * @param row The current row
	 * @return The next row of the maze
	 */
	private static String getNextRow( final String row ) {
		final int cols = row.length( );
		final StringBuilder nextrow = new StringBuilder( );
		
		// covert row into trap/safe booleans;
		final int[] traps = ("." + row + "." ).chars( ).map( x -> x == CH_TRAP ? 1 : 0 ).toArray( ); 
				
		for( int c = 1; c < cols + 1; c++ ) {
			final boolean[] t = new boolean[] { traps[c-1] == 1, traps[c] == 1, traps[c+1] == 1 };
			
			// apply rules to determine whether this tile is a trap or not
			final boolean istrap = (t[0] && t[1] && !t[2]) || (!t[0] && t[1] && t[2]) || (t[0] && !t[1] && !t[2]) || (!t[0] && !t[1] && t[2]);
			nextrow.append( istrap ? CH_TRAP : CH_SAFE );
		}
		return nextrow.toString( );
	}
	
	/**
	 * @return The number of safe tiles in the maze
	 */
	public long countSafe( ) {
		return traps.count( false );
	}
	
	/**
	 * Counts the number of safe spaces in the maze without storing the actual maze
	 * 
	 * @param firstrow The initial row of the maze
	 * @param rows The number of rows of the maze
	 * @return The number of safe spaces in it
	 */
	public static long countSafe( final String firstrow, final int rows ) {
		String row = "" + firstrow;
		long safe = StringUtil.count( row, CH_SAFE ); 
		
		// generate the next rows one by one, count the safe tiles and discard the
		// previous row that we no longer need
		for( int r = 1; r < rows; r++ ) {
			row = getNextRow( row );
			safe += StringUtil.count( row, CH_SAFE );
		}
		
		return safe;
	}	
	
	/** @return A visual representation of the maze */
	@Override
	public String toString( ) {
		return traps.toString( x -> "" + (x ? CH_TRAP : CH_SAFE) );
	}
}
