package aoc2019.day15.explore;

import aoc2019.day15.RepairDroid;
import aoc2019.day15.ShipSection;
import aoc2019.intcode.exceptions.ICERuntimeException;

public abstract class ExploreStrategy {
	/** The section to explore */
	protected ShipSection section;
	
	/**
	 * Creates a new exploration strategy
	 * 
	 * @param section The ship section to explore
	 */
	public void setSection( final ShipSection section ) {
		this.section = section;
	}
	
	/**
	 * Explores the section using the implemented strategy
	 *  
	 * @param droid The robot 
	 * @throws ICERuntimeException 
	 */
	
	public abstract void explore( final RepairDroid droid ) throws ICERuntimeException;
}
