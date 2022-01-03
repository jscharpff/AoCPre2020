package aoc2015.day14;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.object.LabeledObject;

/**
 * Captures a single reindeer
 * 
 * @author Joris
 */
public class Reindeer extends LabeledObject {
	/** The name of the reindeer */
	public final String name;
	
	/** The flight speed of the reindeer */
	private final int flightspeed;
	
	/** The maximal flight duration in seconds */
	private final int flightduration;
	
	/** The rest time after flying */
	private final int restdurations;

	/**
	 * Creates a new Reindeer with all its characteristics
	 * 
	 * @param name The name of the reindeer
	 * @param flightspeed Its maximum flight speed
	 * @param flighttime The time in seconds it can fly at top speed
	 * @param resttime The time in seconds it needs to rest after flying
	 */
	public Reindeer( final String name, final int flightspeed, final int flighttime, final int resttime ) {
		super( name );
		
		this.name = name;
		this.flightspeed = flightspeed;
		this.flightduration = flighttime;
		this.restdurations = resttime;
	}
	
	/**
	 * Computes the distance that this reindeer may have travelled after racing
	 * for the given time
	 * 
	 * @param time The time that has elapsed since the beginning of the race
	 * @return The total distance travelled so far
	 */
	public int getDistanceTravelled( final int time ) {
		final int cycletime = flightduration + restdurations;
		return ((time / cycletime) * flightduration + Math.min( time % cycletime, flightduration)) * flightspeed;
	}
	
	/**
	 * Parses the reindeer properties from a string and returns the Reindeer
	 * 
	 * @param input The input string
	 * @return The reindeer object
	 */
	public static Reindeer fromString( final String input ) {
		final Matcher m = Pattern.compile( "(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds" ).matcher( input );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid reindeer description: " + input );
		
		return new Reindeer( m.group( 1 ), Integer.parseInt( m.group( 2 ) ), Integer.parseInt( m.group( 3 ) ), Integer.parseInt( m.group( 4 ) ) );
	}
}
