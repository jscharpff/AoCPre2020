package aoc2015.day08;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.string.RegexMatcher;

/**
 * Class that helps Santa escape/unescape his text messages to prepare them
 * for communication 
 * 
 * @author Joris
 */
public class StringEscaper {
	
	/**
	 * Escapes an input string
	 * 
	 * @param input The input string
	 * @return The escaped string
	 */
	public static String escape( final String input ) {
		String escaped = "" + input;
		
		// first escape backslashes
		escaped = escaped.replaceAll( "\\\\", "\\\\\\\\" );		
		
		// escape quotes
		escaped = escaped.replaceAll( "\"", "\\\\\"" );
		
		return "\"" + escaped + "\"";
	}

	/**
	 * Unescapes an input string
	 * 
	 * @param input The input string
	 * @return The unescaped string
	 */
	public static String unescape( final String input ) {
		// get the value between quotes
		String unescaped = RegexMatcher.extract( "^\"(.*)\"$", input );
		
		// unescape hexadecimally encoded characters
		Matcher m = Pattern.compile( "\\\\x([0-9a-f]{2})" ).matcher( unescaped );
		while( m.find( ) ) {
			final char ch = (char)Integer.parseInt( m.group( 1 ), 16 );
			unescaped = unescaped.substring( 0, m.start( 1 ) - 2 ) + ch + unescaped.substring( m.end( 1 ) ); 
			m = Pattern.compile( "\\\\x([0-9a-f]{2})" ).matcher( unescaped );
		}
		
		// unescape quotes characters
		unescaped = unescaped.replaceAll( "\\\\\"", "\"" );
		
		// unescape double slashes
		unescaped = unescaped.replaceAll( "\\\\\\\\", "\\\\" );

		// return unescaped string
		return unescaped;
	}
}
