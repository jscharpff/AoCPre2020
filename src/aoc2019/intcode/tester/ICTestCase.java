package aoc2019.intcode.tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICException;
import aoc2019.intcode.logger.Logger.Logtype;
import aocutil.string.StringUtil;

public class ICTestCase {
	/** The intcode program to run */
	protected final IntCode program;
	
	/** The options map */
	protected final Map<String, String> options;
	
	/** The original string description of the test case */
	protected final String testcase;
	
	/** The set of allowed options */
	private static final String[] OPTIONS_ALLOWED = {
		"in", "out", "result", "program", "log"
	};
	
	/**
	 * Constructs a new test case from a string description
	 * 
	 * @param num The test case number
	 * @param str The testcase
	 * @throws IOException if parsing the test case failed
	 */
	public ICTestCase( final int num, final String str ) throws IOException {
		// parse test case input 
		final String[] s = str.split( "\\|" );
		if( s.length != 2 ) throw new IOException( "Test case is not valid, it does not contain a '|' character: " + str );
		
		this.program = IntCode.parse( "Test " + num, s[0] );
		
		// parse options
		this.options = new HashMap<>( );
		for( String option : s[1].split( ";" ) ) {
			final String[] kv = option.split( "=" );
			options.put( kv[0].trim( ), kv[1].trim( ) );
		}
		
		// test validity of options
		final Set<String> invalid = new HashSet<>( options.keySet( ) );
		for( final String opt : OPTIONS_ALLOWED )	invalid.remove( opt );
		if( invalid.size( ) > 0 ) throw new IOException( "Invalid option(s) " + invalid.toString( ) + " in test case " + str ); 
		
		// store original test case specification
		testcase = "" + str;
	}
	
	/** @return The program to test */
	public IntCode getProgram( ) { return program; }
	
	/**
	 * Runs the test case
	 * 
	 * @param casenum The index of the test case
	 * @param logtypes The logtypes to use
	 */
	public void run( final int casenum, final Logtype... logtypes ) {
		// set up the program
		final IntCode IC = program;
		IC.setLogtypes( logtypes );
		IC.getIO( ).setOutputEnabled( false );
	
		// case description
		final String casestr = String.format( "%2d ", casenum );
		
		// run the program
		try { 
			// setup test options
			if( options.containsKey( "in" ) ) IC.setInput( StringUtil.toLongArray( options.get( "in" ) ) );
			if( options.containsKey( "log" ) ) {
				final String[] logs = options.get( "log" ).split( "," );
				final Logtype[] types = new Logtype[ logs.length ];
				for( int i = 0; i < logs.length; i++ ) types[i] = Logtype.valueOf( logs[i] );
				IC.setLogtypes( types );
			}
			
			IC.run( );
			
			// failure messages, if any
			final List<String> fails = new ArrayList<>( );
			
			// check full result
			if( options.containsKey( "result" ) ) {
				final String expectedresult = options.get( "result" );
				final String result = IC.getFullProgram( );
				if( !expectedresult.equals( result ) ) fails.add( "End state does not match expected end state, result is " + result );
			}

			// check program result only
			if( options.containsKey( "program" ) ) {
				final String expectedresult = options.get( "program" );
				final String result = IC.getProgram( );
				if( !expectedresult.equals( result ) ) fails.add( "Program end state does not match expected end state, result is " + result );
			}
			
			// check output
			if( options.containsKey( "out" ) ) {
				final String output = StringUtil.fromArray( IC.getOutput( ) );
				final String expectedout = options.get( "out" );
				if( !output.equals( expectedout ) )
					fails.add( "Output does not match expected output " + expectedout + ", output is " + output );
			}

			// output result and failures
			if( fails.size( ) == 0 ) {
				System.out.println( casestr + "[PASSED] " + testcase  );
			} else {
				System.err.println( casestr + "[FAILED] " + testcase );
				System.err.println( "Program end state: " + IC.getFullProgram( ) );
				for( String s : fails )
					System.err.println( s );
			}
			
		} catch( ICException e ) {
			System.err.println( casestr + "[ERROR] " + testcase + ": " + e.toString( ) );
		}
		
		
	}
}
