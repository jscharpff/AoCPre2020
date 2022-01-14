package aoc2017.day09;

/**
 * Garbage collector that scores and cleans input data
 * 
 * @author Joris
 */
public class GarbageCollector {
	/** The string of data to consider */
	private final String data;
	
	/** The cleaned string of data */
	private final String cleandata;
	
	/** The count of data cleaned */
	private int garbagelen;
	
	/**
	 * Creates a new garbage collector for the given data string
	 * 
	 * @param data The data string to clean up
	 */
	public GarbageCollector( final String data ) {
		this.data = data;
		this.cleandata = clean( data );
	}
	
	/**
	 * Cleans the input string by replacing all marked characters by simpler
	 * pieces of data and removing the garbage data
	 * 
	 * @param input The input string
	 * @return The cleaned data string
	 */
	private String clean( final String input ) {
		garbagelen = 0;
		
		// first remove all marked characters
		String cleaned = input.replaceAll( "!.", "" );
		
		// then remove garbage groups
		int idx = -1;
		int lastidx = 0;
		final StringBuilder res = new StringBuilder( );
		while( ++idx < cleaned.length( ) ) {
			// keep scanning until we encounter garbage
			if( cleaned.charAt( idx ) != '<' ) continue;
			
			// first extract good part
			res.append( cleaned.substring( lastidx, idx ) );
			
			// now move index until we encounter the closing bracket, set lastidx to
			// count the removed characters
			lastidx = idx;
			while( cleaned.charAt( ++idx ) != '>' );
			garbagelen += idx - lastidx - 1; 
			lastidx = ++idx;
		}
		
		// extract last part as well
		res.append( cleaned.substring( lastidx ) );
		
		return res.toString( );
	}
	
	/**
	 * Computes the score of the garbage data
	 * 
	 * @return The score
	 */
	public int getScore( ) {
		// score groups based upon their depth
		int score = 0;
		int depth = 0;
		
		for( final char c : cleandata.toCharArray( ) ) {
			if( c == '{' || c == '<' ) depth++;
			else if( c == '}' || c == '>' ) {
				score += depth;
				depth--;
			}
		}
		
		return score;
	}
	
	/** @return The number of characters cleaned by the GC */
	public int getGarbageCollected( ) { return garbagelen; }

	/**
	 * @return The data in this garbage collector
	 */
	@Override
	public String toString( ) {
		return data + "\n" + cleandata;
	}
}
