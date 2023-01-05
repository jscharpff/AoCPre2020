package aoc2019.day23.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * A network of intcode program computers
 * 
 * @author Joris
 */
public class ICNetwork {
	/** The peer lease pool */
	protected final List<ICPeer> pool;
	
	/** The queue of packets to send */
	protected final Queue<ICPacket> Q;


	/**
	 * Creates a new network of N machines that run the specified program
	 * 
	 * @param N The number of machines in the network
	 * @param program The program to run
	 */
	public ICNetwork( final int N, final String program ) {
		// create pool of network peers
		pool = new ArrayList<>( N );
		Q = new LinkedList<>( );

		try {
			// initialise peers with correct programs
			for( int i = 0; i < N; i++ )
				pool.add( new ICPeer( this, IntCode.parse( "IC Peer " + i, program ), i ) );
						
		} catch( ICEInvalidState e ) {
			System.err.println( "Failed to initialise network" );
			e.printStackTrace( );
		}
	}
	
	/**
	 * Activates the network transmission and continues until the first packet is
	 * sent to address 255
	 * 
	 * @param stopOnBroadcast True to stop the execution on the first broadcast
	 *   and return the Y value of that packet. Otherwise the NAT will ensure
	 *   continuation of the transmission process
	 * @return Either the Y value of the first broadcast packet (if stop is true)
	 *   or the first Y value that is sent twice to peer 0 by the NAT
	 * @throws ICERuntimeException 
	 */
	public long run( final boolean stopOnBroadcast ) throws ICERuntimeException {
		// start their programs and collect packets
		for( final ICPeer p : pool ) p.activate( );

		// the last sent package by the NAT device
		ICPacket natpack = null;
		
		// history of Y packet values sent by the NAT
		final Set<Long> H = new HashSet<>( );
		
		// keep transmitting packets until the first machine has terminated
		while( pool.get( 0 ).isActive( ) ) {
			
			// are there packets to transmit? If not, send out a -1 to all
			if( Q.isEmpty( ) ) {
				final int notidle = broadcastEmpty( );
				if( notidle == 0 ) {
					// check if we've already seen this one before?
					final long Y = natpack.Y;
					if( H.contains( Y ) ) return Y;
					H.add( Y );
					
					// nope, send it to peer 0
					pool.get( 0 ).receive( natpack.reroute( 0 ) );
				}
				continue;
			}
			
			// yes, send next packet
			final ICPacket pack = Q.poll( );
			
			// is this a broadcast packet?
			if( pack.dest == 255 ) {
				natpack = pack;
				if( stopOnBroadcast ) return pack.Y;
			} else {
				// no, just a regular packet
				pool.get( pack.dest ).receive( pack );				
			}
		}
		
		return -1;
	}
	
	/**
	 * Broadcasts a packet to all peers signifying that the current packet queue
	 * is empty
	 * 
	 * @return The number of peers that are generating output in response
	 * @throws ICERuntimeException
	 */
	protected int broadcastEmpty( ) throws ICERuntimeException {
		int count = 0;
		for( final ICPeer p : pool )
			count += p.receive( new ICPacket( 255, p.iface, -1, 0 ) ) ? 1 : 0;
		return count;
	}
	
	/**
	 * Routes a packet to the destination specified in the packet
	 * 
	 * @param pack The packet to route
	 */
	public void route( final ICPacket pack ) {
		Q.add( pack );
	}
}
