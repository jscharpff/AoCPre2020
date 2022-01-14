package aoc2017.day17;

import java.util.ArrayList;
import java.util.List;

/**
 * A spinlock that fills data in a cyclic memory structure
 * 
 * @author Joris
 */
public class Spinlock {
	/** The spin cycle length */
	private final int cycle;
	
	/** The circular buffer of data */
	private final List<Integer> buffer;
	
	/** The current position in the buffer */
	private int pos;

	/**
	 * Creates a new Spinlock
	 * 
	 * @param cyclelen The length of its cycles
	 */
	public Spinlock( final int cyclelen ) {
		this.cycle = cyclelen;
		this.buffer = new ArrayList<>( );
		buffer.add( 0 );
	}
	
	/**
	 * Fills the cyclic buffer
	 * 
	 * @param steps The number of steps to perform the filling process
	 */
	public void fillBuffer( final int steps ) {
		pos = 0;
		for( int i = 0; i < steps; i++ ) {
			pos = (pos + cycle) % buffer.size( ) + 1;
			buffer.add( pos, i + 1 );
		}
	}

	/**
	 * Runs the filling process but but only keeps track of the value at the
	 * given index
	 * 
	 * @param steps The number of steps to perform the filling process
	 * @param index The index to track the value of
	 * @return The value at the index after performing the number of filling
	 *   steps
	 */
	public int track( final int steps, final int index ) {
		pos = 0;
		int value = -1;
		int size = 1;
		for( int i = 0; i < steps; i++ ) {
			pos = (pos + cycle) % size + 1;
			size++;
			if( pos == index ) value = (i+1);
		}
		return value;
	}
	
	/**
	 * Gets the index of the buffer with the specified value
	 * 
	 * @param value The value to search for
	 * @return The buffer index
	 */
	public int find( final int value ) {
		return buffer.indexOf( value );
	}
	
	/**
	 * Returns the value contained in te buffer at the specified index
	 * 
	 * @param index The index
	 * @return The value at the given position
	 */
	public int getValue( final int index ) {
		return buffer.get( index );
	}
	
	/** @return The buffer contents, starting from index 0 */
	@Override
	public String toString( ) {
		return buffer.toString( );
	}
}
