package aoc2015.day16;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.object.LabeledObject;
import aocutil.string.RegexMatcher;

/**
 * Represents one of the Aunts called Sue
 *  
 * @author Joris
 */
public class AuntSue extends LabeledObject {
	/** Map of the property values for this aunt */
	private final Map<String, Integer> props;
	
	/**
	 * Creates a new Aunt Sue
	 * 
	 * @param ID The ID of this aunt
	 */
	private AuntSue( final int ID ) {
		super( "Sue " + ID );
		
		props = new HashMap<String, Integer>( );
	}

	/**
	 * Checks if this aunt Sue meets the specified criteria. If old equals false,
	 * it will use greater than and lesser than checks for certain properties
	 * 
	 * @param criteria The map of properties and values that this aunt needs to
	 *   have
	 * @param old True for old version, false for new version
	 * @return True iff all criteria are met
	 */
	public boolean meetsCriteria( final Map<String, Integer> criteria, final boolean old ) {
		for( final String key : props.keySet( ) ) {
			if( !old && (key.equals( "cats" ) || key.equals( "trees" ) ) ) {
				if( criteria.get( key ) >= props.get( key ) ) return false;
			} else if(  !old && (key.equals( "pomeranians" ) || key.equals( "goldfish") ) ) { 
				if( criteria.get( key ) <= props.get( key ) ) return false;
			} else { 
				if( criteria.get( key ) != props.get( key ) ) return false;
			}
		}
		
		return true;
	}
	
	
	/** @return The description of the aunt */
	@Override
	public String toString( ) {
		return super.toString( ) + ": " + props.toString( );
	}
	
	/**
	 * Creates a new aunt sue
	 * 
	 * @param input The input that describes the aunt
	 * @return The aunt
	 */
	public static AuntSue fromString( final String input ) {
		// first get the ID of this sue
		final AuntSue sue = new AuntSue( Integer.parseInt( RegexMatcher.extract( "Sue (\\d+):", input ) ) );
		
		// then get every remaining key: value pair in the input string
		final Matcher m = Pattern.compile( "(\\w+): (-?\\d+)" ).matcher( input.substring( input.indexOf( ":" ) + 1 ) );
		while( m.find( ) ) {
			sue.props.put( m.group( 1 ), Integer.parseInt( m.group( 2 ) ) );
		}
		
		return sue;
	}
}
