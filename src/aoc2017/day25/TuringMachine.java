package aoc2017.day25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.string.RegexMatcher;

/**
 * A tape-based Turing Machine
 * 
 * @author Joris
 */
public class TuringMachine {	
	/** The number of steps to run the machine */
	private final int steps;
	
	/** The map of rules per state */
	private final Map<Character, TMInstruction> instructions;

	/** The tape of bits */
	private List<Boolean> tape;
	
	/** The current cursor position */
	private int cursor;
	
	/** The current state */
	private char state;
	
	/**
	 * Creates a new Turning Machine
	 * 
	 * @param input The set of instructions for the machine
	 */
	public TuringMachine( final List<String> input ) {
		tape = new ArrayList<>( );
		tape.add( false	);
		cursor = 0;
		
		// get initial state and number of execution steps from input
		final List<String> in = new ArrayList<>( input );
		final String[] setup = in.remove( 0 ).split( ";" );
		state = RegexMatcher.match( "Begin in state ([A-Z])", setup[0] ).getChar( 1 );
		steps = RegexMatcher.match( "checksum after (\\d+) steps", setup[1] ).getInt( 1 );		
		
		// parse the instruction set
		instructions = new HashMap<>( in.size( ) );
		for( final String s : in ) {
			final TMInstruction instr = new TMInstruction( s );
			instructions.put( instr.matchstate, instr );
		}
	}
	
	/**
	 * Run the turing machine following the list of instructions
	 */
	public void run( ) {
		for( int step = 0; step < steps; step++ ) {
			// find rule that applies to the current state
			final TMRule rule = instructions.get( state ).getRule( tape.get( cursor ) );
			
			// apply the rule
			tape.set( cursor, rule.newvalue );
			cursor += rule.dir;
			if( cursor < 0 ) {
				// shift the "infinite" tape
				cursor++;
				tape.add( 0, false );
			} else if( cursor == tape.size( ) ) {
				tape.add( false );
			}
			state = rule.newstate;
		}
	}
	
	/** @return The checksum on the tape */
	public int checksum( ) {
		return tape.stream( ).mapToInt( b -> b ? 1 : 0 ).sum( );
	}
	
	/**
	 * An instruction set
	 */
	private class TMInstruction {
		/** The state in which the instruction is to be applied */
		private final char matchstate;
		
		/** The rule set */
		private final Map<Boolean, TMRule> rules;
		
		/**
		 * Creates an instruction from a string description
		 * 
		 * @param input The string description of the instruction
		 */
		private TMInstruction( final String input ) {
			final String[] i = input.split( ";" );
			matchstate = RegexMatcher.match( "In state ([A-Z])", i[0] ).getChar( 1 );
			
			rules = new HashMap<>( ); 
			for( int r = 1; r < i.length - 1; r += 4 ) {
				final TMRule rule = new TMRule( i[r], i[r+1], i[r+2], i[r+3] );
				rules.put( rule.value, rule );
			}
		}
		
		/**
		 * Get the rule that applies for the read bit value
		 * 
		 * @param value The bit value
		 * @return The rule for the bit value
		 */
		protected TMRule getRule( final boolean value ) {
			return rules.get( value );
		}
		
		/**
		 * @return The string description of the instruction 
		 */
		@Override
		public String toString( ) {
			return "[" + matchstate + "] " + rules;
		}
	}
	
	/**
	 * A single rule in the instruction set
	 */
	private class TMRule {
		/** The bit value to trigger on */
		private final boolean value;
		
		/** The new value to write */
		private final boolean newvalue;
		
		/** The move direction */
		private final int dir;
		
		/** The new state */
		private char newstate;
		
		/**
		 * Creates a new rule set from the strings
		 * 
		 * @param input The array of rule input
		 */
		private TMRule( final String... input ) {
			value = RegexMatcher.match( "current value is (\\d+)", input[0] ).getInt( 1 ) == 1;
			newvalue = RegexMatcher.match( "Write the value (\\d+)", input[1] ).getInt( 1 ) == 1; 
			dir = RegexMatcher.match( "Move one slot to the (left|right)", input[2] ).get( 1 ).equals( "left" ) ? -1 : 1;
			newstate = RegexMatcher.match( "Continue with state ([A-Z])", input[3] ).getChar( 1 ); 
		}
		
		/** @return The rule string */
		@Override
		public String toString( ) {
			return (value ? "1" : "0") + " => W" + (newvalue ? 1 : 0) + ", M" + (dir == -1 ? "L" : "R") + " -> " + newstate ; 
		}
	}
}
