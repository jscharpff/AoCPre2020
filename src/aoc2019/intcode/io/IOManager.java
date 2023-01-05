package aoc2019.intcode.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import aoc2019.intcode.IntCode;
import aoc2019.intcode.exceptions.ICEInputOutput;
import aocutil.Util;

/**
 * The IO manager of an intcode program
 * 
 * @author Joris
 */
public class IOManager {
	/** The program it is linked to */
	protected final IntCode program;
	
	/** The stream used for input */
	protected InputStream in;
	
	/** The stream used for output */
	protected PrintStream out;
	
	/** Input buffer, will be polled until new input becomes available */
	protected Queue<Long> inbuffer;
	
	/** Output buffer, keeps track of all output until it is consumed */
	protected Queue<Long> outbuffer;
	
	/** The sleep time to wait for input/output (in msec) */
	protected static final long SLEEP_TIME = 1;
	
	/** The maximum sleep time to wait for input/output (in msec) */
	protected static final long MAX_SLEEP = 1000;


	/**
	 * Creates a new IO Manager that uses the default system streams for input
	 * and output
	 * 
	 * @param program The reference to the IntCode program
	 */
	public IOManager( final IntCode program ) {
		this.program = program;
		setInputEnabled( true );
		setOutputEnabled( true );
		
		// FIFO queues for input and output
		this.inbuffer = new LinkedList<>( );
		this.outbuffer = new LinkedList<>( );
	}
	
	/**
	 * Feeds one or more values to the input feed, will be consumed by the input command
	 * 
	 * @param values The values to feed
	 */
	public void feed( final long... values ) {
		for( final long value : values )
			inbuffer.add( value );
	}		
	
	/**
	 * Reads a single integer from the input stream. Will prefer reading from the
	 * input feed first, but uses input stream if the feed is empty.
	 * 
	 * @return The integer that was read
	 * @throws ICEInputOutput if the input stream failed
	 */
	public long read( ) throws ICEInputOutput {
		// use input stream?
		if( in != null && inbuffer.size( ) == 0 ) {
			try {
				return in.read( );
			} catch( IOException e ) {
				throw new ICEInputOutput( program, "Exception in stream input: " + e.getMessage( ) );
			}
		}
		
		// no, wait for input to become available
		long wait = MAX_SLEEP;
		while( !hasInput( ) ) {
			wait = sleep( wait, "read" );
		}
			
		return inbuffer.poll( );
	}
	
	/**
	 * Waits until the input buffer is cleared
	 * 
	 * @throws ICEInputOutput when the waiting timed out or the program terminated while waiting
	 */
	public void awaitReader( ) throws ICEInputOutput {
		long wait = MAX_SLEEP;
		while( hasInput( ) ) {
			wait = sleep( wait, "awaitReader" );
		}
	}

	
	/**
	 * Outputs a single integer to the output stream
	 * 
	 * @param value The integer value to output
	 */
	public void write( final long value ) {
		if( out != null ) out.print( value );
		outbuffer.add( value );
	}
	
	/**
	 * Reads a single value from the output buffer, removes it from the buffer. If no
	 * value is available, it will wait for new output until time out
	 * 
	 * @return The integer
	 * @throws ICEInputOutput if the output buffer is empty
	 */
	public long consume( ) throws ICEInputOutput {
		// wait for output to become available
		long wait = MAX_SLEEP;
		while( !hasOutput( ) ) {
			wait = sleep( wait, "consume" );
		}
		
		return outbuffer.poll( );
	}
	
	/**
	 * Waits for the output to contain at least the number of outputs
	 * 
	 * @param count The output count
	 * @throws ICEInputOutput if the waiting timed out or the program terminated unexpectedly
	 */
	public void awaitWriter( final int count ) throws ICEInputOutput {
		// wait for output to become available
		long wait = MAX_SLEEP;
		while( outbuffer.size( ) < count ) {
			wait = sleep( wait, "awaitWriter" );
		}
	}
	
	/**
	 * @return The contents of the current output buffer without consuming it
	 */
	public long[] getOutputBuffer( ) {
		return outbuffer.stream( ).mapToLong( i -> i ).toArray( );
	}
	
	/** @return True iff there is output in the output buffer */
	public boolean hasOutput( ) { return outbuffer.size( ) > 0; }
	
	/** @return True iff there is input in the input buffer */
	public boolean hasInput( ) { return inbuffer.size( ) > 0; }
	
	/**
	 * Enables or disables input via input stream
	 * 
	 * @param enable True to enable, false to disable
	 */
	public void setInputEnabled( final boolean enable ) {
		this.in = enable ? System.in: null;
	}
	
	/**
	 * Enables or disables output to used out stream
	 * 
	 * @param enable True to enable, false to disable
	 */
	public void setOutputEnabled( final boolean enable ) {
		this.out = enable ? System.out : null;
	}
	
	/**
	 * Internal method to sleep for a single tick
	 * 
	 * @param wait Remaining wait time
	 * @param descr The description to use in time out exceptions
	 * @return The new remaining wait time after the sleep
	 * @throws ICEInputOutput if the waiting timed out or the program terminated unexpectedly
	 */
	protected long sleep( final long wait, final String descr ) throws ICEInputOutput {
		if( wait <= 0 ) throw new ICEInputOutput( program, "Wait for timed out: " + descr );
		if( !program.isRunning( ) ) throw new ICEInputOutput( program, "Program terminated while waiting for: " + descr );

		// sleep for the specified amount of time, if successful also update remaining sleep time
		if( Util.sleep( SLEEP_TIME ) )
			return wait - SLEEP_TIME;
		return wait;
	}
	
	/**
	 * Closes the IO Manager
	 */
	public void close( ) {
		
	}
}
