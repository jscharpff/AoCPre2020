package aoc2018.day03;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.geometry.Coord2D;

/**
 * Claim to a part of fabric
 * 
 * @author Joris
 */
public class Claim {
	/** The claim ID */
	protected final int ID;
	
	/** The start position of the claim (from top left corner) */
	protected final Coord2D start;
	
	/** The end position of the claim (from top left corner) */
	protected final Coord2D end;
	
	/**
	 * Creates a new claim
	 * 
	 * @param ID The identifier
	 * @param pos The start coordinate (top leeft)
	 * @param width The width of the claimed area
	 * @param height The height of the claimed area
	 */
	public Claim( final int ID, final Coord2D pos, final int width, final int height ) {
		this.ID = ID;
		this.start = pos;
		this.end = pos.move( width - 1, height - 1 );
	}
	
	/** @return The ID of the claim */
	public int getID( ) { return ID; }
	
	/** @return The claim height */
	public int getHeight( ) { return end.y - start.y + 1; }

	/** @return The claim width */
	public int getWidth( ) { return end.x - start.x + 1; }

	/**
	 * Creates a claim from a string
	 * 
	 * @param claim The claim, formatted as #ID @ X,Y: WxH
	 * @return The corresponding claim object
	 */
	public static Claim fromString( final String claim ) {
		final Matcher m = Pattern.compile( "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)" ).matcher( claim );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid claim format: " + claim );
		
		return new Claim(
				Integer.valueOf( m.group( 1 ) ),
				new Coord2D( Integer.valueOf( m.group( 2 ) ), Integer.valueOf( m.group( 3 ) ) ),
				Integer.valueOf( m.group( 4 ) ), Integer.valueOf( m.group( 5 ) )
			);
	}
	
	/** @return The claim as a string */
	@Override
	public String toString( ) {
		return "#" + ID + " @ " + start.toString( ) + ": " + getWidth( ) + "x" + getHeight( );
	}

}
