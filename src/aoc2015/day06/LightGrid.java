package aoc2015.day06;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Square2D;
import aocutil.string.RegexMatcher;

/**
 * Creates a new LightGrid that holds an array of lights
 * 
 * @author Joris
 *
 */
public class LightGrid {
	/** The list of squares that represent the current set of active lights */
	private int[][] lights;
	
	/** The size of the grid */
	private final int N;
	
	/** True if the grid has brightness control */
	private final boolean bctl;
	
	/**
	 * Creates a new grid with all lights disabled
	 * 
	 * @param N The size of the (square) grid
	 * @param bctl True if the grid has brightness control
	 */
	public LightGrid( final int N, final boolean bctl ) {
		lights = new int[N][N];
		this.N = N;
		this.bctl = bctl;
	}
	
	/** @return The number of active lights */
	public long countActive( ) {
		long count = 0;
		for( int x = 0; x < N; x++ )
			for( int y = 0; y < N; y++ )
				count += lights[x][y] > 0 ? 1 : 0;
		return count;
	}
	
	/** @return The total brightness of all active lights */
	public long countBrightness( ) {
		long count = 0;
		for( int x = 0; x < N; x++ )
			for( int y = 0; y < N; y++ )
				count += lights[x][y];
		return count;
	}
	
	/**
	 * Sets the lighting pattern based on the list of instructions
	 * 
	 * @param input The list of instructions formatted as: turn on|off x1,y1 
	 *   through x2,y2
	 */
	public void parseInstructions( final List<String> input ) {
		// parse each instruction
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "(turn off|turn on|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)", s );

			// convert into a square
			final String action = rm.get( 1 );
			final int[] coords = rm.getInts( new int[] { 2, 3, 4, 5 } );
			final Square2D area = new Square2D( coords[0], coords[1], coords[2], coords[3] );
			
			// perform the instruction
			if( action.equals( "toggle" ) ) {
				for( final Coord2D c : area )
					lights[c.x][c.y] = bctl ? (lights[c.x][c.y] + 2) : (1 - lights[c.x][c.y]);
			} else {
				final boolean on = action.equals( "turn on" );
				for( final Coord2D c : area ) 
					lights[c.x][c.y] = bctl ? Math.max( lights[c.x][c.y] + (on ? 1 : -1), 0 ) : (on ? 1 : 0);
			}
		}
	}
	
	/** @return The areas in which lights are active */
	@Override
	public String toString( ) {
		return lights.toString( );
	}
}
