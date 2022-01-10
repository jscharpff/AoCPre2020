package aoc2015.day16;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * My First Crime Scene Analysis Machine!
 * 
 * @author Joris
 */
public class MFCSAM {
	/** The set of all (remaining) aunts */
	private Set<AuntSue> sues;
	
	/**
	 * Creates a new MFCSAM without any sues
	 */
	public MFCSAM( ) {
		sues = new HashSet<>( );
	}
	
	/**
	 * Filters the set of aunt based upon the specified property criteria
	 * 
	 * @param criteria The criteria that need to be matched, described via a 
	 *   comma separated key: value pairs string
	 * @param old True to use old filtering, false for new
	 */
	public void filter( final String criteria, final boolean old ) {
		// translate criteria into a map
		final Map<String, Integer> filter = new HashMap<>( );
		for( final String criterium : criteria.split( "," ) ) {
			final String[] c = criterium.split( ": " );
			filter.put( c[0].trim( ), Integer.parseInt( c[1] ) );
		}
				
		// and filter all the aunts that do not meet this criteria
		final Set<AuntSue> keep = new HashSet<>( );
		for( final AuntSue s : sues ) {
			if( s.meetsCriteria( filter, old ) ) keep.add( s );
		}
		
		sues = keep;
	}
	
	/**
	 * Adds all Aunts from a list of their descriptions
	 * 
	 * @param input The list of aunts
	 */
	public void addAll( final Collection<String> input ) {
		for( final String s : input )
			sues.add( AuntSue.fromString( s ) );
	}
	
	/** @return The list of aunt sues */
	@Override
	public String toString( ) {
		return sues.toString( );
	}
}
