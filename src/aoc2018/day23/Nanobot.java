package aoc2018.day23;

import aocutil.geometry.Coord3D;
import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

public class Nanobot extends LabeledObject {
	/** The position of the Nanobot */
	private final Coord3D pos;
	
	/** The scanning radius of the nanobot */
	private final int radius; 
	
	/**
	 * Creates a new Nanobot
	 * 
	 * @param ID The bot ID
	 * @param pos The position of the nanobot
	 * @param radius The scanning radius of the Nanobot
	 */
	private Nanobot( final int ID, final Coord3D pos, final int radius ) {
		super( "n" + ID );
		this.pos = pos;
		this.radius = radius;
	}
	
	/** @return The position of the nanobot */
	public Coord3D getPosition( ) { return pos; }
	
	/** @return The radius of the nanobot's scanner */
	public int getRadius( ) { return radius; }
	
	/**
	 * Tests whether the given nanobot is in range
	 * 
	 * @param bot The nanobot to test
	 * @return True iff the bot is in the range of this bot's scanner
	 */
	public boolean inRange( final Nanobot bot ) {
		return pos.getManhattanDist( bot.pos ) <= radius;
	}
	
	/**
	 * Tests whether the scan ranges of both bots overlap
	 * 
	 * @param bot The other bot to test against
	 * @return True iff the scanners of both bots overlap at at least one
	 *   coordinate
	 */
	public boolean overlaps( final Nanobot bot ) {
		return pos.getManhattanDist( bot.pos ) <= radius + bot.radius;
	}
	
	/** 
	 * Determines the between the coordinate and this nanobots scanning range
	 * 
	 * @param coord The coordinate
	 * @return The distance to the scanning range from the given coordinate
	 */
	public int getDistanceToScanRange( final Coord3D coord ) {
		return pos.getManhattanDist( coord ) - radius;
	}
	
	/**
	 * Reconstructs a nanobot from a string description
	 * 
	 * @param ID The ID of the bot in the device
	 * @param input The string description
	 * @return The nanobot
	 */
	public static Nanobot fromString( final int ID, final String input ) {
		final Coord3D pos = Coord3D.fromString( RegexMatcher.extract( "pos=<(-?\\d+,-?\\d+,-?\\d+)>", input ) );
		final int r = Integer.parseInt( RegexMatcher.extract( "r=(\\d+)", input ) );
		return new Nanobot( ID, pos, r );
	}
}
