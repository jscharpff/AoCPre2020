package aoc2017.day13;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A layer-based firewall implementation that employs moving sniffers on each
 * layer of the firewall
 * 
 * @author Joris
 */
public class Firewall {
	/** The firewall layers */
	private final Map<Integer, FWLayer> layers;
	
	/** The number of the last layer in the FW */
	private final int depth;

	/**
	 * Creates a new Firewall with the given layer map
	 * 
	 * @param layers The firewall layers
	 */
	private Firewall( final Map<Integer, FWLayer> layers ) {
		this.layers = layers;
		this.depth = layers.keySet( ).stream( ).mapToInt( x -> x  ).max( ).getAsInt( );
	}
	
	/**
	 * Traces the movement of a single packet through the firewall
	 * 
	 * @return The severity of the packet 
	 */
	public int trace(  ) {
		// reset firewall layers
		reset( );
		
		// start the trace
		int severity = 0;
		for( int layer = 0; layer <= depth; layer++ ) {
			// check if the packet is caught
			if( layers.containsKey( layer ) ) {
				final FWLayer l = layers.get( layer );
				if( l.getGate( ) == 1 ) severity += layer * l.size;
			}
			
			// move the packet
			moveAll( );
		}
		
		return severity;
	}
	
	/**
	 * Traces the movement of a single packet through the firewall
	 * 
	 * @param starttime the time at which the packet starts to go through the
	 *   firewall
	 * @return The severity of the packet 
	 */
	public boolean passes( final int starttime ) {
		// reset firewall layers
		reset( );

		// determine for each layer the time at which the packet will arrive and
		// check if this is the time at which its sniffer is at gate 1
		for( final FWLayer l : layers.values( ) ) {
			final int hittime = starttime + l.layer;
			if( l.getGate( hittime ) == 1 ) return false;
		}
		return true;
	}	
		
	/**
	 * Moves the sniffer in all firewall layers one position
	 */
	private void moveAll( ) {
		for( final FWLayer f : layers.values( ) ) f.move( );
	}
	
	/**
	 * Resets the firewall sniffers
	 */
	private void reset( ) {
		for( final FWLayer f : layers.values( ) ) f.reset( );		
	}
	
	/**
	 * Reconstructs a firewall from a string description of its layers
	 * 
	 * @param input The layers, one per string
	 * @return The firewall
	 */
	public static Firewall fromStringList( final List<String> input ) {
		final Map<Integer, FWLayer> layers = new HashMap<>( input.size( ) );
		input.stream( ).map( FWLayer::fromString ).forEach( x -> layers.put( x.layer, x ) );
		return new Firewall( layers );
	}
	
	/** @return The string of the firewall and its layers */
	@Override
	public String toString( ) {
		return layers.toString( );
	}
	
	/**
	 * A single layer in the firewall 
	 */
	private static class FWLayer {
		/** The layer it is in */
		protected final int layer;
		
		/** The range of the layer */
		protected final int size;
		
		/** The current position of the sniffer in this layer */
		protected int pos;
		
		/**
		 * Creates a new firewall layer
		 * 
		 * @param depth The layer depth
		 * @param size The size of the layer
		 */
		private FWLayer( final int depth, final int size ) {
			this.layer = depth;
			this.size = size;
			reset( );
		}
		
		/** Resets the firewall layer to its initial state */
		private void reset( ) {
			pos = 0;
		}
		
		/**
		 * Moves one position in the current firewall range
		 */
		private void move( ) {
			pos = (pos + 1) % ((size - 1) * 2);
		}
		
		/** @return The current gate it is sniffing */
		private int getGate( ) {
			return getGate( pos );
		}
		
		/**
		 * @param time The time passed
		 * @return The gate it would be sniffing at the specified time
		 */
		private int getGate( final int time ) {
			final int timepos = time % ((size - 1) * 2);
			return timepos < size ? timepos + 1 : (size * 2 - timepos - 1);
		}
		
		/**
		 * Creates a new Firewall layer from a string
		 * 
		 * @param input The firewall layer as string
		 * @return The FWLayer 
		 */
		private static FWLayer fromString( final String input ) {
			final String[] s = input.split( ": " );
			return new FWLayer( Integer.parseInt( s[0] ), Integer.parseInt( s[1] ) );
		}
		
		/** @return The string describing the layer */
		@Override
		public String toString( ) {
			return "[" + layer + "] " + getGate( ) + "/" + size;
		}
	}
}
