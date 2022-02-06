package aoc2018.day03;

import java.util.ArrayList;
import java.util.List;

/**
 * A nice empty piece of fabric to cut
 * 
 * @author Joris
 */
public class Canvas {
	/** The canvas width */
	protected final int width;
	
	/** The canvas height */
	protected final int height;
	
	/** The current claims on the canvas */
	protected final List<Claim> claims;
	
	/** The current number of claims per canvas piece */
	protected final int[][] piececlaims;
	
	/**
	 * Creates a new canvas
	 * 
	 * @param width The canvas width
	 * @param height The canvas height
	 */
	public Canvas( final int width, final int height ) {
		this.width = width;
		this.height = height;
		
		this.claims = new ArrayList<>( );
		this.piececlaims = new int[ width ][ height ];
	}
	
	/**
	 * Adds a claim to the canvas
	 * 
	 * @param claim The claim
	 */
	public void addClaim( Claim claim ) {
		// add it to the list
		claims.add( claim );
		
		// and process its claim on the pieces of the canvas
		for( int x = claim.start.x; x <= claim.end.x; x++ )
			for( int y = claim.start.y; y <= claim.end.y; y++ )
				piececlaims[x][y]++;
	}
	
	/** @return The claims */
	public List<Claim> getClaims( ) { return claims; }
	
	/**
	 * Counts the number of pieces that are claimed more than once
	 * 
	 * @return The count of pieces
	 */
	public int countMultipleClaims( ) {
		int count = 0;
		for( int x = 0; x < width; x++ )
			for( int y = 0; y < height; y++ )
				if( piececlaims[x][y] > 1 ) count++;
		
		return count;
	}
	
	/**
	 * Checks if the specified claim does not interfere with other claims
	 * 
	 * @param claim The claim to verify
	 * @return True iff no other claim is in the region of this one
	 */
	public boolean isValid( final Claim claim ) {
		for( int x = claim.start.x; x <= claim.end.x; x++ )
			for( int y = claim.start.y; y <= claim.end.y; y++ )
				if( piececlaims[x][y] > 1 ) return false;
		
		return true;
	}
	
	/**
	 * @return String containing the current claims per piece 
	 */
	@Override
	public String toString() {
		String res = "";
		for( int y = 0; y < height; y++ ) {
			for( int x = 0; x < width; x++ ) {
				res += piececlaims[x][y];
			}
			res += "\n";
		}
		return res;
	}
}
