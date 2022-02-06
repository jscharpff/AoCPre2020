package aoc2018.day07;

import java.util.List;

import aocutil.io.FileReader;

public class Day07 {
	
	/**
	 * Day 7 of the AoC2018
	 * 
	 * https://adventofcode.com/2018/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 2, 0 ) );
		System.out.println( "Part 2 : " + part2( input, 5, 60 ) );
	}

	/**
	 * Determines the order of instruction steps of the assembly process
	 * 
	 * @param input The instruction set procession rules
	 * @return The ordering of instructions according to the rules
	 */
	protected static String part1( final List<String> input ) {
		final SleighKit kit = SleighKit.fromStringList( input );
		return kit.getInstructionPlan( );
	}
	
	/**
	 * Determines the time required to perform the assembly with the number of
	 * workers
	 * 
	 * @param input The assembly instructions
	 * @param workers The number of workers that can assemble in parallel
	 * @param basetime The base time it takes to complete a task
	 * @return The total lead time of the asembly
	 */
	protected static int part2( final List<String> input, final int workers, final int bassetime ) {
		final SleighKit kit = SleighKit.fromStringList( input );
		return kit.getExecutionTime( workers, bassetime );
	}
}
