package aoc2019.day21;

import aoc2019.day11.Robot;
import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCode.VisualMode;
import aoc2019.intcode.exceptions.ICEInvalidState;

public class JumpBot extends Robot {
	/**
	 * Creates a new JumpBot with the specified program as its software
	 * 
	 * @param program The program
	 * @throws ICEInvalidState 
	 */
	public JumpBot( final IntCode program ) throws ICEInvalidState {
		super( program );
		
		program.setVisualisationMode( VisualMode.ASCII );
	}
}
