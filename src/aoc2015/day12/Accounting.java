package aoc2015.day12;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that implements some standard accounting functions for Santa's little
 * elves
 * 
 * @author Joris
 */
public class Accounting {
	
	/**
	 * Scans the (JSON) input document and simply sums all numbers that are
	 * present in it
	 *  
	 * @param input The input document
	 * @return The sum of all (negative) numbers present in the document
	 */
	public static long sumNumbers( final String input ) {
		// simply scan for numbers to parse and sum over them
		long sum = 0;
		final Matcher m = Pattern.compile( "(-?\\d+)" ).matcher( input );
		while( m.find( ) ) sum += Integer.parseInt( m.group( 1 ) );
		return sum;
	}
	
	/**
	 * Sums over all the numbers in the JSON input document but filters out all
	 * numbers that are contained within an object that also has a key : value
	 * pair of which the value equals the filter value
	 * 
	 * @param input The JSON input document
	 * @param filtervalue The value to filter object with. Objects are filtered
	 *   if they contain at least one property that has the value to filter
	 * @return The sum over non-filtered numbers
	 */
	public static long sumNumbersFiltered( final String input, final String filtervalue ) {
		// remove objects that contain a property with value "red"
		String transformed = "" + input;
		Matcher m = Pattern.compile( ":\"" + filtervalue + "\"" ).matcher( transformed );
		while( m.find( ) ) {
			// remove the whole object from the position of the found object 
			// and rescan the resulting document
			transformed = removeObject( transformed, m.start( 0 ) );			
			m = Pattern.compile( ":\"red\"" ).matcher( transformed );
		}
		
		// now sum all numbers in the remaining string
		return sumNumbers( transformed );
	}
	
	/**
	 * Removes the object from the input of which the definition contains the
	 * specified index. That is, the index is somewhere between the start and
	 * end index of the object in the input string
	 * 
	 * @param input The input string that contains the object
	 * @param index The index at which the object is defined
	 * @return The input string with the object removed. NOTE: the result is no
	 *   longer a valid JSON. 
	 */
	private static String removeObject( final String input, final int index ) {
		// finds the opening and closing brackets that enclose the object from the
		// given index
		final int leftidx = findGroupDelimiter( input, index, '{' );
		final int rightidx = findGroupDelimiter( input, index, '}' );
		return input.substring( 0, leftidx ) + input.substring( rightidx + 1 );
	}
	
	/**
	 * Scans the input string until the specified delimiter is found that opens
	 * or closes the group of which the index position is present. It will not
	 * simply return the first occurrence of the delimiter, rather it finds the
	 * delimiter that actually opens/closes the group. In other words, it will
	 * include other delimiters is they are contained within the same JSON object
	 * 
	 * @param input The input string to scan
	 * @param startidx The index to start scanning from
	 * @param delimiter The delimiter to find
	 * @return The index of the delimiter that opens/closes the group
	 */
	private static int findGroupDelimiter( final String input, final int startidx, final char delimiter ) {
		int idx = startidx;
		final int dir = delimiter == '{' ? -1 : 1;
		int level = 1;
		while( level > 0 ) {
			idx += dir;
			if( idx < 0 || idx >= input.length( ) ) throw new RuntimeException( "Failed to extract group from input: " + input );
			
			final char curr = input.charAt( idx );
			if( curr == '{' ) level += dir;
			else if( curr == '}' ) level -= dir;
		}
		
		return idx;
	}
}
