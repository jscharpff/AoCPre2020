package aoc2019.day19;

import aoc2019.day11.Robot;
import aoc2019.intcode.IntCode;

public class Drone extends Robot {
	/**
	 * Creates a new Drone with the IntCode program as its software
	 * 
	 * @param program The IntCode program
	 */
	public Drone( final IntCode program ) {
		super( program );
	}
}
