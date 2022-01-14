package aoc2017.day18;

import java.util.List;
import java.util.Stack;

import aoc2017.day18.DuetAssembly.OutputConsumer;
import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2017
	 * 
	 * https://adventofcode.com/2017/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day18.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day18.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	
		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Runs a single DuetAssembly program and returns the value that was last
	 * received
	 * 
	 * @param input The program to execute
	 * @return The last received value
	 */
	private static long part1( final List<String> input ) {
		final DuetAssembly da = new DuetAssembly( "0" );
		final Stack<Long> output = new Stack<>( );
		da.setOutputConsumer( new OutputConsumer( ) {			
			@Override
			public void consume( long value ) {	output.push( value ); }
		}  );
		
		da.run( input );
		return output.pop( );
	}
	
	/**
	 * Runs two DuetAssembly machines in semi-parallel, continuously feeding
	 * outputs of one machine as input to the other until no more output is being
	 * observed
	 * 
	 * @param input The program source 
	 * @return The number of signals sent by the second machine
	 */
	private static long part2( final List<String> input ) {
		final Orchestrator pd = new Orchestrator( input, 2 );
		pd.run( );
		return pd.getOutput( 1 ).size( );
	}
}
