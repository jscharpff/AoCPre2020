package aoc2016.day15;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * A kinetic sculpture that drops a captule through a set of discs with a
 * single opening slot
 *  
 * @author Joris
 */
public class Sculpture {
	/** The list of discs */
	private final List<Disc> discs;
	
	/**
	 * Creates the sculpture
	 * 
	 * @param discs The list of discs in the sculpture
	 */
	public Sculpture( final Collection<Disc> discs ) {
		this.discs = new ArrayList<>( discs );
	}
	
	/**
	 * Determines the first time at which we can drop a capsule such that it
	 * passes through all discs
	 * 
	 * @return The first time at which the button can be pressed
	 */
	public long getTimeToPress( ) {
		// push them onto a stack in reverse order, so that disc 1 is popped first
		final Stack<Disc> remaining = new Stack<>( );
		for( int i = discs.size( ) - 1; i >= 0; i-- )
			remaining.push( discs.get( i ) );		
		
		while( remaining.size( ) > 1 ) {
			final Disc d1 = remaining.pop( );
			final Disc d2 = remaining.pop( );
			
			remaining.push( d1.align( d2 ) );
		}
		
		return remaining.get( 0 ).getDropTime( );
	}

	/**
	 * Creates a kinetic sculpture from the list of discs
	 * 
	 * @param input The discs
	 * @return The sculpture
	 */
	public static Sculpture fromStringList( final List<String> input ) {
		return new Sculpture( input.stream( ).map( Disc::fromString ).toList( ) );
	}
	
	/** @return It's discs */
	@Override
	public String toString( ) {
		return discs.toString( );
	}
	
}
