package aoc2018.day04;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.io.FileReader;

public class Day04 {
	/**
	 * Day 4 of AoC2018
	 * 
	 * https://adventofcode.com/2018/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day04.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Part 1 : " + part1( input ) );

		System.out.println( "\n--[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Part 2 : " + part2( input ) );
}
	
	/**
	 * Part 1: find the guard with the most sleeping minutes and return its ID 
	 * 				 times the minute he sleeps most at
	 * 
	 * @param input The log of guard sleeping times
	 * @return The ID * peak sleep time of the guard that sleeps the most in total
	 */
	protected static int part1( final List<String> input ) {
		final Map<Integer, Guard> guards = processInput( input );
		
		// find the guard that sleeps the most
		Guard Gmax = null;
		for( Guard g : guards.values( ) ) {
			if( Gmax == null ) {
				Gmax = g;
				continue;
			}
			
			if( g.getSleepTime( ) > Gmax.getSleepTime( ) ) Gmax = g;
		}
		
		return Gmax.getID( ) * Gmax.getMostSleepedAt( );
	}
	
	/**
	 * Part 2: find the guard that sleeps the most at one given minute
	 * 
	 * @param input The log of guard sleeping times
	 * @return The ID * the minute in time at which the guard sleeps the most
	 */
	protected static int part2( final List<String> input ) {
		final Map<Integer, Guard> guards = processInput( input );
		
		// find the guard that has the most times it sleeps at a single minute
		Guard Gmax = null;
		for( Guard g : guards.values( ) ) {
			if( Gmax == null ) {
				Gmax = g;
				continue;
			}
			
			if( g.getMaxSleepCount( ) > Gmax.getMaxSleepCount( ) ) Gmax = g;
		}
		
		return Gmax.getID( ) * Gmax.getMostSleepedAt( );
	}
	
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	protected static Map<Integer, Guard> processInput( final List<String> input ) {
		final Map<Integer, Guard> guards = new HashMap<>( );
		
		Collections.sort( input );
		
		// the guard currently on shift according to the log
		Guard currguard = null;
		int sleepstart = -1;
		for( String s : input ) {
			// parse log entries
			final Matcher m = Pattern.compile( "\\[\\d{4}-\\d{2}-\\d{2} (\\d{2}):(\\d{2})\\] ([\\w\\d\\s#]+)" ).matcher( s );
			if( !m.find( ) ) throw new RuntimeException( "Invalid log entry: " + s );
		
			final int hour = Integer.valueOf( m.group( 1 ) );
			final int min = hour == 23 ? 0 : Integer.valueOf( m.group( 2 ) );
			final String command = m.group( 3 );

			// parse log event 
			if( command.startsWith( "Guard" ) ) {
				final Matcher mID = Pattern.compile( "Guard #(\\d+) begins shift" ).matcher( command );
				if( !mID.find( ) ) throw new RuntimeException( "Invalid guard ID command: " + command );
				final int ID = Integer.valueOf( mID.group( 1 ) );
				
				if( !guards.containsKey( ID ) ) guards.put( ID, new Guard( ID ) );
				currguard = guards.get( ID );
			} else if( command.equals( "falls asleep" ) ) {
				sleepstart = min;
			} else if( command.equals( "wakes up" ) ) {
				currguard.addSleepWindow( sleepstart, min );
			}
		}
		
		return guards;
	}
}
