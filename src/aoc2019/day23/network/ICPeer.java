package aoc2019.day23.network;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.IntCodeMachine;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aoc2019.intcode.exceptions.ICEInvalidState;
import aoc2019.intcode.exceptions.ICERuntimeException;

/**
 * Single network peer in the IntCodeNetwork
 * 
 * @author Joris
 */
public class ICPeer extends IntCodeMachine {
	/** The network it a peer in of */
	private final ICNetwork network;
	
	/** The interface address of the peer */
	protected final int iface;
	
	/**
	 * Creates a new network peer program
	 * 
	 * @param network The network this peer is part of
	 * @param program The program it is to run
	 * @param iface The interface address of this peer
	 * @throws ICEInvalidState if the program was not expecting its address
	 */
	public ICPeer( final ICNetwork network, final IntCode program, final int iface ) throws ICEInvalidState {
		super( program, true );
		
		this.network = network;
		this.iface = iface;

		// disable the program output to stream
		getProgram( ).getIO( ).setOutputEnabled( false );		
		
		// feed the interface number to the program
		getProgram( ).setInput( this.iface );
	}
	
	/**
	 * Receives a packet routed to this peer
	 * 
	 * @param pack The packet to receive
	 * @return True iff receiving the packet resulted in new output
	 * @throws ICERuntimeException 
	 */
	public boolean receive( final ICPacket pack ) throws ICERuntimeException {
		// check packet destination
		if( pack.dest != iface ) throw new ICERuntimeException( getProgram(), "Packet has invalid destination " + pack + " (expected " + iface + ")" );
		
		// send the packet to the peer 
		if( pack.X == -1 ) {
			feed( -1l );
		} else {
			feed( pack.X );
			feed( pack.Y );
		}
		getProgram( ).resume( );
		
		// await a potential response from the machine
		try {
			final long[] output = getProgram( ).consume( 3 );
			network.route( new ICPacket( iface, (int)output[0], output[1], output[2] ) );
			return true;
		} catch (ICEInputOutput e) { /* No output */ }
		
		return false;
	}
}
