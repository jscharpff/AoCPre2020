package aoc2019;

import java.io.IOException;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICERuntimeException;
import aoc2019.intcode.exceptions.ICException;
import aoc2019.intcode.tester.IntCodeTester;
import aoc2019.intcode.ui.TextWindow;
import aocutil.Util;
import aocutil.io.FileReader;
import aocutil.string.StringUtil;

/**
 * Base class for all AoC 2019 IntCode challenges
 * 
 *  @author Joris
 */
public abstract class IntCodeChallenge2019 {	
	/** The input program */
	protected final String input;
	
	/** The program tester, if test cases are supplied */
	protected final IntCodeTester tester;
	
	/** The active text window, if any */
	protected static TextWindow screen;
	
	/** The tick duration, used to pause a little in visual mode */
	protected static final long TICK_LENGTH = 10;
	

	/**
	 * The main function that starts the tests and program
	 * 
	 * @throws IOException if file reading failed
	 */
	public IntCodeChallenge2019( ) {		
		try {
			// read program input
			this.input = new FileReader( getClass( ).getResource( "input.txt" ) ).readLines( ).get( 0 );
			
			// read test cases, if any
			if( getClass( ).getResource( "tests.txt" ) == null ) {
				tester = null;
				return;
			}
			tester = new IntCodeTester( getClass( ).getResource( "tests.txt" ).getPath( ) );
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}
	
	/** @return True if an ouput screen is used */
	public static boolean isVisual( ) { return screen != null && screen.isVisible( ); }
	
	/**
	 * Sets up a new text window for output
	 * 
	 * @param title The text window title
	 */
	public void useWindow( final String title ) {
		screen = new TextWindow( title );
	}

	/**
	 * Outputs text to the active screen, if any is used
	 * 
	 * @param text The text to output
	 */
	public static void display( final String text ) {
		display( text, 0 );
	}
	
	/**
	 * Outputs text to the active screen, if any is used, and waits for the specified
	 * amount of time to enable users to see the output
	 * 
	 * @param text The text to output
	 * @param ticklength The tick length
	 */
	public static void display( final String text, final long ticklength ) {
		if( !isVisual( ) ) return;
		
		screen.setText( text );
		tick( ticklength );
	}
	
	/**
	 * Briefly pauses the execution to allow users to see the visualisation
	 */
	public static void tick( ) {
		tick( TICK_LENGTH );
	}
	
	/**
	 * Briefly pauses the execution to allow users to see the visualisation
	 * 
	 * @param time The duration to sleep
	 */
	public static void tick( final long time ) {
		if( time == 0 ) return;
		Util.sleep( time > 0 ? time : TICK_LENGTH );
	}
	
	/**
	 * Runs the challenges of the day
	 */
	public void run( ) {
		run( true );
	}
	
	/**
	 * Runs the challenges of the day
	 * 
	 * @param stackdump True to enable stack dump on error
	 */
	public void run( final boolean stackdump ) {
		// start screen
		if( screen != null ) screen.show( );
		
		// run tests
		if( tester != null ) {
			tester.run( );
		}
		
		try {
			System.out.println( "---[ Part 1 ]---" );
			System.out.println( "Ouput: " + part1( ) );
			System.out.println(  );
	
			System.out.println( "---[ Part 2 ]---" );
			System.out.println( "Ouput: " + part2( ) );
			
		} catch( ICException e ) {
			System.err.println( "\n\n" + e.getClass( ) + ": " + e.getMessage( ) );
			
			if( stackdump && e instanceof ICERuntimeException )
				System.err.println( ((ICERuntimeException) e).getProgram( ) );
		}

		// unload the screen
		if( isVisual( ) ) {
			screen.unload( );
			screen = null;
		}
	}
	
	/**
	 * Part 1 of the challenge
	 * 
	 * @return The result of part 1 as a string
	 * @throws ICException
	 */
	public abstract String part1( ) throws ICException;
	
	/**
	 * Part 2 of the challenge
	 * 
	 *  @return The result of part 2 as a String
	 *  @throws ICException
	 */
	public abstract String part2( ) throws ICException;
	
	/**
	 * Parses the input into a fresh copy of the IntCode machine
	 * 
	 * @param name The IntCode machine name
	 * @return The IntCode machine
	 */
	public IntCode newIntCode( final String name ) {
		return newIntCode( name, false );
	}
	
	/**
	 * Parses the input into a fresh copy of the IntCode machine
	 * 
	 * @param name The IntCode machine name
	 * @param silent True to configure the machine to suppress all logging and output
	 * @return The IntCode machine
	 */
	public IntCode newIntCode( final String name, final boolean silent ) {
		final IntCode IC = IntCode.parse( name, input );
		IC.setLogtypes( );
		IC.getIO( ).setInputEnabled( false );
		IC.getIO( ).setOutputEnabled( false );
		return IC;
	}
	
	
	/**
	 * Runs the IntCode machine and returns the value at index 0
	 * 
	 * @param name The IntCode machine name
	 * @param in The program input, if an
	 * @return The value at index 0 after execution of the program 
	 * @throws ICERuntimeException if an error was thrown by the machine 
	 */
	public IntCode runIntCode( final String name, final long... in ) throws ICERuntimeException {
		final IntCode IC = newIntCode( name, true );

		IC.setInput( in );
		IC.run( );
		
		return IC;
	}
	
	/**
	 * Fetches as output of the given machine
	 * 
	 * @param program The IntCode program
	 * @return String containing all program output
	 * @throws ICEInvalidState if the program was not terminated yet
	 */
	protected String getOutput( final IntCode program ) throws ICEInvalidState {
		return StringUtil.fromArray( program.getOutput( ) );
	}
}
