package aoc2019.day15;

import aoc2019.IntCodeChallenge2019;
import aoc2019.day15.explore.DFSExploreStrategy;
import aoc2019.day15.explore.ExploreStrategy;
import aoc2019.intcode.exceptions.ICException;

public class Day15 extends IntCodeChallenge2019 {	
	/** The ship section used in both challenges */
	// create a new section of the ship to explore
	final ShipSection section = new ShipSection( );	
	
	/**
	 * Day 15 of the AoC 2019
	 * 
	 * @param args The command line arguments
	 */
	public static void main( final String[] args ) {
		final IntCodeChallenge2019 day15 = new Day15( );
		day15.useWindow( "Day15 - Repair droid" );
		day15.run( );
	}
	
	/**
	 * Part 1: let the robot explore the maze of pathways 
	 */
	@Override
	public String part1( ) throws ICException {		
		// create the droid and start it, it will wait for a movement instruction
		final RepairDroid droid = new RepairDroid( newIntCode( "Droid" ) );
		droid.activate( );
		
		// explore the section's maze
		final ExploreStrategy strategy = new DFSExploreStrategy( );
		strategy.setSection( section );
		strategy.explore( droid );

		// now do a BFS search to get determine distance from start to oxygen supply
		return "" + section.getDistanceToOxygen( );
	}
	
	@Override
	public String part2( ) throws ICException {
		// create the droid and start it, it will wait for a movement instruction
		final RepairDroid droid = new RepairDroid( newIntCode( "Droid" ) );
		droid.activate( );

		// now do a BFS search to get determine distance from start to oxygen supply
		return "" + section.getTimeToFillWithOxygen( );
	}

}
