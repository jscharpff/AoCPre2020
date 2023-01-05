package aoc2019.intcode.tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aoc2019.intcode.logger.Logger.Logtype;
import aocutil.io.FileReader;

public class IntCodeTester {
	/** The programs to test */
	protected final List<ICTestCase> tests;
	
	/**
	 * Creates a new IntCode tester
	 * 
	 * @param programs The test input
	 * @throws IOException if parsing of a test case failed 
	 */
	protected IntCodeTester( final List<String> programs ) throws IOException {
		tests = new ArrayList<>( programs.size( )	);
		int testnum = 1;
		for( String s : programs ) {
			// allow for some documentation
			if( s.trim( ).isEmpty( ) || s.startsWith( "//" ) ) continue;
			
			// parses an actual test case
			tests.add( new ICTestCase( testnum++, s ) );
		}
	}
	
	/**
	 * Creates a new IntCode tester, reads test cases from input file
	 * 
	 * @param file The path to the input file
	 * @throws IOException on file error
	 */
	public IntCodeTester( final String file ) throws IOException {
		this( new FileReader( file ).readLines( ) );
	}
	
	/**
	 * Runs a series of tests using the list of programs as input. Each line
	 * contains two programs, separated by "=", that describe the start and end
	 * state of the instruction set.
	 *  
	 * @param tests The tests to run
	 * @param logtypes The types of log messages to log during output
	 */
	public void run( final Logtype... logtypes ) {
		System.out.println( "---[ TESTS ]---" );
		int testnum = 0;
		for( final ICTestCase test : tests ) {
			testnum++;
			test.run( testnum, logtypes );
		}
		System.out.println(  );
	}
}
