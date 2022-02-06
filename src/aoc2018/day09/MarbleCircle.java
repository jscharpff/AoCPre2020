package aoc2018.day09;

/**
 * Circular List implementation of a circle of marbles
 * 
 * @author Joris
 */
public class MarbleCircle {
	/** The currently selected marble */
	private Marble curr;
	
	/** The current size of the list */
	private int size;

	/**
	 * Creates a new MarbleCircle with only the element 0 in it
	 */
	public MarbleCircle( ) {
		curr = new Marble( 0 );
		size = 1;
		curr.next = curr;
		curr.prev = curr;
	}
	
	/**
	 * Inserts a new Marble with the given value after the current marble. Sets
	 * the current marble to the inserted one
	 * 
	 * @param value The value to insert
	 */
	public void add( final int value ) {
		final Marble m = new Marble( value );
		size++;
		
		// insert it and update links
		m.prev = curr;
		m.next = curr.next;
		curr.next.prev = m;
		curr.next = m;
		curr = m;
	}
	
	/**
	 * Moves the current marble by the number of steps
	 * 
	 * @param steps The number of steps to move. Positive values move in the
	 *   direction of the list, negative values traverse links backwards
	 */
	public void move( int steps ) {
		while( steps != 0 ) {
			if( steps > 0 ) {
				curr = curr.next;
				steps--;
			} else {
				curr = curr.prev;
				steps++;
			}
		}
	}
	
	/**
	 * Removes the current node and sets current to its successor
	 * 
	 * @return The value of the removed marble
	 */
	public int remove( ) {
		// extract it by removing the links to it
		size--;
		final Marble m = curr;
		m.prev.next = m.next;
		m.next.prev = m.prev;
		
		// update the current node to its successor
		curr = m.next;

		// and return its value
		return m.value;
	}

	/** @return The marbles in the circle, with the current node at the head */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		
		sb.append( curr.toString( ) );
		Marble m = curr.next;
		while( !m.equals( curr ) ) {
			sb.append( "," + m );
			m = m.next;
		}
		return sb.toString( );
	}
	
	/** @return The current size of the marble circle */
	public int size( ) { return size; }
	
	/**
	 * A single marble
	 */
	protected class Marble {
		/** The previous marble */
		private Marble prev;
		
		/** The next marble */
		private Marble next;
		
		/** The marble value */
		private final int value;
		
		/**
		 * Creates a new marble
		 * 
		 * @param value The marble value
		 */
		public Marble( final int value ) {
			this.value = value;
		}
		
		/** @return The marble */
		@Override
		public String toString( ) {
			return "(" + value + ")";
		}
		
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof Marble) ) return false;
			return ((Marble)obj).value == value;
		}
	}
}
