package aoc2019.day17;

import aoc2019.IntCodeChallenge2019;
import aoc2019.intcode.IntCode.VisualMode;
import aoc2019.intcode.exceptions.ICException;
import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class Day17 extends IntCodeChallenge2019 {
	/** Definition of the used tiles */
	public final static char TILE_OPEN = ' ';
	public final static char TILE_SCAFFOLD = '#';
	public final static char TILE_INTERSECT = 'O';
	
	/**
	 * Day 17 of the AoC 2019
	 *
	 * @param args The command line arguments
	 */
	public static void main( final String[] args ) {
		final IntCodeChallenge2019 day17 = new Day17( );
//		day17.useWindow( "Day17 - Set and Forget" );
		day17.run( );
	}

	/**
	 * Part 1: determine all intersections in the Robot's camera output and
	 * return the sum of their distance products
	 */
	@Override
	public String part1( ) throws ICException {
		// create camera bot and start running the bot
		final CameraBot bot = new CameraBot( newIntCode( "CamBot" ) );
		bot.activate( );
		
		// process bot camera view
		final CoordGrid<Character> view = bot.processOutput( );
		
		// find all intersections and compute distance to borders
		int result = 0;
		for( final Coord2D c : view ) {
			// only check when the current tile is a scaffold
			if( view.get( c ) != TILE_SCAFFOLD ) continue;
			
			int count = 0;
			for( final Coord2D n : c.getAdjacent( false ) )
				count += view.get( n ) == TILE_SCAFFOLD ? 1 : 0;

			if( count == 4 ) {
				view.set( c, TILE_INTERSECT );
				result += c.x * c.y;
			}
		}
		
		// return sum of distance products
		return "" + result;
	}

	@Override
	public String part2( ) throws ICException {
		// create camera bot and override its operation mode
		final CameraBot bot = new CameraBot( newIntCode( "CamBot" ) );
		bot.getProgram( ).setIntcodeAt( 0, 2 );
		bot.getProgram( ).setVisualisationMode( VisualMode.ASCII );
		bot.activate( );
		
		// now the robot functions in an input->output loop, first update its camera
		bot.processOutput( );
		
		/**
		 * Supply input to the robot:
		 * 
		 * A,B,A,A,B,C,B,C,C,B
		 * A=L12,R8,L6,R8,L6
		 * B=R8,L12,L12,R8
		 * C=L6,R6,L12
		 */
		bot.feedASCII( "A,B,A,A,B,C,B,C,C,B" );
		bot.feedASCII( "L,12,R,8,L,6,R,8,L,6" );
		bot.feedASCII( "R,8,L,12,L,12,R,8" );
		bot.feedASCII( "L,6,R,6,L,12" );
		bot.feedASCII( "y" );
		bot.activate( );

		// get output of the robot
		do {
			bot.processOutput( );
		} while( bot.isActive( ) );
		
		// consume final output
		bot.processOutput( );
		
		// the bots final output should be the dust it has collected
		return "" + bot.consume( );
	}
	
	protected static int countNeighbours( final CoordGrid<Character> view, final Coord2D c, final char tile, final boolean diagonals ) {
		int count = 0;
		for( final Coord2D n : c.getAdjacent( diagonals ) )
			count += view.get( n ) == tile ? 1 : 0;
		return count;
	}
} 
